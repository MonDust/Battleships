package pg.edu.pl.communication;

public class GameAction {
    public String action; // "shoot", "placeShip", etc.
    public int x;
    public int y;
}

// client
/*
{ "type": "PLACE_SHIP", "shipType": "DESTROYER", "x": 1, "y": 3, "horizontal": true }
{ "type": "FIRE", "x": 4, "y": 6 }
{ "type": "RESTART" }

{ "type": "place_ship", "x": 1, "y": 2, "size": 3, "horizontal": true }
{ "type": "shoot", "x": 4, "y": 5 }

 */

// server
/*
{ "type": "RESULT", "result": "HIT", "nextTurn": "Player 2" }

 { "type": "shot_result", "result": "miss" }
{ "type": "update_board", "board": [[...]] }
 */