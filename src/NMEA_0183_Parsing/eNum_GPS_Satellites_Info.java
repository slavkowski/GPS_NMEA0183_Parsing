package NMEA_0183_Parsing;


public class eNum_GPS_Satellites_Info {


    public enum eNUM_GPS_info {
        GPS_13("43","Rb","IIR",""),
        GPS_11("43","Rb","IIR",""),
        GPS_20("43","Rb","IIR",""),
        GPS_28("43","Rb","IIR",""),
        GPS_14("43","Rb","IIR",""),
        GPS_18("43","Rb","IIR",""),
        GPS_16("43","Rb","IIR",""),
        GPS_21("43","Rb","IIR",""),
        GPS_22("43","Rb","IIR",""),
        GPS_19("43","Rb","IIR",""),
        GPS_23("43","Rb","IIR",""),
        GPS_02("43","Rb","IIR",""),

        GPS_17("43","Rb","IIR",""),
        GPS_31("43","Rb","IIR",""),
        GPS_12("43","Rb","IIR",""),
        GPS_15("43","Rb","IIR",""),
        GPS_29("43","Rb","IIR",""),
        GPS_07("43","Rb","IIR",""),
        GPS_05("43","Rb","IIR",""),
        GPS_25("43","Rb","IIR",""),
        GPS_01("43","Rb","IIR",""),
        GPS_24("43","Rb","IIR",""),
        GPS_27("43","Rb","IIR",""),
        GPS_30("43","Rb","IIR",""),
        GPS_06("43","Rb","IIR",""),
        GPS_09("43","Rb","IIR",""),
        GPS_03("43","Rb","IIR",""),
        GPS_26("43","Rb","IIR",""),
        GPS_08("43","Rb","IIR",""),
        GPS_10("43","Rb","IIR",""),
        GPS_32("43","Rb","IIR","");

        private final String svn;
        private final String clock;
        private final String block;
        private final String launchDate;

        public String getSvn() {
            return svn;
        }
        public String getClock() {
            return clock;
        }
        public String getBlock() {
            return block;
        }
        public String getLaunchDate() {
            return launchDate;
        }
        eNUM_GPS_info(String svn, String clock, String block, String launchDate) {
            this.svn = svn;
            this.clock = clock;
            this.block = block;
            this.launchDate = launchDate;

        }
    }
}
