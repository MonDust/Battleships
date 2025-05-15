package pg.edu.pl.entities;

import lombok.Getter;
import lombok.Setter;

import pg.edu.pl.entities.interfaces.IBoard;
import pg.edu.pl.entities.interfaces.IPlayer;

import pg.edu.pl.utils.Player_choice;
import pg.edu.pl.utils.ShotResult;

@Getter
@Setter
public class Player implements IPlayer {
    private final Player_choice id;
    private final IBoard board;

    /**
     * Creates a player with a certain id and a new board for the player.
     * @param id id of the player
     */
    public Player(Player_choice id) {
        this.id=id;
        this.board=new Board();
    }

    public Player(Player_choice id, IBoard board) {
        this.id=id;
        this.board=board;
    }

    /**
     * Handles an incoming shot at this players board.
     * @param x x coordinate of the field to shoot
     * @param y y coordinate of the field to shoot
     * @return ALREADY_REVEALED - if the field has been revealed before and cannot be shot at again,
     * HIT - if the shoot hit a ship,
     * MISS - if the field shot at is empty,
     */
     @Override
     public ShotResult shoot(int x, int y) {
        return board.shoot(x, y);
    }
}
