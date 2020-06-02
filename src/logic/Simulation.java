package logic;

import logic.category.PopulationType;
import logic.simulation_object.*;
import matrix_gui.FieldNullException;
import opensimplex.OpenSimplexNoise;
import view.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Simulation {

    private SimulationParams simulationParams;
    private SimulationCanvas simulationCanvas;
    private RuntimeStatistics[] runtimeStatistics;
    private OpenSimplexNoise openSimplexNoise;
    private Field[][] fields;
    private ArrayList<Organism> organisms;
    private JProgressBar simulationProgress;

    public Simulation(SimulationParams simulationParams, SimulationCanvas simulationCanvas, JProgressBar simulationProgress) {
        this.simulationParams = simulationParams;
        this.simulationCanvas = simulationCanvas;
        this.openSimplexNoise = new OpenSimplexNoise();
        this.organisms = new ArrayList<>();
        this.fields = new Field[simulationParams.getSimulationLength()][simulationParams.getSimulationLength()];
        for(int x = 0; x < fields.length; x++) {
            for(int y = 0; y < fields[x].length; y++) {
                double zoomX = (double)x / simulationParams.getMapZoom(), zoomY = (double)y / simulationParams.getMapZoom();
                int heightPercentage = (int)Math.round((openSimplexNoise.eval(zoomX, zoomY) + 1) * 50);
                fields[x][y] = new Field(simulationParams.getGroundTypeArray().get(PreviewMapPanel.getGroundTypeIndex(simulationParams, heightPercentage)), x, y);
            }
        }
        this.runtimeStatistics = new RuntimeStatistics[simulationParams.getPopulationTypeArray().size()];
        for(int i = 0; i < runtimeStatistics.length; i++) {
            this.runtimeStatistics[i] = new RuntimeStatistics(simulationParams, i);
        }

        this.simulationProgress = simulationProgress;
    }

    public void initiate() {
        for(int populationNumber = 0; populationNumber < simulationParams.getPopulationTypeArray().size(); populationNumber++) {
            PopulationType populationType = simulationParams.getPopulationTypeArray().get(populationNumber);
            for(int i = 0; i < populationType.getStartPopulationSize(); i++) {
                Random random = new Random();
                int x, y;
                do {
                    x = random.nextInt(simulationParams.getSimulationLength());
                    y = random.nextInt(simulationParams.getSimulationLength());
                } while(!fields[x][y].getGroundType().canWalk());
                organisms.add(new Organism(populationType.getProperties(), populationNumber, populationType.getPopulationColor(), simulationParams, x, y, populationType.getStartPopulationEnergy()));
            }

        }
    }

    public void play() {
        int generations = 0;
        boolean bHasFinished = false;

        while(!bHasFinished) {

            for(int x = 0; x < fields.length; x++) {
                for(int y = 0; y < fields[x].length; y++) {
                    fields[x][y].generateHasFood(fields[x][y].getGroundType().leaveFood());
                }
            }
            while(!isGenerationComplete()) {
                this.tick();
            }

            int[] populationSizes = new int[simulationParams.getPopulationTypeArray().size()];
            for(Organism organism : organisms) {
                populationSizes[organism.getPopulationNumber()]++;

                for(int p = 0; p < organism.getProperties().size(); p++) {
                    double doublePercentage = ((double)organism.getProperties().get(p).getCurrentEnergyGain() / (double)organism.getProperties().get(p).getFoodType().getDefaultEnergy());
                    runtimeStatistics[organism.getPopulationNumber()].addMutationDistributionEntry(p, generations, (int)Math.round(doublePercentage * 100));
                }
            }
            for(int i = 0; i < populationSizes.length; i++) {
                runtimeStatistics[i].addPopulationSizeEntry(generations, populationSizes[i]);
            }

            Organism[][] newOrganisms = new Organism[organisms.size()][];
            for(int i = 0; i < organisms.size(); i++) {
                newOrganisms[i] = organisms.get(i).calculateState();
            }
            int originalSize = organisms.size();
            for(int i = 0; i < originalSize; i++) {

                if(newOrganisms[i] == null) {
                    organisms.remove(i);
                    organisms.add(i, null);
                }
                else if(newOrganisms[i].length == 1) {
                    organisms.remove(i);
                    organisms.add(i, newOrganisms[i][0]);
                }
                else {
                    organisms.remove(i);
                    for(int newOrganismIndex = 0; newOrganismIndex < newOrganisms[i].length; newOrganismIndex++) {
                        organisms.add(newOrganisms[i][newOrganismIndex]);
                    }

                }
            }
            for(int i = 0; i < organisms.size(); i++) {
                if(organisms.get(i) == null) {
                    organisms.remove(i);
                    i--;
                }
            }
            try {
                Thread.sleep(simulationParams.getMsGeneration());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            generations++;
            simulationProgress.setValue(generations);
            bHasFinished = (generations > simulationParams.getGenerations());
        }
    }

    private void tick() {
        for(int x = 0; x < fields.length; x++) {
            for(int y = 0; y < fields[x].length; y++) {

                try {
                    simulationCanvas.getColorFieldAt(x, y).setColor(fields[x][y].getColor());
                } catch (FieldNullException e) {
                    e.printStackTrace();
                }
            }
        }
        for(int i = 0; i < organisms.size(); i++) {
            Organism currentOrganism = organisms.get(i);

            Field nextField;
            Direction nextDirection = Direction.getRandomDirection(organisms.get(i).getLastDirection());
            Point relativeLocation = Direction.getRelativeLocation(nextDirection);
            try {
                nextField = fields[organisms.get(i).getX() + relativeLocation.x][organisms.get(i).getY() + relativeLocation.y];
                if(!nextField.getGroundType().canWalk()) {
                    nextDirection = Direction.getOppositeDirection(nextDirection);
                }
            } catch(IndexOutOfBoundsException e) {
                nextDirection = Direction.getOppositeDirection(nextDirection);
            }
            organisms.get(i).step(nextDirection);

            Field currentField = fields[currentOrganism.getX()][currentOrganism.getY()];
            if(currentField.hasFood()) {
                currentField.setHasFood(false);
                organisms.get(i).collectFood(currentField.getGroundType().getFoodType());
            }

            try {
                simulationCanvas.getColorFieldAt(currentOrganism.getX(), currentOrganism.getY()).setColor(currentOrganism.getColor());
            } catch (FieldNullException e) {
                e.printStackTrace();
            }
        }
        simulationCanvas.repaint(organisms);
        try {
            Thread.sleep(simulationParams.getMsStep());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean isGenerationComplete() {
        int completeCount = 0;
        for(Organism organism : this.organisms) {
            if(!organism.hasEnergy()) completeCount++;
        }
        return(completeCount == this.organisms.size());
    }

    public RuntimeStatistics[] getRuntimeStatistics() { return this.runtimeStatistics; }
    public SimulationCanvas getSimulationCanvas() {
        return simulationCanvas;
    }
}
