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

  // caso nao seja passado um arquivo, o servidor fornece a pagina index.html
  public static String indexHTML = "pages/index.html";

  public Conexao(Socket s) {
    clientSocket = s;
  }

  // no metodo abaixo sera tratada a comunicacao com o browser
  public void connectionHandler() {
    // GET / HTTP/1.1
    // Host: localhost:8081
    // Connection: keep-alive
    // Cache-Control: max-age=0
    // Upgrade-Insecure-Requests: 1
    // User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6)
    // AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36
    // Accept:
    // text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8
    // Accept-Encoding: gzip, deflate, br
    // Accept-Language: pt,en-US;q=0.9,en;q=0.8,ja;q=0.7
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

      System.out.println(request);

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
