package com.nat.maze.game;

import com.googlecode.lanterna.screen.Screen;
import com.nat.maze.game.entity.Entity;
import com.nat.maze.math.Vector2;

import java.io.IOException;

public class Game {
    private final Screen screen;
    private final GameConfig gameConfig;
    private final GameState gameState;
    private final Renderer renderer;
    private final Input input;

    public Game(GameConfig cfg, Screen s) {
        gameConfig = cfg;
        screen = s;
        gameState = new GameState(gameConfig);
        renderer = new Renderer(gameState, screen);
        input = new Input(gameState, screen);
    }

    public void update(double delta) throws IOException {
        input.update(delta);

        Vector2 pos = gameState.player.position;
        if (gameState.map.getMap()[(int) pos.x][(int) pos.y] == Entity.Type.Flag) {
            gameState.won = true;
            gameState.done = true;
        }
    }

    public void render(double delta) throws IOException {
        renderer.render(delta);
    }

    public void loop() throws IOException {
        double startTime = System.currentTimeMillis();

        int targetMillis = 1000 / 60,
            lastTime = (int) System.currentTimeMillis(),
            targetTime = lastTime + targetMillis;
        double delta;

        while (!gameState.done) {
            int current = (int) System.currentTimeMillis(); // Now time
            if (current < targetTime) continue; // Stop here if it's not time for the next frame
            delta = (double) targetMillis / (current - lastTime);//Scale game on how late frame is.
            lastTime = current;
            targetTime = targetMillis + targetTime;// Create next frame where it should be (in targetMillis) and subtract if frame was late.

            update(delta);
            render(delta);

            screen.refresh();
            Thread.yield();
        }

        gameState.clearTime = (System.currentTimeMillis() - startTime) / 1000;
    }

    public boolean isWon() {
        return gameState.won;
    }

    public double getClearTime() {
        return gameState.clearTime;
    }
}
