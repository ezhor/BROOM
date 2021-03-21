package com.arensis.crcr.manager;

import com.arensis.crcr.model.KeyboardInput;
import com.arensis.crcr.model.RobotStatus;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

@SuppressWarnings("restriction")
public class GuiManager {
	private static final String BAR_CHART_RED_STYLE_PATH = "./style/bar-chart-red.css";
	private static final String BAR_CHART_BLUE_STYLE_PATH = "./style/bar-chart-blue.css";

	private Data<String, Number> leftMotorData;
	private Data<String, Number> rightMotorData;
	private Data<String, Number> shoulderRotationData;
	private Data<String, Number> elbowRotationData;
	private Data<String, Number> wristRotationData;
	private Data<String, Number> handRotationData;

	public void start(Stage primaryStage, InputManager inputManager) {
		final Scene scene;
		final GridPane mainPane = createMainGridPane();

		primaryStage.setTitle("CRCR: Cool Remote Controlled Robot");
		mainPane.add(createMotorsBarChart(), 0, 0);
		mainPane.add(createArmBarChart(), 0, 1);
		scene = new Scene(mainPane, 500, 500);
		scene.setOnKeyPressed(inputManager);
		scene.setOnKeyReleased(inputManager);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void update(RobotStatus robotStatus) {
		this.leftMotorData.setYValue(robotStatus.getLeftMotorPower());
		this.rightMotorData.setYValue(robotStatus.getRightMotorPower());
		this.shoulderRotationData.setYValue(robotStatus.getShoulderRotation());
		this.elbowRotationData.setYValue(robotStatus.getElbowRotation());
		this.wristRotationData.setYValue(robotStatus.getWristRotation());
		this.handRotationData.setYValue(robotStatus.getHandRotation());
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

	private BarChart<String, Number> createMotorsBarChart() {
		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis(-100, 100, 10);
		final BarChart<String, Number> motorsBarChart = new BarChart<>(xAxis, yAxis);
		final XYChart.Series<String, Number> series = new Series<>();

		this.leftMotorData = new Data<>("Left Motor", 0);
		this.rightMotorData = new Data<>("Right Motor", 0);
		
		series.setName("Motors");
		series.getData().addAll(leftMotorData, rightMotorData);
		motorsBarChart.getData().addAll(series);
		
		motorsBarChart.getStylesheets().add(BAR_CHART_RED_STYLE_PATH);
		motorsBarChart.autosize();
		motorsBarChart.setAnimated(false);

		return motorsBarChart;
	}

	private BarChart<String, Number> createArmBarChart() {
		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis(0, 180, 10);
		final BarChart<String, Number> armBarChart = new BarChart<>(xAxis, yAxis);
		final XYChart.Series<String, Number> series = new Series<>();

		this.shoulderRotationData = new Data<>("Shoulder Rotation", 0);
		this.elbowRotationData = new Data<>("Elbow Rotation", 0);
		this.wristRotationData = new Data<>("Wrist Rotation", 0);
		this.handRotationData = new Data<>("Hand Rotation", 0);
		
		series.setName("Arm");
		series.getData().addAll(shoulderRotationData, elbowRotationData, wristRotationData, handRotationData);		
		armBarChart.getData().addAll(series);
		
		armBarChart.getStylesheets().add(BAR_CHART_BLUE_STYLE_PATH);
		armBarChart.setAnimated(false);
		
		return armBarChart;
	}
}