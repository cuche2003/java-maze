package com.nat.maze.game;

import com.nat.maze.game.entity.Player;


public class GameState {
    public final GameConfig gameConfig;
    public final Map map;
    public final Player player;
    public boolean done = false;
    public boolean won = false;
    public double clearTime = 0;

    public GameState(GameConfig cfg) {
        gameConfig = cfg;
        map = new Map(gameConfig.getMapPath());
        player = new Player(map.playerInitialPosition);
    }
}
