import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Conexao implements Runnable {

  // socket que vai responder o cliente.
  private Socket clientSocket;
  private String requestMethod;
  private String requestPath;
  private String requestHTTPVersion;
  private String requestHost;
  private String requestPort;

  // caso nao seja passado um arquivo, o servidor fornece a pagina index.html
  public static String indexHTML = "pages/index.html";

  public Conexao(Socket s) {
    clientSocket = s;
  }

  private void getInfoRequest(String request) {
    String[] lines = request.split(System.lineSeparator());
    for (String line : lines) {
      if (line.contains("GET")) {
        String[] methodLine = line.split(" ");
        requestMethod = methodLine[0];
        requestPath = methodLine[1];
        requestHTTPVersion = methodLine[2];
      }
      if (line.contains("Host: ")) {
        String[] hostLine = line.split(" ")[1].split(":");
        requestHost = hostLine[0];
        requestPort = hostLine[1];
      }
    }
    System.out.println("Metodo: " + requestMethod);
    System.out.println("Caminho: " + requestPath);
    System.out.println("HTTP Version: " + requestHTTPVersion);
    System.out.println("Host: " + requestHost);
    System.out.println("Port: " + requestPort);
  }

  private void connectionHandler(String request) {
    this.getInfoRequest(request);
  }

  private String readFile(String path) {
    String result = "";

    try {
      // Lendo arquivo index.html
      BufferedReader br = new BufferedReader(new FileReader(indexHTML));
      StringBuilder sb = new StringBuilder();
      String lineFile = null;

      while ((lineFile = br.readLine()) != null) {
        sb.append(lineFile);
        sb.append(System.lineSeparator());
      }

      result = sb.toString();
      br.close();

    } catch (IOException e) {
      e.printStackTrace();
    }

    return result;
  }

  public void run() {
    try {
      // Recebido do cliente (browser)
      BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "ASCII"));
      // Envia para o cliente (browser)
      OutputStreamWriter out = new OutputStreamWriter(clientSocket.getOutputStream());

      // Lendo requisicao (header + content)
      String line = null;
      String request = "";
      while ((line = in.readLine()) != null) {
        request = request + "\r\n" + line;
        if (line.length() == 0)
          break;
      }

      this.connectionHandler(request);

      // Lendo arquivo index.html
      String responseContent = this.readFile(indexHTML);

      System.out.println("Someone connected: ");
      // Header RESPOSTA (SERVIDOR -> CLIENTE)
      out.write("HTTP/1.1 200 OK\r\n");
      out.write("Server: Task1\r\n");
      out.write("MIME-version: 1.0\r\n");
      out.write("Content-type: text/html\r\n");
      out.write("Content-lenght: " + responseContent.length() + "\r\n");

      // Content RESPOSTA (SERVIDOR -> CLIENTE)
      out.write(responseContent);

      out.flush();
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
