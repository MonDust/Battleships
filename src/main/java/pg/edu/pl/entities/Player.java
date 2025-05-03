package pg.edu.pl.entities;

import pg.edu.pl.utils.Player_choice;

public class Player {
    private final Player_choice id;
    private final Board board;

    /**
     * Creates a player with a certain id and a new board for the player.
     * @param id id of the player
     */
    public Player(Player_choice id) {
        this.id=id;
        this.board=new Board();
    }

    public Player(Player_choice id, Board board) {
        this.id=id;
        this.board=board;
    }

    public Player_choice getPlayerID() {
        return id;
    }

    public Board getBoard() {
        return board;
    }

    /**
     * Handles an incoming shot at this players board.
     * @param x x coordinate of the field to shoot
     * @param y y coordinate of the field to shoot
     * @return ALREADY_REVEALED - if the field has been revealed before and cannot be shot at again,
     * HIT - if the shoot hit a ship,
     * MISS - if the field shot at is empty,
     */
     public Board.ShotResult shoot(int x, int y) {
        return board.shoot(x, y);
    }
}
