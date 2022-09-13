# URL Shortener
## App startup
First, start the database : 
```shell
docker-compose up -d
```

Then, create the schema inside the database :
```shell
docker exec -it url-shortener-database-1 bash # Adapt the container name if needed

# Inside the container, run the following :
PGPASSWORD=root psql -U root -h localhost -c 'CREATE SCHEMA "url-shortener"'
```

Finally, start the app :
```shell
# Build the app
./mvnw -s settings.xml clean install

# Start the app 
./mvnw -s settings.xml spring-boot:run
```

To launch the tests, run :
```shell
./mvnw -s settings.xml clean verify
```

## App usage
Please access [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) for easier usage.

Thoughts about the exercise in INFO.md