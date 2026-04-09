import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;

public class OLevel1Classic {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/redirect", new RedirectHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Server started on port 8080.");
    }

    static class RedirectHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String query = t.getRequestURI().getQuery();
            String redirectTo = query != null && query.startsWith("url=") ? query.substring(4) : "/";
            t.getResponseHeaders().add("Location", redirectTo);
            t.sendResponseHeaders(302, -1);
            t.close();
        }
    }
}
