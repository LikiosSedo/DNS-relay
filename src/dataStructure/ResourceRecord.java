package dataStructure;

import util.Utils;

public class ResourceRecord {
    Header header;
    int ANCount = 0;
    byte[] response = new byte[1024];
    RRecord[] rRecords;

    public ResourceRecord(Header header) {
        this.header = header;
        this.ANCount = header.getAnswerCount();
    }

    public void read(byte[] data) {
        int offset = 0;
        rRecords=new RRecord[ANCount];
        for (int i = 0; i < ANCount; i++) {
            RRecord rRecord = new RRecord(data, offset);
            offset = rRecord.getLens();//mark the current offset for next Question
            rRecords[i]=rRecord;
        }
        response=data;
    }

    public void write(Record record, byte[] type, byte[] clazz,String ipV) {
        // set packet state as response
        header.setQR((byte) 1);

        String IPv4 = record.getIPV4();
        String IPv6 = record.getIPV6();
        // if in black sheet, then set it cannot find

        if ("0.0.0.0".equals(IPv4) || "0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0".equals(IPv6)) {
            header.setRCode((byte) 3);
            return;
        }

        // set number of resource Record
        header.setAnswerCount(1);
        int offset = 0;
        // if ipv4 exists then write
        if (ipV.equals("IpV4")) {
            // write "COOC" pointer pointing to QNAME
            System.arraycopy(new Utils().int2Byte(0xc00c), 0, response, 0, 2);
            offset += 2;
            // write type
            for (int i = 0; i < 2; i++, offset++)
                response[offset] = type[i];
            // write Class
            for (int i = 0; i < 2; i++, offset++)
                response[offset] = clazz[i];
            // write TTL
            System.arraycopy(new Utils().int2Byte2((int) record.getOut4()), 0, response, offset, 4);
            offset += 4;
            // write RDLENGTH
            System.arraycopy(new Utils().int2Byte(4), 0, response, offset, 2);
            offset += 2;
            // write IP
            System.arraycopy(new Utils().string2Byte(IPv4), 0, response, offset, 4);
            offset += 4;
        }
        // if ipv6 exists then write
        if (ipV.equals("IpV6")) {
            // write COOC pointer pointing to QNAME
            System.arraycopy(new Utils().int2Byte(0xc00c), 0, response, offset, 2);
            offset += 2;
            // write type
            for (int i = 0; i < 2; i++, offset++)
                response[offset] = type[i];
            // write Class
            for (int i = 0; i < 2; i++, offset++)
                response[offset] = clazz[i];
            // write TTL
            System.arraycopy(new Utils().int2Byte2((int) record.getOut6()), 0, response, offset, 4);
            offset += 4;
            // fill RDLENGTH
            System.arraycopy(new Utils().int2Byte(16), 0, response, offset, 2);
            offset += 2;
            // fill IP
            System.arraycopy(new Utils().string2Byte(IPv6), 0, response, offset, 16);
            offset += 4;

        }
        byte[] immediate = new byte[offset];
        System.arraycopy(response, 0, immediate, 0, offset);
        response=immediate;

    }


    public RRecord[] getRRecords() {
        // before get , call read
        return rRecords;
    }



    public byte[] toBytes() {
        int len = response.length;
        if(response[0]==0){
            len=0;
        }
        byte[] realResponse = new byte[len];
        System.arraycopy(response, 0, realResponse, 0, len);
        return realResponse;
    }

}

