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
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;

@SuppressWarnings("restriction")
public class GuiManager {
    private static final String STAGE_ICON_PATH = "image/stage-icon.png";
    private static final String BOOST_ICON_PATH = "image/boost-icon.png";
    private static final String BAR_CHART_BLUE_STYLE_PATH = "./style/bar-chart-blue.css";
    private static final String SCATTER_CHART_BLUE_CROSS_STYLE_PATH = "./style/scatter-chart-blue-cross.css";
    private static final String PIE_CHART_RED_STYLE = "-fx-pie-color: #d50000";
    private static final String PIE_CHART_GREEN_STYLE = "-fx-pie-color: #64dd17";

    private DoughnutChart motorChart;
    private PieChart.Data motorEmptyData;
    private PieChart.Data motorValueData;
    private Label motorLabel;
    private Data<Number, String> steeringData;
    private Data<Number, Number> cameraRotationData;
    private ImageView boostIcon;

    public void start(Stage primaryStage, InputManager inputManager) {
        final Scene scene;
        final GridPane mainPane = createMainGridPane();
        final Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

        primaryStage.setTitle("BROOM BROOM!");
        primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResource(STAGE_ICON_PATH).toExternalForm()));
        mainPane.add(createSteeringChart(), 0, 0);
        mainPane.add(createCameraRotationChart(), 1, 0);
        mainPane.add(createMotorChart(), 2, 0);
        mainPane.add(createMotorLabel(), 2, 0);
        mainPane.add(createBoostIcon(), 3, 0);
        scene = new Scene(mainPane, 1400, 250);
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
            motorEmptyData.setPieValue(100 - broomStatus.getMotorPower());
            motorValueData.setPieValue(broomStatus.getMotorPower());
        } else {
            motorChart.setClockwise(false);
            motorEmptyData.setPieValue(100 + broomStatus.getMotorPower());
            motorValueData.setPieValue(-broomStatus.getMotorPower());
        }
        motorLabel.setText(String.valueOf(Math.abs(broomStatus.getMotorPower())));
        boostIcon.setVisible(broomStatus.isBoost());
        steeringData.setXValue(broomStatus.getSteering());
        cameraRotationData.setXValue(broomStatus.getCameraRotationX());
        cameraRotationData.setYValue(broomStatus.getCameraRotationY());
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

    private Label createMotorLabel() {
        motorLabel = new Label("0");
        motorLabel.setAlignment(Pos.CENTER);
        motorLabel.setTextAlignment(TextAlignment.CENTER);
        motorLabel.setLabelFor(motorChart);
        motorLabel.setMinWidth(500);
        motorLabel.setFont(Font.font("Arial", 50));
        return motorLabel;
    }

    private BarChart<Number, String> createSteeringChart() {
        final NumberAxis xAxis = new NumberAxis(-100, 100, 10);
        final CategoryAxis yAxis = new CategoryAxis();
        final BarChart<Number, String> steeringBarChart = new BarChart<>(xAxis, yAxis);
        final XYChart.Series<Number, String> series = new Series<>();

        this.steeringData = new Data<>(0, "");

        series.getData().add(steeringData);
        steeringBarChart.getData().add(series);
        steeringBarChart.setLegendVisible(false);

        steeringBarChart.getStylesheets().add(getClass().getClassLoader().getResource(BAR_CHART_BLUE_STYLE_PATH).toExternalForm());
        steeringBarChart.setAnimated(false);

        return steeringBarChart;
    }

    private ScatterChart<Number, Number> createCameraRotationChart() {
        final NumberAxis xAxis = new NumberAxis(-180, 180, 10);
        final NumberAxis yAxis = new NumberAxis(-180, 180, 10);
        final ScatterChart<Number, Number> cameraRotationChart = new ScatterChart<>(xAxis, yAxis);
        final XYChart.Series<Number, Number> series = new XYChart.Series<>();

        cameraRotationData = new Data<>(0, 0);
        series.getData().add(cameraRotationData);
        cameraRotationChart.getData().add(series);
        cameraRotationChart.setAnimated(false);
        cameraRotationChart.setLegendVisible(false);
        cameraRotationChart.getStylesheets().add(getClass().getClassLoader().getResource(SCATTER_CHART_BLUE_CROSS_STYLE_PATH).toExternalForm());

        return cameraRotationChart;
    }

    private ImageView createBoostIcon() {
        boostIcon = new ImageView(getClass().getClassLoader().getResource(BOOST_ICON_PATH).toExternalForm());
        boostIcon.setFitHeight(100);
        boostIcon.setFitWidth(100);
        return boostIcon;
    }

    private void applyChartColors() {
        motorEmptyData.getNode().setStyle(PIE_CHART_RED_STYLE);
        motorValueData.getNode().setStyle(PIE_CHART_GREEN_STYLE);
    }
}