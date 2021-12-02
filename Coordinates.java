package battleship;

import java.util.ArrayList;

public class Coordinates {
    private int X;
    private int Y;
    public Coordinates(int X,int Y) {
        this.X = X;
        this.Y = Y;
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    public void setX(int X) {
        this.X = X;
    }

    public void setY(int Y) {
        this.Y = Y;
    }

    public static Coordinates[] fullShip(int cell,Coordinates first,Coordinates last){
        Coordinates[] Ship = new Coordinates[cell];

        if (first.getX() == last.getX()) {
            for (int i = 0; i < cell; i++) {
                Ship[i] = new Coordinates(first.getX(),first.getY() + i);
            }
        }

        if (first.getY() == last.getY()) {
            for (int i = 0; i < cell; i++) {
                Ship[i] = new Coordinates(first.getX() + i, first.getY());
            }
        }
        return Ship;
    }

}
