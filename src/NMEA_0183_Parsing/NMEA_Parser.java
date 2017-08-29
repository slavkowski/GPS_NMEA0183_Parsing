package NMEA_0183_Parsing;

import gnu.io.*;
import java.io.*;
import java.util.Enumeration;



public class NMEA_Parser {

    public String data;
    public String lat;
    public String lon;
    public String axisLat;
    public String axisLon;
    public String time;
    public String fixQuality;
    public String numberOfSateliteIsBeingTracked;
    public String PDOP;
    public String HDOP;
    public String VDOP;
    public String altitude;
    public String geoid;


    public void ReadFileFromCOM(String myCOM){

        Enumeration portIdentifiers = CommPortIdentifier.getPortIdentifiers();
        CommPortIdentifier portId = null;

        while (portIdentifiers.hasMoreElements())
        {
            CommPortIdentifier pid = (CommPortIdentifier) portIdentifiers.nextElement();
            if(pid.getPortType() == CommPortIdentifier.PORT_SERIAL &&
                    pid.getName().equals(myCOM))
            {
                portId = pid;
                break;
            }
        }
        if(portId == null)
        {
            System.err.println("Could not find serial port " + myCOM);
            System.exit(1);
        }
//***********************************************************************************
        SerialPort port = null;
        try {
            port = (SerialPort) portId.open(
                    "GPS_VIEW", // Name of the application asking for the port
                    1000   // Wait max. 10 sec. to acquire port
            );
        } catch(PortInUseException e) {
            System.err.println("Port already in use: " + e);
            System.exit(1);
        }
//************************************************************************************
        int baudRate = 4800;
        try {
            port.setSerialPortParams(
                    baudRate,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
        } catch (UnsupportedCommOperationException ex) {
            System.err.println(ex.getMessage());
        }
        try {
            port.setFlowControlMode(
                    SerialPort.FLOWCONTROL_NONE);
            // OR
            // If CTS/RTS is needed
            //serialPort.setFlowControlMode(
            //      SerialPort.FLOWCONTROL_RTSCTS_IN |
            //      SerialPort.FLOWCONTROL_RTSCTS_OUT);
        } catch (UnsupportedCommOperationException ex) {
            System.err.println(ex.getMessage());
        }
//**************************************************************************************
        InputStream is = null;
        StringBuilder sb = new StringBuilder("");
        String lineGPS;
        GeoData gd = new GeoData();
        int i = 0;
        try {
            is = port.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true){
            try {


                int signINT = is.read();

                if(signINT>0&&signINT<=127) {

                    if (signINT==36){
                        sb = new StringBuilder("");
                        sb.append((char)signINT);
                    } else if (signINT==13){
                        lineGPS = sb.toString();
//                        System.out.println(lineGPS);
                        if (NMEA_Utilities.checkSum(lineGPS)) {
                            i++;
                            switch (lineGPS.substring(3, 6)) {
                                case "GGA":
                                    gd.setTime(ParseMethods.parseGGA(lineGPS.substring(lineGPS.indexOf("$"),lineGPS.indexOf("*"))).getTime());
                                    gd.setLon(ParseMethods.parseGGA(lineGPS.substring(lineGPS.indexOf("$"),lineGPS.indexOf("*"))).getLon());
                                    gd.setAxisLon(ParseMethods.parseGGA(lineGPS.substring(lineGPS.indexOf("$"),lineGPS.indexOf("*"))).getAxisLon());
                                    gd.setLat(ParseMethods.parseGGA(lineGPS.substring(lineGPS.indexOf("$"),lineGPS.indexOf("*"))).getLat());
                                    gd.setAxisLat(ParseMethods.parseGGA(lineGPS.substring(lineGPS.indexOf("$"),lineGPS.indexOf("*"))).getAxisLat());
                                    gd.setFixQuality(ParseMethods.parseGGA(lineGPS.substring(lineGPS.indexOf("$"),lineGPS.indexOf("*"))).getFixQuality());
                                    gd.setNumberOfSateliteIsBeingTracked(ParseMethods.parseGGA(lineGPS.substring(lineGPS.indexOf("$"),lineGPS.indexOf("*"))).getNumberOfSateliteIsBeingTracked());
                                    gd.setNumberOfSateliteIsBeingTracked(ParseMethods.parseGGA(lineGPS.substring(lineGPS.indexOf("$"),lineGPS.indexOf("*"))).getNumberOfSateliteIsBeingTracked());
                                    gd.setHDOP(ParseMethods.parseGGA(lineGPS.substring(lineGPS.indexOf("$"),lineGPS.indexOf("*"))).getHDOP());
                                    gd.setAltitude(ParseMethods.parseGGA(lineGPS.substring(lineGPS.indexOf("$"),lineGPS.indexOf("*"))).getAltitude());
                                    gd.setGeoid(ParseMethods.parseGGA(lineGPS.substring(lineGPS.indexOf("$"),lineGPS.indexOf("*"))).getGeoid());
                                    break;
                                case "GSA":
                                    gd.setPDOP(ParseMethods.parseGSA(lineGPS.substring(lineGPS.indexOf("$"),lineGPS.indexOf("*"))).getPDOP());
                                    gd.setHDOP(ParseMethods.parseGSA(lineGPS.substring(lineGPS.indexOf("$"),lineGPS.indexOf("*"))).getHDOP());
                                    gd.setVDOP(ParseMethods.parseGSA(lineGPS.substring(lineGPS.indexOf("$"),lineGPS.indexOf("*"))).getVDOP());


                                    break;
//                    case "GSV":
//                        ParseGSV(line);
//                    case "RMC":
//                        ParseRMC(line);
//                    case "VTG":
//                        ParseVTG(line);
//                    case "GLL":
//                        ParseGLL(line);
                            }

//**************************   MIEJSCE NA UPDATOWANIE ZMIENNYCH DO GUI*****************************************************


//*********************************************************************************************************************

                            System.out.println(i +" "+ gd.getTime() + " UTC * " + " Lat: " + gd.getLat() + gd.getAxisLat() + " Lon: " + gd.getLon() + gd.getAxisLon() +
                                    " * GPS status: " + gd.getFixQuality() + " * Satelites being tracked: " + gd.getNumberOfSateliteIsBeingTracked() +
                                    " * PDOP: " + gd.getPDOP() + " * VDOP: " + gd.getVDOP() +
                                    " * HDOP: " + gd.getHDOP() + " * Altitude: " + gd.getAltitude() + " * Height of geoid above WGS84 elipsoid: " + gd.getGeoid());




                        }

                    }else{
                        sb.append((char)signINT);
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            }


        }



    }





    public void ReadFile() {
        String line;
        BufferedReader br = null;
        try {

            FileReader file = new FileReader("d:/NMEA_BU.txt");
            br = new BufferedReader(file);
//            ********************************************************************************************************
           int i = 0;
            while ((line = br.readLine()) != null) {
                i++;
                if (NMEA_Utilities.checkSum(line)) {
                    switch (line.substring(3, 6)) {
                        case "GGA":
                            ParseMethods.parseGGA(line.substring(line.indexOf("$"),line.indexOf("*")));
                            break;
                        case "GSA":
                            ParseMethods.parseGSA(line.substring(line.indexOf("$"),line.indexOf("*")));
                            break;
//                    case "GSV":
//                        ParseGSV(line);
//                    case "RMC":
//                        ParseRMC(line);
//                    case "VTG":
//                        ParseVTG(line);
//                    case "GLL":
//                        ParseGLL(line);
                    }

//**************************   MIEJSCE NA UPDATOWANIE ZMIENNYCH DO GUI*****************************************************


//*********************************************************************************************************************

//                    System.out.println(i + time + " UTC * " + " Lat: " + lat + axisLat + " Lon: " + lon + axisLon +
//                            " * GPS status: " + fixQuality + " * Satelites being tracked: " + numberOfSateliteIsBeingTracked +
//                            " * PDOP: " + PDOP + " * VDOP: " + VDOP +
//                            " * HDOP: " + HDOP + " * Altitude: " + altitude + " * Height of geoid above WGS84 elipsoid: " + geoid);

                    System.out.println(i + time + " UTC * " + " Lat: " + lat + axisLat + " Lon: " + lon + axisLon +
                            " * GPS status: " + fixQuality + " * Satelites being tracked: " + numberOfSateliteIsBeingTracked +
                            " * PDOP: " + PDOP + " * VDOP: " + VDOP +
                            " * HDOP: " + HDOP + " * Altitude: " + altitude + " * Height of geoid above WGS84 elipsoid: " + geoid);

//                System.out.println(line);
//
//                System.out.println(NMEA_Utilities.checkSum(line));
                }
            }
//            *********************************************************************************************************
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }


    }

















    private void parseGGA(String line) {
       String aLine[] = line.split(",");
       time = aLine[1];
       lat = aLine[2];
       axisLat = aLine[3];
       lon = aLine[4];
       axisLon = aLine[5];
       switch (aLine[6]){
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
               fixQuality ="unknown";
               break;
       }
       numberOfSateliteIsBeingTracked = aLine[7];
       HDOP = aLine[8];
       altitude = aLine[9];
       geoid = aLine[11];
    }


}
