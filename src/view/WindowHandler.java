package view;

import logic.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public abstract class WindowHandler {

    public static String SIMULATION_NAME = "Simulation Natürliche Auslese (von Ferdinand Bujanowski)";
    public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public static ProjectFrame startProject() {

        Dimension projectFrameDimension = new Dimension(1200, 800);
        ProjectFrame projectFrame = new ProjectFrame(projectFrameDimension, new Dimension(300, 300));
        projectFrame.setLocation(getCentralLocation(projectFrameDimension));

        return projectFrame;
    }

    public static Simulation runSimulation(SimulationParams simulationParams, int maximumWidth, JProgressBar progressBar) {

        SimulationCanvas simulationCanvas = new SimulationCanvas(simulationParams, maximumWidth);
        simulationCanvas.repaint();
        Simulation simulation = new Simulation(simulationParams, simulationCanvas, progressBar);

        return simulation;
    }

    public static Point getCentralLocation(Dimension dimension) {
        int x, y;
        x = (screenSize.width - dimension.width) / 2;
        y = (screenSize.height - dimension.height) / 2;

        return new Point(x, y);
    }

    public static JFileChooser getDefaultFileSaveChooser() {
        return new JFileChooser() {

            public void approveSelection() {
                File f = getSelectedFile();
                if (f.exists() && getDialogType() == SAVE_DIALOG) {
                    int result = JOptionPane.showConfirmDialog(this,
                            "Diese Datei existiert bereits, ersetzen?", "Existierende Datei",
                            JOptionPane.YES_NO_CANCEL_OPTION);
                    switch (result) {
                        case JOptionPane.YES_OPTION:
                            super.approveSelection();
                            return;
                        case JOptionPane.CANCEL_OPTION:
                            cancelSelection();
                            return;
                        default:
                            return;
                    }
                }
                super.approveSelection();
            }
        };
    }
}
