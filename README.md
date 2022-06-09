
# Simple Token Bucket

This java library provides a simple implementation of a token bucket algorithm.
Can be used to ensure a client can only use a fixed amount of a resource.


At the moment I've only implemented a persistent version of a token bucket, which will rely on a persistence layer of your 
choice (usually a Key/Value database) to persist and recover the bucket state.


There are already few Open Source token bucket implementations available, but some looked designed for more complex use-cases and felt 
too complicated and some were impossible to test in a deterministic way, unless you're ok with mocking static calls on a clock... 
which I'm not.

Interacting with this library should be pretty straightforward. 

For example, say you want to implement a way to limit a user to create more than 100 repositories a day, you could do something like this: 
(I'll add a builder later on)

    
     
    TokenBucket tockeBucket =  new PersistentTokenBucket(bucketPersistence, "repositories.maxCreatePerDay", 10, Duration.ofDays(1), Clock.systemDefaultZone());

