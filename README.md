# Battleships

A real-time Battleships game implemented in Java using TCP sockets for communication and Swing for the graphical user interface (under development).
The current version supports Command Line Interface (CLI) gameplay for two players, with the server managing up to 50 concurrent client connections.
All the other connections get rejected.

## Gameplay
- classic Battleships mechanics, winner is the one who destroys all ships,
- server supports up to 50 clients simultaneously,
- automatic session pairing (2 players per game), client will wait until paired,
- CLI-based user interaction (more advanced GUI under development).

## Purpose
Project implementing battleships in Java with Swing used for graphics.

## Current state
The gameplay of the Battleships is implemented, as is connection between players and the server.

### Client module
Handles user interaction, board rendering in CLI, and communication with a server.
### Server module
Manages sessions, informs the players of the state of the game, and receives move instructions from players.
Enforces game rules.

## Under development
- Graphical Interface - currently only demo available in the shared module.
- Issues with player readiness, and turn-based gameplay.
- Testing for overlooked issues.

## How to use

### Clone the Repository
```
git clone ___
cd ___
```

### Configuration
Edit `config.properties` file if needed.

Path: `Gameplay/src/main/resources/config.properties` 

File: [config.properties](Gameplay/src/main/resources/config.properties)

### Compile
Use preferred Java IDE like IntelliJ IDEA to import and build the project easily. 

Requirements:
- Java 17 or higher installed and properly configured in your environment.
- Ensure that you can run multiple instances of the client application 

You can configure multiple run/debug configurations in IntelliJ to launch several client instances simultaneously e.g. "Allow multiple instances".

1. Running server: `Server/src/main/java/pg/edu/pl/ServerMain.java`
2. Running client: `Client/src/main/java/pg/edu/pl/ClientMain.java`

Running demo of the GUI (current implementation): `Gameplay/src/main/java/pg/edu/pl/Main.java`

## License
GNU AFFERO GENERAL PUBLIC LICENSE
Version 3, 19 November 2007

## Authors
O. Paszkiewicz

Aleksandra Susmarska
