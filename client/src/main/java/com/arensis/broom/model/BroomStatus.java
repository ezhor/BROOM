package com.arensis.broom.model;

import java.lang.reflect.Field;
import java.util.HashMap;

public class BroomStatus {
    private int motorPower;
    private int steering;
    private boolean boost;

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
        return motorPower + ";" + steering;
    }

    public boolean isBoost() {
        return boost;
    }

    public void setBoost(boolean boost) {
        this.boost = boost;
    }
}
