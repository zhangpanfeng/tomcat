package com.darren.tomcat.chapter.one.example.one;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Response {

    private static final int BUFFER_SIZE = 1024;
    private Request request;
    private OutputStream out;

    public Response(OutputStream out) {
        this.out = out;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public void sendStaticResource() throws IOException {
        byte[] bytes = new byte[BUFFER_SIZE];
        FileInputStream fin = null;
        try {
            File file = new File(HttpServer.PAGE_ROOT, request.getUri());
            if (file.exists()) {
                fin = new FileInputStream(file);
                out.write(this.getResponseHeader(200, "OK", file.length()).getBytes());

                int ch = fin.read(bytes, 0, BUFFER_SIZE);
                while (ch != -1) {
                    out.write(bytes, 0, ch);
                    ch = fin.read(bytes, 0, BUFFER_SIZE);
                }
            } else {
                // file not found
                String errorMessage = "<h1>File Not Found</h1>";
                out.write(this.getResponseHeader(404, "File Not Found", errorMessage.getBytes().length).getBytes());
                out.write(errorMessage.getBytes());
            }
        } catch (Exception e) {
            // thrown if cannot instantiate a File object
            System.out.println(e.toString());
        } finally {
            if (fin != null) {
                fin.close();
            }
        }
    }

    private String getResponseHeader(int stateCode, String stateMessage, long contentLength) {
        String header = "HTTP/1.1 " + stateCode + " " + stateMessage + "\r\n" 
            + "Content-Type: text/html\r\n"
            + "Content-Length: " + contentLength + "\r\n" 
            + "\r\n";

        return header;
    }
}
