package ch.antbou;

import ch.antbou.control.GameServer;

import java.io.IOException;

public class Main {
    private static final int PORT = 6666;

    public static void main(String[] args) throws IOException {
        GameServer server = new GameServer(PORT);
        server.start();
    }
}
