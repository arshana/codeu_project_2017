// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package codeu.chat.client.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import codeu.chat.common.BasicController;
import codeu.chat.common.ConversationHeader;
import codeu.chat.common.Interest;
import codeu.chat.common.Message;
import codeu.chat.common.NetworkCode;
import codeu.chat.common.User;
import codeu.chat.util.AccessControl;
import codeu.chat.util.Logger;
import codeu.chat.util.Serializers;
import codeu.chat.util.Uuid;
import codeu.chat.util.connections.Connection;
import codeu.chat.util.connections.ConnectionSource;

final class Controller implements BasicController {

  private final static Logger.Log LOG = Logger.newLog(Controller.class);

  private final ConnectionSource source;

  public Controller(ConnectionSource source) {
    this.source = source;
  }

  Queue<String> queue = new LinkedList<String>();

  static BufferedWriter bw = null;
  static FileWriter fw = null;
  
  static {
      try {
          
          fw = new FileWriter("log.txt", true);
          bw = new BufferedWriter(fw);
          
      }
      catch (IOException e) {
        e.printStackTrace();
      }
  }

  @Override
  public Message newMessage(Uuid author, Uuid conversation, String body) {

    Message response = null;

    try (final Connection connection = source.connect()) {

      Serializers.INTEGER.write(connection.out(), NetworkCode.NEW_MESSAGE_REQUEST);
      Uuid.SERIALIZER.write(connection.out(), author);
      Uuid.SERIALIZER.write(connection.out(), conversation);
      Serializers.STRING.write(connection.out(), body);

      if (Serializers.INTEGER.read(connection.in()) == NetworkCode.NEW_MESSAGE_RESPONSE) {
        response = Serializers.nullable(Message.SERIALIZER).read(connection.in());
      } else {
        LOG.error("Response from server failed.");
      }
    } catch (Exception ex) {
      System.out.println("ERROR: Exception during call on server. Check log for details.");
      LOG.error(ex, "Exception during call on server.");
    }

    return response;
  }

  @Override
  public Interest newInterest(Uuid id, Uuid userid, String title, String type) {
      Interest response = null;

      try (final Connection connection = source.connect()) {

        Serializers.INTEGER.write(connection.out(), NetworkCode.NEW_INTEREST_REQUEST);
        Uuid.SERIALIZER.write(connection.out(), id);
        Uuid.SERIALIZER.write(connection.out(), id);
        Serializers.STRING.write(connection.out(), type);
        Serializers.STRING.write(connection.out(), title);
        bw.write("ADD-INTEREST " + id + " " + type + " " + title + "\n");
        bw.flush();
        
        if (Serializers.INTEGER.read(connection.in()) == NetworkCode.NEW_INTEREST_RESPONSE) {
          response = Serializers.nullable(Interest.SERIALIZER).read(connection.in());
        } else {
          LOG.error("Response from server failed.");
        }
      } catch (Exception ex) {
        System.out.println("ERROR: Exception during call on server. Check log for details.");
        LOG.error(ex, "Exception during call on server.");
      }

      return response;
    }

  @Override
  public void removeInterest(Uuid id, Uuid userid, String title, String type) {

      try (final Connection connection = source.connect()) {

          Serializers.INTEGER.write(connection.out(), NetworkCode.REMOVE_INTEREST_REQUEST);
          Uuid.SERIALIZER.write(connection.out(), id);
          Uuid.SERIALIZER.write(connection.out(), userid);
          Serializers.STRING.write(connection.out(), type);
          Serializers.STRING.write(connection.out(), title);
          queue.add("REMOVE-INTEREST ID: " + id + " Type: " + type + " Title: " + title);

      } catch (Exception ex) {
          System.out.println("ERROR: Exception during call on server. Check log for details.");
          LOG.error(ex, "Exception during call on server.");
      }
  }


  @Override
  public User newUser(String name) {

    User response = null;

    try (final Connection connection = source.connect()) {

      Serializers.INTEGER.write(connection.out(), NetworkCode.NEW_USER_REQUEST);
      Serializers.STRING.write(connection.out(), name);
      LOG.info("newUser: Request completed.");

      if (Serializers.INTEGER.read(connection.in()) == NetworkCode.NEW_USER_RESPONSE) {
        response = Serializers.nullable(User.SERIALIZER).read(connection.in());
        LOG.info("newUser: Response completed.");
      } else {
        LOG.error("Response from server failed.");
      }
    } catch (Exception ex) {
      System.out.println("ERROR: Exception during call on server. Check log for details.");
      LOG.error(ex, "Exception during call on server.");
    }

    return response;
  }

  @Override
  public ConversationHeader newConversation(String title, Uuid owner, AccessControl access) {

    ConversationHeader response = null;

    try (final Connection connection = source.connect()) {

      Serializers.INTEGER.write(connection.out(), NetworkCode.NEW_CONVERSATION_REQUEST);
      Serializers.STRING.write(connection.out(), title);
      Uuid.SERIALIZER.write(connection.out(), owner);
      AccessControl.SERIALIZER.write(connection.out(), access);

      if (Serializers.INTEGER.read(connection.in()) == NetworkCode.NEW_CONVERSATION_RESPONSE) {
        response = Serializers.nullable(ConversationHeader.SERIALIZER).read(connection.in());
      } else {
        LOG.error("Response from server failed.");
      }
    } catch (Exception ex) {
      System.out.println("ERROR: Exception during call on server. Check log for details.");
      LOG.error(ex, "Exception during call on server.");
    }

    return response;
  }

  @Override
  public void addMember(String user, Uuid conversation) {
    try (final Connection connection = source.connect()) {
      Serializers.INTEGER.write(connection.out(), NetworkCode.ADD_MEMBER_REQUEST);
      Serializers.STRING.write(connection.out(), user);
      Uuid.SERIALIZER.write(connection.out(), conversation);
      LOG.info("addMember: Request completed.");

      if (Serializers.INTEGER.read(connection.in()) == NetworkCode.ADD_MEMBER_RESPONSE) {
        LOG.info("addMember: Response completed.");
      } else {
        LOG.error("Response from server failed.");
      }
    } catch (Exception ex) {
      System.out.println("ERROR: Exception during call on server. Check log for details.");
      LOG.error(ex, "Exception during call on server.");
    }
  }

  @Override
  public void addOwner(String user, Uuid conversation) {
    try (final Connection connection = source.connect()) {
      Serializers.INTEGER.write(connection.out(), NetworkCode.ADD_OWNER_REQUEST);
      Serializers.STRING.write(connection.out(), user);
      Uuid.SERIALIZER.write(connection.out(), conversation);
      LOG.info("addOwner: Request completed.");

      if (Serializers.INTEGER.read(connection.in()) == NetworkCode.ADD_OWNER_RESPOND) {
        LOG.info("addOwner: Response completed.");
      } else {
        LOG.error("Response from server failed.");
      }
    } catch (Exception ex) {
      System.out.println("ERROR: Exception during call on server. Check log for details.");
      LOG.error(ex, "Exception during call on server.");
    }
  }


  public Queue<String> getQueue(){
    return queue;
  }
  
  public void printLog() throws InterruptedException{
    BufferedWriter bw = null;
    FileWriter fw = null;

    try {

      fw = new FileWriter("log.txt");
      bw = new BufferedWriter(fw);

      String newLine = queue.remove();
      while(newLine != null){
        bw.write(newLine + "\n");
        newLine = queue.remove();
      }

    } catch (IOException e) {
      e.printStackTrace();

    }
  }
  
  //when do I call this method? How can I tell when the server is down?
  public void readLog() throws InterruptedException{
    BufferedReader bw = null;
    FileReader fw = null;

    try {

      fw = new FileReader("log.txt");
      bw = new BufferedReader(fw);
      
      String line = bw.readLine();
      while(line != null){
        line = bw.readLine();
        queue.add(line);
      }

    } catch (IOException e) {

      e.printStackTrace();

    }
  }
}
