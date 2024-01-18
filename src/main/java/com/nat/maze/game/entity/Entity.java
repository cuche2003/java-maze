package com.nat.maze.game.entity;

import com.nat.maze.math.Vector2;

public class Entity {
    public enum Type {
        None(0),
        Player(1),
        Wall(2),
        Flag(3),
        ;

        private static final Type[] typeValues = Type.values();

        public static Type fromValue(int v) {
            return typeValues[v];
        }

        public final int value;

        public boolean equals(int v){return value == v;}

        public String toString() {
            return Type.fromValue(value).toString();
        }

        Type(int v) {
            value = v;
        }
    }
    private final Type type;
    public Vector2 position;

    Entity() {
        type = Type.None;
        position = new Vector2();
    }
    Entity(Type t, Vector2 p) {
        type = t;
        position = p;
    }
}
