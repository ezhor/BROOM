package com.arensis.broom.manager;

import com.arensis.broom.model.BroomStatus;
import com.arensis.broom.model.DoughnutChart;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

@SuppressWarnings("restriction")
public class GuiManager {
    private static final String BAR_CHART_BLUE_STYLE_PATH = "./style/bar-chart-blue.css";
    private static final String PIE_CHART_RED_STYLE = "-fx-pie-color: #d50000";
    private static final String PIE_CHART_GREEN_STYLE = "-fx-pie-color: #64dd17";

    private DoughnutChart motorChart;
    private PieChart.Data motorEmptyData;
    private PieChart.Data motorValueData;
    private Data<Number, String> steeringData;

    public void start(Stage primaryStage, InputManager inputManager) {
        final Scene scene;
        final GridPane mainPane = createMainGridPane();
        final Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

        primaryStage.setTitle("BROOM BROOM!");
        mainPane.add(createSteeringChart(), 0, 0);
        mainPane.add(createMotorChart(), 1, 0);
        scene = new Scene(mainPane, 1000, 250);
        primaryStage.setX(bounds.getMinX() + bounds.getWidth() / 2f - scene.getWidth() / 2f);
        primaryStage.setY(bounds.getMinY() + bounds.getHeight() - scene.getHeight() - 50f);
        scene.setOnKeyPressed(inputManager);
        scene.setOnKeyReleased(inputManager);

        primaryStage.setScene(scene);
        primaryStage.show();
        applyChartColors();
    }

    public void update(BroomStatus broomStatus) {
        if (broomStatus.getMotorPower() >= 0) {
            motorChart.setClockwise(true);
            this.motorEmptyData.setPieValue(100 - broomStatus.getMotorPower());
            this.motorValueData.setPieValue(broomStatus.getMotorPower());
        } else {
            motorChart.setClockwise(false);
            this.motorEmptyData.setPieValue(100 + broomStatus.getMotorPower());
            this.motorValueData.setPieValue(-broomStatus.getMotorPower());
        }
        this.steeringData.setXValue(broomStatus.getSteering());
    }

    private GridPane createMainGridPane() {
        final GridPane gridPane = new GridPane();

        gridPane.setMinSize(400, 200);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.CENTER);

        return gridPane;
    }

    private DoughnutChart createMotorChart() {
        motorEmptyData = new PieChart.Data("", 100);
        motorValueData = new PieChart.Data("", 0);
        motorChart = new DoughnutChart(FXCollections.observableArrayList(motorValueData, motorEmptyData));
        motorChart.setLabelsVisible(false);
        motorChart.setAnimated(false);
        motorChart.setLegendVisible(false);
        motorChart.setStartAngle(270d);

        return motorChart;
    }

    private BarChart<Number, String> createSteeringChart() {
        final NumberAxis xAxis = new NumberAxis(-180, 180, 10);
        final CategoryAxis yAxis = new CategoryAxis();
        final BarChart<Number, String> steeringBarChart = new BarChart<>(xAxis, yAxis);
        final XYChart.Series<Number, String> series = new Series<>();

        this.steeringData = new Data<>(0, "");

        series.getData().addAll(steeringData);
        steeringBarChart.getData().addAll(series);
        steeringBarChart.setLegendVisible(false);

        steeringBarChart.getStylesheets().add(BAR_CHART_BLUE_STYLE_PATH);
        steeringBarChart.setAnimated(false);

        return steeringBarChart;
    }

    private void applyChartColors() {
        motorEmptyData.getNode().setStyle(PIE_CHART_RED_STYLE);
        motorValueData.getNode().setStyle(PIE_CHART_GREEN_STYLE);
    }
}