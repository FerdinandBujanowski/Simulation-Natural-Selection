package logic.category;

import java.awt.*;
import java.io.Serializable;

public class GroundType implements Serializable {

    private FoodType foodType;
    private Color color;
    private int maximumHeightPercent;
    private boolean canWalk;
    private boolean leaveFood;

    public GroundType(FoodType foodType, Color color, int maximumHeightPercent, boolean canWalk, boolean leaveFood) {
        this.foodType = foodType;
        this.color = color;
        this.maximumHeightPercent = maximumHeightPercent;
        this.canWalk = canWalk;
        this.leaveFood = leaveFood;
    }
    public GroundType(Color color, int maximumHeightPercent, boolean canWalk, boolean leaveFood) {
        this(null, color, maximumHeightPercent, canWalk, leaveFood);
    }
    public GroundType() {
        this(Color.GREEN, 100, true, false);
    }

    public void setFoodType(FoodType foodType) { this.foodType = foodType; }
    public FoodType getFoodType() { return this.foodType; }
    public void setColor(Color color) { this.color = color; }
    public Color getColor() { return this.color; }
    public void setMaximumHeightPercent(int maximumHeightPercent) { this.maximumHeightPercent = maximumHeightPercent; }
    public int getMaximumHeightPercent() { return this.maximumHeightPercent; }
    public void setCanWalk(boolean canWalk) { this.canWalk = canWalk; }
    public boolean canWalk() { return this.canWalk; }
    public void setLeaveFood(boolean leaveFood) { this.leaveFood = leaveFood; }
    public boolean leaveFood() { return this.leaveFood; }
}
