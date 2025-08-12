package com.savdev.rest.api;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.EntityPart;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path(MultiPartFileUploadingApi.FILE_UPLOADING_BASE_PATH)
@Consumes(MediaType.MULTIPART_FORM_DATA)
public interface MultiPartFileUploadingApi {

  String FILE_UPLOADING_BASE_PATH = "/test/api/multi/part/upload";

  @POST
  void uploadMultipleFiles(List<EntityPart> parts);
}
