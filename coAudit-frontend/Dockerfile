
FROM node:18 as build

WORKDIR /app


COPY package*.json ./

# Install project dependencies
RUN npm install --legacy-peer-deps
RUN npm install -g @angular/cli
# Copy the entire project to the container
COPY . .

# Build the Angular app for production
RUN ng build --configuration production


# Use a smaller, production-ready image as the final image
FROM nginx:alpine

# Copy the production-ready Angular app to the Nginx webserver's root directory
COPY --from=build /app/dist/modernize  /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expose port 80
EXPOSE 80

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]