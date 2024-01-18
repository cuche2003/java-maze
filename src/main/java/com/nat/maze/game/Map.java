package com.nat.maze.game;

import com.nat.maze.game.entity.Entity;
import com.nat.maze.math.Vector2;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Map {
    private Entity.Type[][] map = new Entity.Type[][]{

    };
    public Entity.Type[][] getMap() {
        return map;
    }

    private Vector2 mapSize;

    public Vector2 playerInitialPosition;

    public Map(String file) {
        Charset characterSet = Charset.defaultCharset();
        Path path = Paths.get(file);
        try (BufferedReader reader = Files.newBufferedReader(path, characterSet)) {
            String line = reader.readLine();
            if (line == null) return;
            String[] sizes = line.split("\\s+");
            int w = Integer.parseInt(sizes[0]);
            int h = Integer.parseInt(sizes[1]);

            mapSize = new Vector2(w,h);

            map = new Entity.Type[h][w];

            int y = 0;
            while ((line = reader.readLine()) != null) {
                String[] values = line.trim().split("\\s+");

                for (int x = 0; x < w; x++) {
                    Entity.Type t = Entity.Type.fromValue(Integer.parseInt(values[x]));
                    if (t == Entity.Type.Player) {
                        playerInitialPosition = new Vector2(x,y);
                        map[y][x] = Entity.Type.None;
                        continue;
                    }

                    map[y][x] = t;
                }

                y++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Vector2 getMapSize() {
        return mapSize;
    }
}
