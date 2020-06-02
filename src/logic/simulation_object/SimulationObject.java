package logic.simulation_object;

import java.awt.*;

public class SimulationObject {

    private int x,y;
    private Color color;

    public SimulationObject(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public void setX(int x) {
        this.x = x;
    }
    public int getX() {
        return x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int getY() {
        return y;
    }
    public void setColor(Color color) {
        this.color = color;
    }
    public Color getColor() {
        return color;
    }
}

