package entity;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import dataStructure.Record;

public class DataAccess implements Data {
    public static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    ArrayList<Record> records = new ArrayList<Record>();
    public String[] array = new String[100];
    public static String path = "record.txt";
    File fin = new File(path);

    public DataAccess() {
        try {
            fin.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.read(fin);
    }

    public void delete(String domain) {
        lock.writeLock().lock();
        for (int i = 0; i < this.records.size(); i++) {
            if (records.get(i).getDomain().equals(domain)) {
                if ((records.get(i).getOut6() - System.currentTimeMillis()) <= 0) {
                    records.get(i).setIPV6(null);
                    records.get(i).setOut6(0);
                }
                if ((records.get(i).getOut4() - System.currentTimeMillis()) <= 0) {
                    records.get(i).setIPV4(null);
                    records.get(i).setOut4(0);

                }
                if (records.get(i).getOut4() - System.currentTimeMillis() <= 0 && records.get(i).getOut6() - System.currentTimeMillis() <= 0) {
                    records.remove(i);
                }
                break;
            }

        }
        lock.writeLock().unlock();


    }

    private int readFile1(File fin) throws IOException {
        FileInputStream fis = new FileInputStream(fin);
        //Construct BufferedReader from InputStreamReader
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String[] S1 = new String[5];
        String line = null;
        int i = 0;
        while ((line = br.readLine()) != null) {
            this.array[i] = line;
            i++;


        }

        br.close();
        fis.close();

        return i;
    }

    public void read(File fin) {
        lock.writeLock().lock();
        String[] S1 = new String[5];
        int i = 0, j = 0;
        try {
            i = this.readFile1(fin);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (j = 0; j < i; j++) {
            String S2 = this.array[j];
            //System.out.println(S2);
            S1 = S2.split(",");
            long l1 = Long.parseLong(S1[3]);
            long l2 = Long.parseLong(S1[4]);
            //System.out.println(l);
            this.records.add(new Record(S1[0], S1[1], S1[2], l1, l2));

        }
        lock.writeLock().unlock();
    }

    public void write(Record record) {

        lock.writeLock().lock();
        int i = 0;
        int flag1 = 1;
        int flag2 = 1;
        for (i = 0; i < this.records.size(); i++) {
            if (this.records.get(i).getDomain().equals(record.getDomain())) {
                if ((record.getIPV4() == null||record.getIPV4().equals("")) && records.get(i).getOut4() - System.currentTimeMillis() > 0) {
                    record.setIPV4(this.records.get(i).getIPV4());
                    record.setOut4(this.records.get(i).getOut4());
                    flag1 = 0;
                }
                if ((record.getIPV6() == null||record.getIPV6().equals("")) && records.get(i).getOut6() - System.currentTimeMillis() > 0) {
                    record.setIPV6(this.records.get(i).getIPV6());
                    record.setOut6(this.records.get(i).getOut6());
                    flag2 = 0;
                }
                this.records.remove(i);
                break;
            }
        }
        if (flag1 == 1)
            record.setOut4(record.getOut4() * 1000 + System.currentTimeMillis());
        if (flag2 == 1)
            record.setOut6(record.getOut6() * 1000 + System.currentTimeMillis());
        this.records.add(record);


        lock.writeLock().unlock();

    }

    public Record search(String domain) {
        lock.readLock().lock();

        for (int i = 0; i < this.records.size(); i++) {
            if (records.get(i).getDomain().equals(domain)) {
                Record r = new Record(records.get(i).getDomain(), records.get(i).getIPV4(), records.get(i).getIPV6(), (records.get(i).getOut4() - System.currentTimeMillis()) / 1000, (records.get(i).getOut6() - System.currentTimeMillis()) / 1000);
                lock.readLock().unlock();
                return r;
            }
        }
        lock.readLock().unlock();
        return null;

    }

    public void writeBack() {
        lock.writeLock().lock();
        String[] S1 = new String[5];
        FileWriter fs = null;
        try {
            fin.delete();
            fin = new File(path);
            fs = new FileWriter(fin.getName(), true);
            BufferedWriter bufferWritter = new BufferedWriter(fs);
            for (int i = 0; i < this.records.size(); i++) {
                S1[0] = this.records.get(i).getDomain();
                S1[1] = this.records.get(i).getIPV4();
                S1[2] = this.records.get(i).getIPV6();
                long l1 = this.records.get(i).getOut4();
                long l2 = this.records.get(i).getOut6();
                S1[3] = String.valueOf(l1);
                S1[4] = String.valueOf(l2);
                String s2 = S1[0] + "," + S1[1] + "," + S1[2] + "," + S1[3] + "," + S1[4] + "\r\n";


                bufferWritter.write(s2);
            }
            bufferWritter.close();
            fs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lock.writeLock().unlock();
    }

    @Override
    public void print() {
        String[] S1 = new String[5];
        int i = 0;
        System.out.println("Read database as follow");
        for (i = 0; i < this.records.size(); i++) {
            S1[0] = this.records.get(i).getDomain();
            S1[1] = this.records.get(i).getIPV4();
            S1[2] = this.records.get(i).getIPV6();
            long l1 = this.records.get(i).getOut4();
            long l2 = this.records.get(i).getOut6();
            S1[3] = String.valueOf(l1);
            S1[4] = String.valueOf(l2);
            String s2 = S1[0] + "," + S1[1] + "," + S1[2] + "," + S1[3] + "," + S1[4];
            System.out.println(s2);
        }
        System.out.println("toatal:" + i);
    }

    public static void main(String[] args) throws IOException {
        DataAccess dataAccess = new DataAccess();
        Record ss = new Record("www.bupt.edu.cn","10.3.9.-95",null,4939,0);
        Record s1 = new Record("www.bupt.edu.cn",null,"32.1.13.168.2.21.64.56.0.0.0.0.0.0.1.97",0,1262);
        dataAccess.write(ss);
        dataAccess.write(s1);
        System.out.println(dataAccess.search("www.bing.com"));
        //dataAccess.delete("www.bing.com");
        dataAccess.print();
//        dataAccess.writeBack();
        dataAccess.writeBack();


        Record r = dataAccess.search("0");
        //System.out.println(ss);
        System.out.println(System.currentTimeMillis());
    }
}