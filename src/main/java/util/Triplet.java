package util;

import java.util.Objects;

public class Triplet {

    private int x;

    private int y;

    private int d;

    public Triplet(int x, int y, int r) {
        this.x = x;
        this.y = y;
        this.d = r;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
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
        return x == triplet.x && y == triplet.y && d == triplet.d;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, d);
    }

    @Override
    public String toString() {
        return "(" + x +", " + y + ", " + d + ')';
    }

    @Override
    public Triplet clone() {
        return new Triplet(x, y, d);
    }
}