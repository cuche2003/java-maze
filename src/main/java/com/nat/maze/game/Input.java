package com.nat.maze.game;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.nat.maze.game.entity.Entity;
import com.nat.maze.game.entity.Player;
import com.nat.maze.math.Vector2;

import java.io.IOException;

public class Input {
    private final GameState gameState;
    private final Screen screen;

    public Input(GameState gs, final Screen s) {
        gameState = gs;
        screen = s;
    }

    private boolean isEscapePressed(KeyStroke ks) {
        return (ks != null && (ks.getKeyType() == KeyType.Escape || ks.getKeyType() == KeyType.EOF));
    }


    private boolean isKeyPressed(KeyStroke ks, char c) {
        return (ks != null && ks.getKeyType() == KeyType.Character && ks.getCharacter() == c);
    }

    public void update(double delta) throws IOException {
        Map m = gameState.map;
        Player p = gameState.player;

        Vector2 dir = p.direction;
        Vector2 pos = p.position;
        Vector2 pln = p.plane;
        
        KeyStroke ks = screen.pollInput();
        if (isEscapePressed(ks)) {
            gameState.done = true;
        }

        double dltMvSpd = p.moveSpeed * delta;
        double dltRotSpd = p.rotateSpeed * delta;

        //move forwards
        if (isKeyPressed(ks,'w')) {
            if (m.getMap()[(int) (pos.x + dir.x * dltMvSpd)][(int) (pos.y)] != Entity.Type.Wall) pos.x += dir.x * dltMvSpd;
            if (m.getMap()[(int) (pos.x)][(int) (pos.y + dir.y * dltMvSpd)] != Entity.Type.Wall) pos.y += dir.y * dltMvSpd;
        }
        //move backwards if no wall behind you
        if (isKeyPressed(ks,'s')) {
            if (m.getMap()[(int) (pos.x - dir.x * dltMvSpd)][(int) (pos.y)] != Entity.Type.Wall) pos.x -= dir.x * dltMvSpd;
            if (m.getMap()[(int) (pos.x)][(int) (pos.y - dir.y * dltMvSpd)] != Entity.Type.Wall) pos.y -= dir.y * dltMvSpd;
        }
        // move right
        if (isKeyPressed(ks,'d')) {
            if (m.getMap()[(int) (pos.x + pln.x * dltMvSpd)][(int) (pos.y)] != Entity.Type.Wall)
                pos.x += pln.x * dltMvSpd;
            if (m.getMap()[(int) (pos.x)][(int) (pos.y + pln.y * dltMvSpd)] != Entity.Type.Wall)
                pos.y += pln.y * dltMvSpd;
        }
        //move left
        if (isKeyPressed(ks,'a')) {
            if (m.getMap()[(int) (pos.x - pln.x * dltMvSpd)][(int) (pos.y)] != Entity.Type.Wall)
                pos.x -= pln.x * dltMvSpd;
            if (m.getMap()[(int) (pos.x)][(int) (pos.y - pln.y * dltMvSpd)] != Entity.Type.Wall)
                pos.y -= pln.y * dltMvSpd;
        }
        //rotate to the right
        if (isKeyPressed(ks,'l')) {
            //both camera direction and camera plane must be rotated
            double oldDirX = dir.x;
            dir.x = dir.x * Math.cos(-dltRotSpd) - dir.y * Math.sin(-dltRotSpd);
            dir.y = oldDirX * Math.sin(-dltRotSpd) + dir.y * Math.cos(-dltRotSpd);
            double oldPlaneX = pln.x;
            pln.x = pln.x * Math.cos(-dltRotSpd) - pln.y * Math.sin(-dltRotSpd);
            pln.y = oldPlaneX * Math.sin(-dltRotSpd) + pln.y * Math.cos(-dltRotSpd);
        }
        //rotate to the left
        if (isKeyPressed(ks,'j')) {
            //both camera direction and camera plane must be rotated
            double oldDirX = dir.x;
            dir.x = dir.x * Math.cos(dltRotSpd) - dir.y * Math.sin(dltRotSpd);
            dir.y = oldDirX * Math.sin(dltRotSpd) + dir.y * Math.cos(dltRotSpd);
            double oldPlaneX = pln.x;
            pln.x = pln.x * Math.cos(dltRotSpd) - pln.y * Math.sin(dltRotSpd);
            pln.y = oldPlaneX * Math.sin(dltRotSpd) + pln.y * Math.cos(dltRotSpd);
        }
        if (isKeyPressed(ks,'i')) {
            // look up
            p.pitch += 20 * dltMvSpd;
            if (p.pitch > 200) p.pitch = 200;
        }
        if (isKeyPressed(ks,'k')) {
            // look down
            p.pitch -= 20 * dltMvSpd;
            if (p.pitch < -200) p.pitch = -200;
        }
    }
}
