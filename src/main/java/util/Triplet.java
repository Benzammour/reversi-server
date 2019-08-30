package util;

import java.util.Objects;

/**
 * Created by Marc Luque on 05.04.2019.
 */
public class Triplet {

    private int x;

    private int y;

    private int r;

    public Triplet(int x, int y, int r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Triplet triplet = (Triplet) o;
        return x == triplet.x && y == triplet.y && r == triplet.r;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, r);
    }

    @Override
    public String toString() {
        return "(" + x +", " + y + ", " + r + ')';
    }

    @Override
    public Triplet clone() {
        return new Triplet(x, y, r);
    }
}