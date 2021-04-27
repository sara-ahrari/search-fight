# Search Fight :mag_right:

The application is written in java using the Spring Framework. 

## Initial setup 

Add your API keys for your Google and Bing search engines to the application.yaml file under resources. 

## How to build, test and run 

### Build 

To build the application run the following command in the project root:

```
$ mvn install
```
To ensure that the build target is removed before a new build, add the `clean` target.

```
$ mvn clean install
```

### Test
To run the tests write the following command in the project root:

```
$ mvn test
```
### Run 

Run the jar file searchfight.jar as follow: 

```
$ java -jar searchfight.jar term1 term2 ...
```

## Notes

The search engines used in this application are Google and Bing. Since the free versions are used limitations exist. 

- The Google engine offers only 500 calls/day. 
- The Bing engine offers 3 calls/sec and up to 1k calls/month.






