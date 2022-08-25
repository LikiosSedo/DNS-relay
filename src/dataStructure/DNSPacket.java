package dataStructure;

import util.Utils;

import java.util.Arrays;

public class DNSPacket {
    private Header header;
    private QuestionSection questionSection;
    private ResourceRecord resourceRecord;
    private static int headLen = 12;
    private byte[] headData, body;

    public DNSPacket(byte[] data) {
        // split data into head and body
        int len = data.length;
        headData = new byte[headLen];
        System.arraycopy(data, 0, headData, 0, headLen);
        body = new byte[len - headLen];
        System.arraycopy(data, headLen, body, 0, len - headLen);
        // initialize header and questionSection
        header = new Header(headData);
        questionSection = new QuestionSection(body, header.getQuestionCount());
        // initialize Resource Record
        int qSLen = questionSection.getoffset();
        resourceRecord = new ResourceRecord(header);

        if (len - qSLen - headLen > 0) {
            byte[] rRecord = new byte[len - headLen - qSLen];
            System.arraycopy(data, headLen + qSLen, rRecord, 0, len - headLen - qSLen);
            resourceRecord.read(rRecord);
        }

    }

    public short getID() {
        return header.getID();
    }

    public void setID(short value) {
        header.setID(value);
    }

    public byte getQR() {
        return header.getQR();
    }

    public void setQR(byte value) {
        header.setQR(value);
    }

    public byte getRCode() {
        return header.getRCode();
    }

    public byte getOpcode() {
        return (byte) header.getOpcode();
    }

    public byte getAA() {
        return (byte) header.getAA();
    }

    public byte getTC() {
        return (byte) header.getTC();
    }

    public byte getRD() {
        return header.getRD();
    }

    public void setRD(byte value){header.setRD(value);}

    public byte getRA() {
        return header.getRA();
    }

    public byte getZ() {
        return header.getZ();
    }

    public void setRCode(byte value) {
        header.setRCode(value);
    }

    public short getAnswerCount() {
        return header.getAnswerCount();
    }

    public short getQuestionCount() {
        return header.getQuestionCount();
    }

    public void setAnswerCount(int value) {
        header.setAnswerCount(value);
    }

    public short getAuthorityCount() {
        return header.getAuthorityCount();
    }

    public short getAdditionalCount() {
        return header.getAdditionalCount();
    }

    public StringBuilder[] getQName() {
        return questionSection.getQname();
    }

    public short[] getType() {
        return questionSection.getType();
    }

    public short[] getQClass() {
        return questionSection.getQclass();
    }

    public void write(Record record, String ipV) {
        short type = questionSection.getType()[0];
        short clazz = questionSection.getQclass()[0];
        resourceRecord.write(record, new Utils().int2Byte(type), new Utils().int2Byte(clazz), ipV);
    }

    public int getTTL() {
        RRecord[] rRecords = resourceRecord.getRRecords();
        for (int i = 0; i < rRecords.length; i++) {
            if (rRecords[i].getType() == 1) {
                return rRecords[i].getTTL();
            } else if (rRecords[i].getType() == 28) {
                return rRecords[i].getTTL();
            }
        }
        return 0;
    }

    public String getIp() {

        RRecord[] rRecords = resourceRecord.getRRecords();
        for (int i = 0; i < rRecords.length; i++) {
            if (rRecords[i].getType() == 1) {
                return rRecords[i].getIp();
            } else if (rRecords[i].getType() == 28) {
                return rRecords[i].getIp();
            }
        }
        return null;
    }

    public byte[] toBytes() {
        byte[] bytes = header.toBytes();
        byte[] bytes1 = questionSection.toBytes();
        byte[] bytes2 = resourceRecord.toBytes();
        int hLen = bytes.length;
        int qLen = bytes1.length;
        int rLen = bytes2.length;
        byte[] response = new byte[bytes.length + bytes1.length + bytes2.length];
        int offset = 0;
        System.arraycopy(bytes, offset, response, 0, hLen);
        System.arraycopy(bytes1, 0, response, hLen, qLen);
        System.arraycopy(bytes2, 0, response, hLen + qLen, rLen);
        return response;
    }

    @Override
    public String toString() {
        return "DNSPacket{" +
                "header=" + header +
                ", questionSection=" + questionSection +
                ", resourceRecord=" + resourceRecord +
                ", headData=" + Arrays.toString(headData) +
                ", body=" + Arrays.toString(body) +
                '}';
    }
}

class DNSPacketTest {
    public static void main(String[] args) {
//        输入：[0, 2, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 3, 119, 119, 119, 6, 103, 111, 111, 103, 108, 101, 3, 99, 111, 109, 0, 0, 1, 0, 1]
//        [0, 2, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 3, 119, 119, 119, 6, 103, 111, 111, 103, 108, 101, 3, 99, 111, 109, 0, 0, 1, 0, 1, 114, 112, 97, 0, 0, 12, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
//        byte[] data={0, 2, -127, -128, 0, 1, 0, 1, 0, 0, 0, 0, 3, 119, 119, 119, 6, 103, 111, 111, 103, 108, 101, 3, 99, 111, 109, 0, 0, 1, 0, 1, -64, 12, 0, 1, 0, 1, 0, 0, 1, 44, 0, 4, 74, 86, -105, -94};
        byte[] data = {0, 2, -127, -128, 0, 1, 0, 5, 0, 0, 0, 0, 3, 119, 119, 119, 4, 98, 105, 110, 103, 3, 99, 111, 109, 0, 0, 1, 0, 1, -64, 12, 0, 5, 0, 1, 0, 0, 30, -126, 0, 37, 7, 119, 119, 119, 45, 119, 119, 119, 4, 98, 105, 110, 103, 3, 99, 111, 109, 14, 116, 114, 97, 102, 102, 105, 99, 109, 97, 110, 97, 103, 101, 114, 3, 110, 101, 116, 0, -64, 42, 0, 5, 0, 1, 0, 0, 1, 39, 0, 33, 11, 99, 110, 45, 98, 105, 110, 103, 45, 99, 111, 109, 2, 99, 110, 6, 97, 45, 48, 48, 48, 49, 8, 97, 45, 109, 115, 101, 100, 103, 101, -64, 74, -64, 91, 0, 5, 0, 1, 0, 0, 1, 71, 0, 16, 5, 99, 104, 105, 110, 97, 7, 98, 105, 110, 103, 49, 50, 51, -64, 21, -64, -120, 0, 1, 0, 1, 0, 0, 2, 32, 0, 4, -54, 89, -23, 101, -64, -120, 0, 1, 0, 1, 0, 0, 2, 32, 0, 4, -54, 89, -23, 100};
        DNSPacket dnsPacket = new DNSPacket(data);
        dnsPacket.setID((short)3);
        System.out.println(Arrays.toString(dnsPacket.toBytes()));
    }
}
