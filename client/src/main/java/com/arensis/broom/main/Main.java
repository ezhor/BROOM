package com.arensis.broom.main;

import com.arensis.broom.manager.CommunicationManager;
import com.arensis.broom.manager.GuiManager;
import com.arensis.broom.manager.InputManager;
import com.arensis.broom.model.BroomStatus;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("restriction")
public class Main extends Application {
    private static final int UPDATE_TIME = 50;
    private static final String CRCR_IP = "mirandaserver.ddns.net";
    private final GuiManager guiManager = new GuiManager();
    private final InputManager inputManager = new InputManager();
    private final CommunicationManager communicationManager = new CommunicationManager();
    private final Timer timer = new Timer();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //communicationManager.connect(CRCR_IP);
        guiManager.start(primaryStage, inputManager);
        startUpdateThread();
    }

    private void startUpdateThread() {
        timer.schedule(new RobotStatusUpdater(), 0, UPDATE_TIME);
    }

    @Override
    public void stop() {
        timer.cancel();
    }

    private class RobotStatusUpdater extends TimerTask {

        @Override
        public void run() {
            final BroomStatus broomStatus = inputManager.fetchInputs();
            guiManager.update(broomStatus);
            System.out.println(broomStatus);
            //communicationManager.update(robotStatus);
        }

    }
}