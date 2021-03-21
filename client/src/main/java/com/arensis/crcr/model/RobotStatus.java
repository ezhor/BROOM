package com.arensis.crcr.model;

import java.lang.reflect.Field;
import java.util.HashMap;

public class RobotStatus {
    private int leftMotorPower;
    private int rightMotorPower;
    private int shoulderRotation;
    private int elbowRotation;
    private int wristRotation;
    private int handRotation;
    private int soundEvent;

    public RobotStatus() {

    }

    public RobotStatus(String serializedRobotStatus) {
        final String[] propertiesArray = serializedRobotStatus.split(";");
        final HashMap<String, Integer> propertiesMap = new HashMap<>();
        String[] propertyArray;

        for (String propertyString : propertiesArray) {
            if (propertyString != null && !propertyString.isEmpty()) {
                propertyArray = propertyString.split("=");
                propertiesMap.put(propertyArray[0], Integer.parseInt(propertyArray[1]));
            }
        }

        for (Field field : getClass().getDeclaredFields()) {
            if (propertiesMap.get(field.getName()) != null) {
                try {
                    field.setInt(this, propertiesMap.get(field.getName()));
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int getLeftMotorPower() {
        return leftMotorPower;
    }

    public void setLeftMotorPower(int leftMotorPower) {
        this.leftMotorPower = leftMotorPower;
    }

    public int getRightMotorPower() {
        return rightMotorPower;
    }

    public void setRightMotorPower(int rightMotorPower) {
        this.rightMotorPower = rightMotorPower;
    }

    public int getShoulderRotation() {
        return shoulderRotation;
    }

    public void setShoulderRotation(int shoulderRotation) {
        this.shoulderRotation = shoulderRotation;
    }

    public int getElbowRotation() {
        return elbowRotation;
    }

    public void setElbowRotation(int elbowRotation) {
        this.elbowRotation = elbowRotation;
    }

    public int getWristRotation() {
        return wristRotation;
    }

    public void setWristRotation(int wristRotation) {
        this.wristRotation = wristRotation;
    }

    public int getHandRotation() {
        return handRotation;
    }

    public void setHandRotation(int handRotation) {
        this.handRotation = handRotation;
    }

    public int getSoundEvent() {
        return soundEvent;
    }

    public void setSoundEvent(int soundEvent) {
        this.soundEvent = soundEvent;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Field field : getClass().getDeclaredFields()) {
            sb.append(field.getName());
            sb.append("=");
            try {
                sb.append(field.get(this));
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
            sb.append(";");
        }
        return sb.toString();
    }

}
