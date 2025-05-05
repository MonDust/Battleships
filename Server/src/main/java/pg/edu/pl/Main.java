package pg.edu.pl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pg.edu.pl.server.GameServer;

import javax.annotation.PostConstruct;
import java.io.IOException;

@SpringBootApplication
public class Main implements CommandLineRunner {

    /**
     * default: 60000
     */
    @Value("${server.port:60000}")
    private int port;

    /**
     * default: localhost
     */
    @Value("${server.address:localhost}")
    private String host;

    /**
     * default: 50
     */
    @Value("${server.backlog:50}")
    private int backlog;

    @PostConstruct
    public void printConfig() {
        System.out.printf("Server config: host=%s, port=%d, backlog=%d%n", host, port, backlog);
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) {
        new GameServer().start(host, backlog, port);
    }
}