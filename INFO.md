# URL Shortener
## App design
### API
The API has two routes :
- one which creates a URL in the system with a new or a customised hash
- another one which gets a URL by hash and automatically does the redirection

### Service
I let the possibility to change the main service implementation if we wanted to change the database for example (see next section).
Same for the hashing method, here I use murmur 32 which is idempotent and gives 8 character as a result (32^8 possibilities).
Another hashing method could be implemented thanks to the interface (a proper analysis should be done before migrating to avoid collisions between the old and new hashes).

### Database
For this app, I would have chosen a NoSQL database for multiple reasons :
- We don't need to have relations between entities because we only have one
- NoSQL databases are easier to scale horizontally. For instance, MongoDB provides sharding and replication.
  Here PostgreSQL is a single point of failure if not configured accordingly. Partitioning and replication can still be achieved, but it is more complicated.

...but for this coding challenge I didn't because I am not an expert in NoSQL and for the time I had, a postgres SQL database was easier to configure.
As seen in the previous section, I let the possibility to change the main service implementation for another type of DB usage.
If we wanted to use MongoDB, we'd have to :
- add the right dependencies
- create a similar implementation of the service with a MongoRepository and mark it as Primary bean
- use a migration tool like mongock (if needed)
- configure it properly (sharding, replica sets, ...)

Both DB type manage locks when it comes to concurrent transactions on the same data. So the only bottleneck would be when N users try to create the same URL (same hash).

Then, the DB would have to synchronise those transactions.
The reality wants the "create" endpoint to be called less frequently than the "getAndRedirect" endpoint, so there wouldn't be a problem in prod.

### Improvements
In order to keep the database clean, we could leverage the `creation_datetime` attribute to manage "expired" URLs :
- each time a user gets a URL, we should test its creation date against a generic constant (i.e., 1 week validity) and remove the URL if needed (returns NOT FOUND)
- a cron can run every day and remove the expired dates in the DB

Add a memcache solution to be more efficient when reading URL and redirecting the user. In the case where URL expiration has been implemented, it should also evict the right entries in the cache.

Add a URL validation and normalisation system :
- to avoid creating fake entries that are not URLs 
- to clean any javascript scripts in the URL

Supposing a user knows the hashing algorithm, we could avoid duplications :
when the user creates a certain URL with its real hash as a customised one and then try to create the same URL without customised hash.
This issue is an identified bug in my code but which is unlikely to happen : dangerous in production if no user management.

### Infra
We should of course set up a load balancer in front of the service and have multiple instances of it running. The service should then be connected to a sharded NoSQL DB.
The DB properties available in application.yml would be connected to the master node of the DB.

### Tests
I used restassured for basic API testing and I embedded-postgresql (testcontainers) coupled with spring cloud (that I had already the chance to work with).
Thanks to that, it can start a real postgresql instance for better testing (closer to prod) . I assume that the same would be doable with MongoDB.