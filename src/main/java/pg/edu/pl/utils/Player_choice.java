package pg.edu.pl.utils;

/**
 * Enum to represent players in the Battleships game.
 */
public enum Player_choice {
    PLAYER_ONE("Player 1"),
    PLAYER_TWO("Player 2");

    private final String playerName;

    /**
     * Constructor for the enum
     */
    Player_choice(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Getter method for the player's name
     * @return String - player's name.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * to String.
     * @return String
     */
    @Override
    public String toString() {
        return "Current turn: " + getPlayerName();
    }
}