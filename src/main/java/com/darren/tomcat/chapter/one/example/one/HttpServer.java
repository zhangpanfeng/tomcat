package com.darren.tomcat.chapter.one.example.one;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

    public static final String PAGE_ROOT = System.getProperty("user.dir") + File.separator + "src/main/webapp";

    // shutdown command
    private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";

    // the shutdown received
    private boolean shutdown = false;

    public void await() {
        ServerSocket serverSocket = null;
        int port = 9999;
        try {
            serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        while (!shutdown) {
            Socket socket = null;
            InputStream in = null;
            OutputStream out = null;

            try {
                //this line will block
                socket = serverSocket.accept();
                in = socket.getInputStream();
                out = socket.getOutputStream();
                
                // create Request object and parse
                Request request = new Request(in);
                request.parse();

                // create Response object
                Response response = new Response(out);
                response.setRequest(request);
                response.sendStaticResource();

                // close the socket
                socket.close();

                // check if the previous URL is a shutdown command
                shutdown = request.getUri().equals(SHUTDOWN_COMMAND);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    public static void main(String[] args) {
        HttpServer server = new HttpServer();
        server.await();
    }

}
