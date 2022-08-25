package dataStructure;

public class Record {
    public String domain;
    public String IPV4;
    public String IPV6;
    public long Out4,Out6;
    public Record(String domain, String IPV4, String IPV6, long Out4, long Out6){
        this.domain=domain;
        this.IPV4=IPV4;
        this.IPV6=IPV6;
        this.Out4=Out4;
        this.Out6=Out6;
    }

    public String getDomain() {
        return domain;
    }

    public String getIPV4() {
        return IPV4;
    }

    public String getIPV6() {
        return IPV6;
    }

    public void setIPV6(String IPV6) {
        this.IPV6 = IPV6;
    }

    public long getOut4() {
        return Out4;
    }

    public void setOut4(long out) {
        Out4 = out;
    }

    public long getOut6() {
        return Out6;
    }

    public void setOut6(long out) {
        Out6 = out;
    }

    public void setIPV4(String IPV4) {
        this.IPV4 = IPV4;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public String toString() {
        return  domain + ','
                + IPV4 + ','
                + IPV6 + ','
                + Out4 + ','
                + Out6
                ;
    }
}
