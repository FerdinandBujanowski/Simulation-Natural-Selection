package view;

import color_representer.ColorRepresenter;
import color_representer.ColorVariable;
import logic.*;
import logic.category.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class ParamsPanel extends JTabbedPane {

    private JScrollPane tabSimulation, tabOrganism, tabFood, tabGround;

    private JPanel panelSimulation, panelOrganism, panelFood, panelGround;
    private Dimension dimension;
    private SimulationParams selectedSimulationParams;
    private PreviewMapPanel previewMapPanel;

    /**
     * Landschaft
     */
    private JSpinner simulationLengthSpinner;
    private JCheckBox checkDrawGrid;
    private JSpinner mapZoomSpinner;

    private JSpinner stepMillisecondsSpinner;
    private JSpinner generationMillisecondsSpinner;
    private JSpinner generationsSpinner;

    /**
     * Populationen
     */
    private JButton createPopulationButton;
    private ArrayList<PopulationTypeParamUnit> populationTypeParamUnits;

    /**
     * Nahrung
     */
    private JButton createFoodButton;
    private ArrayList<FoodTypeParamUnit> foodTypeParamUnits;

    /**
     * Boden
     */
    private JButton createGroundButton;
    private ArrayList<GroundTypeParamUnit> groundTypeParamUnits;


    public ParamsPanel(Dimension dimension) {
        super(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);

        this.dimension = dimension;
        this.panelSimulation = new JPanel();
        this.panelOrganism = new JPanel();
        this.panelFood = new JPanel();
        this.panelGround = new JPanel();


        this.tabSimulation = new JScrollPane(panelSimulation, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        this.tabOrganism = new JScrollPane(panelOrganism, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        this.tabFood = new JScrollPane(panelFood, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        this.tabGround = new JScrollPane(panelGround, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        this.addTab("Simulation", tabSimulation);
        this.addTab("Populationen", tabOrganism);
        this.addTab("Nahrung", tabFood);
        this.addTab("Boden", tabGround);

        this.panelSimulation.setLayout(new BoxLayout(panelSimulation, BoxLayout.Y_AXIS));
        this.panelSimulation.setMaximumSize(dimension);
        this.panelOrganism.setLayout(new BoxLayout(panelOrganism, BoxLayout.Y_AXIS));
        this.panelOrganism.setMaximumSize(dimension);
        this.panelFood.setLayout(new BoxLayout(panelFood, BoxLayout.Y_AXIS));
        this.panelFood.setMaximumSize(dimension);
        this.panelGround.setLayout(new BoxLayout(panelGround, BoxLayout.Y_AXIS));
        this.panelGround.setMaximumSize(dimension);

        this.setPreferredSize(dimension);
        this.selectedSimulationParams = new SimulationParams();
        this.previewMapPanel = new PreviewMapPanel(selectedSimulationParams, this.dimension.width);

        this.addChangeListener(e -> {
            updateSimulationParams();
            setCorrectValues();
        });

        /**
         * PARAMETER LANDSCHAFT
         */

        JPanel borderPanel = new JPanel();
        borderPanel.setPreferredSize(new Dimension(this.dimension.width, 10));
        this.panelSimulation.add(borderPanel);

        SpinnerNumberModel simulationLengthModel = new SpinnerNumberModel(5, 5, 1000, 1);
        simulationLengthSpinner = new JSpinner(simulationLengthModel);
        this.panelSimulation.add(new PropertyRow(new Dimension(this.dimension.width, 40), new JLabel("Länge Szenario:"), simulationLengthSpinner));

        SpinnerNumberModel mapZoomModel = new SpinnerNumberModel(5, 1, 15, 1);
        mapZoomSpinner = new JSpinner(mapZoomModel);
        this.panelSimulation.add(new PropertyRow(new Dimension(this.dimension.width, 40), new JLabel("Zoom generiertes Terrain:"), mapZoomSpinner));

        JSeparator panelMapSeperator_1 = new JSeparator(JSeparator.HORIZONTAL);
        this.panelSimulation.add(panelMapSeperator_1);

        checkDrawGrid = new JCheckBox();
        this.panelSimulation.add(new PropertyRow(new Dimension(this.dimension.width, 40), new JLabel("Organismen umranden:"), checkDrawGrid));

        JSeparator panelMapSeperator_2 = new JSeparator(JSeparator.HORIZONTAL);
        this.panelSimulation.add(panelMapSeperator_2);

        SpinnerNumberModel stepMillisecondsModel = new SpinnerNumberModel(1, 1, 1000, 1);
        stepMillisecondsSpinner = new JSpinner(stepMillisecondsModel);
        this.panelSimulation.add(new PropertyRow(new Dimension(this.dimension.width, 40), new JLabel("Dauer Schritt (ms):"), stepMillisecondsSpinner));

        SpinnerNumberModel generationMillisecondsModel = new SpinnerNumberModel(1, 1, 1000, 1);
        generationMillisecondsSpinner = new JSpinner(generationMillisecondsModel);
        this.panelSimulation.add(new PropertyRow(new Dimension(this.dimension.width, 40), new JLabel("Generationenwechsel (ms):"), generationMillisecondsSpinner));

        SpinnerNumberModel generationsModel = new SpinnerNumberModel(10, 10, 1000, 1);
        generationsSpinner = new JSpinner(generationsModel);
        this.panelSimulation.add(new PropertyRow(new Dimension(this.dimension.width, 40), new JLabel("Anzahl Durchläufe:"), generationsSpinner));

        JPanel spacingPanel = new JPanel();
        spacingPanel.setPreferredSize(new Dimension(this.dimension.width, 80));
        this.panelSimulation.add(spacingPanel);


        setupPanelOrganism();
        setupPanelFood();
        setupPanelGround();

        setCorrectValues();

        setupListeners();
    }

    public void updateSimulationParams() {
        selectedSimulationParams.setSimulationLength((int)simulationLengthSpinner.getValue());
        selectedSimulationParams.setDrawGrid(checkDrawGrid.isSelected());
        selectedSimulationParams.setMapZoom((int)mapZoomSpinner.getValue());
        //selectedSimulationParams.setMapSeed(Long.valueOf((String)mapSeedSpinner.getValue()));

        selectedSimulationParams.setMsStep((int)stepMillisecondsSpinner.getValue());
        selectedSimulationParams.setMsGeneration((int)generationMillisecondsSpinner.getValue());
        selectedSimulationParams.setGenerations((int)generationsSpinner.getValue());

        for(PopulationTypeParamUnit populationTypeParamUnits : populationTypeParamUnits) {
            populationTypeParamUnits.updateSimulationParams();
        }

        for(FoodTypeParamUnit foodTypeParamUnit : foodTypeParamUnits) {
            foodTypeParamUnit.updateSimulationParams();
        }

        for(GroundTypeParamUnit groundTypeParamUnit : groundTypeParamUnits) {
            groundTypeParamUnit.updateSimulationParams();
        }

    }

    public void setCorrectValues() {
        previewMapPanel.requestRepaint(selectedSimulationParams);

        simulationLengthSpinner.setValue(selectedSimulationParams.getSimulationLength());
        checkDrawGrid.setSelected(selectedSimulationParams.getDrawGrid());
        mapZoomSpinner.setValue(selectedSimulationParams.getMapZoom());

        stepMillisecondsSpinner.setValue(selectedSimulationParams.getMsStep());
        generationMillisecondsSpinner.setValue(selectedSimulationParams.getMsGeneration());
        generationsSpinner.setValue(selectedSimulationParams.getGenerations());

        for(PopulationTypeParamUnit populationTypeParamUnit : populationTypeParamUnits) {
            populationTypeParamUnit.setCorrectValues();
        }

        for(FoodTypeParamUnit foodTypeParamUnit : foodTypeParamUnits) {
            foodTypeParamUnit.setCorrectValues();
        }

        for(GroundTypeParamUnit groundTypeParamUnit : groundTypeParamUnits) {
            groundTypeParamUnit.setCorrectValues();
        }
    }

    public void toggleTabs(boolean enabled) {
        this.setSelectedIndex(0);

        this.setEnabledAt(0, enabled);
        this.setEnabledAt(1, enabled);
        this.setEnabledAt(2, enabled);
        this.setEnabledAt(3, enabled);

        simulationLengthSpinner.setEnabled(enabled);
        checkDrawGrid.setEnabled(enabled);
        mapZoomSpinner.setEnabled(enabled);

        stepMillisecondsSpinner.setEnabled(enabled);
        generationMillisecondsSpinner.setEnabled(enabled);
        generationsSpinner.setEnabled(enabled);
    }

    public void setupPanelOrganism() {

        this.panelOrganism.removeAll();
        this.populationTypeParamUnits = new ArrayList<>();

        createPopulationButton = new JButton("Neue Population erstellen");
        JPanel helpPanel = new JPanel();
        helpPanel.setMaximumSize(new Dimension(this.dimension.width, 40));
        helpPanel.add(createPopulationButton);
        this.panelOrganism.add(helpPanel);
        this.panelOrganism.add(new JSeparator(JSeparator.HORIZONTAL));

        int i = 0;
        for(PopulationType populationType : selectedSimulationParams.getPopulationTypeArray())  {
            this.createPopulationTypeParamUnit(i);
            i++;
        }

        createPopulationButton.addActionListener( e -> {
            int index = selectedSimulationParams.getPopulationTypeArray().size();
            selectedSimulationParams.addPopulationType(new PopulationType());

            this.createPopulationTypeParamUnit(index);
        });
    }

    public void createPopulationTypeParamUnit(int index) {
        PopulationTypeParamUnit populationTypeParamUnit = new PopulationTypeParamUnit(this.dimension.width, selectedSimulationParams, index, this);
        populationTypeParamUnits.add(populationTypeParamUnit);
        this.panelOrganism.add(populationTypeParamUnit);
        populationTypeParamUnit.setCorrectValues();
        populationTypeParamUnit.setupPropertyRows();
        this.panelOrganism.add(new JSeparator(JSeparator.HORIZONTAL));
        this.repaint();
    }

    public void setupPanelFood() {
        this.panelFood.removeAll();
        this.foodTypeParamUnits = new ArrayList<>();

        createFoodButton = new JButton("Nahrungstyp erstellen");
        JPanel helpPanel = new JPanel();
        helpPanel.setMaximumSize(new Dimension(this.dimension.width, 40));
        helpPanel.add(createFoodButton);
        this.panelFood.add(helpPanel);
        this.panelFood.add(new JSeparator(JSeparator.HORIZONTAL));

        int i = 0;
        for(FoodType foodType : selectedSimulationParams.getFoodTypeArray()) {
            this.createFoodTypeParamUnit(i);
            i++;
        }

        createFoodButton.addActionListener(e -> {
            int index = selectedSimulationParams.getFoodTypeArray().size();
            selectedSimulationParams.addFoodType(new FoodType());

            this.createFoodTypeParamUnit(index);
        });

    }

    public void createFoodTypeParamUnit(int index) {
        FoodTypeParamUnit foodTypeParamUnit = new FoodTypeParamUnit(selectedSimulationParams, index, this.dimension.width);
        foodTypeParamUnits.add(foodTypeParamUnit);
        this.panelFood.add(foodTypeParamUnit);
        foodTypeParamUnit.setCorrectValues();
        this.panelFood.add(new JSeparator(JSeparator.HORIZONTAL));
        this.repaint();
    }

    public void setupPanelGround() {
        this.panelGround.removeAll();
        this.groundTypeParamUnits = new ArrayList<>();

        createGroundButton = new JButton("Bodentyp erstellen");
        JPanel helpPanel = new JPanel();
        helpPanel.setMaximumSize(new Dimension(this.dimension.width, 40));
        helpPanel.add(createGroundButton);
        this.panelGround.add(helpPanel);
        this.panelGround.add(new JSeparator(JSeparator.HORIZONTAL));

        int i = 0;
        for(GroundType groundType : selectedSimulationParams.getGroundTypeArray()) {
            this.createGroundTypeParamUnit(i);
            i++;
        }

        createGroundButton.addActionListener(e -> {
            int index = selectedSimulationParams.getGroundTypeArray().size();
            selectedSimulationParams.addGroundType(new GroundType());

            this.createGroundTypeParamUnit(index);
            groundTypeParamUnits.get(index).setupListeners(previewMapPanel);
        });
    }

    public void createGroundTypeParamUnit(int index) {
        GroundTypeParamUnit groundTypeParamUnit = new GroundTypeParamUnit(selectedSimulationParams, index, this.dimension.width);
        groundTypeParamUnits.add(groundTypeParamUnit);
        this.panelGround.add(groundTypeParamUnit);
        groundTypeParamUnit.setCorrectValues();
        this.panelGround.add(new JSeparator(JSeparator.HORIZONTAL));
        this.repaint();
    }

    public void setupListeners() {
        simulationLengthSpinner.addChangeListener(e -> {
            selectedSimulationParams.setSimulationLength((int)simulationLengthSpinner.getValue());
            previewMapPanel.requestRepaint(selectedSimulationParams);
        });
        mapZoomSpinner.addChangeListener(e -> {
            selectedSimulationParams.setMapZoom((int)mapZoomSpinner.getValue());
            previewMapPanel.requestRepaint(selectedSimulationParams);
        });

        for(GroundTypeParamUnit groundTypeParamUnit : groundTypeParamUnits) {
            groundTypeParamUnit.setupListeners(previewMapPanel);
        }
    }

    public void setSelectedSimulationParams(SimulationParams selectedSimulationParams) { this.selectedSimulationParams = selectedSimulationParams; }
    public SimulationParams getSelectedSimulationParams() { return selectedSimulationParams; }

    public PreviewMapPanel getPreviewMapPanel() { return this.previewMapPanel; }
}

class PropertyRow extends JPanel {

    private JPanel panelLeft, panelRight;
    private Component componentRight, componentLeft;

    public PropertyRow(Dimension dimension, Component componentLeft, Component componentRight) {
        this.setLayout(new GridLayout(1, 2));
        this.setPreferredSize(dimension);
        this.setMaximumSize(dimension);
        this.componentLeft = componentLeft;
        this.componentRight = componentRight;

        Dimension panelDimension = new Dimension(dimension.width / 2, dimension.height);

        this.panelLeft = new JPanel();
        this.panelLeft.setPreferredSize(panelDimension);
        this.panelLeft.setMaximumSize(panelDimension);
        this.panelLeft.add(componentLeft, BorderLayout.CENTER);

        this.panelRight = new JPanel();
        this.panelRight.setPreferredSize(panelDimension);
        this.panelRight.setMaximumSize(panelDimension);
        this.panelRight.add(componentRight);

        this.add(panelLeft);
        this.add(panelRight);
    }
}

class PopulationTypeParamUnit extends JPanel {

    private SimulationParams simulationParams;
    private int populationTypeIndex;
    private int width;

    private JSpinner populationSizeSpinner;
    private JSpinner populationEnergySpinner;
    private JCheckBox multipleChildrenCheckBox;
    private JButton createPropertyButton;
    private ArrayList<PropertyRow> propertyRows;

    private ColorRepresenter representerPopulationColor;
    private ColorVariable populationColor;

    public PopulationTypeParamUnit(int width, SimulationParams simulationParams, int populationTypeIndex, Container parent) {
        this.width = width;
        this.simulationParams = simulationParams;
        this.populationTypeIndex = populationTypeIndex;
        this.propertyRows = new ArrayList<>();

        JLabel nameLabel = new JLabel("Population " + (populationTypeIndex + 1));
        JPanel namePanel = new JPanel();
        namePanel.add(nameLabel);
        this.add(namePanel);

        SpinnerNumberModel populationSizeModel = new SpinnerNumberModel(1, 1, 1000, 1);
        populationSizeSpinner = new JSpinner(populationSizeModel);
        this.add(new PropertyRow(new Dimension(this.width, 40), new JLabel("Populationsgröße:"), populationSizeSpinner));

        SpinnerNumberModel populationEnergyModel = new SpinnerNumberModel(0, 0, 1000, 1);
        populationEnergySpinner = new JSpinner(populationEnergyModel);
        this.add(new PropertyRow(new Dimension(this.width, 40), new JLabel("Startenergie:"), populationEnergySpinner));

        populationColor = new ColorVariable(simulationParams.getPopulationTypeArray().get(populationTypeIndex).getPopulationColor());
        representerPopulationColor = new ColorRepresenter(populationColor);
        representerPopulationColor.setPreferredSize(new Dimension(25, 25));
        this.add(new PropertyRow(new Dimension(this.width, 40), new JLabel("Farbe Organismus:"), representerPopulationColor));

        multipleChildrenCheckBox = new JCheckBox();
        this.add(new PropertyRow(new Dimension(this.width, 40), new JLabel("Mehr als ein Kind nach Teilung:"), multipleChildrenCheckBox));

        createPropertyButton = new JButton("Nahrungsverhalten hinzufügen");
        JPanel helpPanel = new JPanel();
        helpPanel.setMaximumSize(new Dimension(this.width, 40));
        helpPanel.add(createPropertyButton);
        this.add(helpPanel);
        createPropertyButton.addActionListener(e -> {
            int propertyIndex = simulationParams.getPopulationTypeArray().get(populationTypeIndex).getProperties().size();
            simulationParams.getPopulationTypeArray().get(populationTypeIndex).getProperties().add(new Property());
            PropertyDialog propertyDialog = new PropertyDialog(simulationParams, populationTypeIndex, propertyIndex, new Dimension(350, 200));
            propertyDialog.setVisible(true);
            if(!propertyDialog.bAbort) {
                createPropertyRow(propertyIndex);
                parent.repaint();
            }
        });

        this.setPreferredSize(new Dimension(this.width, (6 + propertyRows.size()) * 40));
    }

    public void setCorrectValues() {
        populationSizeSpinner.setValue(simulationParams.getPopulationTypeArray().get(populationTypeIndex).getStartPopulationSize());
        populationEnergySpinner.setValue(simulationParams.getPopulationTypeArray().get(populationTypeIndex).getStartPopulationEnergy());
        multipleChildrenCheckBox.setSelected(simulationParams.getPopulationTypeArray().get(populationTypeIndex).getMultipleChildren());

    }

    public void updateSimulationParams() {
        simulationParams.getPopulationTypeArray().get(populationTypeIndex).setStartPopulationSize((int)populationSizeSpinner.getValue());
        simulationParams.getPopulationTypeArray().get(populationTypeIndex).setStartPopulationEnergy((int)populationEnergySpinner.getValue());
        simulationParams.getPopulationTypeArray().get(populationTypeIndex).setPopulationColor(populationColor.getValue());
        simulationParams.getPopulationTypeArray().get(populationTypeIndex).setMultipleChildren(multipleChildrenCheckBox.isSelected());
    }

    public void setupPropertyRows() {
        this.propertyRows = new ArrayList<>();

        for(int index = 0; index < simulationParams.getPopulationTypeArray().get(populationTypeIndex).getProperties().size(); index++) {
            createPropertyRow(index);
        }
    }

    private void createPropertyRow(int index) {
        Property property = simulationParams.getPopulationTypeArray().get(populationTypeIndex).getProperties().get(index);
        JLabel propertyString = new JLabel(property.toString());
        JButton editProperty = new JButton("bearbeiten");
        PropertyRow propertyRow = new PropertyRow(new Dimension(this.width, 40), propertyString, editProperty);
        editProperty.addActionListener(e -> {
            PropertyDialog propertyDialog = new PropertyDialog(simulationParams, populationTypeIndex, index, new Dimension(350, 200));
            propertyDialog.setCorrectValues();
            propertyDialog.setVisible(true);
            //propertyDialog.updateProperty();
            if(!propertyDialog.bAbort) {
                propertyString.setText(property.toString());
            } else {
                simulationParams.getPopulationTypeArray().get(populationTypeIndex).getProperties().remove(index);
                simulationParams.getPopulationTypeArray().get(populationTypeIndex).getProperties().add(index, property);
            }
        });
        propertyRows.add(propertyRow);
        this.add(propertyRow);
        this.setPreferredSize(new Dimension(this.width, this.getPreferredSize().height + 45));
    }
}

class PropertyDialog extends JDialog {

    private SimulationParams simulationParams;
    private int propertyIndex, populationTypeIndex;
    private Dimension dimension;

    private JPanel contentPanel;
    private JButton saveButton, deleteButton;
    public boolean bAbort;

    private JComboBox foodTypeComboBox;
    private JSpinner relativeValueSpinner;
    private JSpinner mutationProbabilitySpinner;
    private JSpinner mutationStrengthSpinner;

    public PropertyDialog(SimulationParams simulationParams, int populationTypeIndex, int propertyIndex, Dimension dimension) {
        this.simulationParams = simulationParams;
        this.populationTypeIndex = populationTypeIndex;
        this.propertyIndex = propertyIndex;
        this.dimension = dimension;

        this.setLocation(WindowHandler.getCentralLocation(dimension));
        this.setResizable(false);
        this.setTitle("Verhalten bearbeiten");
        this.setAlwaysOnTop(true);

        this.contentPanel = new JPanel();
        this.contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        this.contentPanel.setPreferredSize(dimension);

        this.bAbort = false;

        foodTypeComboBox = new JComboBox(simulationParams.getFoodTypeNameArray());
        this.contentPanel.add(new PropertyRow(new Dimension(this.dimension.width, 40), new JLabel("Nahrungstyp:"), foodTypeComboBox));

        SpinnerNumberModel relativeValueModel = new SpinnerNumberModel(10, -100, 100, 1);
        relativeValueSpinner = new JSpinner(relativeValueModel);
        this.contentPanel.add(new PropertyRow(new Dimension(this.dimension.width, 40), new JLabel("Relativer mutierter Wert (%):"), relativeValueSpinner));

        SpinnerNumberModel mutationProbabilityModel = new SpinnerNumberModel(10, 0, 100, 1);
        mutationProbabilitySpinner = new JSpinner(mutationProbabilityModel);
        this.contentPanel.add(new PropertyRow(new Dimension(this.dimension.width, 40), new JLabel("Mutationswahrscheinlichkeit (%):"), mutationProbabilitySpinner));

        SpinnerNumberModel mutationStrengthModel = new SpinnerNumberModel(10, 0, 100, 1);
        mutationStrengthSpinner = new JSpinner(mutationStrengthModel);
        this.contentPanel.add(new PropertyRow(new Dimension(this.dimension.width, 40), new JLabel("Mutationsstärke (%):"), mutationStrengthSpinner));

        this.contentPanel.add(new JSeparator(JSeparator.HORIZONTAL));

        this.saveButton = new JButton("Speichern");
        this.saveButton.addActionListener(e -> {
            this.setVisible(false);
            updateProperty();
        });
        this.deleteButton = new JButton("Abbrechen");
        this.deleteButton.addActionListener(e -> {
            this.setVisible(false);
            bAbort = true;
        });
        this.contentPanel.add(new PropertyRow(new Dimension(this.dimension.width, 40), saveButton, deleteButton));
        this.add(contentPanel);
        this.setSize(dimension);
        this.setModal(true);

    }

    public void setCorrectValues() {
        Property property = simulationParams.getPopulationTypeArray().get(populationTypeIndex).getProperties().get(propertyIndex);

        foodTypeComboBox.setSelectedIndex(simulationParams.getFoodTypeIndex(property.getFoodType()));
        relativeValueSpinner.setValue(property.getRelativeMutatedValuePercentage() * 100);
        mutationProbabilitySpinner.setValue(property.getMutationProbability()  * 100);
        mutationStrengthSpinner.setValue(property.getRelativeMutationStrengthPercentage() * 100);
    }

    public void updateProperty() {
        simulationParams.getPopulationTypeArray().get(populationTypeIndex).getProperties().get(propertyIndex).setFoodType(simulationParams.getFoodTypeArray().get(foodTypeComboBox.getSelectedIndex()));
        double relativeMutatedValuePercentage = new Double(relativeValueSpinner.getValue().toString());
        simulationParams.getPopulationTypeArray().get(populationTypeIndex).getProperties().get(propertyIndex).setRelativeMutatedValuePercentage(relativeMutatedValuePercentage / 100.d);
        double mutationProbability = new Double(mutationProbabilitySpinner.getValue().toString());
        simulationParams.getPopulationTypeArray().get(populationTypeIndex).getProperties().get(propertyIndex).setMutationProbability(mutationProbability / 100.d);
        double relativeMutationStrength = new Double(mutationStrengthSpinner.getValue().toString());
        simulationParams.getPopulationTypeArray().get(populationTypeIndex).getProperties().get(propertyIndex).setRelativeMutationStrengthPercentage(relativeMutationStrength / 100.d);
    }

}

class FoodTypeParamUnit extends JPanel {

    private SimulationParams simulationParams;
    private int foodTypeIndex;
    private int width;

    private JTextField foodNameTextField;
    private JSpinner foodProbabilitySpinner;
    private JSpinner defaultEnergySpinner;

    private ColorRepresenter representerFoodColor;
    private ColorVariable foodColor;

    public FoodTypeParamUnit(SimulationParams simulationParams, int foodTypeIndex, int width) {
        this.width = width;
        this.simulationParams = simulationParams;
        this.foodTypeIndex = foodTypeIndex;

        JLabel nameLabel = new JLabel("Nahrungstyp " + (foodTypeIndex + 1));
        JPanel namePanel = new JPanel();
        namePanel.add(nameLabel);
        this.add(namePanel);

        foodNameTextField = new JTextField();
        this.add(new PropertyRow(new Dimension(this.width, 40), new JLabel("Name Nahrungstyp:"), foodNameTextField));

        SpinnerNumberModel foodProbabilityModel = new SpinnerNumberModel(0, 0, 100, 1);
        foodProbabilitySpinner = new JSpinner(foodProbabilityModel);
        this.add(new PropertyRow(new Dimension(this.width, 40), new JLabel("Wahrscheinlichkeit (%):"), foodProbabilitySpinner));

        SpinnerNumberModel defaultEnergyModel = new SpinnerNumberModel(100, 1, 1000, 1);
        defaultEnergySpinner = new JSpinner(defaultEnergyModel);
        this.add(new PropertyRow(new Dimension(this.width, 40), new JLabel("Nahrungsenergie:"), defaultEnergySpinner));

        foodColor = new ColorVariable(simulationParams.getFoodTypeArray().get(foodTypeIndex).getColor());
        representerFoodColor = new ColorRepresenter(foodColor);
        representerFoodColor.setPreferredSize(new Dimension(25, 25));
        this.add(new PropertyRow(new Dimension(this.width, 40), new JLabel("Farbe Nahrungstyp:"), representerFoodColor));

        this.setPreferredSize(new Dimension(this.width, 5 * 40 + 20));
    }

    public void setCorrectValues() {
        foodNameTextField.setText(simulationParams.getFoodTypeArray().get(foodTypeIndex).getName());
        foodProbabilitySpinner.setValue(simulationParams.getFoodTypeArray().get(foodTypeIndex).getFoodProbability() * 100);
        defaultEnergySpinner.setValue(simulationParams.getFoodTypeArray().get(foodTypeIndex).getDefaultEnergy());
    }

    public void updateSimulationParams() {
        simulationParams.getFoodTypeArray().get(foodTypeIndex).setName(foodNameTextField.getText());
        double foodProbability = new Double(foodProbabilitySpinner.getValue().toString());
        foodProbability = foodProbability / 100.d;
        simulationParams.getFoodTypeArray().get(foodTypeIndex).setFoodProbability(foodProbability);
        simulationParams.getFoodTypeArray().get(foodTypeIndex).setDefaultEnergy((int)defaultEnergySpinner.getValue());
        simulationParams.getFoodTypeArray().get(foodTypeIndex).setColor(foodColor.getValue());
    }
}

class GroundTypeParamUnit extends JPanel {

    private SimulationParams simulationParams;
    private int groundTypeIndex;
    private int width;

    private JComboBox foodTypeComboBox;
    private JSpinner heightPercentageSpinner;
    private JCheckBox canWalkCheckBox;
    private JCheckBox leaveFoodCheckBox;

    private boolean selectedNothing;

    private ColorRepresenter representerGroundColor;
    private ColorVariable groundColor;

    public GroundTypeParamUnit(SimulationParams simulationParams, int groundTypeIndex, int width) {
        this.simulationParams = simulationParams;
        this.groundTypeIndex = groundTypeIndex;
        this.width = width;

        JLabel nameLabel = new JLabel("Bodentyp " + (groundTypeIndex + 1));
        JPanel namePanel = new JPanel();
        namePanel.add(nameLabel);
        this.add(namePanel);

        foodTypeComboBox = new JComboBox();
        foodTypeComboBox.addItemListener(e -> {
            selectedNothing = foodTypeComboBox.getSelectedIndex() == simulationParams.getFoodTypeArray().size();
        });
        this.add(new PropertyRow(new Dimension(width, 40), new JLabel("Nahrungstyp:"), foodTypeComboBox));

        groundColor = new ColorVariable(simulationParams.getGroundTypeArray().get(groundTypeIndex).getColor());
        representerGroundColor = new ColorRepresenter(groundColor);
        representerGroundColor.setPreferredSize(new Dimension(25, 25));
        this.add(new PropertyRow(new Dimension(width, 40), new JLabel("Farbe Bodentyp:"), representerGroundColor));

        SpinnerNumberModel heightPercentageModel = new SpinnerNumberModel(1, 1, 100, 1);
        heightPercentageSpinner = new JSpinner(heightPercentageModel);
        this.add(new PropertyRow(new Dimension(width, 40), new JLabel("Höhenprofil (%):"), heightPercentageSpinner));

        canWalkCheckBox = new JCheckBox();
        this.add(new PropertyRow(new Dimension(width, 40), new JLabel("Organismen können betreten:"), canWalkCheckBox));

        leaveFoodCheckBox = new JCheckBox();
        this.add(new PropertyRow(new Dimension(width, 40), new JLabel("Nahrung bleibt bestehen:"), leaveFoodCheckBox));

        this.setPreferredSize(new Dimension(this.width, 6 * 40 + 10));
    }

    private String[] getFoodTypeNullableArray() {
        String[] foodTypeNameArray = simulationParams.getFoodTypeNameArray();
        String[] foodTypeNullableArray = Arrays.copyOf(foodTypeNameArray, foodTypeNameArray.length + 1);
        foodTypeNullableArray[foodTypeNameArray.length] = "<keine>";
        return foodTypeNullableArray;
    }

    public void setCorrectValues() {
        GroundType currentGroundType = simulationParams.getGroundTypeArray().get(groundTypeIndex);

        updateComboBox();

        heightPercentageSpinner.setValue(currentGroundType.getMaximumHeightPercent());
        canWalkCheckBox.setSelected(currentGroundType.canWalk());
        leaveFoodCheckBox.setSelected(currentGroundType.leaveFood());
    }

    public void updateSimulationParams() {

        simulationParams.getGroundTypeArray().get(groundTypeIndex).setFoodType(returnNullableFoodType());
        simulationParams.getGroundTypeArray().get(groundTypeIndex).setColor(groundColor.getValue());
        simulationParams.getGroundTypeArray().get(groundTypeIndex).setMaximumHeightPercent((int)heightPercentageSpinner.getValue());
        simulationParams.getGroundTypeArray().get(groundTypeIndex).setCanWalk(canWalkCheckBox.isSelected());
        simulationParams.getGroundTypeArray().get(groundTypeIndex).setLeaveFood(leaveFoodCheckBox.isSelected());
    }

    public void setupListeners(PreviewMapPanel previewMapPanel) {
        heightPercentageSpinner.addChangeListener(e -> {
            simulationParams.getGroundTypeArray().get(groundTypeIndex).setMaximumHeightPercent((int)heightPercentageSpinner.getValue());
            previewMapPanel.requestRepaint(simulationParams);
        });
        representerGroundColor.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                simulationParams.getGroundTypeArray().get(groundTypeIndex).setColor(groundColor.getValue());
                previewMapPanel.requestRepaint(simulationParams);
            }
        });
    }
    private void updateComboBox() {
        GroundType currentGroundType = simulationParams.getGroundTypeArray().get(groundTypeIndex);

        foodTypeComboBox.removeAllItems();
        for(String s : getFoodTypeNullableArray()) {
            foodTypeComboBox.addItem(s);
        }
        if(currentGroundType.getFoodType() == null) {
            selectedNothing = true;
            foodTypeComboBox.setSelectedIndex(foodTypeComboBox.getItemCount() - 1);
        } else {
            foodTypeComboBox.setSelectedIndex(simulationParams.getFoodTypeIndex(currentGroundType.getFoodType()));
            selectedNothing = false;
        }
    }

    private FoodType returnNullableFoodType() {
        if(selectedNothing) {
            return null;
        } else {
            return simulationParams.getFoodTypeArray().get(foodTypeComboBox.getSelectedIndex());
        }
    }
}

