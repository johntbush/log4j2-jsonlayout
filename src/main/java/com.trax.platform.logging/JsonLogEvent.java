package com.trax.platform.logging;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.message.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * Wrap LogEvent so that the serialization of context comes out as an object instead of
 * an array of key/values
 *
 * Created by johnbush on 4/11/16.
 */
@JsonFormat(shape= JsonFormat.Shape.OBJECT)
public class JsonLogEvent implements LogEvent {
    private LogEvent event;
    public JsonLogEvent(LogEvent e) {
        this.event = e;
        context = e.getContextMap();
    }

    private Map context = new HashMap();

    public Map getContext() {
        return context;
    }

    public void setContext(Map context) {
        this.context = context;
    }

    @Override
    public Map<String, String> getContextMap() {
        return event.getContextMap();
    }

    @Override
    public ThreadContext.ContextStack getContextStack() {
        return event.getContextStack();
    }

    @Override
    public String getLoggerFqcn() {
        return event.getLoggerFqcn();
    }

    @Override
    public Level getLevel() {
        return event.getLevel();
    }

    @Override
    public String getLoggerName() {
        return event.getLoggerName();
    }

    @Override
    public Marker getMarker() {
        return event.getMarker();
    }

    @Override
    public Message getMessage() {
        return event.getMessage();
    }

    @Override
    public long getTimeMillis() {
        return event.getTimeMillis();
    }

    @Override
    public StackTraceElement getSource() {
        return event.getSource();
    }

    @Override
    public String getThreadName() {
        return null;
    }

    @Override
    public Throwable getThrown() {
        return event.getThrown();
    }

    @Override
    public ThrowableProxy getThrownProxy() {
        return event.getThrownProxy();
    }

    @Override
    public boolean isEndOfBatch() {
        return event.isEndOfBatch();
    }

    @Override
    public boolean isIncludeLocation() {
        return event.isIncludeLocation();
    }

    @Override
    public void setEndOfBatch(boolean b) {
        event.setEndOfBatch(b);
    }

    @Override
    public void setIncludeLocation(boolean b) {
        event.setIncludeLocation(b);
    }
}
