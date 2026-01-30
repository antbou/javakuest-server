package ch.antbou.control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSession {
    final Socket socket;
    final BufferedReader reader;
    final PrintWriter writer;

    ClientSession(Socket socket) throws IOException {
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new PrintWriter(socket.getOutputStream(), true);
    }

    void close() throws IOException {
        reader.close();
        writer.close();
        socket.close();
    }
}
