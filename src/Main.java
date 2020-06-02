import logic.*;
import view.*;

import javax.swing.*;


public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        Simulation simulation;

        //1. Daten eingeben
        ProjectFrame projectFrame = WindowHandler.startProject();
        projectFrame.setVisible(true);

        boolean hasSimulationFinished = false;
        while(!hasSimulationFinished) {
            if(projectFrame.hasSimulationStarted()) {

                //2. Simulation
                SimulationParams simulationParams = projectFrame.getSelectedSimulationParams();
                simulation = WindowHandler.runSimulation(simulationParams, projectFrame.getSimulationWidth() - 30, projectFrame.getSimulationProgress());
                projectFrame.addSimulationCanvas(simulation.getSimulationCanvas());

                simulation.initiate();
                simulation.play();
                hasSimulationFinished = true;

                //3. Daten ausgeben lassen
                projectFrame.enableStatistics(simulation.getRuntimeStatistics());


            } else {
                try {
                    Thread.sleep(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
