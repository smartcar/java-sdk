/**
 * The Main program is intended as a simple example of how to use the Smartcar Java SDK to
 * have users authenticate and give your app permission to interact with their vehicles in
 * specified ways. This uses the Spark framework to start a web server and defines a few endpoints.
 * January 2017
 * @author Smartcar team
 * @author Robin Jayaswal
 */


import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Exchanger;
import java.util.concurrent.atomic.AtomicReference;

import com.smartcar.sdk.*;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;


import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;
import static spark.Spark.get;



public class Main {

    /**
     * Variables we pass to Smartcar constructor to create a client to interact with the SDK
     */
    static String ID = "INSERT_YOUR_CLIENT_ID_HERE";
    static String SECRET = "INSERT_YOUR_CLIENT_SECRET_HERE";
    static String REDIRECT_URI = "http://localhost:5000/callback";
    static String[] scope = { "read_vehicle_info", "read_location", "read_engine", "control_ignition"};

    static Smartcar client = new Smartcar(ID, SECRET, REDIRECT_URI, scope);

    /**
     * List of carmakers we wish to allow user to connect with. We will populate 'url' using client.getAuthUrl in the Main
     * function. We pass this list to a handlebars template which renders a button redirecting the user to the
     * corresponding url for authorization.
     */
    static OEM[] availableMakes = { new OEM("mock", "Mock", ""), new OEM("bmw", "BMW", "") };


    /**
     * Arrays used to hold data just for this sample app
     */
    static String[] sampleEmployees = { "John Doe", "Jane Doe", "None"};
    static Map<String, VehicleData> vehicleDataMap = new HashMap<>();


    /**
     * Starts the spark webserver which listens for requests to defined routes.
     *
     * The general flow of the app will be as such:
     * -The user will visit "/" endpoint and go to the homepage
     * -The user will press login and visit "/login" endpoint
     * -The user will submit login credentials, posting to "/login". We approve no matter what in this sample app
     * -User is redirected to "/dashboard" endpoint, where they will see buttons for each OEM defined in availableMakes
     * -Upon pressing a button, the user is sent to the url gotten from client.getAuthUrl(oem.name).toString()
     * -User authenticates, and auth.smartcar.com redirects to "/callback" endpoint (which we specified with REDIRECT_URI above)
     * -"/callback" handler exchanges authentication code for an access object, and uses this to get the user's vehicles and information on them
     * -"/callback" handler redirects to "/dashboard", where there are now cars and information to display to the user
     *
     * @param args
     */
    public static void main(String[] args) {
        // define port and location of static files
        port(5000);
        staticFiles.location("/public");

        // temporary storage for our access object we get when user authenticates (would store in db in real app)
        AtomicReference<Access> accessRef = new AtomicReference<>();

        // populate the oem objects with correct authentication urls
        for (OEM oem : availableMakes) {
            oem.url = client.getAuthUrl(oem.name).toString();
        }

        // landing page
        get("/", (req, res) -> new ModelAndView(null, "landing.hbs"), new HandlebarsTemplateEngine());


        // login render and handling
        get("/login", (req, res) -> new ModelAndView(null, "login.hbs"), new HandlebarsTemplateEngine());

        // handler when user presses login, in this example we do no authentication and redirect to dashboard
        post("/login", (req, res) -> {
            res.redirect("/dashboard");
            return null;
        });


        // map to pass to handlebars template engine for dashboard
        Map<String, Object> dashboardMap = new HashMap<>();
        dashboardMap.put("availableMakes", availableMakes);
        dashboardMap.put("carsConnected", false);
        dashboardMap.put("vehicleDataMap", vehicleDataMap);

        // main dashboard where user can connect cars and see connected cars.
        get("/dashboard", (req, res) -> new ModelAndView(dashboardMap, "dashboard.hbs"),new HandlebarsTemplateEngine());


        // our callback that we told smartcar to redirect to with REDIRECT_URI we passed. The user logs in (or rejects)
        // and smartcar redirects here with an auth code. We exchange the auth code for an access object, and use this
        // to get a list of the vehicles the user authorized.
        get("/callback", (req, res) -> {
            try {
                // get the authentication code from query string
                String code = req.queryMap().get("code").value();

                // exchange code for access object
                Access access = client.exchangeCode(code);

                // get ids of vehicles that user authorized with the access token
                Api.Vehicles vehicles = client.getVehicles(access.getAccessToken());

                // loop over vehicle ids and create a vehicle object, then obtain information on each vehicle
                for (Integer i = 0; i < vehicles.vehicles.length; i++) {
                    // get vehicle id and create vehicle object
                    String vehicleId = vehicles.vehicles[i];
                    Vehicle vehicle = new Vehicle(vehicleId, access.getAccessToken());

                    // get info, location, and engine status
                    Api.Info info = vehicle.info();
                    Api.Location location = vehicle.location();
                    Api.Engine engine = vehicle.engine();

                    // dummy data
                    String currentUser = sampleEmployees[i % sampleEmployees.length];

                    VehicleData vd = new VehicleData(vehicle, info, location, engine, currentUser);
                    vehicleDataMap.put(vehicleId, vd);
                }

                // store access object
                accessRef.set(access);

                // flip boolean in handlebars template
                if (vehicleDataMap.size() > 0) {
                    dashboardMap.put("carsConnected", true);
                }

            } catch (Exceptions.SmartcarException e) {
                System.err.println(e);
            }

            res.redirect("/dashboard");
            return null;
        });

        // endpoint that is hit when user presses 'Stop Engine' button next to a connected vehicle.
        post("/stop/:vid", (req, res) -> {
            // get the access object we saved in the callback
            Access access = accessRef.get();

            // renew the access object if it is expired
            if (access.expired()) {
                access = client.exchangeToken(access.getRefreshToken());
                accessRef.set(access);
            }

            // create vehicle object with id and access token, and turn off ignition
            Vehicle vehicle = new Vehicle(req.params(":vid"), access.getAccessToken());
            Api.Success result = vehicle.setIgnitionOff();

            // check the result of the setIgnitionOff call, and display success or error on button
            VehicleData currVehicle = vehicleDataMap.get(req.params(":vid"));
            String buttonMessage = result.status.equals("success") ? "Engine Stopped" : "Error";
            currVehicle.changeButtonMessage(buttonMessage);

            res.redirect("/dashboard");
            return null;
        });
    }
}

/**
 * Helper class to hold information on an OEM.
 */
class OEM {
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
class VehicleData {
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
        if (this.engine.isOn == "true") {
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