package codeu.chat.common;

import codeu.chat.util.Uuid;
import codeu.chat.util.Time;
import java.io.IOException;

/**
 * Created by arshana on 5/25/17.
 * Created during Version Check technical activity
 */
public final class ServerInfo {
    private final static String SERVER_VERSION = "1.0.0";
    public final Uuid version;
    public final Time startTime;

    public ServerInfo(){
        this.startTime = Time.now();
        Uuid version1;
        try {
            version1 = Uuid.parse(SERVER_VERSION);
        } catch (IOException e) {
            e.printStackTrace();
            version1 = null;
        }
        this.version = version1;
    }

    public ServerInfo(Uuid version) {
        this.version = version;
    }
  
    public ServerInfo(Time startTime) {
      this.startTime = startTime;
    }
}
