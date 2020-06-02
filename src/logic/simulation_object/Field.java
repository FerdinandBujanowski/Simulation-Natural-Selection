package logic.simulation_object;

import logic.category.GroundType;

import java.awt.*;
import java.util.Random;

public class Field extends SimulationObject {

    private GroundType groundType;
    private boolean hasFood;

    public Field(GroundType groundType, int x, int y) {
        super(x, y, groundType.getColor());
        this.groundType = groundType;
    }

    public void generateHasFood(boolean leaveFood) {
        if(groundType.getFoodType() == null) {
            this.hasFood = false;
            return;
        }
        if(!this.hasFood || !leaveFood) {
            Random random = new Random();
            this.hasFood = random.nextDouble() < groundType.getFoodType().getFoodProbability();
        }
    }

    @Override
    public Color getColor() {
        return(hasFood ? groundType.getFoodType().getColor() : groundType.getColor());
    }

    public void setHasFood(boolean hasFood) {
        this.hasFood = hasFood;
    }
    public boolean hasFood() {
        return hasFood;
    }

    public GroundType getGroundType() { return this.groundType; }
}
