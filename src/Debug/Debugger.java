package Debug;

import dataStructure.DNSPacket;
import dataStructure.Header;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class Debugger
{
    public static int debug_level=2;
    static int debug_num = 1;
    public  void debug_show(DNSPacket P, InetSocketAddress Address, boolean isSever, boolean isSend)
    {
        synchronized (this) {
            if (debug_level == 1 || debug_level == 2) {
                System.out.println('\n' + "----------START----------------------------------------------------------------------------------------------------");
                System.out.println("DebugNo: " + debug_num + "\t" + "DebugLevel: " + Debugger.debug_level);
                String Date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                System.out.println("Time: " + Date);
                System.out.println("ID: " + P.getID() + '\t' + "IP: " + Address.getAddress().toString());
                System.out.println("DominName: " + P.getQName()[0]);
                System.out.println("Type: " + returnType(P.getType()[0]) + '\t' + "Class: " + returnClass(P.getQClass()[0]));
               debug_num++;
                if (isSend) {
                    System.out.printf("SEND to %s (%d bytes) ", Address, P.toBytes().length);
                } else {
                    System.out.printf("RECV from %s (%d bytes) ", Address, P.toBytes().length);

                }
                if (isSever) {
                    System.out.printf("[ Default DNS Server ]");
                } else {
                    System.out.printf("[ Client ]");
                }
            }
            if (debug_level == 2) {
                System.out.println();
                System.out.println("Data: " + Arrays.toString(P.toBytes()));
                //"QR", "Opcode", "AA", "TC", "RD", "RA", "Z", "Rcode"
                System.out.println("QR: " + P.getQR() + '\t' + "Opcode: " + P.getOpcode() + '\t' + "AA: " + P.getAA() + '\t' + "TC: " + P.getTC());
                System.out.println("RD: " + P.getRD() + '\t' + "Rcode: " + P.getRCode() + '\t' + "RA: " + P.getRA() + '\t' + "Z: " + P.getZ());
                System.out.printf("Question count: %d\t", P.getQuestionCount());
                System.out.printf("Answer count: %d\t", P.getAnswerCount());
                System.out.println();
                System.out.printf("Authority count: %d\t", P.getAuthorityCount());
                System.out.printf("Additional count: %d\t", P.getAdditionalCount());
            }
            if (debug_level == 1 || debug_level == 2)
                System.out.println('\n' + "----------END------------------------------------------------------------------------------------------------------");
        }
    }
    //public static void debug_SEND(DatagramPacket UDP, int isSever){    }
    //public static void debug_RECV(DatagramPacket UDP, int isSever){    }
    public static void setDebug_level(int debug_level)
    {
        Debugger.debug_level = debug_level;
    }
    public static String returnType(short type)
    {
        switch (type){
            case 1:  return "A";
            case 12: return "PTR";
            case 28: return "AAAA";
        }
        return null;
    }
    public static String returnClass(short Qclass)
    {
        switch (Qclass){
            case 1: return "IN";
            case 2: return "CS";
            case 3: return "CH";
            case 4: return "HS";
        }
        return null;
    }
}

class DubuggerTest
{
    public static void main(String[] args) {
        byte[] data={0, 2, -127, -128, 0, 1, 0, 1, 0, 0, 0, 0, 3, 119, 119, 119, 6, 103, 111, 111, 103, 108, 101, 3, 99, 111, 109, 0, 0, 28, 0, 1, -64, 12, 0, 1, 0, 1, 0, 0, 1, 44, 0, 4, 74, 86, -105, -94};
        DNSPacket dnsPacket = new DNSPacket(data);
        InetSocketAddress A = new InetSocketAddress("10.3.9.44",53);
        DatagramPacket UDP =new DatagramPacket(data,data.length,A);
        Debugger debugger =new Debugger();
        Debugger.setDebug_level(2);
        debugger.debug_show(dnsPacket,A,true,false);
        Debugger.setDebug_level(1);
        debugger.debug_show(dnsPacket,A,false,true);
        Debugger.setDebug_level(0);
        debugger.debug_show(dnsPacket,A,false,false);

    }
}

