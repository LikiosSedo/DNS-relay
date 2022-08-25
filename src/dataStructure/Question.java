package dataStructure;

import util.Utils;

import java.util.Arrays;

public class Question {
    private StringBuilder domin =new StringBuilder();
    private byte[] Qname;
    private byte[] Qtype =new byte[2];
    private byte[] Qclass =new byte[2];
    private byte[] data;
    private int offset;

    public Question(byte[] data,int current_offset) {
        //Initialize data
        this.data = new byte[data.length];
        this.data = data;

        //Initialize Qname
        offset = analysisDomainName(current_offset);
        int namelens =domin.length()+1;//length of Qname
        this.Qname = new byte[namelens];
        System.arraycopy(data, current_offset, this.Qname, 0,namelens);

        //Initialize Qtype
        offset++;
        System.arraycopy(data, offset, this.Qtype, 0, 2);

        //Initialize Qclass
        offset += 2;
        System.arraycopy(data, offset, this.Qclass, 0, 2);

        //if here is data[15] and data[16],then we set offset to 17 that is data[17],which is the first digit of the next Question
        offset += 2;//the first digit of the next Question
    }


    //return the cunrrent offset
    private int analysisDomainName(int current_offset)
    {
        int offset = current_offset;//extends the previous offset
        do {
            //After a domain name field ends, add the symbol "."
            //First time do not need to add "."
            if (offset != current_offset) domin.append(".");

            int len = data[offset] & 0xFF;//Get the length of the current field of the domain name
            byte[] field = new byte[len];//Defines the byte array that stores the current field
            offset++;//offset One step forward
            System.arraycopy(data, offset, field, 0, len);//Get the current field of the domain name

            //Add the field of the current domain name to the domain string
            for (int i = 0; i < len; i++) {
                domin.append((char) field[i]);
            }
            offset += len;//Move offset to the next field
        } while ((data[offset] & 0xFF) != 0);//if it is 0, the Qname is END

        return offset;
    }

    public StringBuilder getDomin() {
        return domin;
    }

    public short getType() {
        return new Utils().byte2Short(this.Qtype);
    }

    public short getQclass() {
        return new Utils().byte2Short(this.Qclass);
    }

    public int getLens() {
        return this.offset;
    }
    @Override
    public String toString() {
        return "Question{" +
                "Qname=" + this.domin +
                ", Qtype=" + Arrays.toString(Qtype) +
                ", Qclass=" + Arrays.toString(Qclass) +
                '}';
    }
}
    class QuestionTest {
        public static void main(String[] args) {
            // ID=1,QR=1,RCode=2,QDCount=3,ANCount=3
            byte[] data={4,(byte)98,(byte)117,(byte)112,(byte)116,3,(byte)101,(byte)100,(byte)117,2,(byte)99,(byte)110,0,(byte)0,(byte)1,(byte)0,(byte)1};
            Question question = new Question(data,0);
            System.out.println("Qname:"+question.getDomin());
            System.out.println("Qtype:"+question.getType());
            System.out.println("Qclass:"+question.getQclass());

            System.out.println(question);
        }
    }

