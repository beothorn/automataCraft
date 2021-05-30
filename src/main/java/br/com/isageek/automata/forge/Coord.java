package br.com.isageek.automata.forge;

public class Coord {

    public int x;
    public int y;
    public int z;

    public Coord(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Coord c(int x, int y, int z){
        return new Coord(x, y, z);
    }

    public static Coord c(Coord c){
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
}
