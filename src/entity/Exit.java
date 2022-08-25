package entity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;

public class Exit implements Runnable{
    ExecutorService servicePool;
    Data dataAccess;
    public Exit(ExecutorService servicePool,Data dataAccess){
        this.servicePool=servicePool;
        this.dataAccess=dataAccess;
    }
    @Override
    public void run() {
        while(true){
            Scanner scanner = new Scanner(System.in);
            String s = scanner.nextLine();
            if(s.equals("q")){
                System.out.println("program is exiting:...");
                servicePool.shutdown();
                dataAccess.writeBack();
                Calendar calendar= Calendar.getInstance();
                SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
                System.out.println("Sever shut down at " + dateFormat.format(calendar.getTime()));
                System.exit(0);
            }
        }
    }
}
