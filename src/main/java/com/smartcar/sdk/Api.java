package com.smartcar.sdk;

import java.util.ArrayList;

final public class Api {

  private final String response;
  public Api(String response){
    this.response = response;
  }

  public ArrayList<String> getNestedArray(String key){
    return null;
  }

  static class Info {
    String id, make, model;
    int year;
  }

  static class Accelerometer {
    double x, y, z;
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
    String heading;
  }

  static class CruiseControl {
    double speed, followDistance;
    boolean isOn;
  }

  static class Dimension {
    double height, width, length, weight;
  }

  static class Door {
    String location;
    boolean isOpen;
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

  static class Mirror {
    String location;
    double xTilt, yTilt;
  }

  static class Odometer {
    double distance;
  }

  static class TripOdometer {
    String label;
    double distance;
  }

  static class AcceleratorPedal {
    double percentDepressed;
  }

  static class BrakePedal {
    double percentDepressed;
  }

  static class RainSensor {
    boolean isRaining;
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

  static class Speedometer {
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

  static class Tachometer {
    double speed;
  }

  static class Temperature {
    double inside, outside;
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

  static class FrontTrunk {
    boolean isOpen;
  }

  static class RearTrunk {
    boolean isOpen;
  }

  static class Vin {
    String vin;
  }

  static class WasherFluid {
    double percentRemaining;
  }

  static class Wheel {
    String location;
    double diameter;
  }

  static class WheelSpeed {
    String location;
    double speed;
  }

  static class Window {
    String location;
    boolean isLocked;
    double percentOpen;
  }

  static class Yaw {
    double rate;
  } 
}