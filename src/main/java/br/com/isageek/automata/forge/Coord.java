package br.com.isageek.automata.forge;

import java.util.Objects;

public class Coord {

    public int x;
    public int y;
    public int z;

    public Coord(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Coord coord(int x, int y, int z){
        return new Coord(x, y, z);
    }

    public static Coord coord(Coord c){
        return new Coord(c.x, c.y, c.z);
    }

    public static Coord firstPatternFromTo(Coord start, Coord end){
        if(start.x > end.x){
            return new Coord(start.x - 2, start.y, start.z);
        }
        if(start.x < end.x){
            return new Coord(start.x + 2, start.y, start.z);
        }
        if(start.z > end.z){
            return new Coord(start.x, start.y, start.z - 2);
        }
        if(start.z < end.z){
            return new Coord(start.x, start.y, start.z + 2);
        }

        return null;
    }

    public void moveTowards(Coord c, int amount) {
        if(x > c.x){
            this.x = this.x - amount;
        }
        if(x < c.x){
            this.x = this.x + amount;
        }
        if(z > c.z){
            this.z = this.z - amount;
        }
        if(z < c.z){
            this.z = this.z + amount;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coord coord = (Coord) o;
        return x == coord.x && y == coord.y && z == coord.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return "Coord{x=" + x + ", y=" + y + ", z=" + z + "}";
    }
}
