package com.arensis.crcr.manager;

import com.arensis.crcr.model.KeyboardInput;
import com.arensis.crcr.model.RobotStatus;
import com.github.strikerx3.jxinput.XInputAxes;
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
    private static final int LX_DEAD_ZONE = 20;
	private static final int LY_DEAD_ZONE = 2;

	private RobotStatus robotStatus;
	private KeyboardInput keyboardInput;

	public InputManager(){
		keyboardInput = new KeyboardInput();
	}

	public void setRobotStatus(RobotStatus robotStatus) {
		this.robotStatus = robotStatus;
	}

	public RobotStatus fetchInputs() {
		XInputDevice device;
		if (XInputDevice.isAvailable()) {
			try {
				device = XInputDevice.getDeviceFor(0);
				if(device.poll()){
					calculateMotorsPowerFromGamepad(device.getComponents().getAxes());
					calculateArmPosition(device.getComponents().getAxes());
				}else{
					calculateMotorsPowerFromKeyboard();
				}
			} catch (XInputNotLoadedException e) {
				e.printStackTrace();
			}
		}

		fixMotorsPower();

		return robotStatus;
	}

	private void fixMotorsPower() {
		int left = robotStatus.getLeftMotorPower();
		int right = robotStatus.getRightMotorPower();

		robotStatus.setRightMotorPower(right);
		robotStatus.setLeftMotorPower(left);
	}

	private void calculateMotorsPowerFromGamepad(XInputAxes axes) {
        int rt = Math.round(axes.rt*100);
        int lt = Math.round(axes.lt*100);
        int lx = Math.round(axes.lx*100);

        if(lx < LX_DEAD_ZONE && lx > -LX_DEAD_ZONE){
            lx = 0;
        }

        applyMotorsPower(rt, lt, lx);
	}

	private void calculateMotorsPowerFromKeyboard() {
		int rt = keyboardInput.isUp() ? 100 : 0;
		int lt = keyboardInput.isDown() ? 100 : 0;
		int lx = keyboardInput.isLeft() ? -100 : keyboardInput.isRight() ? 100 : 0;

		applyMotorsPower(rt, lt, lx);
	}

	private void applyMotorsPower(int rt, int lt, int lx){
		if(rt != 0){
			calculateForwardMovement(rt, lx);
		}else if(lt != 0){
			calculateBackwardMovement(lt, lx);
		}else{
			calculateInPlaceRotation(lx);
		}
	}

	private void calculateForwardMovement(int rtNormalized, int lxNormalized) {
		int motorReduction;
		int leftMotorPower;
		int rightMotorPower;

		motorReduction = Math.round((rtNormalized/2f) * lxNormalized/100f);
		leftMotorPower = Math.round(rtNormalized+motorReduction);
		leftMotorPower = leftMotorPower > 100 ? 100 : leftMotorPower;
		rightMotorPower = Math.round(rtNormalized-motorReduction);
		rightMotorPower = rightMotorPower > 100 ? 100 : rightMotorPower;

		robotStatus.setLeftMotorPower(leftMotorPower);
		robotStatus.setRightMotorPower(rightMotorPower);
	}

	private void calculateBackwardMovement(int ltNormalized, int lxNormalized) {
		int motorReduction;
		int leftMotorPower;
		int rightMotorPower;

		motorReduction = Math.round((ltNormalized/2f) * lxNormalized/100f);
		leftMotorPower = -Math.round(ltNormalized+motorReduction);
		leftMotorPower = leftMotorPower < -100 ? -100 : leftMotorPower;
		rightMotorPower = -Math.round(ltNormalized-motorReduction);
		rightMotorPower = rightMotorPower < -100 ? -100 : rightMotorPower;

		robotStatus.setLeftMotorPower(leftMotorPower);
		robotStatus.setRightMotorPower(rightMotorPower);
	}

	private void calculateInPlaceRotation(int lxNormalized) {
		robotStatus.setLeftMotorPower(lxNormalized);
		robotStatus.setRightMotorPower(-lxNormalized);
	}

	private void calculateArmPosition(XInputAxes axes){
		int lyNormalized = Math.round(axes.ly*10);
		int value;

		if(lyNormalized < LY_DEAD_ZONE && lyNormalized > -LY_DEAD_ZONE){
			lyNormalized = 0;
		}

		value = robotStatus.getShoulderRotation()+lyNormalized;
		value = value < 0 ? 0 : value > 180 ? 180 : value;

		robotStatus.setShoulderRotation(value);
	}

	@Override
	public void handle(KeyEvent event) {
		switch (event.getCode().getName()){
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
