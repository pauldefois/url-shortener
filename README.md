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
./mvnw -s settings.xml clean install spring-boot:run
```

## App design
### API
The API has two routes :
- one which creates a URL in the system with a new or a customised hash
- another one which gets a URL by hash and automatically does the redirection

### Service
I let the possibility to change the main service implementation if we wanted to change the database for example (see next section). 
Same for the hashing method, here I use murmur 32 which is idempotent and gives 8 character as a result (32^8 possibilities).
Another hashing method could be implemented thanks ot the interface (a proper analysis should be done before migrating to avoid collisions between the old and new hashes).

### Database
For this app, I would have chosen a NoSQL database for multiple reasons :
- We don't need to have relations between entities because we only have one
- NoSQL databases are easier to configure regarding horizontal scaling. For instance, MongoDB provides sharding and replication.
  Here PostgreSQL is a single point of failure if not configured accordingly. Partitioning and replication can still be achieved, but it is more complicated. 

...but I didn't set up a NoSQL DB, because I am not an expert in NoSQL :) and for the time I had for this coding challenge, a postgres SQL database was easier to configure.
As seen in the previous section, I let the possibility to change the main service implementation for another type of DB usage.
If we wanted to use MongoDB, we'd have to :
- add the right dependencies
- create a similar implementation of the service with a MongoRepository and mark it as Primary bean
- use a migration tool like mongock (if needed)
- configure it properly (sharding, replica sets, ...)

There is still a bottleneck in the service with the transaction : we don't want that two users create the same URL at the same time with a customised hash (inconsistencies between read and write).
We could generally, let the database fail and catch its error silently in case of a duplication, and then return the hash already created (both URLs are the same otherwise there would be "no duplications"). 
It would avoid having to find a URL by hash first before saving it.
The app should trigger an error to the user only in case of a customised hash duplication.

### Improvements
In order to keep the database clean, we could leverage the `creation_datetime` attribute to manage "expired" URLs :
- each time a user gets a URL, we should test its creation date against a generic constant (i.e., 1 week validity) and remove the URL if needed (returns NOT FOUND)
- a cron can run every day and remove the expired dates in the DB

Add a memcache solution to be more efficient when reading URL and redirecting the user. In case where URL expiration has been implemented, it should also evict the right entries in the cache.

### Infra
We should of course set up a load balancer in front of the service and have multiple instances of it running. The service should then be connected to a sharded NoSQL DB. 
The DB properties available in application.yml would be connected to the master node of the DB.