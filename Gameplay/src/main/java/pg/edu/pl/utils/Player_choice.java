package pg.edu.pl.utils;

import lombok.Getter;

/**
 * Enum to represent players in the Battleships game.
 */
@Getter
public enum Player_choice {
    PLAYER_ONE("Player 1"),
    PLAYER_TWO("Player 2");

    /**
     * -- GETTER --
     *  Getter method for the player's name
     */
    private final String playerName;

    /**
     * Constructor for the enum
     */
    Player_choice(String playerName) {
        this.playerName = playerName;
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