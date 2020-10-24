package serverML;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    private final int port;
    public static final int PORTNUMBER = 7777;

    public static void main (String [] args) throws IOException, InterruptedException { //main damit es ablaufen kann
        TCPServer tcpServer = new TCPServer(PORTNUMBER); //Erzeugung eines Objekts vom Typ TCPServer
        
        if(args.length ==1) {
            tcpServer.readFile(args[0]);
        } else {
            //tcpServer.doSomething();
            tcpServer.receiveSensorDate();

        }
    }

    TCPServer (int port) { //Konstruktor
        this.port = port;  //mit Port an dem der Server lauschen kann
    }

    private void receiveSensorDate() throws IOException {
        Socket socket = this.acceptSocket();
        InputStream is = socket.getInputStream();

        DataInputStream dais = new DataInputStream(is);

        long timeStamp = dais.readLong();
        float value = dais.readFloat();
        String sensorName = dais.readUTF();

        System.out.println("timeStamp == " + timeStamp);
        System.out.println("value == " + value);
        System.out.println("sensorName == " + sensorName);

    }

    private void readFile(String fileName) throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName);

        Socket socket = this.acceptSocket();
        InputStream is = socket.getInputStream();

        int read = 0;
        do {
            read = is.read();
            if(read != -1) {
                fos.write(read);
            }
        } while(read != -1);
    }

    private Socket acceptSocket() throws IOException {
        ServerSocket srvSocket = new ServerSocket(this.port); //ServerPort anlegen, Portnummer bekommen wir über Konstruktor
        System.out.println("server socket created");

        //Port wird aufgemacht der von außen sichtbar ist, Aufruf blockiert und kommt erst zurück wenn sich ein Client mit Server verbunden hat
        //Socket socket = srvSocket.accept(); //Socket repräsentiert die Verbindung des Server mit dem Clirnt

        return srvSocket.accept();
    }

    private void doSomething() throws IOException, InterruptedException {
        System.out.println("client connection accepted");

        Socket socket = this.acceptSocket();
        socket.getInputStream().read(); //aus dem Socket lesen
        System.out.println("read something");


        OutputStream os = socket.getOutputStream(); //von Socket den Outputstream holen und zwischenspeichern, damit ich beliebigen String zurückgeben kann
        os.write(":)".getBytes()); //in OutputStream kann man Bytes reinschreiben, kann so daraus Byte Array machen
        System.out.println("write something");

        Thread.sleep(5000); //legt Prozess kurz schlafen damit Webbroser richtig reagiert
        System.out.println("woke up");


        os.close(); //Output Stream schließen

    }
}

