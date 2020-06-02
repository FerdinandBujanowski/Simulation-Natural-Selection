package logic.simulation_object;

import logic.Property;
import logic.SimulationParams;
import logic.category.FoodType;

import java.awt.*;
import java.util.ArrayList;

public class Organism extends SimulationObject {

    private SimulationParams simulationParams;

    private ArrayList<Property> properties;
    private int populationNumber;
    private int currentEnergy;
    private ArrayList<FoodType> collectedFood;
    private boolean bEnergy = true;
    Direction lastDirection;

    public Organism(ArrayList<Property> properties, int populationNumber, Color color, SimulationParams simulationParams, int x, int y, int startEnergy) {
        super(x,y, color);
        this.properties = properties;
        this.populationNumber = populationNumber;
        this.simulationParams = simulationParams;
        this.currentEnergy = startEnergy;
        this.collectedFood = new ArrayList<>();
    }

    public void step(Direction direction) {
        if(bEnergy) {
            move(direction);
            currentEnergy--;
            if(currentEnergy <= 0) {
                bEnergy = false;
            }
        }
    }

    public void move(Direction direction) {

        switch(direction) {
            case UP:
                if(this.getY() > 0) this.setY(this.getY() - 1);
                lastDirection = Direction.UP;
                break;
            case DOWN:
                if(this.getY() < simulationParams.getSimulationLength() - 1) this.setY(this.getY() + 1);
                lastDirection = Direction.DOWN;
                break;
            case LEFT:
                if(this.getX() > 0) this.setX(this.getX() - 1);
                lastDirection = Direction.LEFT;
                break;
            case RIGHT:
                if(this.getX() < simulationParams.getSimulationLength() - 1) this.setX(this.getX() + 1);
                lastDirection = Direction.RIGHT;
                break;
        }
    }

    public void collectFood(FoodType foodType) {this.collectedFood.add(foodType);}

    public Organism[] calculateState() {

        int gainedEnergy = calculateGainedEnergy();
        int usableFoodCount = getUsableFoodCount();

        if(usableFoodCount == 0) {
            return null;
        } else if(usableFoodCount == 1) {
            return new Organism[] {
                    new Organism(properties, populationNumber, this.getColor(), simulationParams, this.getX(), this.getY(), gainedEnergy)
            };
        } else {
            if(!simulationParams.getPopulationTypeArray().get(populationNumber).getMultipleChildren()) {
                int newEnergy = gainedEnergy / 2;

                return new Organism[] {
                        new Organism(properties, populationNumber, this.getColor(), simulationParams, this.getX(), this.getY(), newEnergy),
                        new Organism(getMutatedPropertyArray(), populationNumber, this.getColor(), simulationParams, this.getX(), this.getY(), newEnergy)
                };
            } else {
                return getNewOrganismArray(gainedEnergy);
            }
        }
    }

    private int calculateGainedEnergy() {
        int totalEnergy = 0;

        for(FoodType foodType : collectedFood) {
            for(Property property : properties) {
                if(property.fitsFoodType(foodType)) {
                    totalEnergy += property.getCurrentEnergyGain();
                }
            }
        }
        return totalEnergy;
    }

    private int getUsableFoodCount() {
        int usableFoodCount = 0;

        for(FoodType foodType : collectedFood) {
            for(Property property : properties) {
                if(property.fitsFoodType(foodType)) {
                    usableFoodCount++;
                }
            }
        }
        return usableFoodCount;
    }

    private ArrayList<Property> getMutatedPropertyArray() {
        ArrayList<Property> mutatedProperties = new ArrayList<>();
        for(int i = 0; i < properties.size(); i++) {
            mutatedProperties.add(properties.get(i).getMutatedProperty());
        }
        return mutatedProperties;
    }

    private Organism[] getNewOrganismArray(int gainedEnergy) {
        int leftEnergy = gainedEnergy;
        int averageEnergyGain = getAverageEnergyGain();

        int numberOrganisms = (gainedEnergy / getAverageEnergyGain());

        Organism[] newOrganisms = new Organism[numberOrganisms];
        for(int i = 1; i < numberOrganisms; i++) {
            newOrganisms[i] = new Organism(getMutatedPropertyArray(), populationNumber, this.getColor(), simulationParams, this.getX(), this.getY(), averageEnergyGain);
            leftEnergy -= averageEnergyGain;
        }
        if(numberOrganisms > 0) {
            newOrganisms[0] = new Organism(properties, populationNumber, this.getColor(), simulationParams, this.getX(), this.getY(), leftEnergy);
        }

        return newOrganisms;
    }

    private int getAverageEnergyGain() {
        int totalEnergyGain = 0;

        for(Property property : properties) {
            totalEnergyGain += property.getCurrentEnergyGain();
        }

        return (int)((double)totalEnergyGain / properties.size());
    }

    public int getPopulationNumber() { return this.populationNumber; }
    public ArrayList<Property> getProperties() {return this.properties; }
    public Direction getLastDirection() { return lastDirection; }
    public boolean hasEnergy() { return bEnergy; }
}
