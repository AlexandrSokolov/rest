package com.savdev.rest.client;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.savdev.rest.api.MultiPartFileUploadingApi;
import jakarta.ws.rs.core.EntityPart;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@WireMockTest
public class MultiPartFileUploadingApiTest {

  public static final String PART_NAME_1 = "file1";
  public static final String FILE_NAME_1 = PART_NAME_1 + ".txt";
  public static final String MEDIA_TYPE_1 = MediaType.TEXT_PLAIN;

  public static final String PART_NAME_2 = "file2";
  public static final String FILE_NAME_2 = PART_NAME_2 + ".json";
  public static final String MEDIA_TYPE_2 = MediaType.APPLICATION_JSON;

  public static final String PART_NAME_3 = "file3";
  public static final String FILE_NAME_3 = PART_NAME_3 + ".dat";
  public static final String MEDIA_TYPE_3 = MediaType.APPLICATION_OCTET_STREAM;

  public static final String TEXT_FILE_CONTENT = "test content";
  public static final String JSON_FILE_CONTENT = "{\"key\": \"value\"}";
  public static final String BINARY_FILE_CONTENT = "some random content 43&23$2?";


  @Test
  public void uploadMultipleFiles(WireMockRuntimeInfo wmRuntimeInfo) throws IOException {

    initMultiPartFileUploadingWireMock(wmRuntimeInfo);

    try (var restClient = new JakartaResteasyRestClient(wmRuntimeInfo.getHttpBaseUrl())) {
      restClient.restMultiPartFileUploadingApiProxy()
        .uploadMultipleFiles(
          fileParts());
    }
  }

  private List<EntityPart> fileParts() {
    try {
      return List.of(
        EntityPart.withName(PART_NAME_1)
          .fileName(FILE_NAME_1)
          .content(IOUtils.toInputStream(TEXT_FILE_CONTENT, StandardCharsets.UTF_8))
          .mediaType(MEDIA_TYPE_1)
          .build(),
        EntityPart.withName(PART_NAME_2)
          .fileName(FILE_NAME_2)
          .content(IOUtils.toInputStream(JSON_FILE_CONTENT, StandardCharsets.UTF_8))
          .mediaType(MEDIA_TYPE_2).build(),
        EntityPart.withName(PART_NAME_3)
          .fileName(FILE_NAME_3)
          .content(IOUtils.toInputStream(BINARY_FILE_CONTENT, StandardCharsets.UTF_8))
          .mediaType(MEDIA_TYPE_3)
          .build());
    } catch (IOException e) {
      throw new IllegalStateException(
        "Could not create `multipart/form-data` to upload multiple files. " + "Reason: '" + e.getMessage() + "'",
        e);
    }
  }

  private void initMultiPartFileUploadingWireMock(WireMockRuntimeInfo wmRuntimeInfo) {
    var wireMock = wmRuntimeInfo.getWireMock();
    wireMock.register(
      post(urlPathEqualTo(MultiPartFileUploadingApi.FILE_UPLOADING_BASE_PATH))
        .withHeader(HttpHeaders.CONTENT_TYPE, containing(MediaType.MULTIPART_FORM_DATA))
        .withMultipartRequestBody(
          aMultipart()
            .withName(PART_NAME_1) // Name of the file part
            .withFileName(FILE_NAME_1)
            .withHeader(HttpHeaders.CONTENT_TYPE, equalTo(MEDIA_TYPE_1)) // Content-Type of the file part
            .withBody(binaryEqualTo(TEXT_FILE_CONTENT.getBytes()))) // Match TEXT content
        .withMultipartRequestBody(
          aMultipart()
            .withName(PART_NAME_2) // Name of the file part
            .withFileName(FILE_NAME_2)
            .withHeader(HttpHeaders.CONTENT_TYPE, equalTo(MEDIA_TYPE_2)) // Content-Type of the file part
            .withBody(binaryEqualTo(JSON_FILE_CONTENT.getBytes()))) // Match JSON content
        .withMultipartRequestBody(
          aMultipart()
            .withName(PART_NAME_3) // Name of the file part
            .withFileName(FILE_NAME_3)
            .withHeader(HttpHeaders.CONTENT_TYPE, equalTo(MEDIA_TYPE_3)) // Content-Type of the file part
            .withBody(binaryEqualTo(BINARY_FILE_CONTENT.getBytes()))) // Match the binary content of the file
        .willReturn(
          ok()
            .withStatus(
              Response.Status.OK.getStatusCode())));
  }
}
