/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gps_gui;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.TooManyListenersException;

/**
 *
 * @author Francisco Scaramanga
 */
public class Communicator implements SerialPortEventListener{
    
    private Enumeration ports = null;
    //map the port names to CommPortIdentifiers
    private HashMap portMap = new HashMap();

    //this is the object that contains the opened port
    private CommPortIdentifier selectedPortIdentifier = null;
    private SerialPort serialPort = null;
    //input and output streams for sending and receiving data
    private InputStream input = null;
    private OutputStream output = null;

    //just a boolean flag that i use for enabling
    //and disabling buttons depending on whether the program
    //is connected to a serial port or not
    private boolean bConnected = false;

    //the timeout value for connecting with the port
    final static int TIMEOUT = 2000;
    //some ascii values for for certain things

    //a string for recording what goes on in the program
    //this string is written to the GUI
    String logText = "";
    GUI window = null;
    StringBuilder sb;
    String lineNMEA;
    private int numberOfsentencesGSV;
    private int sentenceGSV;
    private ArrayList<String[]> satellitesInViewAlmanac;
    private boolean newSentenceFlag = false;


    
     public Communicator(GUI window)
    {
        this.window = window;
    }
     
     public void searchForPorts()
    {
        ports = CommPortIdentifier.getPortIdentifiers();
        while (ports.hasMoreElements())
        {
            CommPortIdentifier curPort = (CommPortIdentifier)ports.nextElement();
            //get only serial ports
            if (curPort.getPortType() == CommPortIdentifier.PORT_SERIAL)
            {
                window.cBoxPorts.addItem(curPort.getName());
                portMap.put(curPort.getName(), curPort);
            }
        }
        logText = "Choose port and push CONNECT";
        window.txtLog.setForeground(Color.blue);
        window.txtLog.append(logText + "\n");
    }
    //connect to the selected port in the combo box
    //pre: ports are already found by using the searchForPorts method
    //post: the connected comm port is stored in commPort, otherwise,
    //an exception is generated
    public void connect()
    {
        String selectedPort = (String)window.cBoxPorts.getSelectedItem();
        selectedPortIdentifier = (CommPortIdentifier)portMap.get(selectedPort);
        CommPort commPort = null;
        try
        {
            //the method below returns an object of type CommPort
            commPort = selectedPortIdentifier.open("GPSEye", TIMEOUT);
            //the CommPort object can be casted to a SerialPort object
            serialPort = (SerialPort)commPort;
            //for controlling GUI elements
            setConnected(true);
            //logging
            logText = selectedPort + " opened successfully.";
            window.txtLog.setForeground(Color.black);
            window.txtLog.append(logText + "\n");
            serialPort.setSerialPortParams(
                    4800,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            freezeButtons();
            
        }
        catch (PortInUseException e)
        {
            logText = selectedPort + " is in use. (" + e.toString() + ")";
            window.txtLog.setForeground(Color.RED);
            window.txtLog.append(logText + "\n");
        }
        catch (Exception e)
        {
            logText = "Failed to open " + selectedPort + "(" + e.toString() + ")";
            window.txtLog.append(logText + "\n");
            window.txtLog.setForeground(Color.RED);
        }
    }

    //open the input and output streams
    //pre: an open port
    //post: initialized intput and output streams for use to communicate data
    public boolean initIOStream()
    {
        //return value for whather opening the streams is successful or not
        boolean successful = false;
        try {
            input = serialPort.getInputStream();
            successful = true;
            return successful;
        }
        catch (IOException e) {
            logText = "I/O Streams failed to open. (" + e.toString() + ")";
            window.txtLog.setForeground(Color.red);
            window.txtLog.append(logText + "\n");
            return successful;
        }
    }
    
    public void initListener()
    {
        try
        {
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        }
        catch (TooManyListenersException e)
        {
            logText = "Too many listeners. (" + e.toString() + ")";
            window.txtLog.setForeground(Color.red);
            window.txtLog.append(logText + "\n");
        }
    }

    final public boolean getConnected()
    {
        return bConnected;
    }

    public void setConnected(boolean bConnected)
    {
        this.bConnected = bConnected;
    }
    
    public void disconnect()
    {
        //close the serial port
        try
        {
            serialPort.removeEventListener();
            serialPort.close();
            input.close();
            setConnected(false);
            freezeButtons();
            logText = "Disconnected.";
            window.txtLog.setForeground(Color.red);
            window.txtLog.append(logText + "\n");
        }
        catch (Exception e)
        {
            logText = "Failed to close " + serialPort.getName() + "(" + e.toString() + ")";
            window.txtLog.setForeground(Color.red);
            window.txtLog.append(logText + "\n");
        }
    }
    int numberOfSamples = 0;
    @Override

    public void serialEvent(SerialPortEvent evt) {

        if (evt.getEventType() == SerialPortEvent.DATA_AVAILABLE)
        {
            try
            {
                try
                {
                   int signINT = input.read();
                   if(signINT>0 && signINT<=127) {
                       
                       window.txtGPSNMEA.append(Character.toString((char) signINT));



                       if (signINT==36){
                           sb = new StringBuilder("");
                           sb.append((char)signINT);
                       } else if (signINT==13){
                           lineNMEA = sb.toString();


                           if (NMEA_Utilities.checkSum(lineNMEA)) {
                               numberOfSamples++;
                               window.jTextArea9.setText(Integer.toString(numberOfSamples));
                               GeoData gd = new GeoData();
                               switch (lineNMEA.substring(3, 6))
                               {
                                   case "GGA":
                                       gd.GGA(lineNMEA);
                                       window.jTextArea4.setText(gd.getTime().substring(0, 2)+":"+gd.getTime().substring(2, 4)+":"+gd.getTime().substring(4, 6));
                                       window.jTextArea2.setText(gd.getLon()+""+gd.getAxisLon());
                                       window.jTextArea5.setText(gd.getLat()+""+gd.getAxisLat());
                                       window.jTextArea8.setText(gd.getAltitude()+gd.getAltitudeUnits());
                                       window.jTextArea7.setText(gd.getGeoid()+gd.getGeoidUnits());
                                       window.jTextArea3.setText(gd.getHDOP());
                                       window.jTextArea11.setText(gd.getNumberOfSateliteIsBeingTracked());
                                       window.jTextArea10.setText(gd.getFixQuality());
                                       break;
                                   case "GSA":
                                       gd.GSA(lineNMEA);
                                       window.jTextArea1.setText(gd.getPDOP());
                                       window.jTextArea3.setText(gd.getHDOP());
                                       window.jTextArea6.setText(gd.getVDOP());
                                       break;
                                   case "GSV":
                                       gd.GSV(lineNMEA);
                                       numberOfsentencesGSV=Integer.parseInt(gd.getNumberOfSentencesGSV());
                                       sentenceGSV=Integer.parseInt(gd.getSentenceOfGSV());

                                       if (sentenceGSV==1){
                                           satellitesInViewAlmanac = new ArrayList<>();
                                           newSentenceFlag = true;
                                       }

                                       if (newSentenceFlag){
                                           satellitesInViewAlmanac.addAll(gd.getSatellitesAlmanac());
                                       }
                                       if(sentenceGSV==numberOfsentencesGSV && newSentenceFlag){
                                       
                                           window.jProgressBar1.setValue(Integer.parseInt(satellitesInViewAlmanac.get(0)[3]));
                                           window.jTextArea13.setText(satellitesInViewAlmanac.get(0)[0]);
                                          

                                       }
                                       break;

//                    case "RMC":
//                        ParseRMC(line);
//                    case "VTG":
//                        ParseVTG(line);
//                    case "GLL":
//                        ParseGLL(line);
                               }
                           }
                       }else{
                           sb.append((char)signINT);
                       }


                   }
                } catch (IOException ex) {
                    logText = "Failed to read data1. (" + ex.toString() + ")";
                    window.txtLog.setForeground(Color.red);
                    window.txtLog.append(logText + "\n");
                    disconnect();
                }

            }
            catch (Exception e)
            {
                logText = "Failed to read data2. (" + e.toString() + ")";
                window.txtLog.setForeground(Color.red);
                window.txtLog.append(logText + "\n");
                disconnect();
            }
        }
    }
    public void freezeButtons()
    {
        if (getConnected() == true)
        {
            window.jButton2.setEnabled(true);
            window.jButton1.setEnabled(false);
            window.cBoxPorts.setEnabled(false);
        }
        else
        {
            window.jButton2.setEnabled(false);
            window.jButton1.setEnabled(true);
            window.cBoxPorts.setEnabled(true);
        }
    }
}
