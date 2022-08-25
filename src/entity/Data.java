package entity;

import java.io.File;
import java.util.ArrayList;
import dataStructure.Record;

public interface Data{
    ArrayList<Record> records= new ArrayList<Record>();
    public String[] array= new String[100];
    File fin= new File( "record.txt");
    public void delete(String domain);
    public void write(Record record);
    public Record search(String domain);
    public void writeBack();
    public void print();
}
