package com.smartcar.sdk;

public final class Api {
  public static class Paging {
    int count;
    int offset;
  }
  public static class Permissions {
    String[] permissions;
    Paging paging;
  }
  public static class Vehicles {
    String[] vehicles;
    Paging paging;
  }
  public static class Info {
    String id, make, model;
    int year;
  }
  public static class Accelerometer {
    double x, y, z;
  }
  public static class Airbags {
    Airbag[] airbags;
  }
  public static class Airbag {
    String location;
    boolean isActive, isDeployed;
  }
  public static class Barometer {
    double pressure;
  }
  public static class Battery {
    double range, percentRemaining;
  }
  public static class Charge {
    boolean isPluggedIn;
    String state;
  }
  public static class ChargeLimit {
    double limit;
    String state;
  }
  public static class ChargeSchedule {
    String startTime, state;
  }
  public static class Climate {
    double temperature;
    boolean isOn;
  }
  public static class CollisionSensor {
    boolean isTriggered;
  }
  public static class Compass {
    double heading;
  }
  public static class CruiseControl {
    double speed, followDistance;
    boolean isOn;
  }
  public static class Dimension {
    double height, width, length, weight;
  }
  public static class Doors {
    Door[] doors;
  }
  public static class Door {
    String location;
    boolean isOpen;
  }
  public static class SafetyLocks {
    SafetyLock[] safetyLocks;
  }
  public static class SafetyLock {
    String location;
    boolean isLocked;
  }
  public static class DriveMode {
    String mode;
  }
  public static class Engine {
    String state;
  }
  public static class EngineCoolant {
    double level, temperature;
  }
  public static class EngineHood {
    boolean isOpen;
  }
  public static class EngineOil {
    boolean changeIndicator;
    double percentRemaining, lifeRemaining, pressure, temperature;
  }
  public static class EngineThrottle {
    double percentOpen;
  }
  public static class Fuel {
    double range, percentRemaining;
  }
  public static class HazardLight {
    boolean isOn;
  }
  public static class Headlight {
    String state;
  }
  public static class InteriorLights {
    InteriorLight[] lights;
  }
  public static class InteriorLight {
    String location;
    boolean isOn;
  }
  public static class TurnIndicator {
    String state;
  }
  public static class Location {
    double latitude, longitude, accuracy;
  }
  public static class Mirrors {
    Mirror[] mirrors;
  }
  public static class Mirror {
    String location;
    double xTilt, yTilt;
    public Mirror(String location, double xTilt, double yTilt){
      this.location = location;
      this.xTilt = xTilt;
      this.yTilt = yTilt;
    }
  }
  public static class Odometer {
    double distance;
  }
  public static class TripOdometers {
    TripOdometer[] trips;
  }
  public static class TripOdometer {
    String label;
    double distance;
  }
  /* pedals/{accelerator,brake} */
  public static class Pedal {
    double percentDepressed;
  }
  public static class RainSensor {
    boolean isRaining;
  }
  public static class Seats {
    Seat[] seats;
  }
  public static class Seat {
    String location;
    boolean isOccupied, isBuckled;
  }
  public static class Security {
    boolean isLocked;
  }
  public static class SliBattery {
    double current, voltage, percentRemaining;
  }
  /* speedometer, tachometer */
  public static class Gauge {
    double speed;
  }
  public static class SteeringWheel {
    String location;
    double angle;
  }
  public static class Sunroof {
    String state;
    double percentOpen;
  }
  public static class Temperature {
    double inside, outside;
  }
  public static class Tires {
    Tire[] tires;
  }
  public static class Tire {
    String location;
    double pressure;
  }
  public static class Transmission {
    String type, state;
  }
  public static class TransmissionFluid {
    double temperature, wear;
  }
  /* trunks/{front,rear} */
  public static class Trunk {
    boolean isOpen;
  }
  public static class Vin {
    String vin;
  }
  public static class WasherFluid {
    double percentRemaining;
  }
  public static class Wheels {
    Wheel[] wheels;
  }
  public static class Wheel {
    String location;
    double diameter;
  }
  public static class WheelSpeeds {
    WheelSpeed[] wheelSpeed;
  }
  public static class WheelSpeed {
    String location;
    double speed;
  }
  public static class Windows {
    Window[] windows;
  }
  public static class Window {
    String location;
    boolean isLocked;
    double percentOpen;
    public Window(String location, boolean isLocked, double percentOpen){
      this.location = location;
      this.isLocked = isLocked;
      this.percentOpen = percentOpen;
    }
  }
  public static class Yaw {
    double rate;
  }
  public static class GenericAction {
    String action;
    public GenericAction(String action){
      this.action = action;
    }
  }
  public static class ChargeLimitAction {
    String action;
    double limit;
    public ChargeLimitAction(String action, double limit){
      this.action = action;
      this.limit = limit;
    }
  }
  public static class ChargeScheduleAction {
    String action;
    String startTime;
    public ChargeScheduleAction(String action, String startTime){
      this.action = action;
      this.startTime = startTime;
    }
  }
  public static class ClimateAction {
    String action;
    double temperature;
    public ClimateAction(String action, double temperature){
      this.action = action;
      this.temperature = temperature;
    }
  }
  public static class MirrorAction {
    String action;
    Mirror[] mirrors;
    public MirrorAction(String action, Mirror[] mirrors){
      this.action = action;
      this.mirrors = mirrors;
    }
  }
  public static class WindowAction {
    String action;
    Window[] windows;
    public WindowAction(String action, Window[] windows){
      this.action = action;
      this.windows = windows;
    }
  }
}