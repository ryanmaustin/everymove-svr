package org.everymove.svr.main.structs;

import java.util.Objects;

public class Coordinate 
{
    private int x;
    private int y;
    private String piece;

    public Coordinate() {
        this.x = 1;
        this.y = 1;
    }

    public Coordinate(int x, int y) {
        this.x = x;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setPiece(String piece) {
        this.piece = piece;
    }

    public String getPiece() {
        return this.piece;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Coordinate)) return false;
        Coordinate other = (Coordinate) o;
        return this.x == other.x && this.y == other.y;
    }
    
}
