import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Servidor {
  // static Logger logger = Logger.getInstance();

  private final int port;

  public Servidor(int port) {
    // logger.info("Port set to " + port);
    this.port = port;
  }

  public void listen() {
    ServerSocket servidor = null;

    try {
      servidor = new ServerSocket(port);
      // logger.info(String.format("Porta %d aberta ...", port));

      while (true) {
        // logger.info("Aguardando clientes ...");
        System.out.println("Servidor ouvindo na porta " + port);

        Socket cliente = servidor.accept();
        // logger.info(String.format("Cliente %s conectado!", cliente.getInetAddress().getHostAddress()));
        System.out.println(String.format("Cliente %s conectado!", cliente.getInetAddress().getHostAddress()));
        Conexao con = new Conexao(cliente);
        new Thread(con).start();

      }
    } catch (IOException e) {

      e.printStackTrace();

      if (servidor != null && !servidor.isClosed()) {

        try {
          servidor.close();
        } catch (IOException e1) {
          e1.printStackTrace();
        }

      }
    }
  }

  public static void main(String[] args) throws UnknownHostException, IOException {
    new Servidor(8081).listen();
  }
}
