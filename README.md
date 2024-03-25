# Traffic Tracker

## Overview
Traffic Tracker is a Java application that provides services for tracking bus lines and stops.
It integrates with an external API to fetch data about bus lines and stops,
processes the data, and provides various services such as fetching the top 10 bus lines with the most stops.

## Features
- Fetch information about bus lines and stops from an external API
- Calculate and retrieve the top 10 bus lines with the most stops
- Provide detailed information about bus lines including their stops

## Technologies Used
- Java 17
- Spring Framework
- RESTful APIs
- Gradle

## Getting Started
To use Traffic Tracker, follow these steps:

1. Clone the repository: `git clone https://github.com/marwaGoda/TraficTracker.git`
2. Navigate to the project directory: `cd traffic-tracker`
3. Build the project:  `./gradlew build`
4. Run the application: `java -jar build/libs/TraficTracker-0.0.1-SNAPSHOT.jar`

## Usage
Once the application is running, you can use the provided APIs to fetch information about bus lines and stops. Here are the initial API endpoints:

- `GET /bus/top10lines` - Fetch the top 10 bus lines with the most stops
- `GET /bus/detailedLines` - Fetch the bus lines with their stops


## Configuration
The application uses external configuration properties for endpoints and file paths.
You can configure these properties in the `application.properties` file located in the `src/main/resources` directory.

For fetching LineJourney, due to limitation of new SL API 
the application uses a stub file located at `src/main/resources/journey_response_stub.json`.
