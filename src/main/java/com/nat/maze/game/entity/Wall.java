package com.nat.maze.game.entity;

import com.nat.maze.math.Vector2;

public class Wall extends Entity {
    Wall(Vector2 p) {
        super(Entity.Type.Player, p);
    }
}
