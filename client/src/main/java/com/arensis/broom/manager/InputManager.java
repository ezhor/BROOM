package com.arensis.broom.manager;

import com.arensis.broom.model.KeyboardInput;
import com.arensis.broom.model.BroomStatus;
import com.github.strikerx3.jxinput.XInputAxes;
import com.github.strikerx3.jxinput.XInputButtons;
import com.github.strikerx3.jxinput.XInputDevice;
import com.github.strikerx3.jxinput.exceptions.XInputNotLoadedException;
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
    private boolean holdingBoost;
    private boolean boost;

    public InputManager() {
        keyboardInput = new KeyboardInput();
        broomStatus = new BroomStatus();
    }

    public BroomStatus fetchInputs() {
        XInputDevice device;
        if (XInputDevice.isAvailable()) {
            try {
                device = XInputDevice.getDeviceFor(0);
                if (device.poll()) {
                    calculateBoost(device.getComponents().getButtons());
                    calculateMotorPowerFromGamepad(device.getComponents().getAxes());
                    calculateSteeringFromGamepad(device.getComponents().getAxes());
                } else {
                    calculateMotorPowerFromKeyboard();
                    calculateSteeringFromKeyboard();
                }
            } catch (XInputNotLoadedException e) {
                e.printStackTrace();
            }
        }

        return broomStatus;
    }

    private void calculateBoost(XInputButtons buttons) {
        if (buttons.b) {
            if (!holdingBoost) {
                boost = !boost;
                holdingBoost = true;
            }
        } else {
            holdingBoost = false;
        }
    }

    private void calculateMotorPowerFromGamepad(XInputAxes axes) {
        int rt = Math.round(axes.rt * (boost ? 100 : 50));
        int lt = Math.round(axes.lt * (boost ? 100 : 50));

        applyMotorPower(rt, lt);
    }

    private void calculateMotorPowerFromKeyboard() {
        int rt = keyboardInput.isUp() ? 100 : 0;
        int lt = keyboardInput.isDown() ? 100 : 0;

        applyMotorPower(rt, lt);
    }

    private void applyMotorPower(int rt, int lt) {
        if (rt > 0) {
            broomStatus.setMotorPower(rt);
        } else {
            broomStatus.setMotorPower(-lt);
        }
    }

    private void calculateSteeringFromGamepad(XInputAxes axes) {
        float lx = axes.lx;

        if (lx < LX_DEAD_ZONE && lx > -LX_DEAD_ZONE) {
            lx = 0;
        }

        broomStatus.setSteering(Math.round(lx * 180f));
    }

    private void calculateSteeringFromKeyboard() {
        int rt = keyboardInput.isUp() ? 100 : 0;
        int lt = keyboardInput.isDown() ? 100 : 0;

        broomStatus.setSteering(keyboardInput.isRight() ? 180 : keyboardInput.isLeft() ? -180 : 0);
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

}
