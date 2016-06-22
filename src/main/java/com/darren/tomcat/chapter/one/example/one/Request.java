package com.darren.tomcat.chapter.one.example.one;

import java.io.IOException;
import java.io.InputStream;

public class Request {

    private InputStream in;
    private String uri;

    public Request(InputStream in) {
        this.in = in;
    }

    public void parse() {
        // read a set of characters from the socket
        StringBuffer request = new StringBuffer(2048);
        int i;
        byte[] buffer = new byte[2048];
        try {
            i = in.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            i = -1;
        }

        for (int j = 0; j < i; j++) {
            request.append((char) buffer[j]);
        }

        System.out.println(request.toString());
        uri = this.parseUri(request.toString());
    }

    private String parseUri(String requestString) {
        int index1, index2;
        // find the first space
        index1 = requestString.indexOf(' ');
        if (index1 != -1) {
            // find the secode space
            index2 = requestString.indexOf(' ', index1 + 1);
            if (index2 > index1) {
                return requestString.substring(index1 + 1, index2);
            }
        }

        return null;
    }

    public String getUri() {
        return uri;
    }
}
