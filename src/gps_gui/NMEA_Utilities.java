package gps_gui;

import java.util.ArrayList;

public class NMEA_Utilities {
//************************ CHECKSUM**************************************************************************************
    public static boolean checkSum(String line){
        String strCheckSum;
        if (line.contains("$") && line.contains("*")){
            int endOfLine = line.indexOf("*");
            int cs = 0;
            for (int i = 1; i < endOfLine; i++) {
                cs = cs ^ line.charAt(i);
            }
            strCheckSum = Integer.toHexString(cs).toUpperCase();
            if (strCheckSum.equals(line.substring(endOfLine+1,endOfLine+3))) {
                return true;
            }
            else{
                return false;
            }
        }else{
            return false;
        }
    }
//*********************************************************************************************************************
     public static ArrayList splitRecord(String record){
        ArrayList<String> arrayWithSplitedDataFields = new ArrayList<>();
        StringBuilder sB = new StringBuilder("");
        
         for (int i = 0; i < record.length()-3; i++) {
             if(record.charAt(i)==','){
                 if(sB.length()>0){
                     arrayWithSplitedDataFields.add(sB.toString());
                 }else{
                     arrayWithSplitedDataFields.add("-1");
                 }

                 sB.delete(0,sB.length());
             }else{
                 sB.append(record.charAt(i));
             }
         }
         if(sB.length()>0){
             arrayWithSplitedDataFields.add(sB.toString());
         }else{
             arrayWithSplitedDataFields.add("-1");
         }
         return arrayWithSplitedDataFields;
     }
}

