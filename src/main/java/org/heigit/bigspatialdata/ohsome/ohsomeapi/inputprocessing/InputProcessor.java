package org.heigit.bigspatialdata.ohsome.ohsomeapi.inputprocessing;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.geojson.GeoJsonObject;
import org.heigit.bigspatialdata.ohsome.ohsomeapi.exception.BadRequestException;
import org.heigit.bigspatialdata.ohsome.ohsomeapi.executor.RequestParameters;
import org.heigit.bigspatialdata.ohsome.ohsomeapi.oshdb.DbConnData;
import org.heigit.bigspatialdata.ohsome.ohsomeapi.oshdb.ExtractMetadata;
import org.heigit.bigspatialdata.oshdb.api.mapreducer.MapReducer;
import org.heigit.bigspatialdata.oshdb.api.mapreducer.OSMContributionView;
import org.heigit.bigspatialdata.oshdb.api.mapreducer.OSMEntitySnapshotView;
import org.heigit.bigspatialdata.oshdb.api.object.OSHDBMapReducible;
import org.heigit.bigspatialdata.oshdb.osm.OSMType;
import org.heigit.bigspatialdata.oshdb.util.time.ISODateTimeParser;
import org.heigit.bigspatialdata.oshdb.util.time.OSHDBTimestamps;
import org.wololo.jts2geojson.GeoJSONWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygonal;

/**
 * Holds general input processing and validation methods and validates specific parameters given by
 * the request. Uses geometry methods from
 * {@link org.heigit.bigspatialdata.ohsome.ohsomeapi.inputprocessing.GeometryBuilder
 * GeometryBuilder} and inputProcessingUtils from
 * {@link org.heigit.bigspatialdata.ohsome.ohsomeapi.inputprocessing.InputProcessingUtils
 * InputProcessingUtils}. Throws exceptions depending on their validity.
 */
public class InputProcessor {

  private GeometryBuilder geomBuilder;
  private InputProcessingUtils utils;

  /**
   * Processes the input parameters from the given request.
   * 
   * <p>
   * The other parameters are described in the
   * {@link org.heigit.bigspatialdata.ohsome.ohsomeapi.controller.dataaggregation.CountController#count(String, String, String, String[], String[], String[], String[], String[], String, HttpServletRequest)
   * count} method.
   * 
   * @return {@link org.heigit.bigspatialdata.oshdb.api.mapreducer.MapReducer MapReducer} object
   *         including the settings derived from the given parameters.
   */
  @SuppressWarnings("unchecked") // intentionally unchecked
  public <T extends OSHDBMapReducible> MapReducer<T> processParameters(
      MapReducer<? extends OSHDBMapReducible> mapRed, RequestParameters requestParameters)
      throws Exception {
    String bboxes = requestParameters.getBboxes();
    String bcircles = requestParameters.getBcircles();
    String bpolys = requestParameters.getBpolys();
    String[] types = requestParameters.getTypes();
    String[] keys = requestParameters.getKeys();
    String[] values = requestParameters.getValues();
    String[] time = requestParameters.getTime();
    String[] userids = requestParameters.getUserids();
    ProcessingData.format = requestParameters.getFormat();
    geomBuilder = new GeometryBuilder();
    utils = new InputProcessingUtils();
    String requestMethod = requestParameters.getRequestMethod();
    if (requestMethod.equalsIgnoreCase("post")) {
      bboxes = createEmptyStringIfNull(bboxes);
      bcircles = createEmptyStringIfNull(bcircles);
      bpolys = createEmptyStringIfNull(bpolys);
      types = createEmptyArrayIfNull(types);
      keys = createEmptyArrayIfNull(keys);
      values = createEmptyArrayIfNull(values);
      userids = createEmptyArrayIfNull(userids);
      time = createEmptyArrayIfNull(time);
    }
    boolean isSnapshot = requestParameters.isSnapshot();
    if (isSnapshot) {
      if (DbConnData.keytables == null) {
        mapRed = OSMEntitySnapshotView.on(DbConnData.h2Db);
      } else if (DbConnData.igniteDb == null) {
        mapRed = OSMEntitySnapshotView.on(DbConnData.h2Db).keytables(DbConnData.keytables);
      } else {
        mapRed = OSMEntitySnapshotView.on(DbConnData.igniteDb).keytables(DbConnData.keytables);
      }
    } else {
      if (DbConnData.keytables == null) {
        mapRed = OSMContributionView.on(DbConnData.h2Db);
      } else if (DbConnData.igniteDb == null) {
        mapRed = OSMContributionView.on(DbConnData.h2Db).keytables(DbConnData.keytables);
      } else {
        mapRed = OSMContributionView.on(DbConnData.igniteDb).keytables(DbConnData.keytables);
      }
    }
    String showMetadata = requestParameters.getShowMetadata();
    if (showMetadata == null) {
      ProcessingData.showMetadata = false;
    } else if (showMetadata.replaceAll("\\s", "").equalsIgnoreCase("true")
        || showMetadata.replaceAll("\\s", "").equalsIgnoreCase("yes")) {
      ProcessingData.showMetadata = true;
    } else if (showMetadata.replaceAll("\\s", "").equalsIgnoreCase("false")
        || showMetadata.replaceAll("\\s", "").equals("")
        || showMetadata.replaceAll("\\s", "").equalsIgnoreCase("no")) {
      ProcessingData.showMetadata = false;
    } else {
      throw new BadRequestException(
          "The showMetadata parameter can only contain the values 'true', 'yes', 'false', or "
              + "'no'.");
    }
    ProcessingData.boundary = setBoundaryType(bboxes, bcircles, bpolys);
    try {
      switch (ProcessingData.boundary) {
        case NOBOUNDARY:
          if (ExtractMetadata.dataPoly == null) {
            throw new BadRequestException(
                "You need to define one boundary parameter (bboxes, bcircles, or bpolys).");
          }
          mapRed = mapRed.areaOfInterest((Geometry & Polygonal) ExtractMetadata.dataPoly);
          break;
        case BBOXES:
          ProcessingData.boundaryValues = utils.splitBboxes(bboxes).toArray(new String[1]);
          mapRed = mapRed.areaOfInterest(
              (Geometry & Polygonal) geomBuilder.createBboxes(ProcessingData.boundaryValues));
          break;
        case BCIRCLES:
          ProcessingData.boundaryValues = utils.splitBcircles(bcircles).toArray(new String[1]);
          mapRed = mapRed.areaOfInterest((Geometry & Polygonal) geomBuilder
              .createCircularPolygons(ProcessingData.boundaryValues));
          break;
        case BPOLYS:
          if (bpolys.replaceAll("\\s", "").startsWith("{")) {
            mapRed = mapRed.areaOfInterest(
                (Geometry & Polygonal) geomBuilder.createGeometryFromGeoJson(bpolys, this));
          } else {
            ProcessingData.boundaryValues = utils.splitBpolys(bpolys).toArray(new String[1]);
            mapRed = mapRed.areaOfInterest(
                (Geometry & Polygonal) geomBuilder.createBpolys(ProcessingData.boundaryValues));
          }
          break;
        default:
          throw new BadRequestException(
              "Your provided boundary parameter (bboxes, bcircles, or bpolys) does not fit its "
                  + "format, or you defined more than one boundary parameter.");
      }
    } catch (ClassCastException e) {
      throw new BadRequestException(
          "The content of the provided boundary parameter (bboxes, bcircles, or bpolys) "
              + "cannot be processed.");
    }
    checkFormat(ProcessingData.format);
    if (ProcessingData.format != null && ProcessingData.format.equalsIgnoreCase("geojson")) {
      GeoJSONWriter writer = new GeoJSONWriter();
      Collection<Geometry> boundaryColl = geomBuilder.getBoundaryColl();
      GeoJsonObject[] geoJsonGeoms = new GeoJsonObject[boundaryColl.size()];
      for (int i = 0; i < geoJsonGeoms.length; i++) {
        try {
          geoJsonGeoms[i] = new ObjectMapper().readValue(
              writer.write((Geometry) boundaryColl.toArray()[i]).toString(), GeoJsonObject.class);
        } catch (IOException e) {
          throw new BadRequestException(
              "The geometry of your given boundary input could not be parsed "
                  + "for the creation of the response GeoJSON.");
        }
      }
      geomBuilder.setGeoJsonGeoms(geoJsonGeoms);
    }
    defineOSMTypes(types);
    mapRed = mapRed.osmType(ProcessingData.osmTypes);
    mapRed = extractTime(mapRed, time, isSnapshot);
    mapRed = extractKeysValues(mapRed, keys, values);
    if (userids.length != 0) {
      checkUserids(userids);
      Set<Integer> useridSet = new HashSet<>();
      for (String user : userids) {
        useridSet.add(Integer.valueOf(user));
      }
      mapRed = mapRed.osmEntityFilter(entity -> {
        return useridSet.contains(entity.getUserId());
      });
    } else {
      // do nothing --> all users will be used
    }
    return (MapReducer<T>) mapRed;
  }

  /**
   * Defines the OSMType(s) out of the given String[].
   * 
   * @param types <code>String</code> array containing one, two, or all 3 OSM types (node, way,
   *        relation). If the array is empty, all three types are used.
   * @return <code>EnumSet</code> containing the requested OSM type(s).
   * @throws BadRequestException if the content of the parameter does not represent one, two, or all
   *         three OSM types
   */
  public void defineOSMTypes(String[] types) throws BadRequestException {
    types = createEmptyArrayIfNull(types);
    checkOSMTypes(types);
    if (types.length == 0) {
      ProcessingData.osmTypes = EnumSet.of(OSMType.NODE, OSMType.WAY, OSMType.RELATION);
    } else {
      ProcessingData.osmTypes = EnumSet.noneOf(OSMType.class);
      for (String type : types) {
        if (type.equalsIgnoreCase("node")) {
          ProcessingData.osmTypes.add(OSMType.NODE);
        } else if (type.equalsIgnoreCase("way")) {
          ProcessingData.osmTypes.add(OSMType.WAY);
        } else {
          ProcessingData.osmTypes.add(OSMType.RELATION);
        }
      }
    }
  }

  /**
   * Creates an empty array if an input parameter of a POST request is null.
   * 
   * @param toCheck <code>String</code> array, which is checked.
   * @return <code>String</code> array, which is empty.
   */
  public String[] createEmptyArrayIfNull(String[] toCheck) {
    if (toCheck == null) {
      toCheck = new String[0];
    }
    return toCheck;
  }

  /**
   * Creates an empty <code>String</code>, if a given boundary input parameter of a POST request is
   * null.
   * 
   * @param toCheck <code>String</code>, which is checked.
   * @return <code>String</code>, which is empty, but not null.
   */
  public String createEmptyStringIfNull(String toCheck) {
    if (toCheck == null) {
      toCheck = "";
    }
    return toCheck;
  }

  /**
   * Looks at specific objects within the RequestParameters object and makes them empty, if they are
   * null. Needed for the /ratio computation using POST requests.
   */
  public RequestParameters fillWithEmptyIfNull(RequestParameters requestParameters) {
    String[] types = requestParameters.getTypes();
    if (types == null) {
      types = new String[0];
    }
    String[] keys = requestParameters.getKeys();
    if (keys == null) {
      keys = new String[0];
    }
    String[] values = requestParameters.getValues();
    if (values == null) {
      values = new String[0];
    }
    RequestParameters requestParameters2 =
        RequestParameters.of(requestParameters.getRequestMethod(), requestParameters.isSnapshot(),
            requestParameters.isDensity(), requestParameters.getBboxes(),
            requestParameters.getBcircles(), requestParameters.getBpolys(), types, keys, values,
            requestParameters.getUserids(), requestParameters.getTime(),
            requestParameters.getFormat(), requestParameters.getShowMetadata());

    return requestParameters2;
  }

  /** Checks the given keys and values String[] on their length. */
  public void checkKeysValues(String[] keys, String[] values) throws BadRequestException {
    if (values != null) {
      if (keys.length < values.length) {
        throw new BadRequestException("There cannot be more input values in the values|values2 "
            + "than in the keys|keys2 parameter, as values_n must fit to keys_n.");
      }
    }
  }

  /**
   * Compares the keys and values arrays with each other. Returns true only if keys=keys2 and
   * values=values2.
   */
  public boolean compareKeysValues(String[] keys, String[] keys2, String[] values,
      String[] values2) {
    if (keys.length == keys2.length && values.length == values2.length) {
      if (Arrays.equals(keys, keys2) && Arrays.equals(values, values2)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Adds the filter parameters from keys and values to a list as tags. Only used in the processing
   * of /share requests.
   */
  public List<Pair<String, String>> addFilterKeysVals(String[] keys, String[] values,
      String[] keys2, String[] values2) {
    ArrayList<Pair<String, String>> tags = new ArrayList<Pair<String, String>>();
    for (int i = 0; i < keys.length; i++) {
      String key = keys[i];
      Pair<String, String> tag;
      if (i >= values.length) {
        tag = new ImmutablePair<>(key, "");
      } else {
        tag = new ImmutablePair<>(key, values[i]);
      }
      tags.add(tag);
    }
    for (int i = 0; i < keys2.length; i++) {
      String key = keys2[i];
      Pair<String, String> tag;
      if (i >= values2.length) {
        tag = new ImmutablePair<>(key, "");
      } else {
        tag = new ImmutablePair<>(key, values2[i]);
      }
      tags.add(tag);
    }
    // sorting to have all Pair<key,""> at the end of the list
    Collections.sort(tags, new Comparator<Pair<String, String>>() {
      @Override
      public int compare(Pair<String, String> p1, Pair<String, String> p2) {
        if (p1.getValue().equals("") && p2.getValue().equals("")) {
          return 0;
        } else if (p1.getValue().equals("") && !p2.getValue().equals("")) {
          return 1;
        } else if (!p1.getValue().equals("") && p2.getValue().equals("")) {
          return -1;
        } else {
          return 0;
        }
      }
    });
    return tags;
  }

  /**
   * Compares the given keys arrays and adds those of the first to the second, if it has some, which
   * the second one has not. Only used in the processing of /share requests.
   */
  public String[] addFilterKeys(String[] keys, String[] keys2) {
    if (keys.length == 0) {
      return keys2;
    }
    if (Arrays.equals(keys, keys2)) {
      return keys2;
    }
    List<String> keysList = new ArrayList<>(Arrays.asList(keys2));
    for (String s : keys) {
      if (!keysList.contains(s)) {
        keysList.add(s);
      }
    }
    return keysList.toArray(new String[keysList.size()]);
  }

  /**
   * Checks the given keys and values parameters on their length and includes them in the
   * {@link org.heigit.bigspatialdata.oshdb.api.mapreducer.MapReducer#where(String) where(key)}, or
   * {@link org.heigit.bigspatialdata.oshdb.api.mapreducer.MapReducer#where(String, String)
   * where(key, value)} method.
   * 
   * <p>
   * The keys and values parameters are described in the
   * {@link org.heigit.bigspatialdata.ohsome.ohsomeapi.controller.dataaggregation.CountController#count(String, String, String, String[], String[], String[], String[], String[], String, HttpServletRequest)
   * count} method.
   * 
   * @param mapRed current {@link org.heigit.bigspatialdata.oshdb.api.mapreducer.MapReducer
   *        MapReducer} object
   * @return {@link org.heigit.bigspatialdata.oshdb.api.mapreducer.MapReducer MapReducer} object
   *         including the filters derived from the given parameters.
   * @throws BadRequestException if there are more values than keys given
   */
  private MapReducer<? extends OSHDBMapReducible> extractKeysValues(
      MapReducer<? extends OSHDBMapReducible> mapRed, String[] keys, String[] values)
      throws BadRequestException {
    checkKeysValues(keys, values);
    if (keys.length != values.length) {
      String[] tempVal = new String[keys.length];
      for (int a = 0; a < values.length; a++) {
        tempVal[a] = values[a];
      }
      for (int i = values.length; i < keys.length; i++) {
        tempVal[i] = "";
      }
      values = tempVal;
    }
    // prerequisites: both arrays (keys and values) must be of the same length
    // and key-value pairs need to be at the same index in both arrays
    for (int i = 0; i < keys.length; i++) {
      if (values[i].equals("")) {
        mapRed = mapRed.osmTag(keys[i]);
      } else {
        mapRed = mapRed.osmTag(keys[i], values[i]);
      }
    }
    return mapRed;
  }

  /**
   * Extracts the information from the given time array and fills the toTimestamps[] with content
   * (in case of isSnapshot=false).
   */
  private MapReducer<? extends OSHDBMapReducible> extractTime(
      MapReducer<? extends OSHDBMapReducible> mapRed, String[] time, boolean isSnapshot)
      throws Exception {
    String[] toTimestamps = null;
    String[] timeData;
    if (time.length == 1) {
      timeData = utils.extractIsoTime(time[0]);
      if (!isSnapshot) {
        toTimestamps = utils.defineToTimestamps(timeData);
      }
      if (timeData[2] != null) {
        // interval is given
        mapRed = mapRed.timestamps(new OSHDBTimestamps(timeData[0], timeData[1], timeData[2]));
      } else if (timeData[1] != null) {
        mapRed = mapRed.timestamps(timeData[0], timeData[1]);
      } else {
        if (!isSnapshot) {
          throw new BadRequestException(
              "You need to give at least two timestamps or a time interval for this resource.");
        }
        mapRed = mapRed.timestamps(timeData[0]);
      }
    } else if (time.length == 0) {
      if (!isSnapshot) {
        toTimestamps = new String[] {ExtractMetadata.fromTstamp, ExtractMetadata.toTstamp};
        mapRed = mapRed.timestamps(ExtractMetadata.fromTstamp, ExtractMetadata.toTstamp);
      } else {
        mapRed = mapRed.timestamps(ExtractMetadata.toTstamp);
      }
    } else {
      for (String timestamp : time) {
        ZonedDateTime zdt = ISODateTimeParser.parseISODateTime(timestamp);
        utils.checkTemporalExtend(zdt.format(DateTimeFormatter.ISO_DATE_TIME));
      }
      if (!isSnapshot) {
        toTimestamps = utils.defineToTimestamps(time);
      }
      String firstElem = time[0];
      time = ArrayUtils.remove(time, 0);
      mapRed = mapRed.timestamps(firstElem, firstElem, time);
    }
    utils.setToTimestamps(toTimestamps);
    return mapRed;
  }

  /**
   * Checks the content of the userids <code>String</code> array.
   * 
   * @param userids String array containing the OSM user IDs.
   * @throws BadRequestException if one of the userids is invalid
   */
  private void checkUserids(String[] userids) {
    for (String user : userids) {
      try {
        Long.valueOf(user);
      } catch (NumberFormatException e) {
        throw new BadRequestException("The userids parameter can only contain valid OSM userids, "
            + "which are always a positive whole number");
      }
    }
  }

  /** Checks the given OSMType(s) String[] on its length and content. */
  private void checkOSMTypes(String[] types) throws BadRequestException {
    if (types.length > 3) {
      throw new BadRequestException(
          "Parameter 'types' containing the OSM Types cannot have more than 3 entries.");
    } else if (types.length == 0) {
      // do nothing
    } else {
      for (String type : types) {
        if (!type.equalsIgnoreCase("node") && !type.equalsIgnoreCase("way")
            && !type.equalsIgnoreCase("relation")) {
          throw new BadRequestException(
              "Parameter 'types' can only have 'node' and/or 'way' and/or 'relation' "
                  + "as its content.");
        }
      }
    }
  }

  /**
   * Checks the content of the given format parameter.
   */
  private void checkFormat(String format) throws BadRequestException {
    if (format != null && !format.isEmpty() && !format.equalsIgnoreCase("geojson")
        && !format.equalsIgnoreCase("json")) {
      throw new BadRequestException(
          "The given 'format' parameter is invalid. Please choose between "
              + "'geojson'(only available for /groupBy/boundary requests) or 'json'.");
    }
  }

  /**
   * Sets a corresponding enum (NOBOUNDARY for no boundary, BBOXES for bboxes, BCIRCLES for
   * bcircles, BPOLYS for bpolys) based on the given boundary parameter(s). Only one (or none) of
   * them is allowed to have content in it.
   * 
   * @param bboxes <code>String</code> containing the bounding boxes separated via a pipe (|) and
   *        optional custom names at each first coordinate appended with a colon (:).
   * @param bcircles <code>String</code> containing the bounding circles separated via a pipe (|)
   *        and optional custom names at each first coordinate appended with a colon (:).
   * @param bpolys <code>String</code> containing the bounding polygons separated via a pipe (|) and
   *        optional custom names at each first coordinate appended with a colon (:).
   */
  private BoundaryType setBoundaryType(String bboxes, String bcircles, String bpolys) {
    if (bboxes.isEmpty() && bcircles.isEmpty() && bpolys.isEmpty()) {
      return BoundaryType.NOBOUNDARY;
    } else if (!bboxes.isEmpty() && bcircles.isEmpty() && bpolys.isEmpty()) {
      return BoundaryType.BBOXES;
    } else if (bboxes.isEmpty() && !bcircles.isEmpty() && bpolys.isEmpty()) {
      return BoundaryType.BCIRCLES;
    } else if (bboxes.isEmpty() && bcircles.isEmpty() && !bpolys.isEmpty()) {
      return BoundaryType.BPOLYS;
    } else {
      throw new BadRequestException(
          "Your provided boundary parameter (bboxes, bcircles, or bpolys) does not fit its format, "
              + "or you defined more than one boundary parameter.");
    }
  }

  /*
   * Getters start here
   */

  public GeometryBuilder getGeomBuilder() {
    return geomBuilder;
  }

  public InputProcessingUtils getUtils() {
    return utils;
  }

  public void setUtils(InputProcessingUtils utils) {
    this.utils = utils;
  }
}