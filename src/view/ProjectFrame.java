package view;

import logic.*;
import org.jfree.chart.ChartPanel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;

public class ProjectFrame extends JFrame {

    ParamsPanel paramsPanel;

    private boolean bSimulationStarted;

    private JSplitPane mainPanel, left, simulationSplitPane;
    private JPanel rightTop, rightBottom;
    private PreviewMapPanel previewPanel;
    private JTabbedPane tabbedPaneRight, statisticsPanel;
    private JProgressBar simulationProgress;

    private int simulationWidth;

    public ProjectFrame(Dimension dimension, Dimension previewPanelDimension) {
        super(WindowHandler.SIMULATION_NAME);

        this.bSimulationStarted = false;
        this.paramsPanel = new ParamsPanel(new Dimension(previewPanelDimension.width, dimension.height - previewPanelDimension.height));
        this.previewPanel = paramsPanel.getPreviewMapPanel();
        this.previewPanel.setPreferredSize(previewPanelDimension);
        this.previewPanel.setMinimumSize(previewPanelDimension);
        this.previewPanel.requestRepaint(paramsPanel.getSelectedSimulationParams());
        this.setPreferredSize(dimension);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setResizable(false);


        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("Datei");
        JMenu menuSimulation = new JMenu("Simulation");
        JMenuItem fileOpen = new JMenuItem("Simulationsparameter öffnen");
        JMenuItem fileSave = new JMenuItem("Simulationsparameter speichern");
        JMenuItem simulationStart = new JMenuItem("Simulation starten");
        JMenuItem exit = new JMenuItem("Programm beenden");

        menuFile.add(fileOpen);
        menuFile.add(fileSave);
        menuFile.add(new JSeparator(JSeparator.HORIZONTAL));
        menuFile.add(exit);
        menuSimulation.add(simulationStart);
        menuBar.add(menuFile);
        menuBar.add(menuSimulation);
        menuBar.setBorderPainted(false);
        this.setJMenuBar(menuBar);

        mainPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true) {
            private final int location = previewPanel.getPreferredSize().width;
            { setDividerLocation(location); }
            @Override
            public int getDividerLocation() { return location + 5;}
            @Override
            public int getLastDividerLocation() { return location; }
        };

        this.left = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        simulationWidth = (int)Math.round(0.7 * dimension.height);
        this.simulationSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT) {
            private final int location = simulationWidth;
            { setDividerLocation(location); }
            @Override
            public int getDividerLocation() { return location + 10;}
            @Override
            public int getLastDividerLocation() { return location; }
        };

        this.statisticsPanel = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);

        this.rightTop = new JPanel();
        this.rightTop.setOpaque(true);
        this.rightTop.setBackground(Color.GRAY);

        this.rightBottom = new JPanel();

        this.left.add(previewPanel, JSplitPane.TOP);
        this.left.add(paramsPanel, JSplitPane.BOTTOM);
        this.tabbedPaneRight = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
        this.simulationSplitPane.add(rightTop, JSplitPane.TOP);
        this.simulationSplitPane.add(rightBottom, JSplitPane.BOTTOM);
        this.left.setEnabled(false);

        this.mainPanel.add(left, JSplitPane.LEFT);
        this.tabbedPaneRight.addTab("Simulationsfläche", simulationSplitPane);
        this.tabbedPaneRight.addTab("Statistiken", statisticsPanel);
        this.tabbedPaneRight.setEnabledAt(1, false);
        this.mainPanel.add(tabbedPaneRight, JSplitPane.RIGHT);

        this.mainPanel.setPreferredSize(getSize());
        this.add(mainPanel);
        this.pack();

        simulationProgress = new JProgressBar(JProgressBar.HORIZONTAL);
        simulationProgress.setPreferredSize(new Dimension(rightBottom.getSize().width - 20, 20));
        rightBottom.add(simulationProgress);

        fileSave.addActionListener(e -> {
            JFileChooser fileSaveChooser = WindowHandler.getDefaultFileSaveChooser();
            FileNameExtensionFilter serializedFilter = new FileNameExtensionFilter("Simulationsparameter", "simparams");
            fileSaveChooser.setFileFilter(serializedFilter);
            fileSaveChooser.setCurrentDirectory(new File("params"));
            fileSaveChooser.setSelectedFile(new File("simulations_parameter.simparams"));
            int returnValue = fileSaveChooser.showSaveDialog(this.getParent());
            if(returnValue == JFileChooser.APPROVE_OPTION) {
                try {
                    FileOutputStream fileOut = new FileOutputStream(fileSaveChooser.getSelectedFile().getPath());
                    ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
                    paramsPanel.updateSimulationParams();
                    objectOut.writeObject(paramsPanel.getSelectedSimulationParams());
                    objectOut.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        fileOpen.addActionListener(e -> {
            JFileChooser fileOpenChooser = new JFileChooser();
            FileNameExtensionFilter serializedFilter = new FileNameExtensionFilter("Simulationsparameter", "simparams");
            fileOpenChooser.setFileFilter(serializedFilter);
            fileOpenChooser.setCurrentDirectory(new File("params"));
            int returnValue = fileOpenChooser.showOpenDialog(this.getParent());
            if(returnValue == JFileChooser.APPROVE_OPTION) {
                try {
                    FileInputStream fileIn = new FileInputStream(fileOpenChooser.getSelectedFile().getPath());
                    ObjectInputStream objectIn = new ObjectInputStream(fileIn);
                    paramsPanel.setSelectedSimulationParams((SimulationParams) objectIn.readObject());
                    objectIn.close();
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }

            paramsPanel.setCorrectValues();
            paramsPanel.setupPanelOrganism();
            paramsPanel.setupPanelFood();
            paramsPanel.setupPanelGround();
            paramsPanel.setupListeners();
        });
        exit.addActionListener(e -> {
            System.exit(0);
        });

        simulationStart.addActionListener(e -> {
            simulationStart.setEnabled(false);
            fileOpen.setEnabled(false);

            paramsPanel.updateSimulationParams();
            paramsPanel.toggleTabs(false);
            simulationProgress.setMinimum(0);
            simulationProgress.setMaximum(paramsPanel.getSelectedSimulationParams().getGenerations());
            bSimulationStarted = true;
        });
    }

    public void addSimulationCanvas(SimulationCanvas simulationCanvas) {
        this.rightTop.add(simulationCanvas);
        this.repaint();
        this.pack();
        this.simulationSplitPane.setDividerLocation(simulationCanvas.getHeight() + 10);
        this.simulationSplitPane.setEnabled(false);
    }

    public void enableStatistics(RuntimeStatistics[] runtimeStatistics) {
        for(int i = 0; i < paramsPanel.getSelectedSimulationParams().getPopulationTypeArray().size(); i++) {
            Dimension populationSizeDimension = new Dimension(450, 300);
            JSplitPane statisticSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT) {
                private final int location = populationSizeDimension.height + 10;
                { setDividerLocation(location); }
                @Override
                public int getDividerLocation() { return location + 5;}
                @Override
                public int getLastDividerLocation() { return location; }
            };
            statisticsPanel.addTab("Population " + (i +1), statisticSplitPane);
            JPanel topPanel = new JPanel();
            Color lineColor = paramsPanel.getSelectedSimulationParams().getPopulationTypeArray().get(i).getPopulationColor();
            ChartPanel populationSizePanel = runtimeStatistics[i].createPopulationSizeLineChart(lineColor);
            populationSizePanel.setPreferredSize(populationSizeDimension);
            topPanel.add(populationSizePanel);
            statisticSplitPane.add(topPanel, JSplitPane.TOP);

            JTabbedPane bottomPanel = new JTabbedPane();

            for(int propertyCount = 0; propertyCount < paramsPanel.getSelectedSimulationParams().getPopulationTypeArray().get(i).getProperties().size(); propertyCount++) {
                String tabName = "Verhalten " + (propertyCount + 1) + ": "
                        + paramsPanel.getSelectedSimulationParams().getPopulationTypeArray().get(i).getProperties().get(propertyCount).getFoodType().getName();
                JPanel helpPanel = new JPanel();
                bottomPanel.addTab(tabName, helpPanel);

                ChartPanel mutationDistributionPanel = runtimeStatistics[i].createMutationDistributionScatterChart(propertyCount, lineColor);
                mutationDistributionPanel.setPreferredSize(new Dimension(450, 300));

                helpPanel.add(mutationDistributionPanel);
            }

            statisticSplitPane.add(bottomPanel, JSplitPane.BOTTOM);

        }

        this.tabbedPaneRight.setEnabledAt(1, true);
        this.tabbedPaneRight.setSelectedIndex(1);
    }

    public JProgressBar getSimulationProgress() { return this.simulationProgress; }
    public SimulationParams getSelectedSimulationParams() { return paramsPanel.getSelectedSimulationParams(); }
    public boolean hasSimulationStarted() {return this.bSimulationStarted;}
    public int getSimulationWidth() { return this.simulationWidth; }
}
