/* Copyright 2016 Eric Schölzel, Robert Mörseburg, Zdravko Yanakiev, Jonas Schenke, Oliver Schmidt
 *
 * This file is part of FIS.
 *
 * FIS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FIS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with FIS.  If not, see <http://www.gnu.org/licenses/>.
 */
package fis.data;

import fis.FilterType;
import fis.RailML2Data;
import fis.common.CommonConfig;
import fis.common.ConfigurationException;
import fis.telegramReceiver.TelegramReceiverController;
import fis.telegrams.*;
import fis.telegrams.TrainRouteTelegram.StopData;
import fis.telegrams.TrainRouteTelegram.TrainRouteData;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Predicate;

/**
 * Controller für {@link TimetableData}. Beinhaltet Weiterreichen von
 * einkommenden Telegrammen und Aktionen, die vom ConnectionState abhängen (z.B.
 * Entscheidung, dass RailML-Fahrplan geladen werden soll)
 *
 * @author Luux
 */
@Component
public class TimetableController {
	private static final Logger LOGGER = Logger.getLogger(TimetableController.class);
	@Autowired
	private CommonConfig fisConfig;

	/**
	 * Predicate zum Filtern der {@link TrainRoute}s nach ihrer
	 * {@link TrainCategory}.
	 * <p>
	 * <p>
	 * Um das Filtern zu erleichtern liefert dieses {@link Predicate} alle
	 * {@link TrainCategory}s, die nicht dem übergebenen Verwendungszweck
	 * entsprechen.
	 */
	private class TrainUsagePredicate implements Predicate<TrainCategory> {
		private String usage;

		/**
		 * Standardkonstruktor.
		 *
		 * @param usage der Verwendungszweck, z.B. {@literal PASSENGER}, nach dem
		 *              gefiltert werden soll
		 */
		public TrainUsagePredicate(String usage) {
			super();

			if (usage == null) {
				throw new IllegalArgumentException("You need to specify a usage to filter by!");
			}
			if (usage.isEmpty()) {
				throw new IllegalArgumentException("Empty usage is not allowed.");
			} else
				this.usage = usage;
		}

		/**
		 * Vergleichsmethode.
		 *
		 * @param t Die Kategory, die untersucht wird
		 * @return {@literal true} genau dann, wenn der Verwendungszweck mit dem
		 * im Konstruktor Angegebenen NICHT übereinstimmt, ansonsten
		 * {@literal false}.
		 */
		@Override
		public boolean test(TrainCategory t) {
			return !(t.getTrainUsage().equalsIgnoreCase(this.usage));
		}
	}

	private TimetableData data;
	private LocalTime time = null;
	private Map<Integer, Message> messages;
	@Autowired
	private TelegramReceiverController receiver;
	
	/**
	 * Gibt die zur gegebenen ID gehörende Nachricht zurück
	 *
	 * @param id Die MessageID
	 * @return Die zur ID gehörige Nachricht
	 */
	public String getMessage(int id) {
		if (messages.containsKey(id)) {
			return messages.get(id).getMessage();
		} else {
			return "";
		}
	}

	/**
	 * Gibt die zur gegebenen TrainRoute gehörende Nachricht zurück
	 *
	 * @param route
	 * @return Die zur gegebenen TrainRoute gehörende Nachricht
	 */
	public String getMessage(TrainRoute route) {
		if (route == null) {
			throw new IllegalArgumentException("Route darf nicht null sein!");
		}

		return getMessage(route.getMessageId());
	}

	/**
	 * Gibt die zum gegebenen Stop gehörende Nachricht zurück
	 *
	 * @param stop
	 * @return Die zum gegebenen Stop gehörende Nachricht
	 */
	public String getMessage(Stop stop) {
		if (stop == null) {
			throw new IllegalArgumentException("Stop darf nicht null sein!");
		}

		return getMessage(stop.getMessageId());
	}

	/**
	 * Löscht die bisherige Datenstruktur
	 */
	public void resetData() {
		data = new TimetableData();
	}

	/**
	 * @return Die Map mit allen Messages
	 */
	public Map<Integer, Message> getMessageMap() {
		return messages;
	}

	@Autowired
	public TimetableController(CommonConfig config) {
		this.fisConfig = config;
		try {
			resetData();

		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		try {
			this.messages = CSVMessageLoader.loadCSV(config.getMessagecsvpath());
		} catch (ConfigurationException e) {
			this.messages = new HashMap<>();
			LOGGER.error(e.getMessage());
		}
	}

	/**
	 * @return Aktuelle Laborzeit (falls verfügbar)
	 */
	public LocalTime getTime() {
		return time;
	}

	/**
	 * Setzen der aktuellen Laborzeit
	 *
	 * @param time
	 */
	public void setTime(LocalTime time) {
		this.time = time;
	}

	/**
	 * @return "Rohe" Fahrplandatenstruktur
	 */
	public TimetableData getData() {
		return data;
	}

	/**
	 * @return Stringrepräsentation des aktuellen ConnectionStates
	 */
	public String getStateName() {
		switch (receiver.getConnectionStatus()) {
			case OFFLINE:
				return "Offline";
			case ONLINE:
				return "Online";

			default:
				return "Connecting";
		}
	}

	/**
	 * @see TimetableData#getTrainRoutes
	 */
	public List<TrainRoute> getTrainRoutes() {
		return data.getTrainRoutes();
	}

	/**
	 * @see TimetableData#getStations
	 */
	public List<Station> getStations() {
		return data.getStations();
	}

	/**
	 * @see TimetableData#getTrainCategories
	 */
	public List<TrainCategory> getTrainCategories() {
		return data.getTrainCategories();
	}

	/**
	 * Liefert alle {@link TrainCategory}s, die einen bestimmten
	 * Verwendungszweck erfüllen.
	 *
	 * @param use der zu erfüllende Verwendungszweck.
	 * @return eine (ggf. leere) Liste von {@link TrainCategory}s
	 */
	public List<TrainCategory> getTrainCategories(String use) {
		List<TrainCategory> ret = new ArrayList<>(data.getTrainCategories());
		ret.removeIf(new TrainUsagePredicate(use));
		return ret;
	}

	/**
	 * Liefert eine {@link TrainCategory} anhand ihrer ID.
	 *
	 * @param id die ID, nach der gesucht wird
	 * @return die gesuchte {@link TrainCategory} oder {@literal null}, falls
	 * keine passende gefunden wurde
	 */
	public TrainCategory getTrainCategoryById(String id) {
		if (id == null) {
			throw new IllegalArgumentException("Accessing TrainCategory with ID null!");
		}

		for (TrainCategory tc : data.getTrainCategories()) {
			if (tc.getId().equals(id)) {
				return tc;
			}
		}

		return null;
	}

	/**
	 * @param station
	 * @return Alle Halte eines Bahnhofs, wenn station nicht null ist. Falls
	 * station null ist, gibt die Funktion eine leere Liste zurück.
	 * @see Station#getStops()
	 */
	public List<Stop> getStopsByStation(Station station) {
		if (station == null) {
			return new ArrayList<Stop>();
		}
		return station.getStops();
	}

	/**
	 * Gibt alle Zugläufe eines Bahnhofs aus
	 *
	 * @param station
	 * @param type    Je nach {@link FilterType} gibt die Funktion alle Zugläufe,
	 *                ein- oder ausfahrende Zugläufe an.
	 * @return Alle Zugläufe eines Bahnhofs, sofern station nicht null ist
	 * (sonst leere Liste)
	 */
	public Set<TrainRoute> getTrainRoutesByStation(Station station, FilterType type) {
		if (station == null) {
			return new HashSet<TrainRoute>();
		}

		HashSet<TrainRoute> set = new HashSet<TrainRoute>();
		for (Stop stop : station.getStops()) {
			if ((type == FilterType.ARRIVAL && stop.getStopType() != StopType.BEGIN)
					|| (type == FilterType.DEPARTURE && stop.getStopType() != StopType.END)
					|| (type == FilterType.ANY)) {

				set.add(stop.getTrainRoute());
			}
		}
		return set;
	}

	/**
	 * Aktualisiert eine bereits existierende TrainRoute oder fügt eine neue
	 * hinzu, je nachdem, ob bereits eine TrainRoute mit der ID der übergebenen
	 * TrainRoute in der Datenstruktur existiert
	 *
	 * @param newRoute
	 */
	public void updateTrainRoute(TrainRoute newRoute) {
		if (newRoute == null) {
			throw new IllegalArgumentException("newRoute darf nicht null sein!");
		}

		for (TrainRoute route : data.getTrainRoutes()) {
			if (route.getId().equals(newRoute.getId())) {
				//trainroute already exists
				route.removeStops();
				route.addStops(newRoute.getStops());
				route.setTrainCategory(newRoute.getTrainCategory());
				route.setTrainNumber(newRoute.getTrainNumber());
				return;
			}
		}

		// route doesn't exist yet
			// neue TrainRoute hinzufügen
			this.data.addTrainRoute(newRoute);
	}

	/**
	 * Verarbeitet einkommende Telegram-Objekte und entscheidet über das
	 * spezifische Vorgehen
	 *
	 * @param event
	 */
	@EventListener
	public void forwardTelegram(TelegramParsedEvent event) {
		Telegram telegram = event.getSource();

		if (event.getSource() == null)
			throw new IllegalArgumentException();
		if (telegram.getClass().equals(LabTimeTelegram.class)) {
			setTime(((LabTimeTelegram) telegram).getTime());
		}

		if (telegram.getClass().equals(TrainRouteTelegram.class)) {
			updateTrainRoute(createTrainRouteFromTelegram((TrainRouteTelegram) telegram));
		}

		if (telegram.getClass().equals(StationNameTelegram.class)) {
			String id = Byte.toString((((StationNameTelegram) telegram).getId()));
			String shortName = ((StationNameTelegram) telegram).getCode();
			String longName = ((StationNameTelegram) telegram).getName();
			float x = ((StationNameTelegram) telegram).getX() * 100;
			float y = ((StationNameTelegram) telegram).getY() * 100;

			Station station = new Station(id, longName, shortName, x, y);
			LOGGER.debug("Created Station. X: " + x + ", Y: " + y);
			data.addStation(station);
		}

	}

	/**
	 * Ist beim Wechsel Offline-Fahrplan <--> Telegramme dafür zuständig, dass
	 * sich die Daten nicht vermischen, sondern vorher bereinigt werden
	 *
	 * @param event Das geworfene TimetableEvent
	 */
	@EventListener
	public void receiveEvent(TimetableEvent event) {
		LOGGER.debug("\n received Event " + event.getType() + "\n");
		switch (event.getType()) {
			case cleanup:
				// Löschen der bisherigen Datenstruktur
				setTime(null);
				resetData();
				break;
			case parseRailML:
				resetData();
				loadML();
				break;
			default:
				break;
		}
	}

	/**
	 * Lädt die RailML in die Datenstruktur
	 */
	public void loadML() {
		try {
			// Laden des Offline-Fahrplans
			LOGGER.info("Offline. Laden des RailML-Fahrplans.");
			if (fisConfig.getRailmlpath() == null) {
				throw new ConfigurationException("ungültiger Pfad zur RailML");
			}
			data = RailML2Data.loadML(fisConfig.getRailmlpath());
		} catch (IOException e) {
			LOGGER.error("Fehler beim Laden des RailML-Fahrplans! \n" + e.getMessage());
			e.printStackTrace();
		} catch (JAXBException e) {
			LOGGER.error("Fehler beim Laden des RailML-Fahrplans! \n" + e.getMessage());
			e.printStackTrace();
		} catch (ConfigurationException e) {
			LOGGER.error(e.getMessage());
		}
	}

	/**
	 * Erzeugt ein "richtiges" {@link TrainRoute}-Objekt aus dem rohen
	 * TrainRouteData-Objekt des empfangenen {@link TrainRouteTelegram}s
	 *
	 * @param tel
	 * @return Erzeugtes {@link TrainRoute}-Objekt
	 */
	public TrainRoute createTrainRouteFromTelegram(TrainRouteTelegram tel) {
		if (tel == null) {
			throw new IllegalArgumentException("Das Telegram darf nicht null sein!");
		}
		TrainRouteData routeData = tel.getData();

		if (routeData == null) {
			throw new IllegalArgumentException("Das Telegram muss ein TrainRouteData-Objekt besitzen");
		}
		String trainNr = routeData.getTrainNumber();
		int messageId = routeData.getMessageId();
		List<Stop> routeStops = new ArrayList<Stop>();

		for (StopData stopData : routeData.getStopDataList()) {
			StopType type = StopType.STOP;
			if (routeData.getStopDataList().indexOf(stopData) == 0) {
				type = StopType.BEGIN;
			} else if (routeData.getStopDataList().indexOf(stopData) == routeData.getStopDataList().size() - 1) {
				type = StopType.END;
			}
			Stop stop = new Stop(data.getStationById("" + stopData.getStationId()), type,
					stopData.getScheduledArrival(), stopData.getScheduledDeparture(), "" + stopData.getScheduledTrack(),
					stopData.getMessageId());
			stop.updateArrival(stopData.getActualArrival());
			stop.updateDeparture(stopData.getActualDeparture());
			stop.updateTrack("" + stopData.getActualTrack());
			stop.setActualArrivalNextDay(stopData.getActualArrivalNextDay());
			stop.setActualDepartureNextDay(stopData.getActualDepartureNextDay());
			routeStops.add(stop);
		}

		TrainCategory cat;
		if (data.getTrainCategoryById(routeData.getTrainCategoryShort()) == null) {
			// Nur TrainRoutes mit "PASSENGER" werden angezeigt
			cat = new TrainCategory(routeData.getTrainCategoryShort(), routeData.getTrainCategoryShort(),
					routeData.getTrainCategoryShort(), "PASSENGER");
			data.addTrainCategory(cat);
		} else {
			cat = data.getTrainCategoryById(routeData.getTrainCategoryShort());
		}

		TrainRoute route = new TrainRoute("" + trainNr, trainNr, cat, routeStops, messageId);

		return route;
	}

}
