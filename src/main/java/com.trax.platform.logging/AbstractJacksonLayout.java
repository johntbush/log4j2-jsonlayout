package com.trax.platform.logging;

import java.nio.charset.Charset;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;
import org.apache.logging.log4j.util.Strings;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;

abstract class AbstractJacksonLayout extends AbstractStringLayout {

    private static final long serialVersionUID = 1L;
    protected static final String DEFAULT_EOL = "\r\n";
    protected static final String COMPACT_EOL = Strings.EMPTY;
    protected final String eol;
    protected final ObjectWriter objectWriter;
    protected final boolean compact;
    protected final boolean complete;

    protected AbstractJacksonLayout(final ObjectWriter objectWriter, final Charset charset, final boolean compact, final boolean complete) {
        super(charset);
        this.objectWriter = objectWriter;
        this.compact = compact;
        this.complete = complete;
        this.eol = compact ? COMPACT_EOL : DEFAULT_EOL;
    }

    /**
     * Formats a {@link org.apache.logging.log4j.core.LogEvent}.
     * 
     * @param event The LogEvent.
     * @return The XML representation of the LogEvent.
     */
    @Override
    public String toSerializable(final LogEvent event) {
        try {
            return this.objectWriter.writeValueAsString(new JsonLogEvent(event));
        } catch (final JsonProcessingException e) {
            // Should this be an ISE or IAE?
            LOGGER.error(e);
            return Strings.EMPTY;
        }
    }

}
