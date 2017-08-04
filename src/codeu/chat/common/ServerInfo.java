package codeu.chat.common;

import java.io.IOException;

import codeu.chat.util.Time;
import codeu.chat.util.Uuid;

/**
 * Created by Arshana and Rithu
 * Created during Version Check/Up Time technical activity
 * Holds Server's information
 * Used by both Client and Server
 */
public final class ServerInfo {
  private final static String SERVER_VERSION = "1.0.0";
  public final Uuid version;
  public final Time startTime;
    
  //constructor
  public ServerInfo() {
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
  
  /*
  //constructor added during Version Check activity
  public ServerInfo(Uuid version) {
    this.version = version;
  }
  
  //constructor added during UpTime activity
  public ServerInfo(Time startTime) {
    this.startTime = startTime;
  }*/
  
  //constructor add during code review
  public ServerInfo(Uuid version, Time startTime){
    this.version = version;
    this.startTime = startTime;
  }
}