package com.trax.platform.logging;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.jackson.JsonConstants;
import org.apache.logging.log4j.core.jackson.Log4jJsonObjectMapper;
import org.apache.logging.log4j.core.jackson.Log4jXmlObjectMapper;
import org.apache.logging.log4j.core.jackson.XmlConstants;

import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.dataformat.xml.util.DefaultXmlPrettyPrinter;

abstract class JacksonFactory {

    static class JSON extends JacksonFactory {

        @Override
        protected String getPropertNameForContextMap() {
            return JsonConstants.ELT_CONTEXT_MAP;
        }

        @Override
        protected String getPropertNameForSource() {
            return JsonConstants.ELT_SOURCE;
        }

        @Override
        protected PrettyPrinter newCompactPrinter() {
            return new MinimalPrettyPrinter();
        }

        @Override
        protected ObjectMapper newObjectMapper() {
            return new Log4jJsonObjectMapper();
        }

        @Override
        protected PrettyPrinter newPrettyPrinter() {
            return new DefaultPrettyPrinter();
        }
    }

    static class XML extends JacksonFactory {

        @Override
        protected String getPropertNameForContextMap() {
            return XmlConstants.ELT_CONTEXT_MAP;
        }

        @Override
        protected String getPropertNameForSource() {
            return XmlConstants.ELT_SOURCE;
        }

        @Override
        protected PrettyPrinter newCompactPrinter() {
            // Yes, null is the proper answer.
            return null;
        }

        @Override
        protected ObjectMapper newObjectMapper() {
            return new Log4jXmlObjectMapper();
        }

        @Override
        protected PrettyPrinter newPrettyPrinter() {
            return new DefaultXmlPrettyPrinter();
        }
    }

    abstract protected String getPropertNameForContextMap();

    abstract protected String getPropertNameForSource();

    abstract protected PrettyPrinter newCompactPrinter();

    abstract protected ObjectMapper newObjectMapper();

    abstract protected PrettyPrinter newPrettyPrinter();

    ObjectWriter newWriter(final boolean locationInfo, final boolean properties, final boolean compact) {
        final SimpleFilterProvider filters = new SimpleFilterProvider();
        final Set<String> except = new HashSet<String>(2);
        if (!locationInfo) {
            except.add(this.getPropertNameForSource());
        }
        if (!properties) {
            except.add(this.getPropertNameForContextMap());
        }
        filters.addFilter(Log4jLogEvent.class.getName(), SimpleBeanPropertyFilter.serializeAllExcept(except));
        final ObjectWriter writer = this.newObjectMapper().writer(compact ? this.newCompactPrinter() : this.newPrettyPrinter());
        return writer.with(filters);
    }

}
