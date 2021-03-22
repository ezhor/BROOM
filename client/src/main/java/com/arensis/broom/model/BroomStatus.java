package com.arensis.broom.model;

import java.lang.reflect.Field;
import java.util.HashMap;

public class BroomStatus {
    private int motorPower;
    private int steering;
    private boolean boost;

    public BroomStatus() {

    }

    public BroomStatus(String serializedRobotStatus) {
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

    public int getMotorPower() {
        return motorPower;
    }

    public void setMotorPower(int motorPower) {
        this.motorPower = motorPower;
    }

    public int getSteering() {
        return steering;
    }

    public void setSteering(int steering) {
        this.steering = steering;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(motorPower)
                .append(";")
                .append(steering);
        return sb.toString();
    }

    public boolean isBoost() {
        return boost;
    }

    public void setBoost(boolean boost) {
        this.boost = boost;
    }
}
