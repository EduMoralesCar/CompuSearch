package com.universidad.compusearch.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Attempt {
    private int count;
    private long lastAttemptTime;
}
