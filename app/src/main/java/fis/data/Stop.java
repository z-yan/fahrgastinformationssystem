package fis.data;

import java.time.LocalTime;

public class Stop {
	private Station station;
	private LocalTime ScheduledDeparture;
	private LocalTime ScheduledArrival;
	private LocalTime ActualArrival;
	private LocalTime ActualDeparture;
	
	private String ScheduledTrack;
	private String ActualTrack;
	private String Message; //Hier bin ich noch unsicher, was dort genau rein soll
	private StopType stopType;
	private TrainRoute trainRoute;
	
	public Stop(Station station,StopType stopType,LocalTime ScheduledArrival, LocalTime ScheduledDeparture, String ScheduledTrack){
		this.station=station;
		this.stopType=stopType;
		this.ScheduledArrival=ScheduledArrival;
		this.ScheduledDeparture=ScheduledDeparture;
		this.ScheduledTrack=ScheduledTrack;	
		
		this.ActualArrival=ScheduledArrival;
		this.ActualDeparture=ScheduledDeparture;
		this.ActualTrack=ScheduledTrack;
		
		station.addStop(this);	
	}
	public Station getStation(){return station;}
	
	public void setTrainRoute(TrainRoute route){
		this.trainRoute=route;
	}
	
	public void deleteStop(){
		if(trainRoute!=null){
			trainRoute.getStops().remove(this);
		}
		
		if(station!=null){
			station.getStops().remove(this);
		}
		
	}
	
	public StopType getStopType(){
		return stopType;
	}
	
	
	public void updateStopType(StopType newStopType){
		this.stopType=newStopType;
	}
	
	public void updateArrival(LocalTime ActualArrival){
		this.ActualArrival=ActualArrival;
	}
	
	public void updateDeparture(LocalTime ActualDeparture){
		this.ActualDeparture=ActualDeparture;
	}
	
	public void updateTrack(String ActualTrack){
		this.ActualTrack=ActualTrack;
	}
	
	public LocalTime getScheduledDeparture(){return ScheduledDeparture;}
	public LocalTime getScheduledArrival(){return ScheduledArrival;}
	public LocalTime getActualDeparture(){return ActualDeparture;}
	public LocalTime getActualArrival(){return ActualArrival;}
	public String getScheduledTrack(){return ScheduledTrack;}
	public String getActualTrack(){return ActualTrack;}
	

	
	public void printDebugInformation(){
		//erstmal nur zum Testen
		System.out.println("---");
		if(station!=null){
			System.out.println("Station: "+station.getName());
		} else {
			System.out.println("!!STATION: NULL!!");
		}
		System.out.println("Train Number: ");
		System.out.println("Scheduled Arrival: "+ScheduledArrival);
		System.out.println("Scheduled Departure: "+ScheduledDeparture);
		System.out.println("Scheduled Track: "+ScheduledTrack);
	}
	
}
