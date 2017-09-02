package gps_gui;


import java.util.ArrayList;

public class GeoData {
    private String lat;
    private String lon;
    private String axisLat;
    private String axisLon;
    private String time;
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
    }











    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getAxisLat() {
        return axisLat;
    }

    public void setAxisLat(String axisLat) {
        this.axisLat = axisLat;
    }

    public String getAxisLon() {
        return axisLon;
    }

    public void setAxisLon(String axisLon) {
        this.axisLon = axisLon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFixQuality() {
        return fixQuality;
    }

    public void setFixQuality(String fixQuality) {
        this.fixQuality = fixQuality;
    }

    public String getNumberOfSateliteIsBeingTracked() {
        return numberOfSatelliteIsBeingTracked;
    }

    public void setNumberOfSateliteIsBeingTracked(String numberOfSateliteIsBeingTracked) {
        this.numberOfSatelliteIsBeingTracked = numberOfSateliteIsBeingTracked;
    }

    public String getPDOP() {
        return PDOP;
    }

    public void setPDOP(String PDOP) {
        this.PDOP = PDOP;
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
