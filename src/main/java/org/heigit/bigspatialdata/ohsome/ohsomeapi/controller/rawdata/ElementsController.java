package org.heigit.bigspatialdata.ohsome.ohsomeapi.controller.rawdata;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.heigit.bigspatialdata.ohsome.ohsomeapi.controller.ParameterDescriptions;
import org.heigit.bigspatialdata.ohsome.ohsomeapi.executor.ElementsRequestExecutor;
import org.heigit.bigspatialdata.ohsome.ohsomeapi.executor.RequestParameters;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * REST controller containing the method, which is mapped to "/elements" and used to return OSM
 * data.
 */
@Api(tags = "dataExtraction")
@RestController
@RequestMapping("/elements")
public class ElementsController {

  /**
   * Gives the OSM objects as GeoJSON features.
   * 
   * <p>
   * The parameters are described in the
   * {@link org.heigit.bigspatialdata.ohsome.ohsomeapi.controller.CountController#count(String, String, String, String[], String[], String[], String[], String[], String, HttpServletRequest)
   * count} method.
   * 
   * @return {@link org.heigit.bigspatialdata.ohsome.ohsomeapi.output.dataaggregationresponse.Response
   *         Response}
   */
  @ApiOperation(value = "OSM Data", nickname = "rawData")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "osmMetadata", value = ParameterDescriptions.OSM_METADATA_DESCR,
          defaultValue = "false", paramType = "query", dataType = "string", required = false),
      @ApiImplicitParam(name = "includeTags", value = ParameterDescriptions.INCLUDE_TAGS_DESCR,
          defaultValue = "false", paramType = "query", dataType = "string", required = false)})
  @RequestMapping(value = "", method = {RequestMethod.GET, RequestMethod.POST})
  public void retrieveOSMData(
      @ApiParam(hidden = true) @RequestParam(value = "bboxes", defaultValue = "",
          required = false) String bboxes,
      @ApiParam(hidden = true) @RequestParam(value = "bcircles", defaultValue = "",
          required = false) String bcircles,
      @ApiParam(hidden = true) @RequestParam(value = "bpolys", defaultValue = "",
          required = false) String bpolys,
      @ApiParam(hidden = true) @RequestParam(value = "types", defaultValue = "",
          required = false) String[] types,
      @ApiParam(hidden = true) @RequestParam(value = "keys", defaultValue = "",
          required = false) String[] keys,
      @ApiParam(hidden = true) @RequestParam(value = "values", defaultValue = "",
          required = false) String[] values,
      @ApiParam(hidden = true) @RequestParam(value = "userids", defaultValue = "",
          required = false) String[] userids,
      @ApiParam(hidden = true) @RequestParam(value = "time", defaultValue = "",
          required = false) String[] time,
      @ApiParam(hidden = true) @RequestParam(value = "osmMetadata", defaultValue = "",
          required = false) String osmMetadata,
      @ApiParam(hidden = true) @RequestParam(value = "includeTags", defaultValue = "",
          required = false) String includeTags,
      @ApiParam(hidden = true) @RequestParam(value = "showMetadata",
          defaultValue = "false") String showMetadata,
      @ApiParam(hidden = true) HttpServletRequest request,
      @ApiParam(hidden = true) HttpServletResponse response)
      throws UnsupportedOperationException, Exception {
    ElementsRequestExecutor
        .executeElements(
            new RequestParameters(request.getMethod(), true, false, bboxes, bcircles, bpolys, types,
                keys, values, userids, time, showMetadata),
            osmMetadata, includeTags, false, response);
  }

  /**
   * Gives the geometry of OSM objects as GeoJSON features.
   * 
   * <p>
   * The parameters are described in the
   * {@link org.heigit.bigspatialdata.ohsome.ohsomeapi.controller.CountController#count(String, String, String, String[], String[], String[], String[], String[], String, HttpServletRequest)
   * count} method.
   * 
   * @return {@link org.heigit.bigspatialdata.ohsome.ohsomeapi.output.dataaggregationresponse.Response
   *         Response}
   */
  @ApiOperation(value = "Geometry of OSM Data", nickname = "geomData")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "osmMetadata", value = ParameterDescriptions.OSM_METADATA_DESCR,
          defaultValue = "false", paramType = "query", dataType = "string", required = false)})
  @RequestMapping(value = "/geom", method = {RequestMethod.GET, RequestMethod.POST})
  public void retrieveGeomData(
      @ApiParam(hidden = true) @RequestParam(value = "bboxes", defaultValue = "",
          required = false) String bboxes,
      @ApiParam(hidden = true) @RequestParam(value = "bcircles", defaultValue = "",
          required = false) String bcircles,
      @ApiParam(hidden = true) @RequestParam(value = "bpolys", defaultValue = "",
          required = false) String bpolys,
      @ApiParam(hidden = true) @RequestParam(value = "types", defaultValue = "",
          required = false) String[] types,
      @ApiParam(hidden = true) @RequestParam(value = "keys", defaultValue = "",
          required = false) String[] keys,
      @ApiParam(hidden = true) @RequestParam(value = "values", defaultValue = "",
          required = false) String[] values,
      @ApiParam(hidden = true) @RequestParam(value = "userids", defaultValue = "",
          required = false) String[] userids,
      @ApiParam(hidden = true) @RequestParam(value = "time", defaultValue = "",
          required = false) String[] time,
      @ApiParam(hidden = true) @RequestParam(value = "osmMetadata", defaultValue = "",
          required = false) String osmMetadata,
      @ApiParam(hidden = true) @RequestParam(value = "showMetadata",
          defaultValue = "false") String showMetadata,
      @ApiParam(hidden = true) HttpServletRequest request,
      @ApiParam(hidden = true) HttpServletResponse response)
      throws UnsupportedOperationException, Exception {
    ElementsRequestExecutor.executeElements(new RequestParameters(request.getMethod(), true, false,
        bboxes, bcircles, bpolys, types, keys, values, userids, time, showMetadata), osmMetadata,
        "", true, response);
  }
}
