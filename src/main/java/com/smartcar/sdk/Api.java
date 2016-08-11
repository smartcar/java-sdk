package com.smartcar.sdk;

public final class Api {
  static class Info {
    String id, make, model;
    int year;
  }
  static class Accelerometer {
    double x, y, z;
  }
  static class Airbags {
    Airbag[] airbags;
  }
  static class Airbag {
    String location;
    boolean isActive, isDeployed;
  }
  static class Barometer {
    double pressure;
  }
  static class Battery {
    double range, percentRemaining;
  }
  static class Charge {
    boolean isPluggedIn;
    String state;
  }
  static class ChargeLimit {
    double limit;
    String state;
  }
  static class ChargeSchedule {
    String startTime, state;
  }
  static class Climate {
    double temperature;
    boolean isOn;
  }
  static class CollisionSensor {
    boolean isTriggered;
  }
  static class Compass {
    double heading;
  }
  static class CruiseControl {
    double speed, followDistance;
    boolean isOn;
  }
  static class Dimension {
    double height, width, length, weight;
  }
  static class Doors {
    Door[] doors;
  }
  static class Door {
    String location;
    boolean isOpen;
  }
  static class SafetyLocks {
    SafetyLock[] safetyLocks;
  }
  static class SafetyLock {
    String location;
    boolean isLocked;
  }
  static class DriveMode {
    String mode;
  }
  static class Engine {
    String state;
  }
  static class EngineCoolant {
    double level, temperature;
  }
  static class EngineHood {
    boolean isOpen;
  }
  static class EngineOil {
    boolean changeIndicator;
    double percentRemaining, lifeRemaining, pressure, temperature;
  }
  static class EngineThrottle {
    double percentOpen;
  }
  static class Fuel {
    double range, percentRemaining;
  }
  static class HazardLight {
    boolean isOn;
  }
  static class Headlight {
    String state;
  }
  static class InteriorLights {
    InteriorLight[] lights;
  }
  static class InteriorLight {
    String location;
    boolean isOn;
  }
  static class TurnIndicator {
    String state;
  }
  static class Location {
    double latitude, longitude, accuracy;
  }
  static class Mirrors {
    Mirror[] mirrors;
  }
  static class Mirror {
    String location;
    double xTilt, yTilt;
    public Mirror(String location, double xTilt, double yTilt){
      this.location = location;
      this.xTilt = xTilt;
      this.yTilt = yTilt;
    }
  }
  static class Odometer {
    double distance;
  }
  static class TripOdometers {
    TripOdometer[] trips;
  }
  static class TripOdometer {
    String label;
    double distance;
  }
  /* pedals/{accelerator,brake} */
  static class Pedal {
    double percentDepressed;
  }
  static class RainSensor {
    boolean isRaining;
  }
  static class Seats {
    Seat[] seats;
  }
  static class Seat {
    String location;
    boolean isOccupied, isBuckled;
  }
  static class Security {
    boolean isLocked;
  }
  static class SliBattery {
    double current, voltage, percentRemaining;
  }
  /* speedometer, tachometer */
  static class Gauge {
    double speed;
  }
  static class SteeringWheel {
    String location;
    double angle;
  }
  static class Sunroof {
    String state;
    double percentOpen;
  }
  static class Temperature {
    double inside, outside;
  }
  static class Tires {
    Tire[] tires;
  }
  static class Tire {
    String location;
    double pressure;
  }
  static class Transmission {
    String type, state;
  }
  static class TransmissionFluid {
    double temperature, wear;
  }
  /* trunks/{front,rear} */
  static class Trunk {
    boolean isOpen;
  }
  static class Vin {
    String vin;
  }
  static class WasherFluid {
    double percentRemaining;
  }
  static class Wheels {
    Wheel[] wheels;
  }
  static class Wheel {
    String location;
    double diameter;
  }
  static class WheelSpeeds {
    WheelSpeed[] wheelSpeed;
  }
  static class WheelSpeed {
    String location;
    double speed;
  }
  static class Windows {
    Window[] windows;
  }
  static class Window {
    String location;
    boolean isLocked;
    double percentOpen;
    public Window(String location, boolean isLocked, double percentOpen){
      this.location = location;
      this.isLocked = isLocked;
      this.percentOpen = percentOpen;
    }
  }
  static class Yaw {
    double rate;
  }
  static class GenericAction {
    String action;
    public GenericAction(String action){
      this.action = action;
    }
  }
  static class ChargeLimitAction {
    String action;
    double limit;
    public ChargeLimitAction(String action, double limit){
      this.action = action;
      this.limit = limit;
    }
  }
  static class ChargeScheduleAction {
    String action;
    String startTime;
    public ChargeScheduleAction(String action, String startTime){
      this.action = action;
      this.startTime = startTime;
    }
  }
  static class ClimateAction {
    String action;
    double temperature;
    public ClimateAction(String action, double temperature){
      this.action = action;
      this.temperature = temperature;
    }
  }
  static class MirrorAction {
    String action;
    Mirror[] mirrors;
    public MirrorAction(String action, Mirror[] mirrors){
      this.action = action;
      this.mirrors = mirrors;
    }
  }
  static class WindowAction {
    String action;
    Window[] windows;
    public WindowAction(String action, Window[] windows){
      this.action = action;
      this.windows = windows;
    }
  }
}