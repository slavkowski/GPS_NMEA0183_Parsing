package NMEA_0183_Parsing;


import java.util.ArrayList;

public class ParseMethods {


    public static GeoData parseGSA(String line) {
        GeoData gdGSA = new GeoData();
        ArrayList<String> arrayWithDataFields = NMEA_Utilities.splitRecord(line);

        gdGSA.setPDOP(arrayWithDataFields.get(15));
        gdGSA.setHDOP(arrayWithDataFields.get(16));
        gdGSA.setVDOP(arrayWithDataFields.get(17));
        return gdGSA;
    }

    public static GeoData parseGGA(String line) {
        GeoData gdGGA = new GeoData();
        ArrayList<String> arrayWithDataFields = NMEA_Utilities.splitRecord(line);

        gdGGA.setTime(arrayWithDataFields.get(1));
        gdGGA.setLat(arrayWithDataFields.get(2));
        gdGGA.setAxisLat(arrayWithDataFields.get(3));
        gdGGA.setLon(arrayWithDataFields.get(4));
        gdGGA.setAxisLon(arrayWithDataFields.get(5));
        switch (arrayWithDataFields.get(6)){
            case "0":
                gdGGA.setFixQuality("invalid");
                break;
            case "1":
                gdGGA.setFixQuality("GPS fix");
                break;
            case "2":
                gdGGA.setFixQuality("DGPS fix");
                break;
            default:
                gdGGA.setFixQuality("unknown");
                break;
        }
        gdGGA.setNumberOfSateliteIsBeingTracked(arrayWithDataFields.get(7));
        gdGGA.setHDOP(arrayWithDataFields.get(8));
        gdGGA.setAltitude(arrayWithDataFields.get(9));
        gdGGA.setGeoid(arrayWithDataFields.get(11));
        return gdGGA;
    }

}
