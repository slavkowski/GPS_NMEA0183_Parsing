package NMEA_0183_Parsing;


import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

//          String record = "$GPGSA,,5";
//          NMEA_Utilities testRecordLine = new NMEA_Utilities();
//          ArrayList<String> testArray = new ArrayList<>();
//          testArray = testRecordLine.splitRecord(record);
//        int k = 0;
//        for (String arrayList :testArray) {
//            k++;
//            System.out.println(arrayList);
//        }
//        System.out.println(k);

        NMEA_Parser NMEA = new NMEA_Parser();

//        NMEA.ReadFile();
        NMEA.ReadFileFromCOM("COM3");

    }

}
