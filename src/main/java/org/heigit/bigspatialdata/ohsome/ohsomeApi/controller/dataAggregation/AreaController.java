package org.heigit.bigspatialdata.ohsome.ohsomeApi.controller.dataAggregation;

import org.heigit.bigspatialdata.ohsome.ohsomeApi.controller.DefaultSwaggerParameters;
import org.heigit.bigspatialdata.ohsome.ohsomeApi.controller.ParameterDescriptions;
import org.heigit.bigspatialdata.ohsome.ohsomeApi.exception.BadRequestException;
import org.heigit.bigspatialdata.ohsome.ohsomeApi.executor.ElementsRequestExecutor;
import org.heigit.bigspatialdata.ohsome.ohsomeApi.executor.RequestParameters;
import org.heigit.bigspatialdata.ohsome.ohsomeApi.executor.RequestResource;
import org.heigit.bigspatialdata.ohsome.ohsomeApi.output.dataAggregationResponse.DefaultAggregationResponse;
import org.heigit.bigspatialdata.ohsome.ohsomeApi.output.dataAggregationResponse.RatioShareResponse;
import org.heigit.bigspatialdata.ohsome.ohsomeApi.output.dataAggregationResponse.groupByResponse.GroupByResponse;
import org.springframework.http.MediaType;
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
 * REST controller containing the GET and POST request handling methods, which are mapped to
 * "/elements/area".
 */
@Api(tags = "/elements/area")
@RestController
@RequestMapping("/elements/area")
public class AreaController {

  /**
   * GET request giving the area of OSM objects.
   * <p>
   * The parameters are described in the
   * {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.controller.dataAggregation.CountController#getCount(String, String, String, String[], String[], String[], String[], String[], String)
   * getCount} method.
   * 
   * @return {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.output.dataAggregationResponse.DefaultAggregationResponse
   *         DefaultAggregationResponse}
   */
  @ApiOperation(value = "Area of OSM elements")
  @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
  public DefaultAggregationResponse getArea(
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
      @ApiParam(hidden = true) @RequestParam(value = "showMetadata",
          defaultValue = "false") String showMetadata)
      throws UnsupportedOperationException, Exception {

    return ElementsRequestExecutor.executeCountLengthPerimeterArea(RequestResource.AREA,
        new RequestParameters(false, true, false, bboxes, bcircles, bpolys, types, keys, values,
            userids, time, showMetadata));
  }

  /**
   * GET request giving the area of OSM objects grouped by the OSM type.
   * <p>
   * The parameters are described in the
   * {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.controller.dataAggregation.CountController#getCount(String, String, String, String[], String[], String[], String[], String[], String)
   * getCount} method.
   * 
   * @return {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.output.dataAggregationResponse.groupByResponse.GroupByResponse
   *         GroupByResponseContent}
   */
  @ApiOperation(value = "Area of OSM elements grouped by the type")
  @RequestMapping(value = "/groupBy/type", method = RequestMethod.GET,
      produces = "application/json")
  public GroupByResponse getAreaGroupByType(
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
      @ApiParam(hidden = true) @RequestParam(value = "showMetadata",
          defaultValue = "false") String showMetadata)
      throws UnsupportedOperationException, Exception {

    return ElementsRequestExecutor.executeCountPerimeterAreaGroupByType(RequestResource.AREA,
        new RequestParameters(false, true, false, bboxes, bcircles, bpolys, types, keys, values,
            userids, time, showMetadata));
  }

  /**
   * GET request giving the area of OSM objects grouped by the userId.
   * <p>
   * The parameters are described in the
   * {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.controller.dataAggregation.CountController#getCount(String, String, String, String[], String[], String[], String[], String[], String)
   * getCount} method.
   * 
   * @return {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.output.dataAggregationResponse.groupByResponse.GroupByResponse
   *         GroupByResponse }
   */
  @ApiOperation(value = "Area of OSM elements grouped by the user")
  @RequestMapping(value = "/groupBy/user", method = RequestMethod.GET,
      produces = "application/json")
  public GroupByResponse getAreaGroupByUser(
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
      @ApiParam(hidden = true) @RequestParam(value = "showMetadata",
          defaultValue = "false") String showMetadata)
      throws UnsupportedOperationException, Exception {

    return ElementsRequestExecutor.executeCountLengthPerimeterAreaGroupByUser(RequestResource.AREA,
        new RequestParameters(false, true, false, bboxes, bcircles, bpolys, types, keys, values,
            userids, time, showMetadata));
  }

  /**
   * GET request giving the area OSM objects grouped by the boundary parameter (bounding
   * box/circle/polygon).
   * <p>
   * The parameters are described in the
   * {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.controller.dataAggregation.CountController#getCount(String, String, String, String[], String[], String[], String[], String[], String)
   * getCount} method.
   * 
   * @return {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.output.dataAggregationResponse.groupByResponse.GroupByResponse
   *         GroupByResponse Content}
   */
  @ApiOperation(
      value = "Area of OSM elements in meter grouped by the boundary (bboxes, bcircles, or bpolys)")
  @RequestMapping(value = "/groupBy/boundary", method = RequestMethod.GET,
      produces = "application/json")
  public GroupByResponse getAreaGroupByBoundary(
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
      @ApiParam(hidden = true) @RequestParam(value = "format", defaultValue = "",
          required = false) String format,
      @ApiParam(hidden = true) @RequestParam(value = "showMetadata",
          defaultValue = "false") String showMetadata)
      throws UnsupportedOperationException, Exception {

    return ElementsRequestExecutor.executeCountLengthPerimeterAreaGroupByBoundary(
        RequestResource.AREA, RequestParameters.of(false, true, false, bboxes, bcircles, bpolys,
            types, keys, values, userids, time, format, showMetadata));
  }

  /**
   * GET request giving the area of OSM objects grouped by the key.
   * <p>
   * The other parameters are described in the
   * {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.controller.dataAggregation.CountController#getCount(String, String, String, String[], String[], String[], String[], String[], String)
   * getCount} method.
   * 
   * @param groupByKeys <code>String</code> array containing the key used to create the tags for the
   *        grouping. One or more keys can be provided.
   * @return {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.output.dataAggregationResponse.groupByResponse.GroupByResponse
   *         GroupByResponse Content}
   */
  @ApiOperation(value = "Count of OSM elements grouped by the key")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "groupByKeys", value = ParameterDescriptions.KEYS_DESCR,
          defaultValue = DefaultSwaggerParameters.BUILDING_KEY, paramType = "query",
          dataType = "string", required = true)})
  @RequestMapping(value = "/groupBy/key", method = RequestMethod.GET, produces = "application/json")
  public GroupByResponse getAreaGroupByKey(
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
      @ApiParam(hidden = true) @RequestParam(value = "showMetadata",
          defaultValue = "false") String showMetadata,
      @RequestParam(value = "groupByKeys", defaultValue = "",
          required = false) String[] groupByKeys)
      throws UnsupportedOperationException, Exception {

    return ElementsRequestExecutor.executeCountLengthPerimeterAreaGroupByKey(RequestResource.AREA,
        new RequestParameters(false, true, false, bboxes, bcircles, bpolys, types, keys, values,
            userids, time, showMetadata),
        groupByKeys);
  }

  /**
   * GET request giving the area of OSM objects grouped by the tag.
   * <p>
   * The other parameters are described in the
   * {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.controller.dataAggregation.CountController#getCount(String, String, String, String[], String[], String[], String[], String[], String)
   * getCount} method.
   * 
   * @param groupByKey <code>String</code> array containing the key used to create the tags for the
   *        grouping. At the current implementation, there must be one key given (not more and not
   *        less).
   * @param groupByValues <code>String</code> array containing the values used to create the tags
   *        for grouping. If a given value does not appear in the output, then there are no objects
   *        assigned to it (within the given filters).
   * @return {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.output.dataAggregationResponse.groupByResponse.GroupByResponse
   *         GroupByResponse }
   */
  @ApiOperation(value = "Area of OSM elements grouped by the tag")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "groupByKey", value = ParameterDescriptions.KEYS_DESCR,
          defaultValue = DefaultSwaggerParameters.BUILDING_KEY, paramType = "query",
          dataType = "string", required = true),
      @ApiImplicitParam(name = "groupByValues", value = ParameterDescriptions.VALUES_DESCR,
          defaultValue = "", paramType = "query", dataType = "string", required = false)})
  @RequestMapping(value = "/groupBy/tag", method = RequestMethod.GET, produces = "application/json")
  public GroupByResponse getAreaGroupByTag(
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
      @ApiParam(hidden = true) @RequestParam(value = "showMetadata",
          defaultValue = "false") String showMetadata,
      @RequestParam(value = "groupByKey", defaultValue = "", required = false) String[] groupByKey,
      @RequestParam(value = "groupByValues", defaultValue = "",
          required = false) String[] groupByValues)
      throws UnsupportedOperationException, Exception {

    return ElementsRequestExecutor
        .executeCountLengthPerimeterAreaGroupByTag(
            RequestResource.AREA, new RequestParameters(false, true, false, bboxes, bcircles,
                bpolys, types, keys, values, userids, time, showMetadata),
            groupByKey, groupByValues);
  }

  /**
   * GET request giving the area of items satisfying keys, values (plus other parameters) and part
   * of items satisfying keys2, values2 (plus other parameters).
   * <p>
   * The other parameters are described in the
   * {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.controller.dataAggregation.CountController#getCount(String, String, String, String[], String[], String[], String[], String[], String)
   * getCount} method.
   * 
   * @param keys2 <code>String</code> array having the same format as keys and used to define the
   *        subgroup(share).
   * @param values2 <code>String</code> array having the same format as values and used to define
   *        the subgroup(share).
   * @return {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.output.dataAggregationResponse.elements.ShareResponse
   *         ShareResponse}
   */
  @ApiOperation(
      value = "Share of area of elements satisfying keys2 and values2 within elements selected by types, keys and values")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "keys2", value = ParameterDescriptions.KEYS_DESCR,
          defaultValue = "addr:street", paramType = "query", dataType = "string", required = true),
      @ApiImplicitParam(name = "values2", value = ParameterDescriptions.VALUES_DESCR,
          defaultValue = "", paramType = "query", dataType = "string", required = false)})
  @RequestMapping(value = "/share", method = RequestMethod.GET, produces = "application/json")
  public RatioShareResponse getAreaShare(
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
      @ApiParam(hidden = true) @RequestParam(value = "showMetadata",
          defaultValue = "false") String showMetadata,
      @RequestParam(value = "keys2", defaultValue = "", required = false) String[] keys2,
      @RequestParam(value = "values2", defaultValue = "", required = false) String[] values2)
      throws UnsupportedOperationException, Exception {

    return ElementsRequestExecutor
        .executeCountLengthPerimeterAreaRatio(
            RequestResource.AREA, new RequestParameters(false, true, false, bboxes, bcircles,
                bpolys, types, keys, values, userids, time, showMetadata),
            types, keys2, values2, true);
  }

  /**
   * GET request giving the area of items satisfying keys, values (plus other parameters) and part
   * of items satisfying keys2, values2 (plus other parameters), grouped by the boundary.
   * <p>
   * The other parameters are described in the
   * {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.controller.dataAggregation.CountController#getCount(String, String, String, String[], String[], String[], String[], String[], String)
   * getCount} method.
   * 
   * @param keys2 <code>String</code> array having the same format as keys and used to define the
   *        subgroup(share).
   * @param values2 <code>String</code> array having the same format as values and used to define
   *        the subgroup(share).
   * @return {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.output.dataAggregationResponse.RatioShareResponse
   *         RatioShareResponse}
   */
  @ApiOperation(value = "Share results of OSM elements grouped by the boundary")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "keys2", value = ParameterDescriptions.KEYS_DESCR,
          defaultValue = "addr:street", paramType = "query", dataType = "string", required = true),
      @ApiImplicitParam(name = "values2", value = ParameterDescriptions.VALUES_DESCR,
          defaultValue = "", paramType = "query", dataType = "string", required = false)})
  @RequestMapping(value = "/share/groupBy/boundary", method = RequestMethod.GET,
      produces = "application/json")
  public RatioShareResponse getAreaShareGroupByBoundary(
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
      @ApiParam(hidden = true) @RequestParam(value = "showMetadata",
          defaultValue = "false") String showMetadata,
      @RequestParam(value = "keys2", defaultValue = "", required = false) String[] keys2,
      @RequestParam(value = "values2", defaultValue = "", required = false) String[] values2)
      throws UnsupportedOperationException, Exception {

    return ElementsRequestExecutor
        .executeCountLengthPerimeterAreaRatioGroupByBoundary(
            RequestResource.AREA, new RequestParameters(false, true, false, bboxes, bcircles,
                bpolys, types, keys, values, userids, time, showMetadata),
            types, keys2, values2, true);
  }

  /**
   * GET request giving the density of selected items (area of items per square-kilometers).
   * <p>
   * The parameters are described in the
   * {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.controller.dataAggregation.CountController#getCount(String, String, String, String[], String[], String[], String[], String[], String)
   * getCount} method.
   * 
   * @return {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.output.dataAggregationResponse.DefaultAggregationResponse
   *         DefaultAggregationResponse}
   */
  @ApiOperation(value = "Density of OSM elements (area of elements per square-kilometers)")
  @RequestMapping(value = "/density", method = RequestMethod.GET, produces = "application/json")
  public DefaultAggregationResponse getAreaDensity(
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
      @ApiParam(hidden = true) @RequestParam(value = "showMetadata",
          defaultValue = "false") String showMetadata)
      throws UnsupportedOperationException, Exception {

    return ElementsRequestExecutor.executeCountLengthPerimeterArea(RequestResource.AREA,
        new RequestParameters(false, true, true, bboxes, bcircles, bpolys, types, keys, values,
            userids, time, showMetadata));
  }

  /**
   * GET request giving the density of selected items (area of items per square-kilometers) grouped
   * by the OSM type.
   * <p>
   * The parameters are described in the
   * {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.controller.dataAggregation.CountController#getCount(String, String, String, String[], String[], String[], String[], String[], String)
   * getCount} method.
   * 
   * @return {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.output.dataAggregationResponse.groupByResponse.GroupByResponse
   *         GroupByResponseContent}
   */
  @ApiOperation(
      value = "Density of OSM elements (area of items per square-kilometers) grouped by the type")
  @RequestMapping(value = "/density/groupBy/type", method = RequestMethod.GET,
      produces = "application/json")
  public GroupByResponse getAreaDensityGroupByType(
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
      @ApiParam(hidden = true) @RequestParam(value = "showMetadata",
          defaultValue = "false") String showMetadata)
      throws UnsupportedOperationException, Exception {

    return ElementsRequestExecutor.executeCountPerimeterAreaGroupByType(RequestResource.AREA,
        new RequestParameters(false, true, true, bboxes, bcircles, bpolys, types, keys, values,
            userids, time, showMetadata));
  }

  /**
   * GET request giving the density of selected items (area of items per square-kilometers) grouped
   * by the tag.
   * <p>
   * The other parameters are described in the
   * {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.controller.dataAggregation.CountController#getCount(String, String, String, String[], String[], String[], String[], String[], String)
   * getCount} method.
   * 
   * @param groupByKey <code>String</code> array containing the key used to create the tags for the
   *        grouping. At the current implementation, there must be one key given (not more and not
   *        less).
   * @param groupByValues <code>String</code> array containing the values used to create the tags
   *        for grouping. If a given value does not appear in the output, then there are no objects
   *        assigned to it (within the given filters).
   * @return {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.output.dataAggregationResponse.groupByResponse.GroupByResponse
   *         GroupByResponse Content}
   */
  @ApiOperation(
      value = "Density of selected items (area of items per square-kilometers) grouped by the tag")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "groupByKey", value = ParameterDescriptions.KEYS_DESCR,
          defaultValue = DefaultSwaggerParameters.BUILDING_KEY, paramType = "query",
          dataType = "string", required = true),
      @ApiImplicitParam(name = "groupByValues", value = ParameterDescriptions.VALUES_DESCR,
          defaultValue = "", paramType = "query", dataType = "string", required = false)})
  @RequestMapping(value = "/density/groupBy/tag", method = RequestMethod.GET,
      produces = "application/json")
  public GroupByResponse getAreaDensityGroupByTag(
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
      @ApiParam(hidden = true) @RequestParam(value = "showMetadata",
          defaultValue = "false") String showMetadata,
      @RequestParam(value = "groupByKey", defaultValue = "", required = false) String[] groupByKey,
      @RequestParam(value = "groupByValues", defaultValue = "",
          required = false) String[] groupByValues)
      throws UnsupportedOperationException, Exception {

    return ElementsRequestExecutor.executeCountLengthPerimeterAreaGroupByTag(RequestResource.AREA,
        new RequestParameters(false, true, true, bboxes, bcircles, bpolys, types, keys, values,
            userids, time, showMetadata),
        groupByKey, groupByValues);
  }

  /**
   * GET request giving the ratio of selected items satisfying types2, keys2 and values2 within
   * items selected by types, keys and values.
   * <p>
   * The other parameters are described in the
   * {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.controller.dataAggregation.CountController#getCount(String, String, String, String[], String[], String[], String[], String[], String)
   * getCount} method.
   * 
   * @param types2 <code>String</code> array having the same format as types.
   * @param keys2 <code>String</code> array having the same format as keys.
   * @param values2 <code>String</code> array having the same format as values.
   * @return {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.output.dataAggregationResponse.RatioShareResponse
   *         RatioShareResponse}
   */
  @ApiOperation(
      value = "Ratio of selected items satisfying types2, keys2 and values2 within items selected by types, keys and values")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "types2", value = ParameterDescriptions.TYPES_DESCR,
          defaultValue = "relation", paramType = "query", dataType = "string", required = false),
      @ApiImplicitParam(name = "keys2", value = ParameterDescriptions.KEYS_DESCR,
          defaultValue = DefaultSwaggerParameters.BUILDING_KEY, paramType = "query",
          dataType = "string", required = false),
      @ApiImplicitParam(name = "values2", value = ParameterDescriptions.VALUES_DESCR,
          defaultValue = "", paramType = "query", dataType = "string", required = false)})
  @RequestMapping(value = "/ratio", method = RequestMethod.GET, produces = "application/json")
  public RatioShareResponse getAreaRatio(
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
      @ApiParam(hidden = true) @RequestParam(value = "showMetadata",
          defaultValue = "false") String showMetadata,
      @RequestParam(value = "types2", defaultValue = "", required = false) String[] types2,
      @RequestParam(value = "keys2", defaultValue = "", required = false) String[] keys2,
      @RequestParam(value = "values2", defaultValue = "", required = false) String[] values2)
      throws UnsupportedOperationException, Exception {

    return ElementsRequestExecutor
        .executeCountLengthPerimeterAreaRatio(
            RequestResource.AREA, new RequestParameters(false, true, false, bboxes, bcircles,
                bpolys, types, keys, values, userids, time, showMetadata),
            types2, keys2, values2, false);
  }

  /**
   * GET request giving the ratio of the area of selected items satisfying types2, keys2 and values2
   * within items selected by types, keys and values grouped by the boundary.
   * <p>
   * The other parameters are described in the
   * {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.controller.dataAggregation.CountController#getCountRatio(String, String, String, String[], String[], String[], String[], String[], String, String[], String[], String[])
   * getCountRatio} method.
   * 
   * @return {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.output.dataAggregationResponse.RatioShareResponse
   *         RatioShareResponse}
   */
  @ApiOperation(value = "Ratio of the area of selected items grouped by the boundary")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "types2", value = ParameterDescriptions.TYPES_DESCR,
          defaultValue = DefaultSwaggerParameters.TYPE, paramType = "query", dataType = "string",
          required = false),
      @ApiImplicitParam(name = "keys2", value = ParameterDescriptions.KEYS_DESCR,
          defaultValue = DefaultSwaggerParameters.BUILDING_KEY, paramType = "query",
          dataType = "string", required = false),
      @ApiImplicitParam(name = "values2", value = ParameterDescriptions.VALUES_DESCR,
          defaultValue = "", paramType = "query", dataType = "string", required = false)})
  @RequestMapping(value = "/ratio/groupBy/boundary", method = RequestMethod.GET,
      produces = "application/json")
  public RatioShareResponse getAreaRatioGroupByBoundary(
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
      @ApiParam(hidden = true) @RequestParam(value = "showMetadata",
          defaultValue = "false") String showMetadata,
      @RequestParam(value = "types2", defaultValue = "", required = false) String[] types2,
      @RequestParam(value = "keys2", defaultValue = "", required = false) String[] keys2,
      @RequestParam(value = "values2", defaultValue = "", required = false) String[] values2)
      throws UnsupportedOperationException, Exception {

    return ElementsRequestExecutor
        .executeCountLengthPerimeterAreaRatioGroupByBoundary(
            RequestResource.AREA, new RequestParameters(false, true, false, bboxes, bcircles,
                bpolys, types, keys, values, userids, time, showMetadata),
            types2, keys2, values2, false);
  }

  /**
   * POST request giving the area of OSM objects. POST requests should only be used if the request
   * URL would be too long for a GET request.
   * <p>
   * The parameters are described in the
   * {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.controller.dataAggregation.CountController#getCount(String, String, String, String[], String[], String[], String[], String[], String)
   * getCount} method.
   * 
   * @return {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.output.dataAggregationResponse.DefaultAggregationResponse
   *         DefaultAggregationResponse}
   */
  @ApiOperation(value = "Area of OSM elements")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "bboxes", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.BBOX, required = false,
          value = ParameterDescriptions.BBOXES_DESCR),
      @ApiImplicitParam(name = "bcircles", paramType = "form", dataType = "string",
          required = false, value = ParameterDescriptions.BCIRCLES_DESCR),
      @ApiImplicitParam(name = "bpolys", paramType = "form", dataType = "string", required = false,
          value = ParameterDescriptions.BPOLYS_DESCR),
      @ApiImplicitParam(name = "types", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.TYPE, required = false,
          value = ParameterDescriptions.TYPES_DESCR),
      @ApiImplicitParam(name = "keys", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.BUILDING_KEY, required = false,
          value = ParameterDescriptions.KEYS_DESCR),
      @ApiImplicitParam(name = "values", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.RESIDENTIAL_VALUE, required = false,
          value = ParameterDescriptions.VALUES_DESCR),
      @ApiImplicitParam(name = "userids", paramType = "form", dataType = "string", required = false,
          value = ParameterDescriptions.USERIDS_DESCR),
      @ApiImplicitParam(name = "time", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.TIME, required = false,
          value = ParameterDescriptions.TIME_DESCR),
      @ApiImplicitParam(name = "showMetadata", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.SHOW_METADATA, required = false,
          value = ParameterDescriptions.SHOW_METADATA_DESCR)})
  @RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json",
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public DefaultAggregationResponse postArea(String bboxes, String bcircles, String bpolys,
      String[] types, String[] keys, String[] values, String[] userids, String[] time,
      String showMetadata) throws UnsupportedOperationException, Exception {

    return ElementsRequestExecutor.executeCountLengthPerimeterArea(RequestResource.AREA,
        new RequestParameters(true, true, false, bboxes, bcircles, bpolys, types, keys, values,
            userids, time, showMetadata));
  }

  /**
   * POST request giving the area of OSM objects grouped by the OSM type. POST requests should only
   * be used if the request URL would be too long for a GET request.
   * <p>
   * The parameters are described in the
   * {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.controller.dataAggregation.CountController#getCount(String, String, String, String[], String[], String[], String[], String[], String)
   * getCount} method.
   * 
   * @return {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.output.dataAggregationResponse.groupByResponse.GroupByResponse
   *         GroupByResponseContent}
   */
  @ApiOperation(value = "Area of OSM elements grouped by the type")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "bboxes", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.BBOX, required = false,
          value = ParameterDescriptions.BBOXES_DESCR),
      @ApiImplicitParam(name = "bcircles", paramType = "form", dataType = "string",
          required = false, value = ParameterDescriptions.BCIRCLES_DESCR),
      @ApiImplicitParam(name = "bpolys", paramType = "form", dataType = "string", required = false,
          value = ParameterDescriptions.BPOLYS_DESCR),
      @ApiImplicitParam(name = "types", paramType = "form", dataType = "string",
          defaultValue = "way, relation", required = false,
          value = ParameterDescriptions.TYPES_DESCR),
      @ApiImplicitParam(name = "keys", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.BUILDING_KEY, required = false,
          value = ParameterDescriptions.KEYS_DESCR),
      @ApiImplicitParam(name = "values", paramType = "form", dataType = "string", defaultValue = "",
          required = false, value = ParameterDescriptions.VALUES_DESCR),
      @ApiImplicitParam(name = "userids", paramType = "form", dataType = "string", required = false,
          value = ParameterDescriptions.USERIDS_DESCR),
      @ApiImplicitParam(name = "time", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.TIME, required = false,
          value = ParameterDescriptions.TIME_DESCR),
      @ApiImplicitParam(name = "showMetadata", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.SHOW_METADATA, required = false,
          value = ParameterDescriptions.SHOW_METADATA_DESCR)})
  @RequestMapping(value = "/groupBy/type", method = RequestMethod.POST,
      produces = "application/json", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public GroupByResponse postAreaGroupByType(String bboxes, String bcircles, String bpolys,
      String[] types, String[] keys, String[] values, String[] userids, String[] time,
      String showMetadata) throws UnsupportedOperationException, Exception, BadRequestException {

    return ElementsRequestExecutor.executeCountPerimeterAreaGroupByType(RequestResource.AREA,
        new RequestParameters(true, true, false, bboxes, bcircles, bpolys, types, keys, values,
            userids, time, showMetadata));
  }

  /**
   * POST request giving the area of OSM objects grouped by the userID. POST requests should only be
   * used if the request URL would be too long for a GET request.
   * <p>
   * The parameters are described in the
   * {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.controller.dataAggregation.CountController#getCount(String, String, String, String[], String[], String[], String[], String[], String)
   * getCount} method.
   * 
   * @return {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.output.dataAggregationResponse.groupByResponse.GroupByResponse
   *         GroupByResponse Content}
   */
  @ApiOperation(value = "Area of OSM elements grouped by the user")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "bboxes", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.BBOX, required = false,
          value = ParameterDescriptions.BBOXES_DESCR),
      @ApiImplicitParam(name = "bcircles", paramType = "form", dataType = "string",
          required = false, value = ParameterDescriptions.BCIRCLES_DESCR),
      @ApiImplicitParam(name = "bpolys", paramType = "form", dataType = "string", required = false,
          value = ParameterDescriptions.BPOLYS_DESCR),
      @ApiImplicitParam(name = "types", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.TYPE, required = false,
          value = ParameterDescriptions.TYPES_DESCR),
      @ApiImplicitParam(name = "keys", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.BUILDING_KEY, required = false,
          value = ParameterDescriptions.KEYS_DESCR),
      @ApiImplicitParam(name = "values", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.RESIDENTIAL_VALUE, required = false,
          value = ParameterDescriptions.VALUES_DESCR),
      @ApiImplicitParam(name = "userids", paramType = "form", dataType = "string", required = false,
          value = ParameterDescriptions.USERIDS_DESCR),
      @ApiImplicitParam(name = "time", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.TIME, required = false,
          value = ParameterDescriptions.TIME_DESCR),
      @ApiImplicitParam(name = "showMetadata", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.SHOW_METADATA, required = false,
          value = ParameterDescriptions.SHOW_METADATA_DESCR)})
  @RequestMapping(value = "/groupBy/user", method = RequestMethod.POST,
      produces = "application/json", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public GroupByResponse postAreaGroupByUser(String bboxes, String bcircles, String bpolys,
      String[] types, String[] keys, String[] values, String[] userids, String[] time,
      String showMetadata) throws UnsupportedOperationException, Exception, BadRequestException {

    return ElementsRequestExecutor.executeCountLengthPerimeterAreaGroupByUser(RequestResource.AREA,
        new RequestParameters(true, true, false, bboxes, bcircles, bpolys, types, keys, values,
            userids, time, showMetadata));
  }

  /**
   * POST request giving the area of OSM objects grouped by the boundary parameter (bounding
   * box/circle/polygon). POST requests should only be used if the request URL would be too long for
   * a GET request.
   * <p>
   * The parameters are described in the
   * {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.controller.dataAggregation.CountController#getCount(String, String, String, String[], String[], String[], String[], String[], String)
   * getCount} method.
   * 
   * @return {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.output.dataAggregationResponse.groupByResponse.GroupByResponse
   *         GroupByResponse Content}
   */
  @ApiOperation(
      value = "Area of OSM elements grouped by the boundary (bboxes, bcircles, or bpolys)")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "bboxes", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.BBOX, required = false,
          value = ParameterDescriptions.BBOXES_DESCR),
      @ApiImplicitParam(name = "bcircles", paramType = "form", dataType = "string",
          required = false, value = ParameterDescriptions.BCIRCLES_DESCR),
      @ApiImplicitParam(name = "bpolys", paramType = "form", dataType = "string", required = false,
          value = ParameterDescriptions.BPOLYS_DESCR),
      @ApiImplicitParam(name = "types", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.TYPE, required = false,
          value = ParameterDescriptions.TYPES_DESCR),
      @ApiImplicitParam(name = "keys", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.BUILDING_KEY, required = false,
          value = ParameterDescriptions.KEYS_DESCR),
      @ApiImplicitParam(name = "values", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.RESIDENTIAL_VALUE, required = false,
          value = ParameterDescriptions.VALUES_DESCR),
      @ApiImplicitParam(name = "userids", paramType = "form", dataType = "string", required = false,
          value = ParameterDescriptions.USERIDS_DESCR),
      @ApiImplicitParam(name = "time", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.TIME, required = false,
          value = ParameterDescriptions.TIME_DESCR),
      @ApiImplicitParam(name = "format", paramType = "form", dataType = "string", required = false,
          value = ParameterDescriptions.FORMAT_DESCR),
      @ApiImplicitParam(name = "showMetadata", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.SHOW_METADATA, required = false,
          value = ParameterDescriptions.SHOW_METADATA_DESCR)})
  @RequestMapping(value = "/groupBy/boundary", method = RequestMethod.POST,
      produces = "application/json", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public GroupByResponse postAreaGroupByBoundary(String bboxes, String bcircles, String bpolys,
      String[] types, String[] keys, String[] values, String[] userids, String[] time,
      String format, String showMetadata)
      throws UnsupportedOperationException, Exception, BadRequestException {

    return ElementsRequestExecutor.executeCountLengthPerimeterAreaGroupByBoundary(
        RequestResource.AREA, RequestParameters.of(true, true, false, bboxes, bcircles, bpolys,
            types, keys, values, userids, time, format, showMetadata));
  }

  /**
   * POST request giving the area of OSM objects grouped by the key. POST requests should only be
   * used if the request URL would be too long for a GET request.
   * <p>
   * The other parameters are described in the
   * {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.controller.dataAggregation.CountController#getCount(String, String, String, String[], String[], String[], String[], String[], String)
   * getCount} method.
   * 
   * @param groupByKeys <code>String</code> array containing the key used to create the tags for the
   *        grouping. At the current implementation, there must be one key given (not more and not
   *        less).
   * @return {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.output.dataAggregationResponse.groupByResponse.GroupByResponse
   *         GroupByResponse Content}
   */
  @ApiOperation(value = "Area of OSM elements grouped by the key")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "bboxes", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.BBOX, required = false,
          value = ParameterDescriptions.BBOXES_DESCR),
      @ApiImplicitParam(name = "bcircles", paramType = "form", dataType = "string",
          required = false, value = ParameterDescriptions.BCIRCLES_DESCR),
      @ApiImplicitParam(name = "bpolys", paramType = "form", dataType = "string", required = false,
          value = ParameterDescriptions.BPOLYS_DESCR),
      @ApiImplicitParam(name = "types", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.TYPE, required = false,
          value = ParameterDescriptions.TYPES_DESCR),
      @ApiImplicitParam(name = "keys", paramType = "form", dataType = "string", defaultValue = "",
          required = false, value = ParameterDescriptions.KEYS_DESCR),
      @ApiImplicitParam(name = "values", paramType = "form", dataType = "string", defaultValue = "",
          required = false, value = ParameterDescriptions.VALUES_DESCR),
      @ApiImplicitParam(name = "userids", paramType = "form", dataType = "string", required = false,
          value = ParameterDescriptions.USERIDS_DESCR),
      @ApiImplicitParam(name = "time", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.TIME, required = false,
          value = ParameterDescriptions.TIME_DESCR),
      @ApiImplicitParam(name = "showMetadata", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.SHOW_METADATA, required = false,
          value = ParameterDescriptions.SHOW_METADATA_DESCR),
      @ApiImplicitParam(name = "groupByKeys", paramType = "form", dataType = "string",
          defaultValue = "addr:street", required = true, value = ParameterDescriptions.KEYS_DESCR)})
  @RequestMapping(value = "/groupBy/key", method = RequestMethod.POST,
      produces = "application/json", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public GroupByResponse postAreaGroupByKey(String bboxes, String bcircles, String bpolys,
      String[] types, String[] keys, String[] values, String[] userids, String[] time,
      String showMetadata, String[] groupByKeys)
      throws UnsupportedOperationException, Exception, BadRequestException {

    return ElementsRequestExecutor.executeCountLengthPerimeterAreaGroupByKey(RequestResource.AREA,
        new RequestParameters(true, true, false, bboxes, bcircles, bpolys, types, keys, values,
            userids, time, showMetadata),
        groupByKeys);
  }

  /**
   * POST request giving the area of OSM objects grouped by the tag. POST requests should only be
   * used if the request URL would be too long for a GET request.
   * <p>
   * The other parameters are described in the
   * {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.controller.dataAggregation.CountController#getCount(String, String, String, String[], String[], String[], String[], String[], String)
   * getCount} method.
   * 
   * @param groupByKey <code>String</code> array containing the key used to create the tags for the
   *        grouping. At the current implementation, there must be one key given (not more and not
   *        less).
   * @param groupByValues <code>String</code> array containing the values used to create the tags
   *        for grouping. If a given value does not appear in the output, then there are no objects
   *        assigned to it (within the given filters).
   * @return {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.output.dataAggregationResponse.groupByResponse.GroupByResponse
   *         GroupByResponse Content}
   */
  @ApiOperation(value = "Area of OSM elements grouped by the tag")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "bboxes", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.BBOX, required = false,
          value = ParameterDescriptions.BBOXES_DESCR),
      @ApiImplicitParam(name = "bcircles", paramType = "form", dataType = "string",
          required = false, value = ParameterDescriptions.BCIRCLES_DESCR),
      @ApiImplicitParam(name = "bpolys", paramType = "form", dataType = "string", required = false,
          value = ParameterDescriptions.BPOLYS_DESCR),
      @ApiImplicitParam(name = "types", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.TYPE, required = false,
          value = ParameterDescriptions.TYPES_DESCR),
      @ApiImplicitParam(name = "keys", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.BUILDING_KEY, required = false,
          value = ParameterDescriptions.KEYS_DESCR),
      @ApiImplicitParam(name = "values", paramType = "form", dataType = "string", defaultValue = "",
          required = false, value = ParameterDescriptions.VALUES_DESCR),
      @ApiImplicitParam(name = "userids", paramType = "form", dataType = "string", required = false,
          value = ParameterDescriptions.USERIDS_DESCR),
      @ApiImplicitParam(name = "time", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.TIME, required = false,
          value = ParameterDescriptions.TIME_DESCR),
      @ApiImplicitParam(name = "showMetadata", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.SHOW_METADATA, required = false,
          value = ParameterDescriptions.SHOW_METADATA_DESCR),
      @ApiImplicitParam(name = "groupByKey", paramType = "form", dataType = "string",
          defaultValue = "addr:postcode", required = true,
          value = ParameterDescriptions.KEYS_DESCR),
      @ApiImplicitParam(name = "groupByValues", paramType = "form", dataType = "string",
          defaultValue = "", required = false, value = ParameterDescriptions.VALUES_DESCR)})
  @RequestMapping(value = "/groupBy/tag", method = RequestMethod.POST,
      produces = "application/json", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public GroupByResponse postAreaGroupByTag(String bboxes, String bcircles, String bpolys,
      String[] types, String[] keys, String[] values, String[] userids, String[] time,
      String showMetadata, String[] groupByKey, String[] groupByValues)
      throws UnsupportedOperationException, Exception, BadRequestException {

    return ElementsRequestExecutor.executeCountLengthPerimeterAreaGroupByTag(RequestResource.AREA,
        new RequestParameters(true, true, false, bboxes, bcircles, bpolys, types, keys, values,
            userids, time, showMetadata),
        groupByKey, groupByValues);
  }

  /**
   * POST request giving the share of selected items satisfying keys2 and values2 within items
   * selected by types, keys and values. POST requests should only be used if the request URL would
   * be too long for a GET request.
   * <p>
   * The parameters are described in the
   * {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.controller.dataAggregation.CountController#getCount(String, String, String, String[], String[], String[], String[], String[], String)
   * getCount} method.
   * 
   * @param keys2 <code>String</code> array having the same format as keys and used to define the
   *        subgroup(share).
   * @param values2 <code>String</code> array having the same format as values and used to define
   *        the subgroup(share).
   * @return {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.output.dataAggregationResponse.elements.ShareResponse
   *         ShareResponse}
   */
  @ApiOperation(
      value = "Share of area of elements satisfying keys2 and values2 within elements selected by types, keys and values")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "bboxes", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.BBOX, required = false,
          value = ParameterDescriptions.BBOXES_DESCR),
      @ApiImplicitParam(name = "bcircles", paramType = "form", dataType = "string",
          required = false, value = ParameterDescriptions.BCIRCLES_DESCR),
      @ApiImplicitParam(name = "bpolys", paramType = "form", dataType = "string", required = false,
          value = ParameterDescriptions.BPOLYS_DESCR),
      @ApiImplicitParam(name = "types", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.TYPE, required = false,
          value = ParameterDescriptions.TYPES_DESCR),
      @ApiImplicitParam(name = "keys", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.BUILDING_KEY, required = false,
          value = ParameterDescriptions.KEYS_DESCR),
      @ApiImplicitParam(name = "values", paramType = "form", dataType = "string", defaultValue = "",
          required = false, value = ParameterDescriptions.VALUES_DESCR),
      @ApiImplicitParam(name = "userids", paramType = "form", dataType = "string", required = false,
          value = ParameterDescriptions.USERIDS_DESCR),
      @ApiImplicitParam(name = "time", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.TIME, required = false,
          value = ParameterDescriptions.TIME_DESCR),
      @ApiImplicitParam(name = "showMetadata", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.SHOW_METADATA, required = false,
          value = ParameterDescriptions.SHOW_METADATA_DESCR),
      @ApiImplicitParam(name = "keys2", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.BUILDING_KEY, required = true,
          value = ParameterDescriptions.KEYS_DESCR),
      @ApiImplicitParam(name = "values2", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.RESIDENTIAL_VALUE, required = false,
          value = ParameterDescriptions.VALUES_DESCR)})
  @RequestMapping(value = "/share", method = RequestMethod.POST, produces = "application/json",
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public RatioShareResponse postAreaShare(String bboxes, String bcircles, String bpolys,
      String[] types, String[] keys, String[] values, String[] userids, String[] time,
      String showMetadata, String[] keys2, String[] values2)
      throws UnsupportedOperationException, Exception, BadRequestException {

    return ElementsRequestExecutor.executeCountLengthPerimeterAreaRatio(RequestResource.AREA,
        new RequestParameters(true, true, false, bboxes, bcircles, bpolys, types, keys, values,
            userids, time, showMetadata),
        types, keys2, values2, true);
  }

  /**
   * POST request giving the share of selected items satisfying keys2 and values2 within items
   * selected by types, keys and values, grouped by the boundary. POST requests should only be used
   * if the request URL would be too long for a GET request.
   * <p>
   * The parameters are described in the
   * {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.controller.dataAggregation.CountController#getCount(String, String, String, String[], String[], String[], String[], String[], String)
   * getCount} method.
   * 
   * @param keys2 <code>String</code> array having the same format as keys and used to define the
   *        subgroup(share).
   * @param values2 <code>String</code> array having the same format as values and used to define
   *        the subgroup(share).
   * @return {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.output.dataAggregationResponse.RatioShareResponse
   *         RatioShareResponse}
   */
  @ApiOperation(value = "Share results of OSM elements grouped by the boundary")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "bboxes", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.BBOX, required = false,
          value = ParameterDescriptions.BBOXES_DESCR),
      @ApiImplicitParam(name = "bcircles", paramType = "form", dataType = "string",
          required = false, value = ParameterDescriptions.BCIRCLES_DESCR),
      @ApiImplicitParam(name = "bpolys", paramType = "form", dataType = "string", required = false,
          value = ParameterDescriptions.BPOLYS_DESCR),
      @ApiImplicitParam(name = "types", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.TYPE, required = false,
          value = ParameterDescriptions.TYPES_DESCR),
      @ApiImplicitParam(name = "keys", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.BUILDING_KEY, required = false,
          value = ParameterDescriptions.KEYS_DESCR),
      @ApiImplicitParam(name = "values", paramType = "form", dataType = "string", defaultValue = "",
          required = false, value = ParameterDescriptions.VALUES_DESCR),
      @ApiImplicitParam(name = "userids", paramType = "form", dataType = "string", required = false,
          value = ParameterDescriptions.USERIDS_DESCR),
      @ApiImplicitParam(name = "time", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.TIME, required = false,
          value = ParameterDescriptions.TIME_DESCR),
      @ApiImplicitParam(name = "showMetadata", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.SHOW_METADATA, required = false,
          value = ParameterDescriptions.SHOW_METADATA_DESCR),
      @ApiImplicitParam(name = "keys2", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.BUILDING_KEY, required = true,
          value = ParameterDescriptions.KEYS_DESCR),
      @ApiImplicitParam(name = "values2", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.RESIDENTIAL_VALUE, required = false,
          value = ParameterDescriptions.VALUES_DESCR)})
  @RequestMapping(value = "/share/groupBy/boundary", method = RequestMethod.POST,
      produces = "application/json", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public RatioShareResponse postAreaShareGroupByBoundary(String bboxes, String bcircles,
      String bpolys, String[] types, String[] keys, String[] values, String[] userids,
      String[] time, String showMetadata, String[] keys2, String[] values2)
      throws UnsupportedOperationException, Exception, BadRequestException {

    return ElementsRequestExecutor.executeCountLengthPerimeterAreaRatioGroupByBoundary(
        RequestResource.AREA, new RequestParameters(true, true, false, bboxes, bcircles, bpolys,
            types, keys, values, userids, time, showMetadata),
        types, keys2, values2, true);
  }

  /**
   * POST request giving the density of OSM elements (area of elements per square-kilometers). POST
   * requests should only be used if the request URL would be too long for a GET request.
   * <p>
   * The parameters are described in the
   * {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.controller.dataAggregation.CountController#getCount(String, String, String, String[], String[], String[], String[], String[], String)
   * getCount} method.
   * 
   * @return {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.output.dataAggregationResponse.DefaultAggregationResponse
   *         DefaultAggregationResponse}
   */
  @ApiOperation(value = "Density of OSM elements (area of elements per square-kilometers)")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "bboxes", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.BBOX, required = false,
          value = ParameterDescriptions.BBOXES_DESCR),
      @ApiImplicitParam(name = "bcircles", paramType = "form", dataType = "string",
          required = false, value = ParameterDescriptions.BCIRCLES_DESCR),
      @ApiImplicitParam(name = "bpolys", paramType = "form", dataType = "string", required = false,
          value = ParameterDescriptions.BPOLYS_DESCR),
      @ApiImplicitParam(name = "types", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.TYPE, required = false,
          value = ParameterDescriptions.TYPES_DESCR),
      @ApiImplicitParam(name = "keys", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.BUILDING_KEY, required = false,
          value = ParameterDescriptions.KEYS_DESCR),
      @ApiImplicitParam(name = "values", paramType = "form", dataType = "string", defaultValue = "",
          required = false, value = ParameterDescriptions.VALUES_DESCR),
      @ApiImplicitParam(name = "userids", paramType = "form", dataType = "string", required = false,
          value = ParameterDescriptions.USERIDS_DESCR),
      @ApiImplicitParam(name = "time", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.TIME, required = false,
          value = ParameterDescriptions.TIME_DESCR),
      @ApiImplicitParam(name = "showMetadata", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.SHOW_METADATA, required = false,
          value = ParameterDescriptions.SHOW_METADATA_DESCR)})
  @RequestMapping(value = "/density", method = RequestMethod.POST, produces = "application/json",
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public DefaultAggregationResponse postAreaDensity(String bboxes, String bcircles, String bpolys,
      String[] types, String[] keys, String[] values, String[] userids, String[] time,
      String showMetadata) throws UnsupportedOperationException, Exception, BadRequestException {

    return ElementsRequestExecutor.executeCountLengthPerimeterArea(RequestResource.AREA,
        new RequestParameters(true, true, true, bboxes, bcircles, bpolys, types, keys, values,
            userids, time, showMetadata));
  }

  /**
   * POST request giving the density of OSM elements (area of items per square-kilometers) grouped
   * by the type. POST requests should only be used if the request URL would be too long for a GET
   * request.
   * <p>
   * The parameters are described in the
   * {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.controller.dataAggregation.CountController#getCount(String, String, String, String[], String[], String[], String[], String[], String)
   * getCount} method.
   * 
   * @return {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.output.dataAggregationResponse.groupByResponse.GroupByResponse
   *         GroupByResponseContent}
   */
  @ApiOperation(
      value = "Density of OSM elements (area of items per square-kilometers) grouped by the type")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "bboxes", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.BBOX, required = false,
          value = ParameterDescriptions.BBOXES_DESCR),
      @ApiImplicitParam(name = "bcircles", paramType = "form", dataType = "string",
          required = false, value = ParameterDescriptions.BCIRCLES_DESCR),
      @ApiImplicitParam(name = "bpolys", paramType = "form", dataType = "string", required = false,
          value = ParameterDescriptions.BPOLYS_DESCR),
      @ApiImplicitParam(name = "types", paramType = "form", dataType = "string",
          defaultValue = "way, relation", required = false,
          value = ParameterDescriptions.TYPES_DESCR),
      @ApiImplicitParam(name = "keys", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.BUILDING_KEY, required = false,
          value = ParameterDescriptions.KEYS_DESCR),
      @ApiImplicitParam(name = "values", paramType = "form", dataType = "string", defaultValue = "",
          required = false, value = ParameterDescriptions.VALUES_DESCR),
      @ApiImplicitParam(name = "userids", paramType = "form", dataType = "string", required = false,
          value = ParameterDescriptions.USERIDS_DESCR),
      @ApiImplicitParam(name = "time", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.TIME, required = false,
          value = ParameterDescriptions.TIME_DESCR),
      @ApiImplicitParam(name = "showMetadata", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.SHOW_METADATA, required = false,
          value = ParameterDescriptions.SHOW_METADATA_DESCR)})
  @RequestMapping(value = "/density/groupBy/type", method = RequestMethod.POST,
      produces = "application/json", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public GroupByResponse postAreaDensityGroupByType(String bboxes, String bcircles, String bpolys,
      String[] types, String[] keys, String[] values, String[] userids, String[] time,
      String showMetadata) throws UnsupportedOperationException, Exception, BadRequestException {

    return ElementsRequestExecutor.executeCountPerimeterAreaGroupByType(RequestResource.AREA,
        new RequestParameters(true, true, true, bboxes, bcircles, bpolys, types, keys, values,
            userids, time, showMetadata));
  }

  /**
   * POST request giving the density of selected items (area of items per square-kilometers) grouped
   * by the tag. POST requests should only be used if the request URL would be too long for a GET
   * request.
   * <p>
   * The other parameters are described in the
   * {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.controller.dataAggregation.CountController#getCount(String, String, String, String[], String[], String[], String[], String[], String)
   * getCount} method.
   * 
   * @param groupByKey <code>String</code> array containing the key used to create the tags for the
   *        grouping. At the current implementation, there must be one key given (not more and not
   *        less).
   * @param groupByValues <code>String</code> array containing the values used to create the tags
   *        for grouping. If a given value does not appear in the output, then there are no objects
   *        assigned to it (within the given filters).
   * @return {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.output.dataAggregationResponse.groupByResponse.GroupByResponse
   *         GroupByResponse Content}
   */
  @ApiOperation(
      value = "Density of selected items (area of items per square-kilometers) grouped by the tag")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "bboxes", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.BBOX, required = false,
          value = ParameterDescriptions.BBOXES_DESCR),
      @ApiImplicitParam(name = "bcircles", paramType = "form", dataType = "string",
          required = false, value = ParameterDescriptions.BCIRCLES_DESCR),
      @ApiImplicitParam(name = "bpolys", paramType = "form", dataType = "string", required = false,
          value = ParameterDescriptions.BPOLYS_DESCR),
      @ApiImplicitParam(name = "types", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.TYPE, required = false,
          value = ParameterDescriptions.TYPES_DESCR),
      @ApiImplicitParam(name = "keys", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.BUILDING_KEY, required = false,
          value = ParameterDescriptions.KEYS_DESCR),
      @ApiImplicitParam(name = "values", paramType = "form", dataType = "string", defaultValue = "",
          required = false, value = ParameterDescriptions.VALUES_DESCR),
      @ApiImplicitParam(name = "userids", paramType = "form", dataType = "string", required = false,
          value = ParameterDescriptions.USERIDS_DESCR),
      @ApiImplicitParam(name = "time", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.TIME, required = false,
          value = ParameterDescriptions.TIME_DESCR),
      @ApiImplicitParam(name = "showMetadata", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.SHOW_METADATA, required = false,
          value = ParameterDescriptions.SHOW_METADATA_DESCR),
      @ApiImplicitParam(name = "groupByKey", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.BUILDING_KEY, required = true,
          value = ParameterDescriptions.KEYS_DESCR),
      @ApiImplicitParam(name = "groupByValues", paramType = "form", dataType = "string",
          defaultValue = "", required = false, value = ParameterDescriptions.VALUES_DESCR)})
  @RequestMapping(value = "/density/groupBy/tag", method = RequestMethod.POST,
      produces = "application/json", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public GroupByResponse postAreaDensityGroupByTag(String bboxes, String bcircles, String bpolys,
      String[] types, String[] keys, String[] values, String[] userids, String[] time,
      String showMetadata, String[] groupByKey, String[] groupByValues)
      throws UnsupportedOperationException, Exception, BadRequestException {

    return ElementsRequestExecutor.executeCountLengthPerimeterAreaGroupByTag(RequestResource.AREA,
        new RequestParameters(true, true, true, bboxes, bcircles, bpolys, types, keys, values,
            userids, time, showMetadata),
        groupByKey, groupByValues);
  }

  /**
   * POST request giving the ratio of selected items satisfying types2, keys2 and values2 within
   * items selected by types, keys and values. POST requests should only be used if the request URL
   * would be too long for a GET request.
   * <p>
   * The parameters are described in the
   * {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.controller.dataAggregation.CountController#getCount(String, String, String, String[], String[], String[], String[], String[], String)
   * getCount} method.
   * 
   * @param types2 <code>String</code> array having the same format as types.
   * @param keys2 <code>String</code> array having the same format as keys.
   * @param values2 <code>String</code> array having the same format as values.
   * @return {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.output.dataAggregationResponse.RatioShareResponse
   *         RatioShareResponse}
   */
  @ApiOperation(
      value = "Ratio of selected items satisfying types2, keys2 and values2 within items selected by types, keys and values")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "bboxes", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.BBOX, required = false,
          value = ParameterDescriptions.BBOXES_DESCR),
      @ApiImplicitParam(name = "bcircles", paramType = "form", dataType = "string",
          required = false, value = ParameterDescriptions.BCIRCLES_DESCR),
      @ApiImplicitParam(name = "bpolys", paramType = "form", dataType = "string", required = false,
          value = ParameterDescriptions.BPOLYS_DESCR),
      @ApiImplicitParam(name = "types", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.TYPE, required = false,
          value = ParameterDescriptions.TYPES_DESCR),
      @ApiImplicitParam(name = "keys", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.BUILDING_KEY, required = false,
          value = ParameterDescriptions.KEYS_DESCR),
      @ApiImplicitParam(name = "values", paramType = "form", dataType = "string", defaultValue = "",
          required = false, value = ParameterDescriptions.VALUES_DESCR),
      @ApiImplicitParam(name = "userids", paramType = "form", dataType = "string", required = false,
          value = ParameterDescriptions.USERIDS_DESCR),
      @ApiImplicitParam(name = "time", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.TIME, required = false,
          value = ParameterDescriptions.TIME_DESCR),
      @ApiImplicitParam(name = "showMetadata", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.SHOW_METADATA, required = false,
          value = ParameterDescriptions.SHOW_METADATA_DESCR),
      @ApiImplicitParam(name = "types2", value = ParameterDescriptions.TYPES_DESCR,
          defaultValue = "relation", paramType = "query", dataType = "string", required = false),
      @ApiImplicitParam(name = "keys2", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.BUILDING_KEY, required = false,
          value = ParameterDescriptions.KEYS_DESCR),
      @ApiImplicitParam(name = "values2", paramType = "form", dataType = "string",
          defaultValue = "", required = false, value = ParameterDescriptions.VALUES_DESCR)})
  @RequestMapping(value = "/ratio", method = RequestMethod.POST, produces = "application/json",
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public RatioShareResponse postAreaRatio(String bboxes, String bcircles, String bpolys,
      String[] types, String[] keys, String[] values, String[] userids, String[] time,
      String showMetadata, String[] types2, String[] keys2, String[] values2)
      throws UnsupportedOperationException, Exception, BadRequestException {

    return ElementsRequestExecutor.executeCountLengthPerimeterAreaRatio(RequestResource.AREA,
        new RequestParameters(true, true, false, bboxes, bcircles, bpolys, types, keys, values,
            userids, time, showMetadata),
        types2, keys2, values2, false);
  }

  /**
   * POST request giving the ratio of the area of selected items satisfying types2, keys2 and
   * values2 within items selected by types, keys and values grouped by the boundary. POST requests
   * should only be used if the request URL would be too long for a GET request.
   * <p>
   * The other parameters are described in the
   * {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.controller.dataAggregation.CountController#getCountRatio(String, String, String, String[], String[], String[], String[], String[], String, String[], String[], String[])
   * getCountRatio} method.
   * 
   * @return {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.output.dataAggregationResponse.RatioShareResponse
   *         RatioShareResponse}
   */
  @ApiOperation(value = "Ratio of the area of selected items grouped by the boundary")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "bboxes", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.BBOX, required = false,
          value = ParameterDescriptions.BBOXES_DESCR),
      @ApiImplicitParam(name = "bcircles", paramType = "form", dataType = "string",
          required = false, value = ParameterDescriptions.BCIRCLES_DESCR),
      @ApiImplicitParam(name = "bpolys", paramType = "form", dataType = "string", required = false,
          value = ParameterDescriptions.BPOLYS_DESCR),
      @ApiImplicitParam(name = "types", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.TYPE, required = false,
          value = ParameterDescriptions.TYPES_DESCR),
      @ApiImplicitParam(name = "keys", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.BUILDING_KEY, required = false,
          value = ParameterDescriptions.KEYS_DESCR),
      @ApiImplicitParam(name = "values", paramType = "form", dataType = "string", defaultValue = "",
          required = false, value = ParameterDescriptions.VALUES_DESCR),
      @ApiImplicitParam(name = "userids", paramType = "form", dataType = "string", required = false,
          value = ParameterDescriptions.USERIDS_DESCR),
      @ApiImplicitParam(name = "time", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.TIME, required = false,
          value = ParameterDescriptions.TIME_DESCR),
      @ApiImplicitParam(name = "showMetadata", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.SHOW_METADATA, required = false,
          value = ParameterDescriptions.SHOW_METADATA_DESCR),
      @ApiImplicitParam(name = "types2", paramType = "form", dataType = "string",
          defaultValue = "relation", required = true, value = ParameterDescriptions.TYPES_DESCR),
      @ApiImplicitParam(name = "keys2", paramType = "form", dataType = "string",
          defaultValue = DefaultSwaggerParameters.BUILDING_KEY, required = true,
          value = ParameterDescriptions.KEYS_DESCR),
      @ApiImplicitParam(name = "values2", paramType = "form", dataType = "string",
          defaultValue = "", required = false, value = ParameterDescriptions.VALUES_DESCR)})
  @RequestMapping(value = "/ratio/groupBy/boundary", method = RequestMethod.POST,
      produces = "application/json", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public RatioShareResponse postAreaRatioGroupByBoundary(String bboxes, String bcircles,
      String bpolys, String[] types, String[] keys, String[] values, String[] userids,
      String[] time, String showMetadata, String[] types2, String[] keys2, String[] values2)
      throws UnsupportedOperationException, Exception {

    return ElementsRequestExecutor.executeCountLengthPerimeterAreaRatioGroupByBoundary(
        RequestResource.AREA, new RequestParameters(true, true, false, bboxes, bcircles, bpolys,
            types, keys, values, userids, time, showMetadata),
        types2, keys2, values2, false);
  }

}
