# SmartMat
The SmartMat project was part of the Systemutvikling 2 course and spanned over four weeks. Our team of seven was given a Vision Document from the product owners, outlining the objectives and specifications for the app we were to create. The app's primary purpose was to assist users in keeping track of their food supplies while offering solutions to minimize food waste. To organize and manage our tasks effectively, we employed Scrum methodologies as part of our agile project management approach. Our grade was determined by the quality of the final product, our work process, and the accompanying documentation. To maximize learning for each team member, we divided the tasks mainly based on specific features of the application.

## Some screenshots from the application: 

![home](https://github.com/ecschoye/idatt2106_2023_05/blob/main/screenshots/home.png)

![refrigerator](https://github.com/ecschoye/idatt2106_2023_05/blob/main/screenshots/kjoleskab.png)

![phone](https://github.com/ecschoye/idatt2106_2023_05/blob/main/screenshots/phone.png)

![settings](https://github.com/ecschoye/idatt2106_2023_05/blob/main/screenshots/settings'.png)

![recipes](https://github.com/ecschoye/idatt2106_2023_05/blob/main/screenshots/recipe_dark.png)

![weekmenu](https://github.com/ecschoye/idatt2106_2023_05/blob/main/screenshots/week_dark.png)

# Frontend

## Prerequisites
- Node.js
- NPM

## Installing Dependencies
To install dependencies, go to the frontend folder of the project and execute the following command in the terminal:
```bash
npm i
```

## Development Server
To start the development server, execute the following command in the terminal:
```bash
npm run dev
```
This will initiate the development server at [http://localhost:3000](http://localhost:3000).

## Building for Production
To compile the frontend app for production, run the following command in the terminal:
```bash
npm run build
```
This will generate a 'dist' folder with built static files ready for deployment to a web server.

# Backend

## Prerequisites
- JDK 18
- Maven

## Installing Dependencies
To install dependencies, go to the backend folder of the project and execute the following command in the terminal:
```bash
mvn install
```

## Starting the Server
To launch the backend server, run the following command in the terminal:
```bash
mvn spring-boot:run
```

## Building for Production
To compile the backend app for production, run the following command in the terminal:
```bash
mvn package
```
This will produce an executable .jar file in the 'target' folder, ready for server deployment.
