package entity;

import Debug.Debugger;
import dataStructure.DNSPacket;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class Relayer
{
    private byte[] data =new byte[1024];
    private DatagramPacket client_responsePacket;
    private DatagramPacket responsePacket;
    private SendtoClient Send;
    private InetSocketAddress MainDNS;
    private DatagramPacket receivePacket;
    public static String serverIp="10.3.9.44";//120.226.70.165
    public Relayer(DNSPacket dnsPacket, DatagramSocket s, InetAddress clientAddress, int port, boolean isCached, Data dataAccess)
    {
        relay(dnsPacket,s,clientAddress,port,isCached,dataAccess);
    }
    public void relay(DNSPacket dnsPacket, DatagramSocket s, InetAddress clientAddress, int port, boolean isCached, Data dataAccess){
        try {
            System.out.println("进入relay");
            MainDNS =new InetSocketAddress(serverIp, 53);
            DatagramSocket socket = new DatagramSocket();
            data=dnsPacket.toBytes();
            responsePacket = new DatagramPacket(data,data.length,MainDNS);
            socket.send(responsePacket);
            socket.setSoTimeout(2000);
            // packet to send server
//            Server.debugger.debug_show(dnsPacket, new InetSocketAddress(serverIp,53),true,true);
            receivePacket = new DatagramPacket(new byte[512], 512);
            try {
                socket.receive(receivePacket);
            }
            catch (SocketTimeoutException e) {
                // after 2 seconds, then continue.
                socket.close();
                e.printStackTrace();
                return;
            }

            int len = receivePacket.getLength();
            byte[] data= new byte[len];
            System.arraycopy(receivePacket.getData(), 0, data, 0, len);
            DNSPacket response = new DNSPacket(data);
            // packet received from sever
            Server.debugger.debug_show(response, new InetSocketAddress(serverIp,53),true,false);
//            System.out.println("received response id:"+response.getID());
//            System.out.println(""dnsPacket.getID());

//            response.setID(dnsPacket.getID());
            byte[] bytes = response.toBytes();
            client_responsePacket = new DatagramPacket(bytes,bytes.length,clientAddress,port);

            socket.close();
            Server.sendtoClient.send(s,client_responsePacket);

            if(isCached){
                // cache at the end to avoid delay the response
                new Cache(response,dataAccess);
            }
            // relay packet to client
            Server.debugger.debug_show(response, new InetSocketAddress(clientAddress, port), false, true);

        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
