package entity;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;


public class SendtoClient
{
    public SendtoClient(){}
    public void send(DatagramSocket r, DatagramPacket responsePacket)
    {
        // Use multi-threading to send packets.
        synchronized (this)
        {
            try
            {
                r.send(responsePacket);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
