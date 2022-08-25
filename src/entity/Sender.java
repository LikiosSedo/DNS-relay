package entity;

import Debug.Debugger;
import dataStructure.DNSPacket;
import dataStructure.Record;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

//DNS records are queried and forwarded to the client
public class Sender
{
    private byte[] data;
    private DatagramPacket responsePacket;
    private SendtoClient Send;
    public Sender(Record r, DNSPacket p, DatagramSocket s, InetAddress clientAddress,int port,String ipV)
    {
        p.write(r,ipV);
        data =p.toBytes();
        responsePacket = new DatagramPacket(data, data.length, clientAddress,port);
        Server.sendtoClient.send(s,responsePacket);
//        Server.debugger.debug_show(p, new InetSocketAddress(clientAddress,port),false,true);

    }
}
