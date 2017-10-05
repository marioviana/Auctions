import java.io.*;
import java.net.*;
import java.util.*;

class ChatLog {
  Vector<String> log = new Vector<String>();

  public synchronized void add(String s) {
    log.add(s);
    // System.out.println("get "+log.elementAt(log.size()-1)); 
    notifyAll();
  }

  public void writeLoop (PrintWriter pw) {
    int i=0;
    String s;
    try {
      while(true) {
        synchronized(this) {
          while (i>= log.size())
            wait();
          s = log.elementAt(i);
        }
        pw.println(s);
        i++;
      }
    } catch (InterruptedException e) {}
  }
}

class TreatClientRead implements Runnable {
  Socket cs;
  ChatLog l;

  TreatClientRead (Socket cs, ChatLog l) {
    this.cs = cs;
    this.l = l;
  }
}

public class ChatServ {
  public static void main(String[] args) {
    ServerSocket ss = new ServerSocket(9999);
    Socket cs = null;

    ChatLog l = new ChatLog();

    while (true) {
      cs = ss.accept();

      Thread tr = new Thread (new ThreaClientRead(cs, l));
      Thread tw = new Thread (new ThreaClientWrite(cs, l));
      tr.start();
      tw.start();
    }
    // ss.close();
  }
}
