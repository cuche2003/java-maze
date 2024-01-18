package com.nat.maze.game;

public class GameConfig {
    private String mapPath;

    public GameConfig() {};
    public GameConfig(String p) {
        mapPath = p;
    }

    public String getMapPath() { return mapPath; }
    public void setMapPath(String s) {mapPath = s;}
}
