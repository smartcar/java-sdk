package com.smartcar.sdk.example;

import com.smartcar.sdk.Api;
import com.smartcar.sdk.Vehicle;

/**
 * Created by robinjayaswal on 1/17/2017.
 */
public class Helpers {
    /**
     * Helper class to hold information on an OEM.
     */
    public static class OEM {
        final String name, label;
        String url;

        /**
         * Constructor for OEM class
         *
         * @param name - the name of the OEM as defined by smartcar, i.e, "bmw"
         * @param label - whatever we want to display to the user to identify the OEM, i.e, "BMW!!!"
         * @param url - the url a user must visit to authenticate your app to use one or more vehicles of this OEM (obtained from client.getAuthUrl('bmw'))
         */
        public OEM(String name, String label, String url) {
            this.name = name;
            this.label = label;
            this.url = url;
        }
        public String getLabel() {
            return this.label;
        }
        public String getUrl() {
            return this.url;
        }
        public String getName() {
            return this.name;
        }
    }

    /**
     * Helper class to store all the data we get on a vehicle, such as the Vehicle object itself and its location.
     */
    public static class VehicleData {
        private Vehicle vehicle;
        private Api.Info info;
        private Api.Location location;
        private Api.Engine engine;
        private String currentUser;
        private String ignitionButtonText;

        /**
         * Constructor for VehicleData class
         * @param vehicle - object of Vehicle class. Represents the actual vehicle, and used to make calls on it, i.e, vehicle.location()
         * @param info - obtained from vehicle.info(), includes make, model, and id
         * @param location - obtained from vehicle.location()
         * @param engine - obtained frmo vehicle.engine()
         * @param currentUser - dummy data (in a real fleet management system, you'd have this in your own db)
         */
        public VehicleData(Vehicle vehicle, Api.Info info, Api.Location location, Api.Engine engine, String currentUser) {
            this.vehicle = vehicle;
            this.info = info;
            this.location = location;
            this.engine = engine;
            this.currentUser = currentUser;
            this.ignitionButtonText = "Stop Engine";
        }
        public Vehicle getVehicle() {
            return this.vehicle;
        }
        public String getVid() {
            return this.vehicle.getVid();
        }
        public Api.Info getInfo() {
            return this.info;
        }
        public Api.Location getLocation() {
            return this.location;
        }
        public double getLatitude() {
            return this.location.latitude;
        }
        public double getLongitude() {
            return this.location.longitude;
        }
        public String getName() {
            return this.info.make + " " + this.info.model;
        }
        public String getEngineState() {
            if (this.engine.isOn.equals("true")) {
                return "Running";
            } else {
                return "Off";
            }
        }
        public String getCurrentUser() {
            return this.currentUser;
        }
        public void changeButtonMessage(String message) {
            this.ignitionButtonText = message;
        }
        public String getIgnitionButtonText() {
            return this.ignitionButtonText;
        }
    }
}

