package entity;

import Debug.Debugger;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Server {
    public static Debugger debugger = new Debugger();
    public static SendtoClient sendtoClient = new SendtoClient();
    public Server() {
        // print out basic info
        System.out.println("======================================================");
        System.out.println("Usage: dnsrelay [d|dd] [dns-server-ipaddr] [filename]");
        System.out.println("Debug level:\t\t\t"+Debugger.debug_level);
        System.out.println("dns-server-ipaddress:\t"+Relayer.serverIp);
        System.out.println("filename:\t\t"+DataAccess.path);
        System.out.println("======================================================");
        Data dataAccess = new DataAccess();
        dataAccess.print();
        System.out.println("======================================================");
        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
        System.out.println("Start Sever at " + dateFormat.format(calendar.getTime()));
        System.out.println("press 'q' and 'enter' to quit the system!");
        ExecutorService servicePool = new ThreadPoolExecutor(
                10,
                20,
                3,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(3),
                new ThreadPoolExecutor.DiscardOldestPolicy());

        Thread thread = new Thread(new Exit(servicePool,dataAccess));
        thread.start();
        Debugger debugger = new Debugger();
        try {


            // create socket with port
            DatagramSocket socket = new DatagramSocket(53);

            while (true) {
                // receive datagram packet
                byte[] bytes = new byte[1024];
                DatagramPacket request = new DatagramPacket(bytes, bytes.length);
                // receive data from client. it will block if receive nothing.
                socket.receive(request);
                // start a new thread
                servicePool.execute(new Handler(request,socket,dataAccess));
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}



