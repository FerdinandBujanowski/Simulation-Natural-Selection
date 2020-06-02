package view;

import logic.SimulationParams;
import matrix_gui.Canvas;
import matrix_gui.FieldNullException;
import opensimplex.OpenSimplexNoise;

import java.awt.*;

public class PreviewMapPanel extends Canvas {

    private OpenSimplexNoise openSimplexNoise;
    private int maximumCanvasLength;
    private double pixelLength;

    public PreviewMapPanel(SimulationParams simulationParams, int maximumCanvasLength) {
        super((int)Math.round(getMaximumPixelLength(simulationParams, maximumCanvasLength)), simulationParams.getSimulationLength(), simulationParams.getSimulationLength());

        this.openSimplexNoise = new OpenSimplexNoise();
        this.maximumCanvasLength = maximumCanvasLength;
        this.setDrawGrid(false);
    }

    public void requestRepaint(SimulationParams simulationParams) {
        this.updateMatrix(simulationParams.getSimulationLength(), simulationParams.getSimulationLength());
        this.setUnitLength((int)Math.round(getMaximumPixelLength(simulationParams, maximumCanvasLength)));

        for(int x = 0; x < simulationParams.getSimulationLength(); x++) {
            for(int y = 0; y < simulationParams.getSimulationLength(); y++) {
                double zoomX = (double)x / simulationParams.getMapZoom(), zoomY = (double)y / simulationParams.getMapZoom();
                int heightPercentage = (int)Math.round((openSimplexNoise.eval(zoomX, zoomY) + 1) * 50);
                try {
                    this.getColorFieldAt(x, y).setColor(simulationParams.getGroundTypeArray().get(getGroundTypeIndex(simulationParams, heightPercentage)).getColor());
                } catch (FieldNullException e) {
                    e.printStackTrace();
                }
            }
        }
        this.pixelLength = getMaximumPixelLength(simulationParams, maximumCanvasLength);
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {

        for(int x = 0; x < this.getFieldWidth(); x++) {
            for(int y = 0; y < this.getFieldHeight(); y++) {
                int currentPixelX = (int)Math.round(pixelLength * x);
                int currentPixelY = (int)Math.round(pixelLength * y);
                int width = (int)Math.round(pixelLength);
                try {
                    g.setColor(this.getColorFieldAt(x, y).getColor());
                } catch (FieldNullException e) {
                    e.printStackTrace();
                }
                g.fillRect(currentPixelX, currentPixelY, width + 1, width + 1);
            }
        }
    }

    private static double getMaximumPixelLength(SimulationParams simulationParams, int maximumCanvasLength) {
        return (double)maximumCanvasLength / (double)simulationParams.getSimulationLength();
    }

    public static int getGroundTypeIndex(SimulationParams simulationParams, int heightPercentage) {
        int smallestDelta = 100;
        int index = 0;
        for(int i = 0; i < simulationParams.getGroundTypeArray().size(); i++) {
            int delta = simulationParams.getGroundTypeArray().get(i).getMaximumHeightPercent() - heightPercentage;
            if(delta >= 0 && delta < smallestDelta) {
                smallestDelta = delta;
                index = i;
            }
        }
        return index;
    }
}
