// client.java  

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientSocket {

    public static void main(String args[]) throws UnknownHostException {
        int portNumber = 50059;
        String ipAddress = "189.85.81.90";
        InetAddress address = InetAddress.getByName(ipAddress);
        Encoder encoder = new Encoder();

        try {

            Socket socket = new Socket(ipAddress, portNumber);

            DataOutputStream ostream = new DataOutputStream(socket.getOutputStream());
            DataInputStream istream = new DataInputStream(socket.getInputStream());

            String received = istream.readLine();
            System.out.println("Received: " + received);
            String receivedToHex = encoder.asciiToHex(received);
            System.out.println("Received in hex: " + receivedToHex);
            String result = encoder.decode(receivedToHex);
            System.out.println("Result: " + result);

            while (received != null){
                ostream.writeUTF(result);
                ostream.flush();
                received = istream.readLine();
                if(received.equals("OK")) socket.close();
            }


        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e);
        }
    }
}