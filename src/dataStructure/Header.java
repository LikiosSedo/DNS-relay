package dataStructure;

import util.Utils;

import java.util.Arrays;

public class Header {
    private byte[] ID = new byte[2];
    private byte[] Flags = new byte[2];
    private byte[] QuestionCount = new byte[2];
    private byte[] AnswerCount = new byte[2];
    private byte[] AuthorityCount = new byte[2];
    private byte[] AdditionalCount = new byte[2];
    private byte[] data;

    public Header(byte[] data) {
        this.data=data;
        // initialize the field
        ID[0] = data[0];
        ID[1] = data[1];
        Flags[0] = data[2];
        Flags[1] = data[3];
        QuestionCount[0] = data[4];
        QuestionCount[1] = data[5];
        AnswerCount[0] = data[6];
        AnswerCount[1] = data[7];
        AuthorityCount[0] = data[8];
        AuthorityCount[1] = data[9];
        AdditionalCount[0] = data[10];
        AdditionalCount[1] = data[11];
    }

    public short getID() {
        return new Utils().byte2Short(ID);
    }
    public void setID(short value) {
        byte[] bytes = new Utils().int2Byte(value);
        ID[0]=bytes[0];
        ID[1]=bytes[1];
    }

    public byte getQR() {
        return (byte) ((Flags[0] & 0x80) >>>7);
    }

    public void setQR(byte value) {
        if(value<2){
            Flags[0] = (byte) ((byte) (Flags[0] & 0x7F) | (byte) (value << 7));
        }else{
            System.out.println("setQRErr: QR should be 0 or 1!");
        }

    }
    public byte getRCode() {
        return (byte) (Flags[1] & 0x0F);
    }
    public void setRCode(byte value) {
        if(value<16){
            Flags[1] = (byte) ((Flags[1] & 0xF0) | value);
        }else{
            System.out.println("setRCodeErr: RCode should be less than 16!");
        }

    }
    public byte getOpcode(){
        return (byte) ((Flags[0] & 0x78)>>3);
    }
    public byte getAA(){
        return (byte) ((Flags[0] & 0x04)>>2);
    }
    public byte getTC(){
        return (byte) ((Flags[0] & 0x02)>>1);
    }
    public byte getRD(){
        return (byte)(Flags[0] & 0x01);
    }
    public void setRD(byte value){
        Flags[0]=(byte)((Flags[0]&0xF6)|value);
    }

    public byte getRA(){
        return (byte)((Flags[1] & 0x80)>>7);
    }
    public byte getZ(){
        return (byte)((Flags[1] & 0x40)>>6);
    }
    public short getQuestionCount() {
        return new Utils().byte2Short(QuestionCount);
    }
    public short getAnswerCount(){
        return new Utils().byte2Short(AnswerCount);
    }
    public void setAnswerCount(int value) {
        AnswerCount = new Utils().int2Byte(value);
    }
    public short getAuthorityCount(){
        return new Utils().byte2Short(AuthorityCount);
    }
    public short getAdditionalCount(){
        return new Utils().byte2Short(AdditionalCount);
    }
    public byte[] toBytes(){
        data[0] = ID[0];
        data[1] = ID[1];
        data[2] = Flags[0];
        data[3] = Flags[1];
        data[4] = QuestionCount[0];
        data[5] = QuestionCount[1];
        data[6] = AnswerCount[0];
        data[7] = AnswerCount[1];
        data[8] = AuthorityCount[0];
        data[9] = AuthorityCount[1];
        data[10] = AdditionalCount[0];
        data[11] = AdditionalCount[1];
        return data;
    }
    @Override
    public String toString() {
        return "Header{" +
                "ID=" + Arrays.toString(ID) +
                ", Flags=" + Arrays.toString(Flags) +
                ", QuestionCount=" + Arrays.toString(QuestionCount) +
                ", AnswerCount=" + Arrays.toString(AnswerCount) +
                ", AuthorityCount=" + Arrays.toString(AuthorityCount) +
                ", AdditionalCount=" + Arrays.toString(AdditionalCount) +
                '}';
    }
}

class HeaderTest {
    public static void main(String[] args) {
        // ID=1,QR=1,RCode=2,QDCount=3,ANCount=3
        byte[] data={0,1, (byte) 0xFF,(byte)0xBF,0,3,0,3,0,0,0,(byte)0xFF};

        Header header = new Header(data);
//        System.out.println("ID:"+header.getID());
//        System.out.println("QR:"+header.getQR());
//        header.setQR((byte)0);
//        System.out.println("QRClear:"+header.getQR());
//        System.out.println("QCount:"+header.getQuestionCount());
//        System.out.println("AnCount:"+header.getAnswerCount());
//        header.setQR((byte)3);
//        System.out.println("after set QR=3:"+header);
//        header.setRCode((byte)3);
//        System.out.println("after set RCode:"+header);
//        header.setAnswerCount((byte)2);
//        System.out.println("after set AnCount:"+header);
//        header.setQR((byte)3);
//        header.setID((short)2);
//        header.setQR((byte)1);
//        header.setRCode((byte)3);
//        header.setAnswerCount(4);
        System.out.println("Opcode:"+header.getOpcode());
        System.out.println("AA:"+header.getAA());
        System.out.println("TC:"+header.getTC());
        System.out.println("RD:"+header.getRD());
        System.out.println("RA:"+header.getRA());
        System.out.println("getZ"+header.getZ());
        System.out.println("Question count:"+header.getQuestionCount());
        System.out.println("Answer count:"+header.getAnswerCount());
        System.out.println("Authority count:"+header.getAuthorityCount());
        System.out.println("Addtional count:"+header.getAdditionalCount());
//        byte[] bytes = header.toBytes();

//        for(int i=0;i<bytes.length;i++){
//            System.out.println(bytes[i]);
//        }

    }
}