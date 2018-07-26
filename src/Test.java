
/**
 * Test
 */
public class Test {

  public static void main(String[] args) {
    String message = "GET / HTTP/1.1\n" +
    "Host: localhost:8081\n" +
    "Connection: keep-alive\n" +
    "Cache-Control: max-age=0\n" +
    "Upgrade-Insecure-Requests: 1\n" +
    "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6)\n" +
    "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36\n" +
    "Accept:\n" +
    "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\n" +
    "Accept-Encoding: gzip, deflate, br\n" +
    "Accept-Language: pt,en-US;q=0.9,en;q=0.8,ja;q=0.7";

    String[] lines = message.split("\n");
    String[] firstLineSplitted = lines[0].split(" ");
    String methodRequest = firstLineSplitted[0];
    String pathRequest = firstLineSplitted[1];
    String httpVersionRequest = firstLineSplitted[2];
    System.out.println(methodRequest);
    System.out.println(pathRequest);
    System.out.println(httpVersionRequest);
    for (String line : lines) {
      if (line.substring(0, 6).equals("Host: ")) {
        System.out.println(line);
      }
    }
  }
}