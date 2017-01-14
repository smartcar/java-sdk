# Instructions

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
