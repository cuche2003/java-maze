package com.nat.maze.game.entity;

import com.nat.maze.math.Vector2;

public class Player extends Entity {
    public Vector2 direction = new Vector2(-1, 0);
    public double depth = 16;
    public Vector2 plane = new Vector2(0, 0.66f); //the 2d raycaster version of camera plane
    public double pitch = 0; // looking up/down, expressed in screen pixels the horizon shifts
    public double moveSpeed = 0.1; //the constant value is in squares/second
    public double rotateSpeed = 0.1; //the constant value is in radians/second
    public Player(Vector2 p){
        position = p;
    };

}
