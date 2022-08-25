import entity.DataAccess;
import entity.Relayer;
import entity.Server;
import Debug.Debugger;
public class Main{
    public static void main(String[] args) {
        //debug level 1
        if(args.length != 0 && args[0].equals("-d")) {
            Debugger.setDebug_level(1);
            if (args.length == 2 || args.length == 3)
                Relayer.serverIp = args[1];
            if (args.length == 3)
                DataAccess.path=args[2];
        }
        //debug level 2
        else if(args.length != 0 && args[0].equals("-dd")) {
            Debugger.setDebug_level(2);
            if (args.length == 2 || args.length == 3)
                Relayer.serverIp = args[1];
            if (args.length == 3)
                DataAccess.path=args[2];
        }
        //debug level 0
        else if(args.length != 0){
            if (args.length == 1 || args.length == 2)
                Relayer.serverIp = args[1];
            if (args.length == 2)
                DataAccess.path=args[2];
        }
        new Server();
    }

}