

- Simplifies rest client creation (how to pass only server url and authentication)
- Requests serialization and responses deserialization
- [Authentication utilities]
- [Fine-tuning logging]
- [Errors handling]
- [Dependencies in rest api module](#dependencies-in-rest-api-module)
- [Requests logging](#requests-logging)
- [Files uploading](#files-uploading)

### Requests and responses logging

It depends on http engine, used by rest client framework.

Most popular http engines:
- Apache HttpClient v.4x
- Apache HttpClient v.5.x
- Jetty RS client
- Reactor Netty
- JDK HttpClient

### Requests logging

- [enable debug level for `org.apache.http.wire` in case you use `Apache HttpClient v.4.x`](jakarta/jakarta_resteasy_client/src/test/resources/logback-test.xml)
- [enable debug level for `org.apache.hc.client5.http.wire` in case you use `Apache HttpClient v.5.x`](jakarta/jakarta_resteasy_client/src/test/resources/logback-test.xml)

If you use Spring, it manages all dependencies for you. Just enable debug level in its properties:
```yaml
logging:
  level:
    org:
      apache:
        http:
          wire: debug
```



### Files uploading

You have different MIME types based on file type and it size:
- `application/octet-stream`(`MediaType.APPLICATION_OCTET_STREAM`) - treats the entire body as a single binary stream,
  requiring any metadata to be sent in separate headers or parameters. It is preferred for direct binary data transfer
  or streaming large files in chunks
- `multipart/form-data` (`MediaType.MULTIPART_FORM_DATA`) - allows sending metadata (like field names and filenames)
  alongside the data within the same request. You could also send multiple files in a single request,
  including the subtype `application/octet-stream`

If file type is known in advance, you could use the appropriate MIME type:
- `application/json` (`MediaType.APPLICATION_JSON`) - for json file,
- `application/vnd.openxmlformats-officedocument.spreadsheetml.sheet` (no `MediaType`) - for Excel (`.xlsx`)
- `application/vnd.ms-excel` (no `MediaType`) - for old Excel format (`.xls`)
- `application/pdf` (no `MediaType`)  for pdf files
- `application/zip` for compressed archive files


```java

version=4.5.12
groupId=org.apache.httpcomponents
        artifactId=httpmime

org.apache.http.entity.mime.MultipartEntityBuilder
            MultipartEntityBuilder entity = MultipartEntityBuilder.create().setMimeSubtype("mixed")
                    .addTextBody("attachment",
                            "{ \"name\": \"" + attachmentName + "\", \"comment\": \"" + comment
                                    + "\", \"link\": \"" + attachmentLink + "\", \"newWindow\": true }",
                            ContentType.APPLICATION_JSON)
                    .addPart(FormBodyPartBuilder.create().setName("file")
                            .setBody(new StringBody("", ContentType.TEXT_PLAIN))
                            .build());
            postReq.setEntity(entity.build());
```

```text
POST
Content-Length: 7160\r\n
Content-Type: multipart/form-data; boundary=------------------------3defr6efr7eea308\r\n
--------------------------3defr6efr7eea308
Content-Disposition: form-data; name="part1";
filename="file.json" Content-Type: application/octet-stream
{
  "someCoolId":"11890300",
  // ommissions, but this part is nice human readable json
  "capture":"1623405938252"
}
--------------------------3defr6efr7eea308
// omissions, more parts
"title": "Unsupported Media Type",
"status": 415,
"detail": "Content-Type 'application/octet-stream' is not supported.",
https://stackoverflow.com/questions/77825758/multipart-form-data-with-spring-boot-and-accepting-part-with-content-type-applic/77839353
```

```yaml
multipart/form-data:
  schema:
    type: object
    properties:
      file:
        type: string
        format: binary
      caption:
        type: string
```

```bash
curl -X POST "https://api.example.com/file/upload" \
    -H "Content-Type: multipart/form-data" \
    -F "file=@content.pdf" \
    -F "caption=Sample PDF file"
```

```yaml
application/octet-stream:
  schema:
    type: string
    format: binary
```
```bash
curl -X POST "https://api.example.com/file/upload" \
     -H "Content-Type: application/octet-stream" \
     --data-binary "@image.jpg"
```

```bash
curl -X POST "https://api.example.com/file/upload" \
     -H "Content-Type: application/json" \
     -d '{"file": $(base64 image.jpg), "caption": "Sample JSON file"}'
```

If the file is too large, the client should receive a 413 Request Entity Too Large response.

https://openliberty.io/docs/modules/reference/23.0.0.12/com.ibm.websphere.appserver.api.jaxrs20_1.1-javadoc/com/ibm/websphere/jaxrs20/multipart/AttachmentBuilder.html

```java
 List<IAttachment> parts = new ArrayList<>();
 parts.add(AttachmentBuilder.newBuilder("sinpleString")
                            .inputStream(new ByteArrayInputStream("Hello World!".getBytes()))
                            .build()); // content type for this part will be "text/plain"
 parts.add(AttachmentBuilder.newBuilder("txtFileWithHeader")
                            .inputStream(new FileInputStream("/path/to/myTextFile.txt")
                            .fileName("renamedTextFile.txt")
                            .header("X-MyCustomHeader", someHeaderValue)
                            .build()); // content type for this part will be "application/octet-stream"
 parts.add(AttachmentBuilder.newBuilder("xmlFile")
                            .inputStream("myXmlFile.xml", new FileInputStream("/path/to/myXmlFile.xml"))
                            .contentType(MediaType.APPLICATION_XML)
                            .build());
 Client c = ClientBuilder.newClient();
 WebTarget target = c.target("http://somehost:9080/data/multipart/list");
 Response r = target.request()
                    .header("Content-Type", "multipart/form-data")
                    .post(Entity.entity(attachments, MediaType.MULTIPART_FORM_DATA_TYPE));
 
```

https://commons.apache.org/proper/commons-fileupload/streaming.html

### files uploading by chunks

you will need to inform your server if you sent the last file chunk or not yet (in a form of a boolean flag,
for example: 'isLastChunk', chunksArray.length === 1). 