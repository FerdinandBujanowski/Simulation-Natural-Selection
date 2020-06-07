import logic.*;
import view.*;

import javax.swing.*;


public class Main {
    public static void main(String[] args) {
        /**
         * Das Betriebssystementsprechende Design wird ausgewählt, sofern eines gefunden werden kann
         */
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        /**
        ' siehe Klasse 'Simulation'
         */
        Simulation simulation;

        /**
         * In Facharbeit genannte 3 Phasen des Programmes werden nacheinander ausgeführt:
         */

        /**
         * 1. DATEN EINGEBEN: Fenster wird sichtbar gemacht (siehe abstrakte "Helper"-Klasse 'WindowHandler')
         */
        ProjectFrame projectFrame = WindowHandler.startProject();
        projectFrame.setVisible(true);

        /**
         *
         */
        boolean hasSimulationFinished = false;
        while(!hasSimulationFinished) {
            if(projectFrame.hasSimulationStarted()) {

                /**
                 * 2. SIMULATION: Instanz der Simulation wird mithilfe der Klasse 'WindowHandler' und den ausgewählten Simulationsparametern erzeugt,
                 * dazu wird die Simulationsfläche in das Fenster eingebunden
                 */
                SimulationParams simulationParams = projectFrame.getSelectedSimulationParams();
                simulation = WindowHandler.runSimulation(simulationParams, projectFrame.getSimulationWidth() - 30, projectFrame.getSimulationProgress());
                projectFrame.addSimulationCanvas(simulation.getSimulationCanvas());

                /**
                 * Die Simulation wird gestartet
                 */
                simulation.initiate();
                simulation.play();
                /**
                 * Dieser Punkt wird erst erreicht, sobald der Simulationsprozess beendet ist
                 */
                hasSimulationFinished = true;

                /**
                 * 3. DATEN AUSGEBEN LASSEN: alle verfügbaren Statistiken werden gebündelt in einer Klasse (siehe Klasse 'RuntimeStatistics')
                 * graphisch dargestellt
                 */
                projectFrame.enableStatistics(simulation.getRuntimeStatistics());


            } else {
                /**
                 * Solange Simulation noch nicht gestartet wurde 'wartet' das Programm (mit einer Zeitverzögerung von 0ms)
                 */
                try {
                    Thread.sleep(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
