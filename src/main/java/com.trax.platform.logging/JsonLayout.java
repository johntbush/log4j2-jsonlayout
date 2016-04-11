package com.trax.platform.logging;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;


/**
 * <pre>
 *
 *  {
 *   "timeMillis": 1460403594814,
 *   "level": "INFO",
 *   "loggerName": "com.trax.platform.logging.JsonLayoutTest",
 *   "message": "logging hi world",
 *   "endOfBatch": false,
 *   "context": {
 *      "third": "ToTo",
 *      "first": "Dorothy",
 *      "second": "Wizard"
 *   },
 *   "loggerFqcn": "org.apache.logging.slf4j.Log4jLogger",
 *   "contextMap": [{
 *      "key": "third",
 *      "value": "ToTo"
 *   }, {
 *      "key": "first",
 *      "value": "Dorothy"
 *   }, {
 *      "key": "second",
 *      "value": "Wizard"
 *   }]
 *   }
 * </pre>
 */
@Plugin(name = "TraxJsonLayout", category = Node.CATEGORY, elementType = Layout.ELEMENT_TYPE, printObject = true)
public final class JsonLayout extends AbstractJacksonLayout {

    static final String CONTENT_TYPE = "application/json";

    private static final long serialVersionUID = 1L;

    protected JsonLayout(final boolean locationInfo, final boolean properties, final boolean complete, final boolean compact,
            final Charset charset) {
        super(new JacksonFactory.JSON().newWriter(locationInfo, properties, compact), charset, compact, complete);
    }

    /**
     * Returns appropriate JSON headers.
     *
     * @return a byte array containing the header, opening the JSON array.
     */
    @Override
    public byte[] getHeader() {
        if (!this.complete) {
            return null;
        }
        final StringBuilder buf = new StringBuilder();
        buf.append('[');
        buf.append(this.eol);
        return buf.toString().getBytes(this.getCharset());
    }

    /**
     * Returns appropriate JSON footer.
     *
     * @return a byte array containing the footer, closing the JSON array.
     */
    @Override
    public byte[] getFooter() {
        if (!this.complete) {
            return null;
        }
        return (this.eol + ']' + this.eol).getBytes(this.getCharset());
    }

    @Override
    public Map<String, String> getContentFormat() {
        final Map<String, String> result = new HashMap<String, String>();
        result.put("version", "2.0");
        return result;
    }

    @Override
    /**
     * @return The content type.
     */
    public String getContentType() {
        return CONTENT_TYPE + "; charset=" + this.getCharset();
    }

    /**
     * Creates a JSON Layout.
     *
     * @param locationInfo If "true", includes the location information in the generated JSON.
     * @param properties If "true", includes the thread context in the generated JSON.
     * @param complete If "true", includes the JSON header and footer, defaults to "false".
     * @param compact If "true", does not use end-of-lines and indentation, defaults to "false".
     * @param charset The character set to use, if {@code null}, uses "UTF-8".
     * @return A JSON Layout.
     */
    @PluginFactory
    public static AbstractJacksonLayout createLayout(
            // @formatter:off
            @PluginAttribute(value = "locationInfo", defaultBoolean = false) final boolean locationInfo,
            @PluginAttribute(value = "properties", defaultBoolean = false) final boolean properties,
            @PluginAttribute(value = "complete", defaultBoolean = false) final boolean complete,
            @PluginAttribute(value = "compact", defaultBoolean = false) final boolean compact,
            @PluginAttribute(value = "charset", defaultString = "UTF-8") final Charset charset
            // @formatter:on
    ) {
        return new JsonLayout(locationInfo, properties, complete, compact, charset);
    }

    /**
     * Creates a JSON Layout using the default settings.
     *
     * @return A JSON Layout.
     */
    public static AbstractJacksonLayout createDefaultLayout() {
        return new JsonLayout(false, false, false, false, Charsets.UTF_8);
    }
}
