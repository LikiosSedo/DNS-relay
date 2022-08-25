package entity;

import dataStructure.DNSPacket;

import java.net.DatagramPacket;
import java.net.InetAddress;

public class Request {
    InetAddress sourceIpAddr;
    int sourcePort;
    int ID,QCount;
    DNSPacket dnsPacket;
    public Request(DatagramPacket request){
        sourceIpAddr = request.getAddress();
        sourcePort = request.getPort();
        dnsPacket = new DNSPacket(request.getData());

    }

    @Override
    public String toString() {
        return "Request{" +
                "sourceIpAddr=" + sourceIpAddr +
                ", sourcePort=" + sourcePort +
                ", ID=" + ID +
                ", QCount=" + QCount +
                '}';
    }
}
