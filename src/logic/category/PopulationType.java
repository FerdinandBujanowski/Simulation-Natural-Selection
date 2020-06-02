package logic.category;

import logic.Property;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class PopulationType implements Serializable {

    private int startPopulationSize;
    private int startPopulationEnergy;
    private Color populationColor;
    private boolean multipleChildren;
    private ArrayList<Property> properties;

    public PopulationType(int startPopulationSize, int startPopulationEnergy, Color populationColor, boolean multipleChildren, ArrayList<Property> properties) {
        this.startPopulationSize = startPopulationSize;
        this.startPopulationEnergy = startPopulationEnergy;
        this.populationColor = populationColor;
        this.properties = properties;
        this.multipleChildren = multipleChildren;
    }

    public PopulationType() {
        this(30, 100, Color.WHITE, false, new ArrayList<>());
    }

    public void setStartPopulationSize(int startPopulationSize) { this.startPopulationSize = startPopulationSize; }
    public int getStartPopulationSize() { return this.startPopulationSize; }
    public void setStartPopulationEnergy(int startPopulationEnergy) { this.startPopulationEnergy = startPopulationEnergy; }
    public int getStartPopulationEnergy() { return this.startPopulationEnergy; }
    public void setPopulationColor(Color populationColor) { this.populationColor = populationColor; }
    public Color getPopulationColor() { return this.populationColor; }
    public void setMultipleChildren(boolean multipleChildren) { this.multipleChildren = multipleChildren; }
    public boolean getMultipleChildren() { return this.multipleChildren; }
    public ArrayList<Property> getProperties() { return this.properties; }
}
