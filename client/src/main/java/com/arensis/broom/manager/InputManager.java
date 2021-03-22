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
    private static final float LX_DEAD_ZONE = 0.15f;

    private BroomStatus broomStatus;
    private KeyboardInput keyboardInput;
    private ControllerManager controllers;
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
        }else{
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
        if (lx < LX_DEAD_ZONE && lx > -LX_DEAD_ZONE) {
            lx = 0;
        }

        broomStatus.setSteering(Math.round(lx * 100f));
    }

    private void calculateSteeringFromKeyboard() {
        int rt = keyboardInput.isUp() ? 100 : 0;
        int lt = keyboardInput.isDown() ? 100 : 0;

        broomStatus.setSteering(keyboardInput.isRight() ? 100 : keyboardInput.isLeft() ? -100 : 0);
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

    public void stop(){
        controllers.quitSDLGamepad();
    }

}
