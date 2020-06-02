package view;

import logic.*;
import java.awt.*;
import java.util.ArrayList;

import logic.simulation_object.Organism;
import matrix_gui.Canvas;

public class SimulationCanvas extends Canvas {

    private ArrayList<Organism> currentOrganisms;
    private SimulationParams simulationParams;

    public SimulationCanvas(SimulationParams simulationParams, int maximumWidth) {
        super((int)Math.round((double)maximumWidth / (double)simulationParams.getSimulationLength()), simulationParams.getSimulationLength(), simulationParams.getSimulationLength());
        this.simulationParams = simulationParams;
        this.setDrawGrid(false);
    }

    public void repaint(ArrayList<Organism> currentOrganisms) {
        this.currentOrganisms = currentOrganisms;
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        int unitLength = this.getUnitLength();

        if(simulationParams.getDrawGrid() && currentOrganisms != null) {
            for(Organism organism : currentOrganisms) {
                if(organism != null) {
                    g.drawRect(organism.getX() * unitLength, organism.getY() * unitLength, unitLength, unitLength);
                }
            }
        }
    }

}
