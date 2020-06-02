package logic;

import logic.category.*;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class SimulationParams implements Serializable {

    /**
     * Terrain-Parameter
     */
    private int simulationLength = 50;
    private boolean drawGrid = false;

    private int mapZoom = 5;

    /**
     * Baupläne für Ausprägungen von Nahrungs-, Boden-, und Populationstypen
     */
    private ArrayList<FoodType> foodTypeArray = new ArrayList<>();
    private ArrayList<GroundType> groundTypeArray = new ArrayList<GroundType>() {
        {
            add(new GroundType(Color.GREEN, 100, true, false));
        }
    };

    private ArrayList<PopulationType> populationTypeArray = new ArrayList<>();

    /**
     * Zeit-Parameter
     */
    private int msStep = 10;
    private int msGeneration = 500;
    private int generations = 10;

    public String[] getFoodTypeNameArray() {
        String[] foodTypeNameArray = new String[getFoodTypeArray().size()];
        for(int i = 0; i < foodTypeNameArray.length; i++) {
            foodTypeNameArray[i] = getFoodTypeArray().get(i).getName();
        }

        return foodTypeNameArray;
    }
    public int getFoodTypeIndex(FoodType foodType) {
        for(int i = 0; i < getFoodTypeArray().size(); i++) {
            if(getFoodTypeArray().get(i) == foodType) {
                return i;
            }
        }
        return 0;
    }
    public void setSimulationLength(int simulationLength) { this.simulationLength = simulationLength; }
    public int getSimulationLength() { return simulationLength; }
    public void setDrawGrid(boolean drawGrid) { this.drawGrid = drawGrid; }
    public boolean getDrawGrid() { return drawGrid; }

    public void setMapZoom(int mapZoom) { this.mapZoom = mapZoom; }
    public int getMapZoom() { return this.mapZoom; }

    public void addFoodType(FoodType foodType) { this.foodTypeArray.add(foodType); }
    public ArrayList<FoodType> getFoodTypeArray() { return foodTypeArray; }
    public void addGroundType(GroundType groundType) { this.groundTypeArray.add(groundType); }
    public ArrayList<GroundType> getGroundTypeArray() { return groundTypeArray; }
    public void addPopulationType(PopulationType populationType) { this.populationTypeArray.add(populationType); }
    public ArrayList<PopulationType> getPopulationTypeArray() { return this.populationTypeArray; }

    public void setMsStep(int msStep) { this.msStep = msStep; }
    public int getMsStep() { return msStep; }
    public void setMsGeneration(int msGeneration) { this.msGeneration = msGeneration; }
    public int getMsGeneration() { return msGeneration; }
    public void setGenerations(int generations) { this.generations = generations; }
    public int getGenerations() { return generations; }
}
