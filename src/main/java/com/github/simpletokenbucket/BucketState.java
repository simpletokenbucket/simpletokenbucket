package com.github.simpletokenbucket;

import java.time.Instant;

public record BucketState(Limit limit, long remainingTokens, Instant lastRefill) {
}
