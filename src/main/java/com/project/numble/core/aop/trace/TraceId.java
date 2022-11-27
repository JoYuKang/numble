package com.project.numble.core.aop.trace;

import java.util.UUID;
import lombok.Getter;

@Getter
public class TraceId {

    private int level;
    private String id;

    public TraceId() {
        this.level = 0;
        this.id = UUID.randomUUID().toString().substring(0, 8);
    }

    public TraceId(int level, String id) {
        this.level = level;
        this.id = id;
    }

    public TraceId nextId() {
        return new TraceId(level + 1, id);
    }

    public boolean isFirst() {
        return level == 0;
    }

    public TraceId beforeId() {
        return new TraceId(level - 1, id);
    }
}