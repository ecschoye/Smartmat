# Use the official Node.js image as the base
FROM node:latest as build

# Set the working directory
WORKDIR /app

# Copy the package.json and package-lock.json files into the image
COPY package*.json ./

# Install dependencies
RUN npm ci

# Copy the source code into the image
COPY . .

# Build the project
RUN npm run build

# Create the runtime image
FROM node:latest

# Set the working directory
WORKDIR /app

# Copy the built files from the build image
COPY --from=build /app/dist /app/dist

# Install production dependencies
COPY package*.json ./
RUN npm ci --production

# Expose the service port
EXPOSE 3000

# Run the service
CMD ["npm", "run",  "start"]
