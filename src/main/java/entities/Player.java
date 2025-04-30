package entities;

public class Player {
    private int id;
    private Board board;

    /**
     * Creates a player with a certain id and a new board for the player.
     * @param id id of the player
     */
    public Player(int id) {
        this.id=id;
        this.board=new Board();
    }

    public int getId() {
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
