Provides a log4j2 JsonLayout that outputs the contextMap data as a proper object not an array of key/values, which is really crappy for querying.

See https://issues.apache.org/jira/browse/LOG4J2-623

Solution inspired from 
http://www.cowtowncoder.com/blog/archives/2013/10/entry_482.html

This is where I learned @JsonFormat(shape= JsonFormat.Shape.OBJECT) will tell  Jackson to serialize Maps as Objects

In order to remain backwards compatible with the old JSON syntax, a new attribute called "context" is used.  

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
