package logic;

import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class RuntimeStatistics {

    private XYSeries populationSize;
    private XYSeries[] mutationDistribution;

    public RuntimeStatistics(SimulationParams simulationParams, int indexPopulation) {
        this.populationSize = new XYSeries("Populationsgröße (Population " + (indexPopulation + 1) + ")");
        this.mutationDistribution = new XYSeries[simulationParams.getPopulationTypeArray().get(indexPopulation).getProperties().size()];
        for(int i = 0; i < mutationDistribution.length; i++) {
            this.mutationDistribution[i] = new XYSeries("Mutationsverteilung (Verhalten " + (i + 1) + ")");
        }
    }

    public void addPopulationSizeEntry(int x, int y) { this.populationSize.add(x, y); }
    public void addMutationDistributionEntry(int index, int x, int y) {this.mutationDistribution[index].add(x, y);}

    public ChartPanel createPopulationSizeLineChart(Color lineColor) {
        XYSeriesCollection seriesCollection = new XYSeriesCollection(populationSize);

        JFreeChart lineChart = ChartFactory.createXYLineChart(
                "Organismen pro Durchlauf",
                "Durchläufe", "Anzahl Organismen",
                seriesCollection,
                PlotOrientation.VERTICAL,
                false, true, true
        );
        lineChart.setBackgroundPaint(Color.GRAY);
        ChartPanel lineChartPanel = new ChartPanel(lineChart);

        NumberAxis xAxis = new NumberAxis(lineChart.getXYPlot().getDomainAxis().getLabel());
        xAxis.setLabelFont(lineChart.getXYPlot().getRangeAxis().getLabelFont());
        xAxis.setTickUnit(new NumberTickUnit(10));

        lineChart.getXYPlot().setDomainAxis(xAxis);
        lineChart.getXYPlot().getRenderer().setSeriesPaint(0, lineColor);

        return lineChartPanel;
    }

    public ChartPanel createMutationDistributionScatterChart(int propertyIndex, Color plotColor) {
        XYSeriesCollection seriesCollection = new XYSeriesCollection(mutationDistribution[propertyIndex]);

        JFreeChart scatterChart = ChartFactory.createScatterPlot(
                "Mutationsverteilung pro Durchlauf",
                "Durchläufe", "relativer Energiewert (%)",
                seriesCollection,
                PlotOrientation.VERTICAL,
                false, true, false
        );
        scatterChart.setBackgroundPaint(Color.GRAY);
        ChartPanel scatterChartPanel = new ChartPanel(scatterChart);

        XYItemRenderer renderer = scatterChart.getXYPlot().getRenderer();
        Shape shape1 = new Rectangle2D.Double(2, 2, 2, 2);
        renderer.setSeriesPaint(0, plotColor);
        renderer.setSeriesShape(0, shape1);

        NumberAxis xAxis = new NumberAxis(scatterChart.getXYPlot().getDomainAxis().getLabel());
        xAxis.setLabelFont(scatterChart.getXYPlot().getRangeAxis().getLabelFont());
        xAxis.setTickUnit(new NumberTickUnit(10));

        scatterChart.getXYPlot().setDomainAxis(xAxis);

        return scatterChartPanel;
    }
}
