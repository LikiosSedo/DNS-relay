package dataStructure;

import util.Utils;

import java.util.Arrays;

public class QuestionSection
{
    private int Qcount;
    private StringBuilder[] Qname;
    private short[] Qtype;
    private short[] Qclass;
    private byte[] data;
    private Question[] QuestionArray;
    private byte [] returndata;
    private int offset;

    public QuestionSection(byte[] data,int Qcount)
    {
        //Initialize Qcount
        this.Qcount =Qcount;
        //Initialize data
        this.data = new byte[data.length];
        this.data = data;

        //Initialize Qname
        this.Qname =new StringBuilder[Qcount];
        //Initialize Qtype
        this.Qtype =new short[Qcount];
        //Initialize Qclass
        this.Qclass =new short[Qcount];
        //Initialize QuestionArray
        this.QuestionArray =new Question[Qcount];
        this.offset = analysisQuestions();
    }
    //get offset
    public int analysisQuestions()
    {
        int Current_offset = 0;
        for (int i = 0; i < Qcount; i++)
        {
            Question q = new Question(data, Current_offset);
            this.QuestionArray[i] = q;
            this.Qname[i] = q.getDomin();
            this.Qtype[i] = q.getType();
            this.Qclass[i] = q.getQclass();
            Current_offset = q.getLens();//mark the current offset for next Question
        }
        return Current_offset;
    }

    public StringBuilder[] getQname()
    {
        return this.Qname;
    }

    public short[] getType() {
        return this.Qtype;
    }
    public short[] getQclass() {
        return this.Qclass;
    }
    public Question[] getQuestionArray(){return this.QuestionArray;}
    public int getoffset(){return this.offset;}
    public byte[] toBytes()
    {
       returndata =new byte[offset];
       System.arraycopy(data,0,returndata,0,offset);
       return returndata;
    }
    @Override
    public String toString() {
        return "Questions{" +
                "Qname=" + Arrays.toString(Qname) +
                ", Qtype=" + Arrays.toString(Qtype) +
                ", Qclass=" + Arrays.toString(Qclass) +
                '}';
    }
}
class QuestionSectionTest {
    public static void main(String[] args) {
        // ID=1,QR=1,RCode=2,QDCount=3,ANCount=3
        byte[] data={4,(byte)98,(byte)117,(byte)112,(byte)116,3,(byte)101,(byte)100,(byte)117,2,(byte)99,(byte)110,0,(byte)0,(byte)1,(byte)0,(byte)1};
        //byte[] data={4,(byte)98,(byte)117,(byte)112,(byte)116,3,(byte)101,(byte)100,(byte)117,2,(byte)99,(byte)110,0,(byte)0,(byte)1,(byte)0,(byte)1,3,(byte)98,(byte)117,(byte)116,3,(byte)101,(byte)100,(byte)117,2,(byte)99,(byte)110,0,(byte)0,(byte)1,(byte)0,(byte)1,4,(byte)98,(byte)117,(byte)112,(byte)116,2,(byte)101,(byte)117,2,(byte)99,(byte)110,0,(byte)0,(byte)1,(byte)0,(byte)1};
        QuestionSection question = new QuestionSection(data,1);
//        for(int i=0;i<question.getQname().length;i++)
//        {
//            System.out.println("Qname:"+question.getQname()[i]);
//            System.out.println("Qtype:"+question.getType()[i]);
//            System.out.println("Qclass:"+question.getQclass()[i]);
//        }
//        System.out.println(question);
//        System.out.println(question.getoffset());
        System.out.println(Arrays.toString(question.toBytes()));
    }
}


