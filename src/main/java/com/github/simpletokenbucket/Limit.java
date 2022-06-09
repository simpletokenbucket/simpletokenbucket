package com.github.simpletokenbucket;

import java.time.Duration;

public record Limit(long quantity, Duration duration) { }