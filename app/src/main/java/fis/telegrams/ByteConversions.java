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
package fis.telegrams;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.time.Duration;
import java.time.LocalTime;

/**
 * Kapselt alle Konvertierungs-Methoden für die Byte-Umwandlung
 *
 * @author Robert
 */
public abstract class ByteConversions {
	/**
	 * Magic number, die eine Zeit am Folgetag kennzeichnet
	 */
	public static final int NEXT_DAY_OFFSET = 20000;

	/**
	 * Konvertiert eine Ganzzahl in ein vorzeichenloses Byte.
	 *
	 * @param i die umzuwandelnde Ganzzahl
	 * @return das letzte Byte (LSB) dieser Zahl
	 */
	public static byte toUByte(int i) {
		return (byte) (i & 0xFF);
	}

	/**
	 * Konvertiert ein Byte in eine vorzeichenbehaftete ganze Zahl.
	 *
	 * @param b das Byte
	 * @return die ganze Zahl (-128 &lt; x &lt; 127)
	 */
	public static int toInt(byte b) {
		return (int) b;
	}

	/**
	 * Konvertiert ein Word in eine vorzeichenbehaftete Ganzzahl.
	 *
	 * @param b0           erstes Byte
	 * @param b1           zweites Byte
	 * @param littleEndian Bytereihenfolge
	 * @return die ganze Zahl
	 */
	public static int toInt(byte b0, byte b1, boolean littleEndian) {
		if (littleEndian) return (toInt(b1) << 8) | toUInt(b0);
		return (toInt(b0) << 8) | toUInt(b1);
	}

	/**
	 * Konvertiert ein Byte in eine positive ganze Zahl.
	 *
	 * @param b das umzuwandelnde Byte
	 * @return die ganze Zahl (0 &lt;= x &lt;= 255) mit dem übergeben als letztem Byte
	 */
	public static int toUInt(byte b) {
		return 0xFF & b;
	}

	/**
	 * Konvertiert ein Wort in eine positive ganze Zahl.
	 *
	 * @param b0           erstes Byte
	 * @param b1           zweites Byte
	 * @param littleEndian Bytereihenfolge
	 * @return die ganze Zahl (0 &lt; x &lt; 512) mit den übergebenen Bytes als LSBs
	 */
	public static int toUInt(byte b0, byte b1, boolean littleEndian) {
		if (littleEndian) return (toUInt(b1) << 8) | toUInt(b0);
		return (toUInt(b0) << 8) | toUInt(b1);
	}

	/**
	 * Berechnet einen Zeitpunkt aus Zehntelminutenangaben.
	 *
	 * @param tenth Die Zehntelminuten (u.U. am nächsten Tag)
	 * @return der Zeitpunkt als Uhrzeit
	 * @throws TelegramParseException wenn negative Zehntelminuten übergeben werden.
	 */
	public static LocalTime fromTenthOfMinute(int tenth) throws TelegramParseException {
		return fromTenthOfMinute(tenth, null);
	}

	/**
	 * Berechnet einen Zeitpunkt aus dem Unterschied zu einem Referenzzeitpunkt.
	 *
	 * @param tenth der Zeitunterschied in Zehntelminuten (kann auch negativ sein)
	 * @param base  der Referenzzeitpunkt oder {@literal null}, wenn nur ein Zeitpunkt bestimmt werden soll (dann auch Folgetag möglich).
	 * @return berechneter Zeitpunkt
	 * @throws TelegramParseException wenn ein negativer Zeitunterschied ohne Referenzzeitpunkt angegeben wurde.
	 */
	public static LocalTime fromTenthOfMinute(int tenth, LocalTime base) throws TelegramParseException {
		boolean isNegative = (tenth < 0);
		tenth = (isNegative) ? -tenth : tenth;

		if (base == null) {
			if (isNegative) {
				throw new TelegramParseException("Zeitpunkt kann nicht negativ sein.");
			}

			// Folgetag
			if (tenth >= NEXT_DAY_OFFSET) {
				tenth -= NEXT_DAY_OFFSET;
			}
			return LocalTime.ofSecondOfDay(tenth * 6);
		}

		Duration delta = Duration.ofSeconds(tenth * 6);
		return (isNegative) ? base.minus(delta) : base.plus(delta);
	}

	/**
	 * gibt zurück, ob Basiszeit + Zeitdifferenz auf den nächsten Tag fällt
	 * @param tenthDifference Zeitdifferenz in Zehntelminuten
	 * @param base Basiszeit
	 * @return Gesamtzeit fällt auf nächsten Tag
	 */
	public static boolean isNextDay(int tenthDifference, LocalTime base) {
		return ( (toTenthOfMinute(LocalTime.of(23, 50)) + 1) - toTenthOfMinute(base) <= tenthDifference);
	}

	/**
	 * gibt zu einer angegebenen LocalTime die Zeit im Zehntelminutenformat zurück
	 * auf Zehntelminuten abgerundet
 	 * @param time LocalTime die umgerechnet werden soll
	 * @return Zeit in Zehntelminuten, auf Zehntelminuten abgerundet
	 */
	public static int toTenthOfMinute(LocalTime time) {
		return (time.toSecondOfDay())/6;
	}

	/**
	 * Wandelt ein Array von Ganzzahlen in ein Array von Bytes um.
	 *
	 * @param arr das umzuwandelnde Array
	 * @return Array mit den LSBs der Zahlen.
	 */
	public static byte[] fromIntArr(int[] arr) {
		byte[] ret = new byte[arr.length];
		for (int i = 0; i < arr.length; i++) {
			ret[i] = toUByte(arr[i]);
		}
		return ret;
	}

	/**
	 * Wandelt eine Zeichenkette mit Hexdumps in ein Bytearray um.
	 *
	 * @param s die Zeichenkette
	 * @return das Bytearray.
	 */
	public static byte[] fromString(String s) {
		// Präfix
		if (s.startsWith("0x")) {
			s = s.substring(2);
		}

		// Leerraum entfernen
		s = s.replaceAll("\\s", "");
		// Byte-Gruppen
		if (s.length() % 2 != 0) {
			throw new IllegalArgumentException("Not a valid Byte-String (uneven number of nibbles!");
		}

		// Array erstellen
		byte[] ret = new byte[s.length() / 2];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = (byte) (Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16) & 0xFF);
		}

		return ret;
	}

	/**
	 * Wandelt mehrere Zeichenketten in ein Bytarray um.
	 *
	 * @param arr Zeichenketten, die direkt hintereinander ausgewertet werden
	 * @return das Bytearray.
	 */
	public static byte[] fromString(String... arr) {
		return fromString(String.join("", arr));
	}

	/**
	 * Wandelt ein Byte in eine hexadezimale Zeichenkette um.
	 *
	 * @param b das Byte
	 * @return die hexadezimale Repräsentation
	 */
	public static String toByteString(byte b) {
		return String.format("%02X", b);
	}

	/**
	 * Wandelt ein Bytearray in einen Hexdump um.
	 *
	 * @param arr das Bytearray
	 * @return die hexadezimale Repräsentation
	 */
	public static String toByteString(byte[] arr) {
		return toByteString(arr, 0);
	}

	/**
	 * Wandelt ein Bytearray in einen Hexdump fester Länge um.
	 *
	 * @param arr das Bytearray
	 * @param len die minimale Länge der Ausgabe.
	 * @return die hexadezimale Repräsentation
	 */
	public static String toByteString(byte[] arr, int len) {
		String s = "";
		for (byte b : arr) {
			s += toByteString(b) + " ";
		}

		while (arr.length < len) {
			s += "00 ";
			len--;
		}

		return s.trim();
	}

	public static float toFloat(byte[] in, boolean littleEndian) {
		if (in.length != 4) {
			throw new IllegalArgumentException("Integer benötigt genau 4 Bytes.");
		}

		ByteOrder o = (littleEndian) ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN;
		return ByteBuffer.wrap(in).order(o).getFloat();
	}
}
