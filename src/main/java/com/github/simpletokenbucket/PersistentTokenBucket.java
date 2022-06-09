package com.github.simpletokenbucket;


import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;


public class PersistentTokenBucket implements TokenBucket {
    private final Clock clock;
    private final BucketPersistence bucketPersistence;
    private BucketState currentState;

    public PersistentTokenBucket(BucketPersistence bucketPersistence, String bucketKey, long defaultQuantity, Duration defaultDuration, Clock clock) {
        this.bucketPersistence = bucketPersistence;
        this.clock = clock;
        this.currentState = retrieveBucketState(bucketKey)
                .orElseGet(() -> {
                    BucketState bucketState = new BucketState(new Limit(defaultQuantity, defaultDuration), defaultQuantity, clock.instant());
                    bucketPersistence.save(bucketState);
                    return bucketState;
                });
    }

    @Override
    public boolean tryConsume(long quantity) {
        Instant requestInstant = clock.instant();
        refill(requestInstant);
        if (currentState.remainingTokens() >= quantity) {
            updateState(currentState.remainingTokens() - quantity, requestInstant);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public long getRemaining() {
        refill(clock.instant());
        return currentState.remainingTokens();
    }

    private void refill(Instant requestInstant) {
        Duration durationSinceLastRefill = Duration.between(currentState.lastRefill(), requestInstant);
        if (durationSinceLastRefill.compareTo(currentState.limit().duration()) >= 0) {
            updateState(currentState.limit().quantity(), requestInstant);
        }
    }

    private void updateState(long quantity, Instant requestInstant) {
        currentState = recreate(quantity, requestInstant);
        bucketPersistence.save(currentState);
    }

    private BucketState recreate(long quantity, Instant lastRefill) {
        return new BucketState(currentState.limit(), quantity, lastRefill);
    }

    private Optional<BucketState> retrieveBucketState(String bucketKey) {
        return bucketPersistence.getBucketState(bucketKey);
    }


}
