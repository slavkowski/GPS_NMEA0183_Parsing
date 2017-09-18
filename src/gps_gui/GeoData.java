package gps_gui;


import java.util.ArrayList;

public class GeoData {
    private String lat;
    private String lon;
    private String axisLat;
    private String axisLon;
    private String time = "-1";
    private String fixQuality;
    private String numberOfSatelliteIsBeingTracked;
    private String PDOP;
    private String HDOP;
    private String VDOP;
    private String altitude;
    private String geoid;


    public GeoData() {
    }


    public void GSA(String line) {
        ArrayList<String> arrayWithDataFields = NMEA_Utilities.splitRecord(line);
        PDOP = arrayWithDataFields.get(15);
        HDOP = arrayWithDataFields.get(16);
        VDOP = arrayWithDataFields.get(17);
    }
    public void GGA(String line){
        ArrayList<String> arrayWithDataFields = NMEA_Utilities.splitRecord(line);
        time = arrayWithDataFields.get(1);

        lat = arrayWithDataFields.get(2);
        axisLat = arrayWithDataFields.get(3);
        lon = arrayWithDataFields.get(4);
        axisLon = arrayWithDataFields.get(5);
        switch (arrayWithDataFields.get(6)){
            case "0":
                fixQuality = "invalid";
                break;
            case "1":
                fixQuality = "GPS fix";
                break;
            case "2":
                fixQuality = "DGPS fix";
                break;
            default:
                fixQuality = "unknown";
                break;
        }
        numberOfSatelliteIsBeingTracked = arrayWithDataFields.get(7);
        HDOP = arrayWithDataFields.get(8);
        altitude = arrayWithDataFields.get(9);
        geoid = arrayWithDataFields.get(11);
    }











    public String getLat() {
        return lat;
    }



    public String getLon() {
        return lon;
    }


    public String getAxisLat() {
        return axisLat;
    }



    public String getAxisLon() {
        return axisLon;
    }



    public String getTime() {
        return time;
    }



    public String getFixQuality() {
        return fixQuality;
    }



    public String getNumberOfSateliteIsBeingTracked() {
        return numberOfSatelliteIsBeingTracked;
    }



    public String getPDOP() {
        return PDOP;
    }



    public String getHDOP() {
        return HDOP;
    }

    public void setHDOP(String HDOP) {
        this.HDOP = HDOP;
    }

    public String getVDOP() {
        return VDOP;
    }

    public void setVDOP(String VDOP) {
        this.VDOP = VDOP;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getGeoid() {
        return geoid;
    }

    public void setGeoid(String geoid) {
        this.geoid = geoid;
    }
}
