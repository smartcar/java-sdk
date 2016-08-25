package com.smartcar.sdk;

import okhttp3.Request;
import com.google.gson.Gson;

public class Vehicle {

  private final String token, vehicleId;
  private VehicleRequest api;
  private final Gson gson = new Gson();

  public Vehicle(String token, String vehicleId) {
      this.token = token;
      this.vehicleId = vehicleId;
      this.api = new VehicleRequest(token, vehicleId);
  }

  public String getVid(){
    return this.vehicleId;
  }

  public String getToken(){
    return this.token;
  }

  private String makeAction(String action){
    return gson.toJson(new Api.GenericAction(action));
  }

  void setBaseUrl(String url){
    this.api.setBaseUrl(url);
  }

  /*
  Get Intents: These request data from the API and return json
  */

  public String[] permissions() 
  throws Exceptions.SmartcarException {
    String json = api.permissions();
    return gson.fromJson(json, Api.Permissions.class).permissions;
  }
  public String[] permissions(int limit, int offset)
  throws Exceptions.SmartcarException {
    String json = api.permissions(limit, offset);
    return gson.fromJson(json, Api.Permissions.class).permissions;
  }
  public Api.Info info() 
  throws Exceptions.SmartcarException {
    String json = api.get("");
    return gson.fromJson(json, Api.Info.class);
  }
  public Api.Accelerometer accelerometer() 
  throws Exceptions.SmartcarException {
    String json = api.get("accelerometer");
    return gson.fromJson(json, Api.Accelerometer.class);
  }
  public Api.Airbag[] airbags() 
  throws Exceptions.SmartcarException {
    String json = api.get("airbags");
    return gson.fromJson(json, Api.Airbags.class).airbags;
  }
  public Api.Barometer barometer() 
  throws Exceptions.SmartcarException {
    String json = api.get("barometer");
    return gson.fromJson(json, Api.Barometer.class);
  }
  public Api.Battery battery() 
  throws Exceptions.SmartcarException {
    String json = api.get("battery");
    return gson.fromJson(json, Api.Battery.class);
  }
  public Api.Charge charge() 
  throws Exceptions.SmartcarException {
    String json = api.get("charge");
    return gson.fromJson(json, Api.Charge.class);
  }
  public Api.ChargeLimit chargeLimit() 
  throws Exceptions.SmartcarException {
    String json = api.get("charge/limit");
    return gson.fromJson(json, Api.ChargeLimit.class);
  }
  public Api.ChargeSchedule chargeSchedule()
  throws Exceptions.SmartcarException {
    String json = api.get("charge/schedule");
    return gson.fromJson(json, Api.ChargeSchedule.class);
  }
  public Api.Climate climate()
  throws Exceptions.SmartcarException {
    String json = api.get("climate");
    return gson.fromJson(json, Api.Climate.class);
  }
  public Api.CollisionSensor collisionSensor() 
  throws Exceptions.SmartcarException {
    String json = api.get("collision_sensor");
    return gson.fromJson(json, Api.CollisionSensor.class);
  }
  public Api.Compass compass() 
  throws Exceptions.SmartcarException {
    String json = api.get("compass");
    return gson.fromJson(json, Api.Compass.class);
  }
  public Api.CruiseControl cruiseControl()
  throws Exceptions.SmartcarException {
    String json = api.get("cruise_control");
    return gson.fromJson(json, Api.CruiseControl.class);
  }
  public Api.Dimension dimension() 
  throws Exceptions.SmartcarException {
    String json = api.get("dimension");
    return gson.fromJson(json, Api.Dimension.class);
  } 
  public Api.Door[] doors() 
  throws Exceptions.SmartcarException {
    String json = api.get("doors");
    return gson.fromJson(json, Api.Doors.class).doors;
  }
  public Api.SafetyLock[] safetyLocks() 
  throws Exceptions.SmartcarException {
    String json = api.get("safety_locks");
    return gson.fromJson(json, Api.SafetyLocks.class).safetyLocks;
  }
  public Api.DriveMode driveMode() 
  throws Exceptions.SmartcarException {
    String json = api.get("drive_mode");
    return gson.fromJson(json, Api.DriveMode.class);
  }
  public Api.Engine engine() 
  throws Exceptions.SmartcarException {
    String json = api.get("engine");
    return gson.fromJson(json, Api.Engine.class);
  }
  public Api.EngineCoolant engineCoolant() 
  throws Exceptions.SmartcarException {
    String json = api.get("engine/coolant");
    return gson.fromJson(json, Api.EngineCoolant.class);
  }
  public Api.EngineHood engineHood() 
  throws Exceptions.SmartcarException {
    String json = api.get("engine/hood");
    return gson.fromJson(json, Api.EngineHood.class);
  }
  public Api.EngineOil engineOil() 
  throws Exceptions.SmartcarException {
    String json = api.get("engine/oil");
    return gson.fromJson(json, Api.EngineOil.class);
  }
  public Api.EngineThrottle engineThrottle() 
  throws Exceptions.SmartcarException {
    String json = api.get("engine/throttle");
    return gson.fromJson(json, Api.EngineThrottle.class);
  }
  public Api.Fuel fuel() 
  throws Exceptions.SmartcarException {
    String json = api.get("fuel");
    return gson.fromJson(json, Api.Fuel.class);
  }
  public Api.HazardLight hazardLights() 
  throws Exceptions.SmartcarException {
    String json = api.get("lights/hazard");
    return gson.fromJson(json, Api.HazardLight.class);
  }
  public Api.Headlight headlights() 
  throws Exceptions.SmartcarException {
    String json = api.get("lights/headlight");
    return gson.fromJson(json, Api.Headlight.class);
  }
  public Api.InteriorLight[] interiorLights() 
  throws Exceptions.SmartcarException {
    String json = api.get("lights/interior");
    return gson.fromJson(json, Api.InteriorLights.class).lights;
  }
  public Api.TurnIndicator turnIndicator() 
  throws Exceptions.SmartcarException {
    String json = api.get("lights/turn_indicator");
    return gson.fromJson(json, Api.TurnIndicator.class);
  }
  public Api.Location location() 
  throws Exceptions.SmartcarException {
    String json = api.get("location");
    return gson.fromJson(json, Api.Location.class);
  }
  public Api.Mirror[] mirrors() 
  throws Exceptions.SmartcarException {
    String json = api.get("mirrors");
    return gson.fromJson(json, Api.Mirrors.class).mirrors;
  }
  public Api.Odometer odometer() 
  throws Exceptions.SmartcarException {
    String json = api.get("odometer");
    return gson.fromJson(json, Api.Odometer.class);
  }
  public Api.TripOdometer[] tripOdometers() 
  throws Exceptions.SmartcarException {
    String json = api.get("odometer/trip");
    return gson.fromJson(json, Api.TripOdometers.class).trips;
  }
  public Api.Pedal acceleratorPedal() 
  throws Exceptions.SmartcarException {
    String json = api.get("pedals/accelerator");
    return gson.fromJson(json, Api.Pedal.class);
  }
  public Api.Pedal brakePedal() 
  throws Exceptions.SmartcarException {
    String json = api.get("pedals/brake");
    return gson.fromJson(json, Api.Pedal.class);
  }
  public Api.RainSensor rainSensor() 
  throws Exceptions.SmartcarException {
    String json = api.get("rain_sensor");
    return gson.fromJson(json, Api.RainSensor.class);
  }
  public Api.Seat[] seats() 
  throws Exceptions.SmartcarException {
    String json = api.get("seats");
    return gson.fromJson(json, Api.Seats.class).seats;
  }
  public Api.Security security() 
  throws Exceptions.SmartcarException {
    String json = api.get("security");
    return gson.fromJson(json, Api.Security.class);
  }
  public Api.SliBattery sliBattery() 
  throws Exceptions.SmartcarException {
    String json = api.get("sli_battery");
    return gson.fromJson(json, Api.SliBattery.class);
  }
  public Api.Gauge speedometer() 
  throws Exceptions.SmartcarException {
    String json = api.get("speedometer");
    return gson.fromJson(json, Api.Gauge.class);
  }
  public Api.SteeringWheel steeringWheel() 
  throws Exceptions.SmartcarException {
    String json = api.get("steering_wheel");
    return gson.fromJson(json, Api.SteeringWheel.class);
  }
  public Api.Sunroof sunroof() 
  throws Exceptions.SmartcarException {
    String json = api.get("sunroof");
    return gson.fromJson(json, Api.Sunroof.class);
  }
  public Api.Gauge tachometer() 
  throws Exceptions.SmartcarException {
    String json = api.get("tachometer");
    return gson.fromJson(json, Api.Gauge.class);
  }
  public Api.Temperature temperature() 
  throws Exceptions.SmartcarException {
    String json = api.get("temperature");
    return gson.fromJson(json, Api.Temperature.class);
  }
  public Api.Tire[] tires() 
  throws Exceptions.SmartcarException {
    String json = api.get("tires");
    return gson.fromJson(json, Api.Tires.class).tires;
  }
  public Api.Transmission transmission() 
  throws Exceptions.SmartcarException {
    String json = api.get("transmission");
    return gson.fromJson(json, Api.Transmission.class);
  }
  public Api.TransmissionFluid transmissionFluid() 
  throws Exceptions.SmartcarException {
    String json = api.get("transmission/fluid");
    return gson.fromJson(json, Api.TransmissionFluid.class);
  }
  public Api.Trunk frontTrunk() 
  throws Exceptions.SmartcarException {
    String json = api.get("trunks/front");
    return gson.fromJson(json, Api.Trunk.class);
  }
  public Api.Trunk rearTrunk() 
  throws Exceptions.SmartcarException {
    String json = api.get("trunks/rear");
    return gson.fromJson(json, Api.Trunk.class);
  }
  public Api.Vin vin() 
  throws Exceptions.SmartcarException {
    String json = api.get("vin");
    return gson.fromJson(json, Api.Vin.class);
  }
  public Api.WasherFluid washerFluid() 
  throws Exceptions.SmartcarException {
    String json = api.get("washer_fluid");
    return gson.fromJson(json, Api.WasherFluid.class);
  }
  public Api.Wheel[] wheels() 
  throws Exceptions.SmartcarException {
    String json = api.get("wheels");
    return gson.fromJson(json, Api.Wheels.class).wheels;
  }
  public Api.WheelSpeed[] wheelSpeeds() 
  throws Exceptions.SmartcarException {
    String json = api.get("wheels/speed");
    return gson.fromJson(json, Api.WheelSpeeds.class).wheelSpeed;
  }
  public Api.Window[] windows() 
  throws Exceptions.SmartcarException {
    String json = api.get("windows");
    return gson.fromJson(json, Api.Windows.class).windows;
  }
  public Api.Yaw yaw() 
  throws Exceptions.SmartcarException {
    String json = api.get("yaw");
    return gson.fromJson(json, Api.Yaw.class);
  }

  /*
  Action Intents: These send data to the API, and only 
  return a status code
  */
 
  public void disconnect() 
  throws Exceptions.SmartcarException {
    api.disconnect();
  }
  public void startCharging() 
  throws Exceptions.SmartcarException {
    api.action("charge", makeAction("START"));
  }
  public void stopCharging() 
  throws Exceptions.SmartcarException {
    api.action("charge", makeAction("STOP"));
  }
  public void enableChargeLimit() 
  throws Exceptions.SmartcarException {
    api.action("charge/limit", makeAction("ENABLE"));
  }
  public void enableChargeLimit(double limit) 
  throws Exceptions.SmartcarException {
    String body = gson.toJson(
      new Api.ChargeLimitAction("ENABLE", limit)
    );
    api.action("charge/limit", body);
  }
  public void disableChargeLimit() 
  throws Exceptions.SmartcarException {
    api.action("charge/limit", makeAction("DISABLE"));
  }
  public void enableChargeSchedule() 
  throws Exceptions.SmartcarException {
    api.action("charge/schedule", makeAction("ENABLE"));
  }
  public void enableChargeSchedule(String time) 
  throws Exceptions.SmartcarException {
    String body = gson.toJson(
      new Api.ChargeScheduleAction("ENABLE", time)
    );
    api.action("charge/schedule", body);
  }
  public void disableChargeSchedule() 
  throws Exceptions.SmartcarException {
    api.action("charge/schedule", makeAction("DISABLE"));
  }
  public void startClimate() 
  throws Exceptions.SmartcarException {
    api.action("climate", makeAction("START"));
  }
  public void startClimate(double temp) 
  throws Exceptions.SmartcarException {
    String body = gson.toJson(
      new Api.ClimateAction("START", temp)
    );
    api.action("climate", body);
  }
  public void stopClimate() 
  throws Exceptions.SmartcarException {
    api.action("climate", makeAction("STOP"));
  }
  public void startEngine() 
  throws Exceptions.SmartcarException {
    api.action("engine", makeAction("START"));
  }
  public void stopEngine() 
  throws Exceptions.SmartcarException {
    api.action("engine", makeAction("STOP"));
  }
  public void turnEngineOn() 
  throws Exceptions.SmartcarException {
    api.action("engine", makeAction("ON"));
  }
  public void turnEngineAC1() 
  throws Exceptions.SmartcarException {
    api.action("engine", makeAction("ACCESSORY_1"));
  }
  public void turnEngineAC2() 
  throws Exceptions.SmartcarException {
    api.action("engine", makeAction("ACCESSORY_2"));
  }
  public void openHood() 
  throws Exceptions.SmartcarException {
    api.action("engine/hood", makeAction("OPEN"));
  }
  public void closeHood()
  throws Exceptions.SmartcarException {
    api.action("engine/hood", makeAction("CLOSE"));
  }
  public void honkHorn() 
  throws Exceptions.SmartcarException {
    api.action("horn", makeAction("HONK"));
  }
  public void flashHeadlights() 
  throws Exceptions.SmartcarException {
    api.action("lights/headlights", makeAction("FLASH"));
  }
  public void adjustMirrors(Api.Mirror[] mirrors) 
  throws Exceptions.SmartcarException {
    String body = gson.toJson(
      new Api.MirrorAction("TILT", mirrors)
    );
    api.action("mirrors", body);
  }
  public void startPanic() 
  throws Exceptions.SmartcarException {
    api.action("panic", makeAction("START"));
  }
  public void stopPanic() 
  throws Exceptions.SmartcarException {
    api.action("panic", makeAction("STOP"));
  }
  public void openChargePort() 
  throws Exceptions.SmartcarException {
    api.action("ports/charge", makeAction("OPEN"));
  }
  public void closeChargePort() 
  throws Exceptions.SmartcarException {
    api.action("ports/charge", makeAction("CLOSE"));
  }
  public void openFuelPort() 
  throws Exceptions.SmartcarException {
    api.action("ports/fuel", makeAction("OPEN"));
  }
  public void closeFuelPort() 
  throws Exceptions.SmartcarException {
    api.action("ports/fuel", makeAction("CLOSE"));
  }
  public void lock() 
  throws Exceptions.SmartcarException {
    api.action("security", makeAction("LOCK"));
  }
  public void unlock() 
  throws Exceptions.SmartcarException {
    api.action("security", makeAction("UNLOCK"));
  }
  public void openSunroof() 
  throws Exceptions.SmartcarException {
    api.action("sunroof", makeAction("OPEN"));
  }
  public void ventSunroof() 
  throws Exceptions.SmartcarException {
    api.action("sunroof", makeAction("VENT"));
  }
  public void closeSunroof() 
  throws Exceptions.SmartcarException {
    api.action("sunroof", makeAction("CLOSE"));
  }
  public void openFrontTrunk() 
  throws Exceptions.SmartcarException {
    api.action("trunks/front", makeAction("OPEN"));
  }
  public void closeFrontTrunk() 
  throws Exceptions.SmartcarException {
    api.action("trunks/front", makeAction("CLOSE"));
  }
  public void openRearTrunk() 
  throws Exceptions.SmartcarException {
    api.action("trunks/rear", makeAction("OPEN"));
  }
  public void closeRearTrunk() 
  throws Exceptions.SmartcarException {
    api.action("trunks/rear", makeAction("CLOSE"));
  }
  public void openWindows(Api.Window[] windows) 
  throws Exceptions.SmartcarException {
    String body = gson.toJson(
      new Api.WindowAction("OPEN", windows)
    );
    api.action("windows", body);
  }
  public void closeWindows(Api.Window[] windows) 
  throws Exceptions.SmartcarException {
    String body = gson.toJson(
      new Api.WindowAction("CLOSE", windows)
    );
    api.action("windows", body);
  }
  public void lockWindows(Api.Window[] windows) 
  throws Exceptions.SmartcarException {
    String body = gson.toJson(
      new Api.WindowAction("LOCK", windows)
    );
    api.action("windows", body);
  }
  public void unlockWindows(Api.Window[] windows) 
  throws Exceptions.SmartcarException {
    String body = gson.toJson(
      new Api.WindowAction("UNLOCK", windows)
    );
    api.action("windows", body);
  }
}
