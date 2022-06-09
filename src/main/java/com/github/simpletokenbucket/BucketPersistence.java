package com.github.simpletokenbucket;

import java.util.Optional;

public interface BucketPersistence {
    Optional<BucketState> getBucketState(String key);

    BucketState save(BucketState entity);
}
