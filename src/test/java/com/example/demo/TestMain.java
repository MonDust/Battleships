package com.example.demo;

import pg.edu.pl.entities.Player;
import pg.edu.pl.entities.Ship;
import pg.edu.pl.utils.ShotResult;
import pg.edu.pl.utils.Player_choice;

public class TestMain {
    public static void main(String[] args) {
        // Create two players
        Player p1 = new Player(Player_choice.PLAYER_ONE);
        Player p2 = new Player(Player_choice.PLAYER_TWO);

        System.out.println("=== PLACING SHIPS ===");
        // Player 1: place a Destroyer of length 2 horizontally at (0,0)
        Ship destroyer1 = new Ship(2);
        boolean placed1 = p1.getBoard().placeShip(destroyer1, 0, 0, true);
        System.out.println("P1: place destroyer at (0,0) horizontal -> " + placed1);

        // Player 2: place a Cruiser of length 3 vertically at (2,2)
        Ship cruiser2 = new Ship(3);
        boolean placed2 = p2.getBoard().placeShip(cruiser2, 2, 2, false);
        System.out.println("P2: place cruiser at (2,2) vertical -> " + placed2);

        // Attempt collision: place another ship for P1 overlapping the first
        Ship collide = new Ship(2);
        boolean placedCollision = p1.getBoard().placeShip(collide, 0, 0, true);
        System.out.println("P1: place overlapping ship at (0,0) -> " + placedCollision);

        System.out.println("\n=== SHOOTING ===");
        // Player 1 shoots at Player 2's board
        ShotResult r1 = p2.shoot(2, 2);
        System.out.println("P1 shoots P2 at (2,2) -> " + r1); // expected HIT

        ShotResult r2 = p2.shoot(2, 3);
        System.out.println("P1 shoots P2 at (2,3) -> " + r2); // expected HIT

        ShotResult r3 = p2.shoot(2, 4);
        System.out.println("P1 shoots P2 at (2,4) -> " + r3); // expected HIT, ship sunk

        // Check if all Player 2's ships are sunk
        boolean p2Dead = p2.getBoard().areAllShipsSunk();
        System.out.println("Are all P2 ships sunk? -> " + p2Dead);

        // Player 1 shoots in the water
        ShotResult r4 = p2.shoot(0, 7);
        System.out.println("P1 shoots P2 at (0,7) -> " + r4); // expected MISS

        // Player 1 repeats the shot at the same coordinate
        ShotResult r5 = p2.shoot(0, 7);
        System.out.println("P1 shoots P2 at (0,7) again -> " + r5); // expected ALREADY_REVEALED

        System.out.println("\n=== SINKING P1 ===");
        // Player 2 shoots at Player 1's board to sink the destroyer
        ShotResult rr1 = p1.shoot(0, 0);
        System.out.println("P2 shoots P1 at (0,0) -> " + rr1); // expected HIT

        ShotResult rr2 = p1.shoot(1, 0);
        System.out.println("P2 shoots P1 at (1,0) -> " + rr2); // expected HIT, ship sunk

        // Check if all Player 1's ships are sunk
        boolean p1Dead = p1.getBoard().areAllShipsSunk();
        System.out.println("Are all P1 ships sunk? -> " + p1Dead);
    }
}
