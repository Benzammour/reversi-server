package util;

import java.util.Objects;

/**
 * Created by Marc Luque on 08.04.2019.
 */
public class Tuple {

    public int x;

    public int y;

    public Tuple(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Tuple tuple = (Tuple) o;
        return x == tuple.x && y == tuple.y;
    }

    @Override
    public Tuple clone() {
        return new Tuple(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ')';
    }
}