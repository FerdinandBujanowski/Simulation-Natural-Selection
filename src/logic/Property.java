package logic;

import logic.category.FoodType;

import java.io.Serializable;
import java.util.Random;

public class Property implements Serializable {

    private FoodType foodType;
    private int currentEnergyGain;
    private double relativeMutatedValuePercentage;
    private double mutationProbability;
    private double relativeMutationStrengthPercentage;

    public Property(FoodType foodType, double relativeMutatedValuePercentage, double mutationProbability, double relativeMutationStrengthPercentage) {
        this.foodType = foodType;
        this.currentEnergyGain = (int)Math.round(foodType.getDefaultEnergy() * (1 + relativeMutatedValuePercentage));
        this.relativeMutatedValuePercentage = relativeMutatedValuePercentage;
        this.mutationProbability = mutationProbability;
        this.relativeMutationStrengthPercentage = relativeMutationStrengthPercentage;
    }

    public Property() {this(new FoodType(), 0, 0, 0);}

    public boolean fitsFoodType(FoodType foodType) {
        return(this.foodType == foodType);
    }

    public int getCurrentEnergyGain() { return this.currentEnergyGain; }

    public Property getMutatedProperty() {
        Random mutationRandom = new Random();
        if(mutationRandom.nextDouble() < mutationProbability) {
            if(mutationRandom.nextBoolean()) {
                currentEnergyGain = (int)Math.round(currentEnergyGain * (1 + relativeMutationStrengthPercentage));
            } else {
                currentEnergyGain = (int)Math.round(currentEnergyGain * (1 - relativeMutationStrengthPercentage));
            }
        }
        return new Property(foodType, ((double)currentEnergyGain / (double)foodType.getDefaultEnergy()) - 1, mutationProbability, relativeMutationStrengthPercentage);
    }

    public String toString() {
        return(foodType.getName() + ": " + relativeMutatedValuePercentage + ", ...");
    }

    public void setFoodType(FoodType foodType) { this.foodType = foodType; }
    public FoodType getFoodType() { return this.foodType; }
    public void setRelativeMutatedValuePercentage(double relativeMutationStrengthPercentage) {
        this.relativeMutatedValuePercentage = relativeMutationStrengthPercentage;
        this.currentEnergyGain = (int)Math.round(foodType.getDefaultEnergy() * (1 + relativeMutatedValuePercentage));
    }
    public double getRelativeMutatedValuePercentage() { return this.relativeMutatedValuePercentage; }
    public void setMutationProbability(double mutationProbability) { this.mutationProbability = mutationProbability; }
    public double getMutationProbability() { return this.mutationProbability; }
    public void setRelativeMutationStrengthPercentage(double relativeMutationStrengthPercentage) {
        this.relativeMutationStrengthPercentage = relativeMutationStrengthPercentage;
    }
    public double getRelativeMutationStrengthPercentage() { return this.relativeMutationStrengthPercentage; }
}
