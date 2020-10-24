package clientML;

import java.io.*;
import java.net.Socket;

public class TCPClient { //Client repräsentiert eine Verbindung zu einem Server
    private static final String IRGENDWAS = "irgendwas";
    private final String hostname; //brauche Namen  und Port von Server um Verbindung aufzubauen
    private final int port;

    public static final String HOST = "localhost";
    public static final int PORT = 7777;

    public static void main (String [] args) throws IOException {
        if(args.length < 2) {
            System.out.println("missing parameters: hostname, portnumber");
        }

        String fileName = null;
        String hostname = args[0];
        String portString = args[1];

        if(args.length > 2) {
            fileName = args[2];
        }

        int portnumber = Integer.parseInt(portString);
        TCPClient tcpClient = new TCPClient(hostname, portnumber); //neues Objekt Client angelegt

        if(fileName != null) {
            tcpClient.copyFile(fileName);
        }else {
           // tcpClient.doSomething();
            long timeStamp = System.currentTimeMillis();
            float value = (float) 42.0;
            String sensorName = "Sensor A";
            tcpClient.sendSensorDate(timeStamp, value, sensorName);

        }
    }

    TCPClient (String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    private void sendSensorDate(long timeStamp, float value, String sensorName) throws IOException {
        Socket socket = new Socket(this.hostname, this.port); //Socket für Server erzeugen

        OutputStream os = socket.getOutputStream();

        DataOutputStream daos = new DataOutputStream(os);
        daos.writeLong(timeStamp);
        daos.writeFloat(value);
        daos.writeUTF(sensorName);

        os.close();
    }

    private void copyFile(String fileName) throws IOException { //was ich machen muss um ein File von einem Client zu einem Server zu übertragen
        Socket socket = new Socket(this.hostname, this.port); //Socket für Server erzeugen

        FileInputStream fis = new FileInputStream(fileName);
        OutputStream os = socket.getOutputStream();

        int read = 0;
        do {
            read = fis.read(); //Byte vom Input Stream lesen
            if(read != -1) { //und in den Socket schreiben
                os.write(read);
            }
        } while (read != -1);
        os.close();
    }

    private void doSomething() throws IOException {
        Socket socket = new Socket(this.hostname, this.port); //Verbindung zum Server herstellen mHv Socket
        socket.getOutputStream().write(IRGENDWAS.getBytes()); //Zeichenfolge an den Server senden; getBytes weil wir Bytes schicken

        InputStream is = socket.getInputStream();  //Rückgabewerte empfangen

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        //byte[] buffer = new byte[10000];
        int i = 0;

        int read = 0;
        do {
            read = is.read();
            if(read != -1) { //wenn ich -1 lese ist es ein Symbol dafür dass der Server nichts mehr schickt
                byte readByte = (byte) read;
               // buffer[i++] = readByte;
                baos.write(read);
            }
        } while (read != -1);

        baos.toByteArray();
       /*byte[] receivedBytes = new byte[i];
       for(int j = 0; j < i; j++) {
           receivedBytes[j] = buffer[j];
       }


       String receivedString = new String(receivedBytes);
        */

        String receivedString = new String(baos.toByteArray());

       System.out.println("received: " + receivedString);

    }
}
