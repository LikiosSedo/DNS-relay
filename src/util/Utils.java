package util;

public  class Utils {
    public short byte2Short(byte[] data) {
        return (short)(((data[0] & 0xff) << 8) | (data[1] & 0xff));
    }
    public int byte2Int(byte[] data) {
        return (int)(((data[0] & 0xff) << 32) | ((data[1] & 0xff)<< 16)|((data[2] & 0xff) << 8)| (data[3] & 0xff));
    }
    public String byte2String(byte[] data) {
        String result="";
        for(int i=0;i<data.length-1;i++){
            result+=(data[i]&0xFF)+".";
        }
        result+=data[data.length-1];
        return result;
    }
    public byte[] int2Byte(int data) {
        return new byte[]{
                (byte) ((data >> 8) & 0xFF),
                (byte) (data & 0xFF)
        };
    }

    public byte[] int2Byte2(int data) {
        byte[] result = new byte[4];
        result[0] = (byte) ((data >> 24) & 0xFF);
        result[1] = (byte) ((data >> 16) & 0xFF);
        result[2] = (byte) ((data >> 8) & 0xFF);
        result[3] = (byte) (data & 0xFF);
        return result;
    }

    public byte[] string2Byte(String IP) {
        String[] IPpart = IP.split("\\.");
        if (IPpart.length != 4 && IPpart.length!=16 ) {
            return null;
        }
        byte[] result = new byte[IPpart.length];
        for (int i = 0; i < IPpart.length; i++) {
            int num = Integer.parseInt(IPpart[i]);
            byte tmp;
            if (num > 127) {
                tmp = (byte) (num - 256);
            } else {
                tmp = (byte) num;
            }
            result[i] = tmp;
        }
        return result;
    }
}
