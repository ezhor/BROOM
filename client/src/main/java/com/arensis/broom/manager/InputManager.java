package com.arensis.broom.manager;

import com.arensis.broom.model.BroomStatus;
import com.arensis.broom.model.KeyboardInput;
import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public class InputManager implements EventHandler<KeyEvent> {
    private static final String UP = "Up";
    private static final String DOWN = "Down";
    private static final String LEFT = "Left";
    private static final String RIGHT = "Right";
    private static final String KEY_PRESSED = "KEY_PRESSED";
    private static final float LS_DEAD_ZONE = 0.15f;
    private static final float RS_DEAD_ZONE = 0.15f;

    private final BroomStatus broomStatus;
    private final KeyboardInput keyboardInput;
    private final ControllerManager controllers;
    private boolean holdingBoost;

    public InputManager() {
        keyboardInput = new KeyboardInput();
        broomStatus = new BroomStatus();
        controllers = new ControllerManager();
        controllers.initSDLGamepad();
    }

    public BroomStatus fetchInputs() {
        ControllerState controllerState = controllers.getState(0);
        if (controllerState.isConnected) {
            calculateBoost(controllerState.b);
            calculateMotorPowerFromGamepad(controllerState.leftTrigger, controllerState.rightTrigger);
            calculateSteeringFromGamepad(controllerState.leftStickX);
            calculateCameraRotationFromGamepad(controllerState.rightStickX, controllerState.rightStickY);
        } else {
            calculateMotorPowerFromKeyboard();
            calculateSteeringFromKeyboard();
        }
        return broomStatus;
    }


    private void calculateBoost(boolean buttonB) {
        if (buttonB) {
            if (!holdingBoost) {
                broomStatus.setBoost(!broomStatus.isBoost());
                holdingBoost = true;
            }
        } else {
            holdingBoost = false;
        }
    }

    private void calculateMotorPowerFromGamepad(float lt, float rt) {
        int boostedLt = Math.round(lt * (broomStatus.isBoost() ? 100 : 50));
        int boostedRt = Math.round(rt * (broomStatus.isBoost() ? 100 : 50));

        applyMotorPower(boostedLt, boostedRt);
    }

    private void calculateMotorPowerFromKeyboard() {
        int rt = keyboardInput.isUp() ? 100 : 0;
        int lt = keyboardInput.isDown() ? 100 : 0;

        applyMotorPower(lt, rt);
    }

    private void applyMotorPower(int lt, int rt) {
        if (rt > 0) {
            broomStatus.setMotorPower(rt);
        } else {
            broomStatus.setMotorPower(-lt);
        }
    }

    private void calculateSteeringFromGamepad(float lx) {
        if (lx < LS_DEAD_ZONE && lx > -LS_DEAD_ZONE) {
            lx = 0;
        }

        broomStatus.setSteering(Math.round(lx * 100f));
    }

    private void calculateSteeringFromKeyboard() {
        broomStatus.setSteering(keyboardInput.isRight() ? 100 : keyboardInput.isLeft() ? -100 : 0);
    }

    private void calculateCameraRotationFromGamepad(float rightStickX, float rightStickY) {
        if (rightStickX < RS_DEAD_ZONE && rightStickX > -RS_DEAD_ZONE &&
                rightStickY < RS_DEAD_ZONE && rightStickY > -RS_DEAD_ZONE) {
            rightStickX = 0;
            rightStickY = 0;
        }

        broomStatus.setCameraRotationX(Math.round(rightStickX * 180));
        broomStatus.setCameraRotationY(Math.round(rightStickY * 180));
    }

    @Override
    public void handle(KeyEvent event) {
        switch (event.getCode().getName()) {
            case UP:
                keyboardInput.setUp(KEY_PRESSED.equals(event.getEventType().getName()));
                break;
            case DOWN:
                keyboardInput.setDown(KEY_PRESSED.equals(event.getEventType().getName()));
                break;
            case LEFT:
                keyboardInput.setLeft(KEY_PRESSED.equals(event.getEventType().getName()));
                break;
            case RIGHT:
                keyboardInput.setRight(KEY_PRESSED.equals(event.getEventType().getName()));
                break;
        }
    }

    public void stop() {
        controllers.quitSDLGamepad();
    }

}
