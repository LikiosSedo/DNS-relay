package entity;

import dataStructure.DNSPacket;
import dataStructure.Record;
public class Cache {
    public Cache(DNSPacket response, Data dataAccess){
        if(response.getRCode()!=0){
           // if response state is not right，then don't cache.
            return;
        }
        Record record=null;
        if(response.getType()[0]==1){
            // IpV4
            String IpV4=response.getIp();
            if(IpV4==null || IpV4.equals("")){
                return;
            }
            record= new Record(response.getQName()[0].toString(),response.getIp(),"",response.getTTL(),0);
        }else if(response.getType()[0]==28){
            // IpV6
            String IpV6=response.getIp();
            if(IpV6==null || IpV6.equals("")){
                return;
            }
            record = new Record(response.getQName()[0].toString(),"",response.getIp(),0,response.getTTL());
        }
        if(record!=null){
            dataAccess.write(record);
        }
    }
}
