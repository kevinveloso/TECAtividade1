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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Conexao implements Runnable {

  // socket que vai responder o cliente.
  private Socket clientSocket;
  private String requestMethod;
  private String requestPath;
  private String requestHTTPVersion;
  private String requestHost;
  private String requestPort;

  private String responseHeader;
  private String responseContent;

  // caso nao seja passado um arquivo, o servidor fornece a pagina index.html
  public static String root = "pages";

  public Conexao(Socket s) {
    clientSocket = s;
  }

  private String getPath(String path) {
    return path.equals("/") ? "./" + root + "/index.html" : "./" + root + path;
  }

  private void getInfoRequest(String request) {
    if (request == null) return;
    String[] lines = request.split(System.lineSeparator());
    String[] methodLine = lines[0].split(" ");
    requestMethod = methodLine[0];
    requestPath = methodLine[1];
    requestHTTPVersion = methodLine[2];

    for (String line : lines) {
      if (line.contains("Host: ")) {
        String[] hostLine = line.split(" ")[1].split(":");
        requestHost = hostLine[0];
        requestPort = hostLine[1];
      }
    }
    // System.out.println("Metodo: " + requestMethod);
    // System.out.println("Caminho: " + requestPath);
    // System.out.println("HTTP Version: " + requestHTTPVersion);
    // System.out.println("Host: " + requestHost);
    // System.out.println("Port: " + requestPort);
  }

  private String readFile(String path) throws IOException {
    String result = "";
    BufferedReader br = new BufferedReader(new FileReader(path));
    StringBuilder sb = new StringBuilder();
    String lineFile = null;

    try {
      while ((lineFile = br.readLine()) != null) {
        sb.append(lineFile);
        sb.append(System.lineSeparator());
      }

      result = sb.toString();
    } finally {
      br.close();
    }

    return result;
  }

  private void connectionHandler(String request) throws IOException {
    OutputStreamWriter out = new OutputStreamWriter(clientSocket.getOutputStream());
    // Manipula cabeçalho para retirar informacoes importantes
    this.getInfoRequest(request);

    // Recupera caminho do arquivo solicitado
    if (requestPath == null) return;

    String requestedFileString = this.getPath(requestPath);
    Path requestedFilePath = Paths.get(requestedFileString);

    // Verificar o metodo solicitado
    if (!requestMethod.equals("GET")) {
      // Nao possui o metodo implementado (PUT, POST, DELETE), retorna erro 501
      responseHeader = "HTTP/1.1 501 Not Implemented\nContent-type: text/html\n\n";
      responseContent = "<h1>Erro 501 Not Implemented</h1>";
    }
    // Verificar se o arquivo existe
    else if (!Files.exists(requestedFilePath)) {
      // Nao possui o arquivo solicitado, retorna erro 404
      responseHeader = "HTTP/1.1 404 Not Found\nContent-type: text/html\n\n";
      responseContent = this.readFile(this.getPath("/notFound.html"));
    } else {
      // ok
      responseHeader = "HTTP/1.1 200 OK\nContent-type: text/html\n\n";
      responseContent = this.readFile(requestedFileString);
    }

    try {
      out.write(responseHeader);
      out.write(responseContent);
    } finally {
      out.flush();
      out.close();
    }
  }

  public void run() {
    try {
      // Recebido do cliente (browser)
      BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "ASCII"));

      // Lendo requisicao (header + content)
      String line = in.readLine();
      String request = line;
      while ((line = in.readLine()) != null) {
        request = request + "\r\n" + line;
        if (line.length() == 0)
          break;
      }

      this.connectionHandler(request);
      in.close();
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
    }
  }
}
