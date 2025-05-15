package pg.edu.pl;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.*;

import pg.edu.pl.TestClient;
import pg.edu.pl.server.GameServer;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GameServerTest {

    private static Thread serverThread;
    private static final int TEST_PORT = 60001;

    @BeforeAll
    static void startServer() {
        serverThread = new Thread(() -> new GameServer().start("localhost", 50, TEST_PORT));
        serverThread.start();
        try {
            Thread.sleep(1000); // give server time to start
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @AfterAll
    static void stopServer() throws IOException {
        try (TestClient adminClient = new TestClient("localhost", TEST_PORT)) {
            adminClient.sendMessage("shutdown");
        }
    }

    @Test
    void testTwoClientsStartGame() throws IOException, InterruptedException {
        try (
                TestClient player1 = new TestClient("localhost", TEST_PORT);
                TestClient player2 = new TestClient("localhost", TEST_PORT)
        ) {
            System.out.println("New");
            String msg1 = player1.readMessage();
            String msg2 = player2.readMessage();

            assertTrue(msg1.contains("CONNECTED"));
            assertTrue(msg2.contains("CONNECTED"));

            System.out.println("MESSAGE BIG");
            System.out.println(msg1);
            System.out.println(msg2);

            // Send a dummy board print command to see if it gets processed
            player1.sendMessage("{\"type\": \"PRINT_MY_BOARD\"}");
            String board1 = player1.readMessage();
            assertTrue(board1.contains(".") || board1.contains("X"), "Expected a board response");

            System.out.println("MESSAGE SMALL");
            System.out.println(board1);

            player2.sendMessage("{\"type\": \"PRINT_MY_BOARD\"}");
            String board2 = player2.readMessage();
            assertTrue(board2.contains(".") || board2.contains("X"), "Expected a valid board response.");

            System.out.println(board2);

            player1.sendMessage("{\"type\": \"EXIT\"}");
            player2.sendMessage("{\"type\": \"EXIT\"}");

        } catch (IOException e) {
            fail("Failed during game session test: " + e.getMessage());
        }
    }

    @Test
    void testShipPlacement() throws IOException {
        try (TestClient client = new TestClient("localhost", TEST_PORT);
             TestClient player2 = new TestClient("localhost", TEST_PORT)) {

            String response1 = client.readMessage();
            String response2 = player2.readMessage();

            System.err.println("PLAYER 1 RESPONSE: " + response1);
            System.err.println("PLAYER 2 RESPONSE: " + response2);


            // Create a message to place a ship
            JsonObject placeShipMessage = new JsonObject();
            placeShipMessage.addProperty("type", "PLACE_SHIP");
            placeShipMessage.addProperty("x", 2);
            placeShipMessage.addProperty("y", 3);
            placeShipMessage.addProperty("horizontal", true);
            placeShipMessage.addProperty("size", 3);
            client.sendMessage(placeShipMessage.toString());

            // Read the server response
            String response = client.readMessage();

            System.err.println("RESPONSE: " + response);
            assertTrue(response.contains("PLACED: true"), "Expected a ship placement success message.");
        }
    }
}
