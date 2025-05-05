package pg.edu.pl;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

public class TestClient implements AutoCloseable {
    private final Socket socket;
    private final BufferedReader reader;
    private final BufferedWriter writer;

    public TestClient(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void sendMessage(String msg) throws IOException {
        writer.write(msg);
        writer.newLine();
        writer.flush();
    }

    public String readMessage() throws IOException {
        String line = reader.readLine();
        System.out.println("Response from server: " + line);
        return line;
    }

    @Override
    public void close() throws IOException {
        socket.close();
        reader.close();
        writer.close();
    }
}
