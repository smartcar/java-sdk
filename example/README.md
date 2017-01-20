# Instructions

## Initial

It is likely that in order for you to run this example, you will need to open it on its own in Intellij (or IDE of choice), as opposed
to opening the sdk and trying to run the example as a nested project.

This example depends on the smartcar sdk. As this is not publicly available on maven yet,
you will need to install the jar manually. To do so, follow these steps:

* Run `gradle jar` on the main java-sdk. This will build a jar to `java-sdk/build/libs`
* Take the jar and put it into `example/libs`
* The `build.gradle` file should include the following line:
```
compile files('libs/smartcar-sdk-0.0.1.jar')
```
If it does not, be sure to add it in dependencies.

You should now be set!

## Main file

This website sets up a simple landing page, a login screen, and a dashboard for seeing connected cars. 

At the top of the file, you will see that we create a Smartcar client. Here, you should put your own client_id and client_secret
from an application you create on developer.smartcar.com. Also, make sure that on developer.smartcar.com you add http:localhost:5000/callback as a whitelisted redirect URI.

## Running Project

Now, run the main folder and the Spark Java framework should start a web server. Open up localhost:5000 and press login. You can provide anything to login credentials, as the authentication is simply for show.

You should now reach the dashboard. Here, you will see buttons for connecting with car manufacturers. We have used `client.getAuthUrl('MOCK')` to populate the url that the MOCK button links to, and likewise for the others. Pressing these will take the user to smartcar authentication. If you press 'MOCK', then any login with the email ending in '@random.com' will pass and give you three dummy vehicles. Once you authenticate, it redirects to the redirect URI you gave the client, which in this case is our '/callback' endpoint. When it hits this endpoint with an authentication code, we exchange this for an access object and fetch all the vehicles the user has authenticated. You should now see vehicles being rendered in the dashboard!
