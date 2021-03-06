## Motiviation

Provides a log4j2 JsonLayout that outputs the contextMap data as a proper object not an array of key/values, which is really crappy for querying from elasticsearch, jq, or probably anything that is used to dealing with JSON semantics.  

This issue is discussed here: See https://issues.apache.org/jira/browse/LOG4J2-623.  

The serialization solution inspired from http://www.cowtowncoder.com/blog/archives/2013/10/entry_482.html.  This is where I learned `@JsonFormat(shape= JsonFormat.Shape.OBJECT)` will tell Jackson to serialize Maps as Objects.

In order to remain backwards compatible with the old JSON syntax, a new attribute called "context" is used.  

Here is what the old format looked like:

```
{
        "timeMillis": 1460403594814,
        "level": "INFO",
        "loggerName": "com.trax.platform.logging.JsonLayoutTest",
        "message": "logging hi world",
        "endOfBatch": false,
        "loggerFqcn": "org.apache.logging.slf4j.Log4jLogger",
        "contextMap": [{
                "key": "third",
                "value": "ToTo"
        }, {
                "key": "first",
                "value": "Dorothy"
        }, {
                "key": "second",
                "value": "Wizard"
        }]
}
``` 

Here is what the new format looks like:

```
{
        "timeMillis": 1460403594814,
        "level": "INFO",
        "loggerName": "com.trax.platform.logging.JsonLayoutTest",
        "message": "logging hi world",
        "endOfBatch": false,
        "context": {
                "third": "ToTo",
                "first": "Dorothy",
                "second": "Wizard"
        },
        "loggerFqcn": "org.apache.logging.slf4j.Log4jLogger",
        "contextMap": [{
                "key": "third",
                "value": "ToTo"
        }, {
                "key": "first",
                "value": "Dorothy"
        }, {
                "key": "second",
                "value": "Wizard"
        }]
}
```

##  Using this JsonLayout

To use simply include the layout in your various appenders in your log4j2 config like this:

```
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="WARN">
    <Appenders>
        <Console name="Console" target="SYSTEM_OUT">
            <TraxJsonLayout properties="true" compact="false" complete="true"/>
            <PatternLayout pattern="%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"/>
        </Console>
    </Appenders>
    <Loggers>
        <Root level="debug">
            <AppenderRef ref="Console"/>
        </Root>
    </Loggers>
</Configuration>
```

## Implementation

This issue is discussed here: See https://issues.apache.org/jira/browse/LOG4J2-623.  Unfortunately, the community decided to not support proper Json encoding and is worried about backwards compatibility.  They are also Java nazi's and put final all over the place, curse you Java nazis!  This forced me to copy a few classes, else do some bytecode manipulation that I really didn't want to do, to avoid bringing in more dependencies.  What's more is they have non public Abstract classes ? What's the point of something being Abstract if you don't let others use it ?  Anyway I digress...

So any way I had to copy a few classes and then I simply wrapped `LogEvent` in order to expose a new context attribute that would be serialized correctly.

```
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
    ...
```
