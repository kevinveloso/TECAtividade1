package br.sorage.ufpb.selecao.servidor;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Conexao implements Runnable {

  // socket que vai responder o cliente.
  Socket socket;

  // caso nao seja passado um arquivo, o servidor fornece a pagina index.html
  public static String arqi = "pages/index.html";

  public Conexao(Socket s) {
    socket = s;
  }

  // no metodo abaixo serah tratada a comunicacao com o browser
  public void trataConexao() {
    String metodo; // metodo HTTP requerido
    String ct; // tipo de arquivo: text/html;image/jpg....
    String versao = ""; // versao do Protocolo.

    // arquivos que irao ser enviados.
    File arquivo = new File("files/protocol.txt");

    // o nome do arquivo.
    String nomeArq = arquivo.getName();

    // diretorio raiz do servidor.
    String raiz = ".";

    // inicio da linh do pedido do clientea
    String inicio;

    // nome e a senha do usuario
    String senha_user = "";
  }

  public void run() {

    try {
      String filename = "files/protocol.txt";
      FileInputStream fileInput = new FileInputStream(arqi);
      BufferedInputStream input = new BufferedInputStream(fileInput);

      PrintWriter out = new PrintWriter(socket.getOutputStream());

      out.println("HTTP/1.1 200 OK");
      out.println("Content-Type: text/html");

      while (input.available() > 0) {
        char c = (char) input.read();
        out.println(c);
      }

      input.close();
      fileInput.close();
      out.flush();
      out.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

}
