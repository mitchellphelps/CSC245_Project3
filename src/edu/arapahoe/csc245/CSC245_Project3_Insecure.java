package edu.arapahoe.csc245;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.*;
import com.google.gson.reflect.*;

import static java.lang.System.in;

////////////////////////////////////////////////////////////////////////////////////
//
// This program was created for Arapahoe Community College's CSC-245 course and
// identifies the current temperature for a location using the Open Weather Map API.
//
// The use of the API (openweathermap.org) was applied for and access granted 202010321
// The key comes with several technical constraints regarding its usage, including:
//     Hourly forecast: unavailable
//     Daily forecast: unavailable
//     Calls per minute: 60
//     3 hour forecast: 5 days
//
// Details on the use of the API can be found here:
//     https://openweathermap.org/current
//
// The default location is Castle Rock, CO (encoded as Castle Rock, US) but can be
// changed, as required. The GPS coordinates for Castle Rock, CO is
// latitude 39.3722        longitude -104.8561
//
// CSC 245 Secure Software Development
//
// Change log:
//      20210321 API access granted
//      20210322 Initially created (ddl)
//
// Dependencies:
//      gson-2.2.2.jar is needed for correct functioning
//		Oracle JDK 1.8
//
////////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////////
//
// Modified By Mitchell Phelps
// Date Modified: March 20, 2022
// Modifications: Changed code to ensure proper security of program.
//
/////////////////////////////////////////////////////////////////////////////////////
public class CSC245_Project3_Insecure {

	// Java Maps are used with many API interactions. OpenWeatherMap also uses Java Maps.
	public static Map<String, Object> jsonToMap(String str) {
		Map<String, Object> map = new Gson().fromJson(
				str, new TypeToken<HashMap<String, Object>>() {}.getType()
				);
		return map;
	}

	public static String getTempForCity (String cityString, String api_key) {
		String urlString = "http://api.openweathermap.org/data/2.5/weather?q=" +
				cityString + "&appid=" + api_key + "&units=imperial";
		try {
			StringBuilder result = new StringBuilder();
			URL url = new URL(urlString);
			URLConnection conn = url.openConnection();
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			// No proper cleanup at program termination.
			// VIOLATION: FIO14-J. Perform proper cleanup at program termination.
			// FIX: Add a new thread that allows for the program to return properly in the event
			// of an unexpected shutdown.
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				// Log shutdown and close all resources
				try {
					rd.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}));
			String line;
			// The while loop is not using braces.
			// VIOLATION: Use braces for the body of an if, for, or while statement.
			// FIX: Add braces.
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			System.out.println(result);

			Map<String, Object > respMap = jsonToMap (result.toString());
			Map<String, Object > mainMap = jsonToMap (respMap.get("main").toString());

			return mainMap.get("temp").toString();

		} catch (IOException e){
			System.out.println(e.getMessage());
			return "Temp not available (API problem?)";
		}

	}

	public static void main(String[] args) {
		// Two variables are being declared in the same declaration.
		// VIOLATION: Do not declare more than one variable per declaration.
		// FIX: Create two separate declarations and add comments about variable usage.
		// VIOLATION: Use meaningful symbolic constants to represent literal values in program logic
		// FIX: Rename owm to API_KEY to allow for readability.
		String API_KEY = "API_KEY_HERE";	// Include the API key here.
		String LOCATION = "Castle Rock, US";					// Include location here.

		// This is not necessary because the urlString variable is not used in this scope.
		// VIOLATION: Minimize the scope of variables.
		// FIX: Remove the line and ensure that the declaration is inside the getTempForCity method.
		// String urlString = "http://api.openweathermap.org/data/2.5/weather?q=" + LOCATION +
				// "&appid=" + owm + "&units=imperial";

		// The following line is out of scope for mitigation and can be ignored.
        // System.out.println("URL invoked to get temperature data=" + urlString);

		// The for loop has a semicolon after it.
		// VIOLATION: Do not place a semicolon immediately following an if, for, or while condition.
		// FIX: Remove the semicolon and remove the for loop.
		// for (int i=0;i<10;i++)
			System.out.println("Current temperature in " + LOCATION +" is: "
					+ getTempForCity(LOCATION,API_KEY) + " degrees.");

		// urlString is set to null to try to help the garbage collector.
		// VIOLATION: Do not attempt to help the garbage collector by setting local reference variables to null.
		// FIX: Remove this line of code.
		// urlString = "";

	}

}
