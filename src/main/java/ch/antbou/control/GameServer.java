package ch.antbou.control;

import ch.antbou.boundary.dto.PlayerDto;
import ch.antbou.boundary.in.JoinRequest;
import ch.antbou.boundary.out.ErrorResponse;
import ch.antbou.boundary.out.JoinResponse;
import ch.antbou.boundary.out.StartResponse;
import ch.antbou.entity.Player;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.factory.Mappers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameServer {
    private static final int PLAYERS_REQUIRED = 2;

    private final int port;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PlayerMapper playerMapper = Mappers.getMapper(PlayerMapper.class);
    private final List<Player> players = new ArrayList<>();
    private final List<ClientSession> sessions = new ArrayList<>();
    private int nextPlayerId = 1;

    public GameServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server listening on port " + port);
            while (players.size() < PLAYERS_REQUIRED) {
                Socket clientSocket = serverSocket.accept();
                createSession(clientSocket);
            }
            broadcastStart();
        }
    }

    private void createSession(Socket clientSocket) throws IOException {
        ClientSession session = new ClientSession(clientSocket);
        sessions.add(session);
        JoinRequest request = readJoinRequest(session);
        if (request == null) {
            return;
        }
        registerPlayer(session, request);
    }

    private JoinRequest readJoinRequest(ClientSession session) throws IOException {
        String json = session.reader.readLine();
        if (json == null || json.isBlank()) {
            sendError(session, "Empty request");
            session.close();
            return null;
        }
        JoinRequest request = objectMapper.readValue(json, JoinRequest.class);
        if (!"join".equalsIgnoreCase(request.getType())) {
            sendError(session, "Invalid message type");
            session.close();
            return null;
        }
        return request;
    }

    private void registerPlayer(ClientSession session, JoinRequest request) throws IOException {
        Player player = playerMapper.toEntity(request);
        player.setId(nextPlayerId++);
        players.add(player);

        JoinResponse response = new JoinResponse();
        response.setType("join-ack");
        response.setPlayer(playerMapper.toDto(player));
        response.setPlayersRequired(PLAYERS_REQUIRED);
        response.setConnectedPlayers(players.size());

        session.writer.println(objectMapper.writeValueAsString(response));
    }

    private void broadcastStart() throws IOException {
        StartResponse response = new StartResponse();
        response.setType("start");

        List<PlayerDto> playerDtos = new ArrayList<>();
        for (Player player : players) {
            playerDtos.add(playerMapper.toDto(player));
        }
        response.setPlayers(playerDtos);

        String payload = objectMapper.writeValueAsString(response);
        for (ClientSession session : sessions) {
            session.writer.println(payload);
        }
    }

    private void sendError(ClientSession session, String message) throws IOException {
        ErrorResponse response = new ErrorResponse(message);
        session.writer.println(objectMapper.writeValueAsString(response));
    }
}
