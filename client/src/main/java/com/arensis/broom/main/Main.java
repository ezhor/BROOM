package com.arensis.broom.main;

import com.arensis.broom.manager.CommunicationManager;
import com.arensis.broom.manager.GuiManager;
import com.arensis.broom.manager.InputManager;
import com.arensis.broom.model.BroomStatus;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("restriction")
public class Main extends Application {
    private static final int UPDATE_DELAY = 50;
    private static final String RELAY_IP = "mirandaserver.ddns.net";
    private final GuiManager guiManager = new GuiManager();
    private final InputManager inputManager = new InputManager();
    private final CommunicationManager communicationManager = new CommunicationManager();
    private final Timer timer = new Timer();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //communicationManager.start();
        guiManager.start(primaryStage, inputManager);
        startUpdateThread();
    }

    private void startUpdateThread() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                final BroomStatus broomStatus = inputManager.fetchInputs();
                //communicationManager.update(broomStatus);
                Platform.runLater(() -> guiManager.update(broomStatus));
            }
        }, 0, UPDATE_DELAY);
    }

    @Override
    public void stop() {
        communicationManager.stop();
        timer.cancel();
        inputManager.stop();
    }
}