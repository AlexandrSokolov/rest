package com.savdev.rest.client.api;

import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
public interface MatrixParamsRestApi {

  String PATH = "/categories/objects";

  String MATRIX_PARAM_NAME = "name";

  /**
   * Url looks like: `/categories/objects;name=green`
   *
   * Note that the @MatrixParam annotation value refers to a name of a matrix parameter
   * that resides in the last matched path segment of the Path-annotated Java structure
   * that injects the value of the matrix parameter.
   *
   * @param objectName
   * @return
   */
  @GET
  @Path(PATH)
  String getMatrixParam(@MatrixParam(MATRIX_PARAM_NAME) String objectName);

  /**
   * Url looks like: `/categories;name=foo/objects;name=green`
   *
   * Implementation might look like:
   * ```java
   * MultivaluedMap<String, String> matrixParameters = categorySegment.getMatrixParameters();
   * String categorySegmentPath = categorySegment.getPath();
   * String string = String.format(
   *  "object %s, path:%s, matrixParams:%s%n",
   *  objectName, categorySegmentPath, matrixParameters);
   * ```
   * Outputs:
   * `object green, path:categories, matrixParams:[name=foo]`
   *
   * @param categorySegment
   * @param objectName
   * @return
   */
  @GET
  @Path("{categoryVar:categories}/objects")
  String objectsByCategory(
    @PathParam("categoryVar") PathSegment categorySegment,
    @MatrixParam("name") String objectName);

  /**
   * Url: `/categories;name=foo/objects;name=green/attributes;name=size`
   *
   * Note: I could not send a request for this signature!
   *
   * See MatrixParamsTest#testMatrixAllSegments
   *
   * Possible implementation:
   * for (PathSegment pathSegment : pathSegments) {
   *     sb.append("path: ");
   *     sb.append(pathSegment.getPath());
   *     sb.append(", matrix parameters ");
   *     sb.append(pathSegment.getMatrixParameters());
   *     sb.append("<br/>");
   *   }
   */
  @GET
  @Encoded
  @Path("/all/{var:.+}")
  String allSegments(@PathParam("var") List<PathSegment> pathSegments);
}
