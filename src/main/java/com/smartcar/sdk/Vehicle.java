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

  public Api.Permissions permissions()
  throws Exceptions.SmartcarException {
    String json = api.permissions();
    return gson.fromJson(json, Api.Permissions.class);
  }
  public Api.Permissions permissions(int limit, int offset)
  throws Exceptions.SmartcarException {
    String json = api.permissions(limit, offset);
    return gson.fromJson(json, Api.Permissions.class);
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
  public Api.Airbags airbags()
  throws Exceptions.SmartcarException {
    String json = api.get("airbags");
    return gson.fromJson(json, Api.Airbags.class);
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
  public Api.Dimensions dimensions()
  throws Exceptions.SmartcarException {
    String json = api.get("dimension");
    return gson.fromJson(json, Api.Dimensions.class);
  }
  public Api.Doors doors()
  throws Exceptions.SmartcarException {
    String json = api.get("doors");
    return gson.fromJson(json, Api.Doors.class);
  }
  public Api.SafetyLocks safetyLocks()
  throws Exceptions.SmartcarException {
    String json = api.get("doors/safety_locks");
    return gson.fromJson(json, Api.SafetyLocks.class);
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
  public Api.HazardLight hazardLight()
  throws Exceptions.SmartcarException {
    String json = api.get("lights/hazard");
    return gson.fromJson(json, Api.HazardLight.class);
  }
  public Api.Headlight headlight()
  throws Exceptions.SmartcarException {
    String json = api.get("lights/headlight");
    return gson.fromJson(json, Api.Headlight.class);
  }
  public Api.InteriorLights interiorLights()
  throws Exceptions.SmartcarException {
    String json = api.get("lights/interior");
    return gson.fromJson(json, Api.InteriorLights.class);
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
  public Api.Mirrors mirrors()
  throws Exceptions.SmartcarException {
    String json = api.get("mirrors");
    return gson.fromJson(json, Api.Mirrors.class);
  }
  public Api.Odometer odometer()
  throws Exceptions.SmartcarException {
    String json = api.get("odometer");
    return gson.fromJson(json, Api.Odometer.class);
  }
  public Api.TripOdometers tripOdometers()
  throws Exceptions.SmartcarException {
    String json = api.get("odometer/trip");
    return gson.fromJson(json, Api.TripOdometers.class);
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
  public Api.Seats seats()
  throws Exceptions.SmartcarException {
    String json = api.get("seats");
    return gson.fromJson(json, Api.Seats.class);
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
  public Api.Tires tires()
  throws Exceptions.SmartcarException {
    String json = api.get("tires");
    return gson.fromJson(json, Api.Tires.class);
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
  public Api.WheelSpeeds wheelSpeeds()
  throws Exceptions.SmartcarException {
    String json = api.get("wheels/speed");
    return gson.fromJson(json, Api.WheelSpeeds.class);
  }
  public Api.Windows windows()
  throws Exceptions.SmartcarException {
    String json = api.get("windows");
    return gson.fromJson(json, Api.Windows.class);
  }
  public Api.Yaw yaw()
  throws Exceptions.SmartcarException {
    String json = api.get("yaw");
    return gson.fromJson(json, Api.Yaw.class);
  }

  /*
  Action Intents: These send data to the API,
  and only return a status code
  */

  public Api.Success disconnect()
  throws Exceptions.SmartcarException {
    String json = api.disconnect();
    return gson.fromJson(json, Api.Success.class);
  }
  public Api.Success startCharging()
  throws Exceptions.SmartcarException {
    String json = api.action("charge", makeAction("START"));
    return gson.fromJson(json, Api.Success.class);
  }
  public Api.Success stopCharging()
  throws Exceptions.SmartcarException {
    String json = api.action("charge", makeAction("STOP"));
    return gson.fromJson(json, Api.Success.class);
  }
  public Api.Success enableChargeLimit()
  throws Exceptions.SmartcarException {
    String json = api.action("charge/limit", makeAction("ENABLE"));
    return gson.fromJson(json, Api.Success.class);
  }
  public Api.Success enableChargeLimit(double limit)
  throws Exceptions.SmartcarException {
    String body = gson.toJson(
      new Api.ChargeLimitAction("ENABLE", limit)
    );
    String json = api.action("charge/limit", body);
    return gson.fromJson(json, Api.Success.class);
  }
  public Api.Success disableChargeLimit()
  throws Exceptions.SmartcarException {
    String json = api.action("charge/limit", makeAction("DISABLE"));
    return gson.fromJson(json, Api.Success.class);
  }
  public Api.Success enableChargeSchedule()
  throws Exceptions.SmartcarException {
    String json = api.action("charge/schedule", makeAction("ENABLE"));
    return gson.fromJson(json, Api.Success.class);
  }
  public Api.Success enableChargeSchedule(String time)
  throws Exceptions.SmartcarException {
    String body = gson.toJson(
      new Api.ChargeScheduleAction("ENABLE", time)
    );
    String json = api.action("charge/schedule", body);
    return gson.fromJson(json, Api.Success.class);
  }
  public Api.Success disableChargeSchedule()
  throws Exceptions.SmartcarException {
    String json = api.action("charge/schedule", makeAction("DISABLE"));
    return gson.fromJson(json, Api.Success.class);
  }
  public Api.Success startClimate()
  throws Exceptions.SmartcarException {
    String json = api.action("climate", makeAction("START"));
    return gson.fromJson(json, Api.Success.class);
  }
  public Api.Success startClimate(double temp)
  throws Exceptions.SmartcarException {
    String body = gson.toJson(
      new Api.ClimateAction("START", temp)
    );
    String json = api.action("climate", body);
    return gson.fromJson(json, Api.Success.class);
  }
  public Api.Success stopClimate()
  throws Exceptions.SmartcarException {
    String json = api.action("climate", makeAction("STOP"));
    return gson.fromJson(json, Api.Success.class);
  }
  public Api.Success startEngine()
  throws Exceptions.SmartcarException {
    String json = api.action("engine", makeAction("START"));
    return gson.fromJson(json, Api.Success.class);
  }
  public Api.Success stopEngine()
  throws Exceptions.SmartcarException {
    String json = api.action("engine", makeAction("STOP"));
    return gson.fromJson(json, Api.Success.class);
  }
  public Api.Success turnEngineOn()
  throws Exceptions.SmartcarException {
    String json = api.action("engine", makeAction("ON"));
    return gson.fromJson(json, Api.Success.class);
  }
  public Api.Success turnEngineAC1()
  throws Exceptions.SmartcarException {
    String json = api.action("engine", makeAction("ACCESSORY_1"));
    return gson.fromJson(json, Api.Success.class);
  }
  public Api.Success turnEngineAC2()
  throws Exceptions.SmartcarException {
    String json = api.action("engine", makeAction("ACCESSORY_2"));
    return gson.fromJson(json, Api.Success.class);
  }
  public Api.Success openHood()
  throws Exceptions.SmartcarException {
    String json = api.action("engine/hood", makeAction("OPEN"));
    return gson.fromJson(json, Api.Success.class);
  }
  public Api.Success closeHood()
  throws Exceptions.SmartcarException {
    String json = api.action("engine/hood", makeAction("CLOSE"));
    return gson.fromJson(json, Api.Success.class);
  }
  public Api.Success honkHorn()
  throws Exceptions.SmartcarException {
    String json = api.action("horn", makeAction("HONK"));
    return gson.fromJson(json, Api.Success.class);
  }
  public Api.Success flashHeadlight()
  throws Exceptions.SmartcarException {
    String json = api.action("lights/headlight", makeAction("FLASH"));
    return gson.fromJson(json, Api.Success.class);
  }
  public Api.Success adjustMirrors(Api.Mirror[] mirrors)
  throws Exceptions.SmartcarException {
    String body = gson.toJson(
      new Api.MirrorAction("TILT", mirrors)
    );
    String json = api.action("mirrors", body);
    return gson.fromJson(json, Api.Success.class);
  }
  public Api.Success startPanic()
  throws Exceptions.SmartcarException {
    String json = api.action("panic", makeAction("START"));
    return gson.fromJson(json, Api.Success.class);
  }
  public Api.Success stopPanic()
  throws Exceptions.SmartcarException {
    String json = api.action("panic", makeAction("STOP"));
    return gson.fromJson(json, Api.Success.class);
  }
  public Api.Success openChargePort()
  throws Exceptions.SmartcarException {
    String json = api.action("ports/charge", makeAction("OPEN"));
    return gson.fromJson(json, Api.Success.class);
  }
  public Api.Success closeChargePort()
  throws Exceptions.SmartcarException {
    String json = api.action("ports/charge", makeAction("CLOSE"));
    return gson.fromJson(json, Api.Success.class);
  }
  public Api.Success openFuelPort()
  throws Exceptions.SmartcarException {
    String json = api.action("ports/fuel", makeAction("OPEN"));
    return gson.fromJson(json, Api.Success.class);
  }
  public Api.Success closeFuelPort()
  throws Exceptions.SmartcarException {
    String json = api.action("ports/fuel", makeAction("CLOSE"));
    return gson.fromJson(json, Api.Success.class);
  }
  public Api.Success lock()
  throws Exceptions.SmartcarException {
    String json = api.action("security", makeAction("LOCK"));
    return gson.fromJson(json, Api.Success.class);
  }
  public Api.Success unlock()
  throws Exceptions.SmartcarException {
    String json = api.action("security", makeAction("UNLOCK"));
    return gson.fromJson(json, Api.Success.class);
  }
  public Api.Success openSunroof()
  throws Exceptions.SmartcarException {
    String json = api.action("sunroof", makeAction("OPEN"));
    return gson.fromJson(json, Api.Success.class);
  }
  public Api.Success ventSunroof()
  throws Exceptions.SmartcarException {
    String json = api.action("sunroof", makeAction("VENT"));
    return gson.fromJson(json, Api.Success.class);
  }
  public Api.Success closeSunroof()
  throws Exceptions.SmartcarException {
    String json = api.action("sunroof", makeAction("CLOSE"));
    return gson.fromJson(json, Api.Success.class);
  }
  public Api.Success openFrontTrunk()
  throws Exceptions.SmartcarException {
    String json = api.action("trunks/front", makeAction("OPEN"));
    return gson.fromJson(json, Api.Success.class);
  }
  public Api.Success closeFrontTrunk()
  throws Exceptions.SmartcarException {
    String json = api.action("trunks/front", makeAction("CLOSE"));
    return gson.fromJson(json, Api.Success.class);
  }
  public Api.Success openRearTrunk()
  throws Exceptions.SmartcarException {
    String json = api.action("trunks/rear", makeAction("OPEN"));
    return gson.fromJson(json, Api.Success.class);
  }
  public Api.Success closeRearTrunk()
  throws Exceptions.SmartcarException {
    String json = api.action("trunks/rear", makeAction("CLOSE"));
    return gson.fromJson(json, Api.Success.class);
  }
  public Api.Success openWindows(Api.Window[] windows)
  throws Exceptions.SmartcarException {
    String body = gson.toJson(
      new Api.WindowAction("OPEN", windows)
    );
    String json = api.action("windows", body);
    return gson.fromJson(json, Api.Success.class);
  }
  public Api.Success closeWindows(Api.Window[] windows)
  throws Exceptions.SmartcarException {
    String body = gson.toJson(
      new Api.WindowAction("CLOSE", windows)
    );
    String json = api.action("windows", body);
    return gson.fromJson(json, Api.Success.class);
  }
  public Api.Success lockWindows(Api.Window[] windows)
  throws Exceptions.SmartcarException {
    String body = gson.toJson(
      new Api.WindowAction("LOCK", windows)
    );
    String json = api.action("windows", body);
    return gson.fromJson(json, Api.Success.class);
  }
  public Api.Success unlockWindows(Api.Window[] windows)
  throws Exceptions.SmartcarException {
    String body = gson.toJson(
      new Api.WindowAction("UNLOCK", windows)
    );
    String json = api.action("windows", body);
    return gson.fromJson(json, Api.Success.class);
  }
}
