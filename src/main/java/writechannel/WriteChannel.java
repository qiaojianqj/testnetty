package writechannel;

import helloclient.HelloNettyClient;
import helloserver.HelloNettyServer;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import timeclient.TimeClient;
import timeserver.TimeServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @create: 2018-11-22 19:05
 */
public class WriteChannel {
    private List<Channel> cms = new ArrayList<> (  );

    private static WriteChannel instance;

    public void addChannelToCms(Channel channel) {
       cms.add ( channel );
    }

    public List<Channel> getChannel() {
       return cms;
    }

    public static synchronized WriteChannel getInstance() {
        if (instance == null) {
            instance = new WriteChannel ();
        }
        return instance;
    }

    public static void main_Time() throws InterruptedException {
        TimeServer.startServer ();
        Thread.sleep (5000 );
        TimeClient.startClient ();

        for (;;) {
            WriteChannel writeChannel = WriteChannel.getInstance ();
            int i = 0;
            for (i = 0; i < writeChannel.getChannel ().size (); i++) {
                Thread.sleep ( 2000 );
                System.out.println ( "write to channel " + writeChannel.getChannel ().get ( i ) );
                //writeChannel.getChannel ().get ( i ).writeAndFlush ( new UnixTime (  ));
                writeChannel.getChannel ().get ( i ).writeAndFlush ( Unpooled.buffer().writeLong ( System.currentTimeMillis () ));
            }
            //if (i >= 2) {
            //    break;
            //}
        }
    }

    public static void main_HelloNettyServer() throws IOException {
        HelloNettyServer.startServer ();
        //HelloNettyClient.startClient ();

        for (;;) {
            WriteChannel writeChannel = WriteChannel.getInstance ();
            int i = 0;
            if (writeChannel.getChannel ().size () != 0) {
                for (;;) {
                    BufferedReader reader = new BufferedReader ( new InputStreamReader ( System.in ) );
                    String msg = reader.readLine ();
                    System.out.println ( "your input msg: " + msg );
                    if (msg == null) {
                        System.out.println ( "error, input again" );
                        continue;
                    }
                    //以下write操作会发送到Outbound处理
                    writeChannel.getChannel ().get ( 0 ).writeAndFlush ( msg );
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        main_Time ();
        //main_HelloNettyServer ();
    }
}
