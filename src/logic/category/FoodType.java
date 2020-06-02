package logic.category;

import java.awt.*;
import java.io.Serializable;

public class FoodType implements Serializable {

    private String name;
    private Color color;
    private double foodProbability;
    private int defaultEnergy;

    public FoodType(String name, Color color, double foodProbability, int defaultEnergy) {
        this.name = name;
        this.color = color;
        this.foodProbability = foodProbability;
        this.defaultEnergy = defaultEnergy;
    }
    public FoodType() {
        this("Neue Nahrung", Color.RED, 0.15, 100);
    }

    public void setName(String name) { this.name = name; }
    public String getName() { return this.name; }
    public void setColor(Color color) { this.color = color; }
    public Color getColor() { return this.color; }
    public void setFoodProbability(double foodProbability) { this.foodProbability = foodProbability; }
    public double getFoodProbability() { return this.foodProbability; }
    public void setDefaultEnergy(int defaultEnergy) { this.defaultEnergy = defaultEnergy; }
    public int getDefaultEnergy() { return this.defaultEnergy; }
}
