package com.github.simpletokenbucket;

public interface TokenBucket {
    boolean tryConsume(long quantity);

    long getRemaining();
}
