package com.savdev.rest.api;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;

import java.io.InputStream;

@Path(OctetStreamFileUploading.FILE_UPLOADING_BASE_PATH)
@Consumes(MediaType.APPLICATION_OCTET_STREAM)
public interface OctetStreamFileUploading {

  String FILE_UPLOADING_BASE_PATH = "/test/api/stream/upload";

  @POST
  void uploadFileAsStrem(InputStream inputStream);
}
