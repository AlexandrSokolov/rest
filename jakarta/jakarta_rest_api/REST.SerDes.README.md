
- [`ObjectMapper` useful and common properties](#objectmapper-common-properties)
- [SerDes rules management](#serdes-rules-management)
- [How to test request serialization and response deserialization](#how-to-test-request-serialization-and-response-deserialization)
- [Date and time SerDes](#date-and-time-serdes)
- [Money SerDes](#money-serdes)
- [Java Enums SerDes](#java-enums-serdes)
- [SerDer for a field that might be of different formats](#serder-for-a-field-that-might-be-of-different-formats)
- [Property name control during SerDes](#property-name-control-during-serdes)

### `ObjectMapper` common properties

Ignore unknown properties - 
1. without annotation via ObjectMapper 
2. with annotation

### SerDes rules management

(annotation on dto, plain dto without annotations)

### How to test request serialization and response deserialization

### Date and time SerDes
### Money SerDes
### Java Enums SerDes
### SerDer for a field that might be of different formats

It might happen that server returns different formats for a field value.
For instance `value` could be a number:
```json
{
  "name": "Test name",
  "value": 12345
}
```
or a string:
```json
{
  "name": "Test name",
  "value": "some string"
}
```
or a map:
```json
{
  "name": "Test name",
  "value": {
    "label": "test label"
  }
}
```
Solutions:

#### Preferable solution - [via Java Generics](src/main/java/com/savdev/rest/dto/MultipleFormatsFieldViaGenerics.java)

[See `MultipleFormatsFieldViaGenericsTest` test](src/test/java/com/savdev/rest/dto/MultipleFormatsFieldViaGenericsTest.java)

You need additionally to implement:
- [request serializer](todo)
- [response deserializer](src/main/java/com/savdev/rest/jackson/MultipleFormatsValueDeserializer.java)

#### [make a field type of `Object` type](src/main/java/com/savdev/rest/dto/MultipleFormatsFieldAsObject.java)

[See `MultipleFormatsFieldAsObject` test](src/test/java/com/savdev/rest/dto/MultipleFormatsFieldAsObjectTest.java)

This is error-prone solution, that requires additional effort from the client to get real value.
For instance in case a format is decimal number, the run-time type will be Double.
Double - is a bad choice, you use BigDecimal in the application.
Create BigDecimal from Double - is error-prone solution, you must use string in constructor of BigDecimal.
At the end it comes to the following statements, difficult to maintain and understand:
```java
var value = new BigDecimal(((Double) r.value()).toString());
```

### Property name control during SerDes