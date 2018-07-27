import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;

public class Logger {
  public static void log(Socket clientSocket, String request, int responseCode) {
    String clientAddress = clientSocket.getRemoteSocketAddress().toString().substring(1);
    LocalDate currentDate = LocalDate.now();
    LocalTime currentTime = LocalTime.now();
    String methodLine = request.split(System.lineSeparator())[0];
    /**
     * Example:
     * 205.160.186.76 unknown – [01/Jan/1996:22:53:58 -0500]
     * “Get /bgs/greenbg.gif HTTP/1.0” 200 50
     */
    String info = clientAddress + " - [" + currentDate + " " + currentTime + "]\n" +
                  "\"" + methodLine.substring(0, methodLine.length() - 1) + "\" " + responseCode + " " + request.length();
    try {
      File logFile = new File("log_info.txt");
      PrintWriter out = new PrintWriter(new FileOutputStream(logFile, true));
      try {
        out.write(info);
        out.close();
      } finally {
        out.close();
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
}