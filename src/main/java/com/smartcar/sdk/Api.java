package com.smartcar.sdk;

import java.util.Arrays;

public final class Api {
  public static class Paging {
    public int count;
    public int offset;

    @Override
    public String toString() {
      return "Paging{" +
              "count=" + count +
              ", offset=" + offset +
              '}';
    }
  }
  public static class Success {
    public String status;
  }
  public static class Permissions {
    public String[] permissions;
    public Paging paging;

    @Override
    public String toString() {
      return "Permissions{" +
              "permissions=" + Arrays.toString(permissions) +
              ", paging=" + paging +
              '}';
    }
  }
  public static class Vehicles {
    public String[] vehicles;
    public Paging paging;

    @Override
    public String toString() {
      return "Vehicles{" +
              "vehicles=" + Arrays.toString(vehicles) +
              ", paging=" + paging +
              '}';
    }
  }
  public static class Info {
    public String id, make, model;
    public int year;

    @Override
    public String toString() {
      return "Info{" +
              "id='" + id + '\'' +
              ", make='" + make + '\'' +
              ", model='" + model + '\'' +
              ", year=" + year +
              '}';
    }
  }
  public static class Accelerometer {
    public double x, y, z;

    @Override
    public String toString() {
      return "Accelerometer{" +
              "x=" + x +
              ", y=" + y +
              ", z=" + z +
              '}';
    }
  }
  public static class Airbags {
    public Airbag[] airbags;

    @Override
    public String toString() {
      return "Airbags{" +
              "airbags=" + Arrays.toString(airbags) +
              '}';
    }
  }
  public static class Airbag {
    public String location;
    public boolean isActive, isDeployed;

    @Override
    public String toString() {
      return "Airbag{" +
              "location='" + location + '\'' +
              ", isActive=" + isActive +
              ", isDeployed=" + isDeployed +
              '}';
    }
  }
  public static class Barometer {
    public double pressure;

    @Override
    public String toString() {
      return "Barometer{" +
              "pressure=" + pressure +
              '}';
    }
  }
  public static class Battery {
    public double range, percentRemaining;

    @Override
    public String toString() {
      return "Battery{" +
              "range=" + range +
              ", percentRemaining=" + percentRemaining +
              '}';
    }
  }
  public static class Charge {
    public boolean isPluggedIn;
    public String state;

    @Override
    public String toString() {
      return "Charge{" +
              "isPluggedIn=" + isPluggedIn +
              ", state='" + state + '\'' +
              '}';
    }
  }
  public static class ChargeLimit {
    public double limit;
    public String state;

    @Override
    public String toString() {
      return "ChargeLimit{" +
              "limit=" + limit +
              ", state='" + state + '\'' +
              '}';
    }
  }
  public static class ChargeSchedule {
    public String startTime, state;

    @Override
    public String toString() {
      return "ChargeSchedule{" +
              "startTime='" + startTime + '\'' +
              ", state='" + state + '\'' +
              '}';
    }
  }
  public static class Climate {
    public double temperature;
    public boolean isOn;

    @Override
    public String toString() {
      return "Climate{" +
              "temperature=" + temperature +
              ", isOn=" + isOn +
              '}';
    }
  }
  public static class CollisionSensor {
    public boolean isTriggered;

    @Override
    public String toString() {
      return "CollisionSensor{" +
              "isTriggered=" + isTriggered +
              '}';
    }
  }
  public static class Compass {
    public double heading;

    @Override
    public String toString() {
      return "Compass{" +
              "heading=" + heading +
              '}';
    }
  }
  public static class CruiseControl {
    public double speed, followDistance;
    public boolean isOn;

    @Override
    public String toString() {
      return "CruiseControl{" +
              "speed=" + speed +
              ", followDistance=" + followDistance +
              ", isOn=" + isOn +
              '}';
    }
  }
  public static class Dimensions {
    public double height, width, length, weight;

    @Override
    public String toString() {
      return "Dimensions{" +
              "height=" + height +
              ", width=" + width +
              ", length=" + length +
              ", weight=" + weight +
              '}';
    }
  }

  public static class Doors {
    public Door[] doors;

    @Override
    public String toString() {
      return "Doors{" +
              "doors=" + Arrays.toString(doors) +
              '}';
    }
  }
  public static class Door {
    public String location;
    public boolean isOpen;

    @Override
    public String toString() {
      return "Door{" +
              "location='" + location + '\'' +
              ", isOpen=" + isOpen +
              '}';
    }
  }
  public static class ChildSafetyLocks {
    public ChildSafetyLock[] childSafetyLocks;

    @Override
    public String toString() {
      return "ChildSafetyLocks{" +
              "childSafetyLocks=" + Arrays.toString(childSafetyLocks) +
              '}';
    }
  }
  public static class ChildSafetyLock {
    public String location;
    public boolean isLocked;

    @Override
    public String toString() {
      return "ChildSafetyLock{" +
              "location='" + location + '\'' +
              ", isLocked=" + isLocked +
              '}';
    }
  }
  public static class DriveMode {
    public String mode;

    @Override
    public String toString() {
      return "DriveMode{" +
              "mode='" + mode + '\'' +
              '}';
    }
  }
  public static class Engine {
    public String isOn;

    @Override
    public String toString() {
      return "Engine{" +
              "state='" + isOn + '\'' +
              '}';
    }
  }
  public static class EngineCoolant {
    public double level, temperature;

    @Override
    public String toString() {
      return "EngineCoolant{" +
              "level=" + level +
              ", temperature=" + temperature +
              '}';
    }
  }
  public static class EngineHood {
    public boolean isOpen;

    @Override
    public String toString() {
      return "EngineHood{" +
              "isOpen=" + isOpen +
              '}';
    }
  }
  public static class EngineOil {
    public boolean changeIndicator;
    public double percentRemaining, lifeRemaining, pressure, temperature;

    @Override
    public String toString() {
      return "EngineOil{" +
              "changeIndicator=" + changeIndicator +
              ", percentRemaining=" + percentRemaining +
              ", lifeRemaining=" + lifeRemaining +
              ", pressure=" + pressure +
              ", temperature=" + temperature +
              '}';
    }
  }
  public static class EngineThrottle {
    public double percentOpen;

    @Override
    public String toString() {
      return "EngineThrottle{" +
              "percentOpen=" + percentOpen +
              '}';
    }
  }
  public static class Fuel {
    public double range, percentRemaining;

    @Override
    public String toString() {
      return "Fuel{" +
              "range=" + range +
              ", percentRemaining=" + percentRemaining +
              '}';
    }
  }
  public static class Gyroscope {
    public double yawRate;

    @Override
    public String toString() {
      return "Gyroscope{" +
              "yawRate=" + yawRate +
              '}';
    }
  }
  public static class HazardLight {
    public boolean isOn;

    @Override
    public String toString() {
      return "HazardLight{" +
              "isOn=" + isOn +
              '}';
    }
  }
  public static class Headlight {
    public String state;

    @Override
    public String toString() {
      return "Headlight{" +
              "state='" + state + '\'' +
              '}';
    }
  }
  public static class Ignition {
    public String state;

    @Override
    public String toString() {
      return "Ignition{" +
              "state=" + state +
              '}';
    }
  }
  public static class InteriorLights {
    public InteriorLight[] lights;

    @Override
    public String toString() {
      return "InteriorLights{" +
              "lights=" + Arrays.toString(lights) +
              '}';
    }
  }
  public static class InteriorLight {
    public String location;
    public boolean isOn;

    @Override
    public String toString() {
      return "InteriorLight{" +
              "location='" + location + '\'' +
              ", isOn=" + isOn +
              '}';
    }
  }
  public static class TurnIndicator {
    public String state;

    @Override
    public String toString() {
      return "TurnIndicator{" +
              "state='" + state + '\'' +
              '}';
    }
  }
  public static class Location {
    public double latitude, longitude;

    @Override
    public String toString() {
      return "Location{" +
              "latitude=" + latitude +
              ", longitude=" + longitude +
              '}';
    }
  }
  public static class Mirrors {
    public Mirror[] mirrors;

    @Override
    public String toString() {
      return "Mirrors{" +
              "mirrors=" + Arrays.toString(mirrors) +
              '}';
    }
  }
  public static class Mirror {
    public String location;
    public double xTilt, yTilt;
    public Mirror(String location, double xTilt, double yTilt) {
      this.location = location;
      this.xTilt = xTilt;
      this.yTilt = yTilt;
    }

    @Override
    public String toString() {
      return "Mirror{" +
              "location='" + location + '\'' +
              ", xTilt=" + xTilt +
              ", yTilt=" + yTilt +
              '}';
    }
  }
  public static class Odometer {
    public double distance;

    @Override
    public String toString() {
      return "Odometer{" +
              "distance=" + distance +
              '}';
    }
  }
  public static class TripOdometers {
    public TripOdometer[] trips;

    @Override
    public String toString() {
      return "TripOdometers{" +
              "trips=" + Arrays.toString(trips) +
              '}';
    }
  }
  public static class TripOdometer {
    public String label;
    public double distance;

    @Override
    public String toString() {
      return "TripOdometer{" +
              "label='" + label + '\'' +
              ", distance=" + distance +
              '}';
    }
  }
  /* pedals/{accelerator,brake} */
  public static class Pedal {
    public double percentDepressed;

    @Override
    public String toString() {
      return "Pedal{" +
              "percentDepressed=" + percentDepressed +
              '}';
    }
  }
  public static class RainSensor {
    public boolean isRaining;

    @Override
    public String toString() {
      return "RainSensor{" +
              "isRaining=" + isRaining +
              '}';
    }
  }
  public static class Seats {
    public Seat[] seats;

    @Override
    public String toString() {
      return "Seats{" +
              "seats=" + Arrays.toString(seats) +
              '}';
    }
  }
  public static class Seat {
    public String location;
    public boolean isOccupied, isBuckled;

    @Override
    public String toString() {
      return "Seat{" +
              "location='" + location + '\'' +
              ", isOccupied=" + isOccupied +
              ", isBuckled=" + isBuckled +
              '}';
    }
  }
  public static class Security {
    public boolean isLocked;

    @Override
    public String toString() {
      return "Security{" +
              "isLocked=" + isLocked +
              '}';
    }
  }
  public static class SliBattery {
    public double current, voltage, percentRemaining;

    @Override
    public String toString() {
      return "SliBattery{" +
              "current=" + current +
              ", voltage=" + voltage +
              ", percentRemaining=" + percentRemaining +
              '}';
    }
  }
  public static class Gauge {
    public double speed;

    @Override
    public String toString() {
      return "Gauge{" +
              "speed=" + speed +
              '}';
    }
  }
  public static class EngineSpeed {
    public double engineSpeed;

    @Override
    public String toString() {
      return "EngineSpeed{" +
              "engineSpeed=" + engineSpeed +
              '}';
    }
  }
  public static class SteeringWheel {
    public String location;
    public double angle;

    @Override
    public String toString() {
      return "SteeringWheel{" +
              "location='" + location + '\'' +
              ", angle=" + angle +
              '}';
    }
  }
  public static class Sunroof {
    public String state;
    public double percentOpen;

    @Override
    public String toString() {
      return "Sunroof{" +
              "state='" + state + '\'' +
              ", percentOpen=" + percentOpen +
              '}';
    }
  }
  public static class Temperature {
    public double temperature;

    @Override
    public String toString() {
      return "Temperature{" +
              "temperature=" + temperature +
              '}';
    }
  }
  public static class Tires {
    public Tire[] tires;

    @Override
    public String toString() {
      return "Tires{" +
              "tires=" + Arrays.toString(tires) +
              '}';
    }
  }
  public static class Tire {
    public String location;
    public double pressure;

    @Override
    public String toString() {
      return "Tire{" +
              "location='" + location + '\'' +
              ", pressure=" + pressure +
              '}';
    }
  }
  public static class Transmission {
    public String type, state;

    @Override
    public String toString() {
      return "Transmission{" +
              "type='" + type + '\'' +
              ", state='" + state + '\'' +
              '}';
    }
  }
  public static class TransmissionFluid {
    public double temperature, wear;

    @Override
    public String toString() {
      return "TransmissionFluid{" +
              "temperature=" + temperature +
              ", wear=" + wear +
              '}';
    }
  }

  public static class Trunk {
    public boolean isOpen;

    @Override
    public String toString() {
      return "Trunk{" +
              "isOpen=" + isOpen +
              '}';
    }
  }
  public static class Vin {
    public String vin;

    @Override
    public String toString() {
      return "Vin{" +
              "vin='" + vin + '\'' +
              '}';
    }
  }
  public static class WasherFluid {
    public double percentRemaining;

    @Override
    public String toString() {
      return "WasherFluid{" +
              "percentRemaining=" + percentRemaining +
              '}';
    }
  }
  public static class Wheels {
    Wheel[] wheels;

    @Override
    public String toString() {
      return "Wheels{" +
              "wheels=" + Arrays.toString(wheels) +
              '}';
    }
  }
  public static class Wheel {
    public String location;
    public double diameter;

    @Override
    public String toString() {
      return "Wheel{" +
              "location='" + location + '\'' +
              ", diameter=" + diameter +
              '}';
    }
  }
  public static class WheelSpeeds {
    public WheelSpeed[] wheelSpeed;

    @Override
    public String toString() {
      return "WheelSpeeds{" +
              "wheelSpeed=" + Arrays.toString(wheelSpeed) +
              '}';
    }
  }
  public static class WheelSpeed {
    public String location;
    public double speed;

    @Override
    public String toString() {
      return "WheelSpeed{" +
              "location='" + location + '\'' +
              ", speed=" + speed +
              '}';
    }
  }
  public static class Windows {
    public Window[] windows;

    @Override
    public String toString() {
      return "Windows{" +
              "windows=" + Arrays.toString(windows) +
              '}';
    }
  }
  public static class Window {
    public String location;
    public boolean isLocked;
    public double percentOpen;

    public Window(String location, double percentOpen) {
      this.location = location;
      this.percentOpen = percentOpen;
    }

    public Window(String location) {
      this.location = location;
    }

    @Override
    public String toString() {
      return "Window{" +
              "location='" + location + '\'' +
              ", isLocked=" + isLocked +
              ", percentOpen=" + percentOpen +
              '}';
    
    }
  }

  public static class GenericAction {
    String action;
    public GenericAction(String action) {
      this.action = action;
    }
  }
  public static class ChargeLimitAction {
    String action;
    double limit;
    public ChargeLimitAction(String action, double limit) {
      this.action = action;
      this.limit = limit;
    }
  }
  public static class ChargeScheduleAction {
    String action;
    String startTime;
    public ChargeScheduleAction(String action, String startTime) {
      this.action = action;
      this.startTime = startTime;
    }
  }
  public static class ClimateAction {
    String action;
    double temperature;
    public ClimateAction(String action, double temperature) {
      this.action = action;
      this.temperature = temperature;
    }
  }
  public static class MirrorAction {
    String action;
    Mirror[] mirrors;
    public MirrorAction(String action, Mirror[] mirrors) {
      this.action = action;
      this.mirrors = mirrors;
    }
  }
  public static class WindowAction {
    String action;
    Window[] windows;
    public WindowAction(String action, Window[] windows) {
      this.action = action;
      this.windows = windows;
    }
  }
}