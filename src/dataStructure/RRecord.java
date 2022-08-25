package dataStructure;

import util.Utils;

import java.util.Arrays;

public class RRecord {
    private byte[] TTL = new byte[4];
    private byte[] RDLen = new byte[2];
    private byte[] type = new byte[2];
    private byte[] clazz = new byte[2];
    private byte[] name = new byte[2];
    private byte[] data;
    private int offset;
    private byte[] ip;
    private StringBuilder domin =new StringBuilder();
    public RRecord(byte[] data,int current_offset) {
        //Initialize data
        int len = data.length;
        this.data = new byte[len-current_offset];
        System.arraycopy(data, 0, this.data, 0, len-current_offset);
        offset=current_offset;
        // analyse name,offset +=2, if it is pointer.
        System.arraycopy(data, offset, name, 0, 1);
        if(name[0]==(byte)0xC0){
            offset+=2;
        }else{
            offset = analysisDomainName(current_offset)+1;
        }
        //analyse type
        System.arraycopy(data, offset, type, 0, 2);
        offset += 2;
        //解析class
        System.arraycopy(data, offset, clazz, 0, 2);
        offset += 2;
        //Initialize TTL
        System.arraycopy(data, offset, TTL, 0, 4);
        offset += 4;

        // Initialize RDLength
        System.arraycopy(data, offset, RDLen, 0, 2);
        offset += 2;

        // Initialize resource data
        len = getRDLength();
        ip = new byte[len];
        System.arraycopy(data, offset, ip, 0, len);
        offset += len;//the first digit of the next ResourceRecord
    }
    private int analysisDomainName(int current_offset)
    {
        int innerOffset = current_offset;//extends the previous offset
        do {
            //After a domain name field ends, add the symbol "."
            //First time do not need to add "."
            if (innerOffset != current_offset) domin.append(".");
            int len = data[innerOffset] & 0xFF;//Get the length of the current field of the domain name
            byte[] field = new byte[len];//Defines the byte array that stores the current field
            innerOffset++;//offset One step forward
            System.arraycopy(data, innerOffset, field, 0, len);//Get the current field of the domain name

            //Add the field of the current domain name to the domain string
            for (int i = 0; i < len; i++) {
                domin.append((char) field[i]);
            }
            innerOffset += len;//Move offset to the next field
        } while ((data[innerOffset] & 0xFF) != 0);//if it is 0, the Qname is END
        return innerOffset;
    }

    public int getTTL(){
        return new Utils().byte2Int(TTL);
    }
    public short getType(){
        return new Utils().byte2Short(type);
    }

    public short getRDLength(){
        return new Utils().byte2Short(RDLen);
    }

    public String getIp(){
        return new Utils().byte2String(ip);
    }

    public int getLens() {
        return this.offset;
    }
    public byte[] toBytes(){
        return data;
    }

    @Override
    public String toString() {
        return "RRecord{" +
                ", TTL=" + getTTL() +
                ", RDLen=" + getRDLength() +
                ", ip=" + getIp() +
                '}';
    }
}

class RRecordTest {
    public static void main(String[] args) {
        // ID=1,QR=1,RCode=2,QDCount=3,ANCount=3, TTL=100,
        byte[] data={4,(byte)98,(byte)117,(byte)112,(byte)116,3,(byte)101,(byte)100,(byte)117,2,(byte)99,(byte)110,0,(byte)0,(byte)1,(byte)0,(byte)1,0,0,0,100,0,4,127,0,0,1};
        RRecord rRecord = new RRecord(data,0);
        System.out.println(rRecord);
    }
}
