package com.tanishqsaluja.convex;

/**
 * Created by tanishqsaluja on 11/4/18.
 */

public class Vector {
    private int x;
    private int y;

    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean turnsLeft(Vector v) {
        if (getY() * v.getX() < getX() * v.getY()) {
            return true;
        } else {
            return false;
        }
    }
}
