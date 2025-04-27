# Use the official Node.js 19 image for the frontend
FROM node:19

WORKDIR /app

# Copy package files and install dependencies
COPY package.json package-lock.json ./
RUN npm install

# Copy the source code
COPY . .

# Expose the frontend port
EXPOSE 5173

# Start the frontend development server
CMD ["npm", "run", "dev", "--", "--host", "0.0.0.0"]
