package com.nat.maze.game;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Arrays;

/*
 * hippity hoppity, your code is my property
 * https://rosettacode.org/wiki/Maze_generation#Java
 */
public class MazeGenerator {
    private final int x;
    private final int y;
    private final int[][] maze;

    public MazeGenerator(int x, int y) {
        this.x = x;
        this.y = y;
        maze = new int[this.x][this.y];
        generateMaze(0, 0);
    }

    private void generateMaze(int cx, int cy) {
        DIR[] dirs = DIR.values();
        Collections.shuffle(Arrays.asList(dirs));
        for (DIR dir : dirs) {
            int nx = cx + dir.dx;
            int ny = cy + dir.dy;
            if (between(nx, x) && between(ny, y)
                    && (maze[nx][ny] == 0)) {
                maze[cx][cy] |= dir.bit;
                maze[nx][ny] |= dir.opposite.bit;
                generateMaze(nx, ny);
            }
        }
    }

    private static boolean between(int v, int upper) {
        return (v >= 0) && (v < upper);
    }

    private enum DIR {
        N(1, 0, -1), S(2, 0, 1), E(4, 1, 0), W(8, -1, 0);
        private final int bit;
        private final int dx;
        private final int dy;
        private DIR opposite;

        // use the static initializer to resolve forward references
        static {
            N.opposite = S;
            S.opposite = N;
            E.opposite = W;
            W.opposite = E;
        }

        private DIR(int bit, int dx, int dy) {
            this.bit = bit;
            this.dx = dx;
            this.dy = dy;
        }
    };

    public void writeToFile(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(String.format("%d %d\n", x * 2 + 1, y * 2 + 1));
            for (int i = 0; i < y; i++) {
                // draw the north edge
                for (int j = 0; j < x; j++) {
                    writer.write((maze[j][i] & 1) == 0 ? "2 2 " : "2 0 ");
                }
                writer.write("2\n");
                // draw the west edge
                for (int j = 0; j < x; j++) {
                    if (i == 0 && j == 0) {
                        writer.write("2 1 ");
                        continue;
                    }
                    if (i == y - 1 && j == x - 1) {
                        writer.write((maze[j][i] & 8) == 0 ? "2 3 " : "0 3 ");
                        continue;
                    }
                    writer.write((maze[j][i] & 8) == 0 ? "2 0 " : "0 0 ");
                }
                writer.write("2\n");
            }
            // draw the bottom line
            for (int j = 0; j < x; j++) {
                writer.write("2 2 ");
            }
            writer.write("2\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}