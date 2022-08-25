package entity;

import Debug.Debugger;
import dataStructure.DNSPacket;
import dataStructure.Record;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

public class Handler implements Runnable {
    DatagramPacket request;
    DatagramSocket socket;
    Data dataAccess;

    public Handler(DatagramPacket request, DatagramSocket socket, Data dataAccess) {
        // 解析DNS Packet
        this.request = request;
        this.socket = socket;
        this.dataAccess=dataAccess;

    }

    @Override
    public void run() {

        // 查询本地域名
        int len = request.getLength();
        byte[] data= new byte[len];
        System.arraycopy(request.getData(), 0, data, 0, len);
        DNSPacket dnsPacket = new DNSPacket(data);

        StringBuilder[] qName = dnsPacket.getQName();
        short[] type = dnsPacket.getType();

        for (int i = 0; i < type.length; i++) {
            // judge type, if type!=A or != AAAA，then relay without cache
            Record record = dataAccess.search(qName[i].toString());
            String domain=null;
            long ttl=1;
            if(type[i]==1){
                // ipv4
                if(record!=null && record.getOut4()<=0&& (!"0.0.0.0".equals(record.getIPV4()))){
                    // if time out，the set record to null，and  delete it at the end to avoid delaying the response
                    domain=record.getDomain();
                    ttl=record.getOut4();
                    record=null;

                }
                if(record==null||record.getIPV4()==null||record.getIPV4().equals("")){
                    // if not exist, then cache
                    new Relayer(dnsPacket,socket, request.getAddress(), request.getPort(),true,dataAccess);
                }else{
                    new Sender(record,dnsPacket,socket,request.getAddress(),request.getPort(),"IpV4");
                }
            }else if(type[i]==28){
                // ipv6
                if(record!=null && record.getOut6()<=0&& (!"0.0.0.0".equals(record.getIPV4()))&& (!"0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0".equals(record.getIPV6()))&& (!"0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0".equals(record.getIPV6()))){
                    // if time out，the set record to null，and  delete it at the end to avoid delaying the response
                    domain=record.getDomain();
                    ttl=record.getOut6();
                    record=null;

                }
                if(record==null||record.getIPV6()==null||record.getIPV6().equals("")){
                    // if not exist, then cache
                    new Relayer(dnsPacket,socket ,request.getAddress(), request.getPort(),true,dataAccess);
                }else{
                    new Sender(record,dnsPacket,socket,request.getAddress(),request.getPort(),"IpV6");
                }

            }else{
                new Relayer(dnsPacket,socket,request.getAddress(), request.getPort(),false,null);
            }
            if(ttl<=0){
                // if time out，the set record to null，and  delete it at the end to avoid delaying the response
                dataAccess.delete(domain);
            }
        }
    }
}
