import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import javax.net.ssl.SSLSocketFactory;


public class HelloBrowser {

    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter a URL: ");
        String addr = input.nextLine();

        if (!addr.startsWith("http://") && !addr.startsWith("https://")) {
            addr = "http://" + addr;
        }

        URL url = new URL(addr);
        String host = url.getHost();
        int port = url.getPort() == -1 ? (url.getProtocol().equals("https") ? 443 : 80) : url.getPort();

        Socket socket;
        if (url.getProtocol().equals("https")) {
            socket = SSLSocketFactory.getDefault().createSocket(host, port);
        } else {
            socket = new Socket(host, port);
        }

        PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

        writer.println("GET " + (url.getPath().isEmpty() ? "/" : url.getPath()) + (url.getQuery() != null ? "?" + url.getQuery() : "") + " HTTP/1.1");
        writer.println("Host: " + host);
        writer.println("Connection: close");
        writer.println();

        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        reader.close();
        writer.close();
        socket.close();
        input.close();
    }
}



