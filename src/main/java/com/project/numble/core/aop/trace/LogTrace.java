package com.project.numble.core.aop.trace;

public interface LogTrace {

    TraceStatus begin(String message);

    void end(TraceStatus status);

    void exception(TraceStatus status, Exception e);
}
