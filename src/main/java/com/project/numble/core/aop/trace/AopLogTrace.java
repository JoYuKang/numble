package com.project.numble.core.aop.trace;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class AopLogTrace implements LogTrace {

    private static final String nextDirection = "|-->";
    private static final String beforeDirection = "|<--";
    private static final String exceptionDirection = "|<X-";

    ThreadLocal<TraceId> threadLocal = new ThreadLocal<>();

    @Override
    public TraceStatus begin(String message) {
        checkBeginThread();
        TraceId traceId = threadLocal.get();
        long beforeTime = System.currentTimeMillis();
        TraceStatus status = new TraceStatus();
        status.setTime(beforeTime);
        status.setName(message);

        log.info("[{}]{}", traceId.getId(),
                addSpace(nextDirection, message, traceId.getLevel()));
        return status;
    }

    @Override
    public void end(TraceStatus status) {
        checkError(status, null);
    }

    @Override
    public void exception(TraceStatus status, Exception e) {
        checkError(status, e);
    }

    private void checkError(TraceStatus status, Exception e) {
        long endTime = System.currentTimeMillis();
        TraceId traceId = threadLocal.get();
        long time = endTime - status.getTime();
        if (e == null)
            log.info("[{}]{} Time={}ms", traceId.getId(),
                    addSpace(beforeDirection, status.getName(), traceId.getLevel())
                    ,time);
        else
            log.info("[{}]{} Time={}ms exception={}", traceId.getId(),
                    addSpace(exceptionDirection, status.getName(), traceId.getLevel())
                    , time
                    , e.toString());
        checkEndThread();
    }

    private String addSpace(String direction, String message, int level) {
        String answer = "";
        for (int i=0; i<level; i++) {
            answer += "|   ";
        }
        return answer + direction + message;
    }

    private void checkBeginThread() {
        TraceId traceId = threadLocal.get();
        if (traceId == null)
            threadLocal.set(new TraceId());
        else
            threadLocal.set(traceId.nextId());
    }

    private void checkEndThread() {
        TraceId traceId = threadLocal.get();
        if (traceId.isFirst())
            threadLocal.remove();
        else
            threadLocal.set(traceId.beforeId());
    }
}