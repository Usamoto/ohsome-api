package org.heigit.bigspatialdata.ohsome.ohsomeapi.inputprocessing;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.heigit.bigspatialdata.ohsome.ohsomeapi.exception.BadRequestException;
import org.heigit.bigspatialdata.ohsome.ohsomeapi.exception.ExceptionMessages;
import org.heigit.bigspatialdata.ohsome.ohsomeapi.exception.NotFoundException;
import org.heigit.bigspatialdata.ohsome.ohsomeapi.oshdb.DbConnData;
import org.heigit.bigspatialdata.ohsome.ohsomeapi.oshdb.ExtractMetadata;
import org.heigit.bigspatialdata.oshdb.api.generic.function.SerializablePredicate;
import org.heigit.bigspatialdata.oshdb.api.mapreducer.MapReducer;
import org.heigit.bigspatialdata.oshdb.api.object.OSHDBMapReducible;
import org.heigit.bigspatialdata.oshdb.api.object.OSMEntitySnapshot;
import org.heigit.bigspatialdata.oshdb.osm.OSMType;
import org.heigit.bigspatialdata.oshdb.util.OSHDBTag;
import org.heigit.bigspatialdata.oshdb.util.tagtranslator.TagTranslator;
import org.heigit.bigspatialdata.oshdb.util.time.ISODateTimeParser;
import org.heigit.bigspatialdata.oshdb.util.time.OSHDBTimestamps;
import org.heigit.bigspatialdata.oshdb.util.time.TimestampFormatter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Lineal;
import org.locationtech.jts.geom.Polygonal;
import org.locationtech.jts.geom.Puntal;

/** Holds utility methods that are used by the input processing and executor classes. */
public class InputProcessingUtils {

  private Object[] boundaryIds;
  private String[] toTimestamps = null;

  /**
   * Finds and returns the EPSG code of the given point, which is needed for
   * {@link org.heigit.bigspatialdata.ohsome.ohsomeapi.inputprocessing.GeometryBuilder#createCircularPolygons(String[] bcircles)
   * createCircularPolygons}.
   * 
   * <p>
   * Adapted code from UTMCodeFromLonLat.java class in the osmatrix project (© by Michael Auer)
   * 
   * @param lon Longitude coordinate of the point.
   * @param lat Latitude coordinate of the point.
   * @return <code>String</code> representing the corresponding EPSG code.
   */
  public String findEpsg(double lon, double lat) {

    if (lat >= 84) {
      return "EPSG:32661"; // UPS North
    }
    if (lat < -80) {
      return "EPSG:32761"; // UPS South
    }
    int zoneNumber = (int) (Math.floor((lon + 180) / 6) + 1);
    if (lat >= 56.0 && lat < 64.0 && lon >= 3.0 && lon < 12.0) {
      zoneNumber = 32;
    }
    // Special zones for Svalbard
    if (lat >= 72.0 && lat < 84.0) {
      if (lon >= 0.0 && lon < 9.0) {
        zoneNumber = 31;
      } else if (lon >= 9.0 && lon < 21.0) {
        zoneNumber = 33;
      } else if (lon >= 21.0 && lon < 33.0) {
        zoneNumber = 35;
      } else if (lon >= 33.0 && lon < 42.0) {
        zoneNumber = 37;
      }
    }
    String isNorth = (lat > 0) ? "6" : "7";
    String zone = (zoneNumber < 10) ? "0" + zoneNumber : "" + zoneNumber;
    return "EPSG:32" + isNorth + zone;
  }

  /**
   * Splits the given bounding boxes and returns them in a <code>List</code>.
   * 
   * @param bboxes contains the given bounding boxes
   * @return <code>List</code> containing the splitted bounding boxes
   * @throws BadRequestException if the bboxes parameter has invalid content
   */
  public List<String> splitBboxes(String bboxes) throws BadRequestException {
    String[] bboxesArray = splitOnHyphen(bboxes);
    List<String> boundaryParamValues = new ArrayList<>();
    boundaryIds = new Object[bboxesArray.length];
    try {
      if (bboxesArray[0].contains(":")) {
        boundaryParamValues = splitBboxesWithIds(bboxesArray);
      } else {
        boundaryParamValues = splitBoundariesWithoutIds(bboxesArray, BoundaryType.BBOXES);
      }
    } catch (Exception e) {
      if (e.getClass() == BadRequestException.class) {
        throw e;
      }
      throw new BadRequestException(ExceptionMessages.BOUNDARY_PARAM_FORMAT);
    }
    boundaryParamValues.removeAll(Collections.singleton(null));
    return boundaryParamValues;
  }

  /**
   * Splits the given bounding circles and returns them in a <code>List</code>.
   * 
   * @param bcircles contains the given bounding circles
   * @return <code>List</code> containing the splitted bounding circles
   * @throws BadRequestException if the bcircles parameter has invalid content
   */
  public List<String> splitBcircles(String bcircles) throws BadRequestException {
    String[] bcirclesArray = splitOnHyphen(bcircles);
    List<String> boundaryParamValues = new ArrayList<>();
    boundaryIds = new Object[bcirclesArray.length];
    try {
      if (bcirclesArray[0].contains(":")) {
        boundaryParamValues = splitBcirclesWithIds(bcirclesArray);
      } else {
        boundaryParamValues = splitBoundariesWithoutIds(bcirclesArray, BoundaryType.BCIRCLES);
      }
    } catch (Exception e) {
      if (e.getClass() == BadRequestException.class) {
        throw e;
      }
      throw new BadRequestException(ExceptionMessages.BOUNDARY_PARAM_FORMAT);
    }
    boundaryParamValues.removeAll(Collections.singleton(null));
    return boundaryParamValues;
  }

  /**
   * Splits the given bounding polygons and returns them in a <code>List</code>.
   * 
   * @param bpolys contains the given bounding polygons
   * @return <code>List</code> containing the splitted bounding polygons
   * @throws BadRequestException if the bpolys parameter has invalid content
   */
  public List<String> splitBpolys(String bpolys) throws BadRequestException {
    String[] bpolysArray = splitOnHyphen(bpolys);
    List<String> boundaryParamValues = new ArrayList<>();
    boundaryIds = new Object[bpolysArray.length];
    try {
      if (bpolysArray[0].contains(":")) {
        boundaryParamValues = splitBpolysWithIds(bpolysArray);

      } else if (bpolysArray[0].contains(",")) {
        boundaryParamValues = splitBoundariesWithoutIds(bpolysArray, BoundaryType.BPOLYS);
      } else {
        throw new BadRequestException(ExceptionMessages.BOUNDARY_PARAM_FORMAT);
      }
    } catch (Exception e) {
      if (e.getClass() == BadRequestException.class) {
        throw e;
      }
      throw new BadRequestException(ExceptionMessages.BOUNDARY_PARAM_FORMAT);
    }
    boundaryParamValues.removeAll(Collections.singleton(null));
    return boundaryParamValues;
  }

  /**
   * Defines the toTimestamps for the result json object for /users responses.
   * 
   * @param timeData contains the requested time
   * @return array having only the toTimestamps
   */
  public String[] defineToTimestamps(String[] timeData) {
    OSHDBTimestamps timestamps;
    if (timeData.length == 3 && timeData[2] != null) {
      // needed to check for interval
      if (timeData[2].startsWith("P")) {
        timestamps = new OSHDBTimestamps(timeData[0], timeData[1], timeData[2]);
        toTimestamps = timestamps.get().stream().map(oshdbTimestamp -> {
          return TimestampFormatter.getInstance().isoDateTime(oshdbTimestamp);
        }).toArray(String[]::new);
      } else {
        // list of timestamps
        toTimestamps = getToTimestampsFromTimestamplist(timeData);
      }
    } else {
      // list of timestamps
      toTimestamps = getToTimestampsFromTimestamplist(timeData);
    }
    return toTimestamps;
  }

  /**
   * Extracts the time information out of the time parameter and checks the content on its format,
   * as well as <a href="https://en.wikipedia.org/wiki/ISO_8601">ISO-8601</a> conformity. This
   * method is used if one datetimestring is given. Following time formats are allowed:
   * <ul>
   * <li><strong>YYYY-MM-DD</strong> or <strong>YYYY-MM-DDThh:mm:ss</strong>: When a timestamp
   * includes 'T', hh:mm must also be given. This applies for all time formats, which use
   * timestamps. If -MM-DD or only -DD is missing, '01' is used as default for month and day.</li>
   * <li><strong>YYYY-MM-DD/YYYY-MM-DD</strong>: start/end timestamps</li>
   * <li><strong>YYYY-MM-DD/YYYY-MM-DD/PnYnMnD</strong>: start/end/period where n refers to the size
   * of the respective period</li>
   * <li><strong>/YYYY-MM-DD</strong>: #/end where # equals the earliest timestamp</li>
   * <li><strong>/YYYY-MM-DD/PnYnMnD</strong>: #/end/period</li>
   * <li><strong>YYYY-MM-DD/</strong>: start/# where # equals the latest timestamp</li>
   * <li><strong>YYYY-MM-DD//PnYnMnD</strong>: start/#/period</li>
   * <li><strong>/</strong>: #/# where # equals the earliest and latest timestamp</li>
   * <li><strong>//PnYnMnD</strong>: #/#/period</li>
   * <li><strong>invalid</strong>: throws BadRequestException</li>
   * </ul>
   * 
   * <p>
   * For clarification: the format YYYY-MM-DDThh:mm:ss can be applied to any format, where a
   * timestamp is used and # is a replacement holder for "no value". Note that the positioning and
   * using of the forward slash '/' is very important.
   * 
   * @param time <code>String</code> holding the unparsed time information.
   * @return <code>String</code> array containing the startTime at at [0], the endTime at [1] and
   *         the period at [2].
   * @throws BadRequestException if the given time parameter is not ISO-8601 conform
   * @throws NotFoundException if the given time is not completely within the timerange of the
   *         underlying data
   */
  public String[] extractIsoTime(String time) throws BadRequestException, NotFoundException {
    String[] split = time.split("/");
    if (split.length == 0 && !"/".equals(time)) {
      // invalid time parameter
      throw new BadRequestException(ExceptionMessages.TIME_FORMAT);
    }
    String[] timeVals = new String[3];
    if (time.startsWith("/")) {
      if (time.length() == 1) {
        // only /
        timeVals[0] = ExtractMetadata.fromTstamp;
        timeVals[1] = ExtractMetadata.toTstamp;
        return timeVals;
      }
      if (split[0].length() == 0 && split.length == 2) {
        // /YYYY-MM-DD
        checkTimestampsOnIsoConformity(split[1]);
        checkTemporalExtend(split[1]);
        timeVals[1] = split[1];
      } else if (split.length == 3 && split[0].length() == 0 && split[1].length() == 0) {
        // //PnYnMnD
        checkPeriodOnIsoConformity(split[2]);
        timeVals[1] = ExtractMetadata.toTstamp;
        timeVals[2] = split[2];
      } else if (split.length == 3 && split[1].length() != 0) {
        // /YYYY-MM-DD/PnYnMnD
        checkTimestampsOnIsoConformity(split[1]);
        checkTemporalExtend(split[1]);
        checkPeriodOnIsoConformity(split[2]);
        timeVals[1] = split[1];
        timeVals[2] = split[2];
      } else {
        // invalid time parameter
        throw new BadRequestException(ExceptionMessages.TIME_FORMAT);
      }
      timeVals[0] = ExtractMetadata.fromTstamp;
    } else if (time.endsWith("/")) {
      if (split.length != 1) {
        // invalid time parameter
        throw new BadRequestException(ExceptionMessages.TIME_FORMAT);
      }
      // YYYY-MM-DD/
      checkTimestampsOnIsoConformity(split[0]);
      checkTemporalExtend(split[0]);
      timeVals[0] = split[0];
      timeVals[1] = ExtractMetadata.toTstamp;
    } else if (split.length == 3) {
      if (split[1].length() == 0) {
        // YYYY-MM-DD//PnYnMnD
        checkTimestampsOnIsoConformity(split[0]);
        checkTemporalExtend(split[0]);
        timeVals[1] = ExtractMetadata.toTstamp;
        timeVals[2] = split[2];
      } else {
        // YYYY-MM-DD/YYYY-MM-DD/PnYnMnD
        checkTimestampsOnIsoConformity(split[0], split[1]);
        checkTemporalExtend(split[0], split[1]);
        timeVals[1] = split[1];
      }
      checkPeriodOnIsoConformity(split[2]);
      timeVals[0] = split[0];
      timeVals[2] = split[2];
    } else if (split.length == 2) {
      // YYYY-MM-DD/YYYY-MM-DD
      checkTimestampsOnIsoConformity(split[0], split[1]);
      checkTemporalExtend(split[0], split[1]);
      timeVals[0] = split[0];
      timeVals[1] = split[1];
    } else if (split.length == 1) {
      // YYYY-MM-DD
      checkTimestampsOnIsoConformity(split[0]);
      checkTemporalExtend(split[0]);
      timeVals[0] = split[0];
      return timeVals;
    } else {
      // invalid time parameter
      throw new BadRequestException(ExceptionMessages.TIME_FORMAT);
    }
    String[] sortedTimestamps = sortTimestamps(new String[] {timeVals[0], timeVals[1]});
    timeVals[0] = sortedTimestamps[0];
    timeVals[1] = sortedTimestamps[1];
    return timeVals;
  }

  /** Sorts the given timestamps from oldest to newest. */
  public String[] sortTimestamps(String[] timestamps) throws BadRequestException {
    List<String> timeStringList = new ArrayList<>();
    for (String timestamp : timestamps) {
      try {
        ZonedDateTime zdt = ISODateTimeParser.parseISODateTime(timestamp);
        checkTemporalExtend(zdt.format(DateTimeFormatter.ISO_DATE_TIME));
        timeStringList.add(zdt.format(DateTimeFormatter.ISO_DATE_TIME));
      } catch (Exception e) {
        throw new BadRequestException(ExceptionMessages.TIME_FORMAT);
      }
    }
    Collections.sort(timeStringList);
    return timeStringList.toArray(timestamps);
  }

  /** Checks the given custom boundary id. At the moment only used if output format = csv. */
  public void checkCustomBoundaryId(String id) {
    if (id.contains(";")) {
      throw new BadRequestException("The given custom ids cannot contain semicolons, "
          + "if you want to use csv as output format.");
    }
  }

  /**
   * Checks if the given geometry is within the underlying data-polygon. Returns also true if no
   * data-polygon is given.
   * 
   * @param geom <code>Geometry</code>, which is tested against the data-polygon
   * @return <code>true</code> - if inside <br>
   *         <code>false</code> - if not inside
   */
  public boolean isWithin(Geometry geom) {
    if (ExtractMetadata.dataPoly != null) {
      return geom.within(ExtractMetadata.dataPoly);
    }
    return true;
  }

  /** Checks if the given String is one of the simple feature types (point, line, polygon). */
  public boolean isSimpleFeatureType(String type) {
    return "point".equalsIgnoreCase(type) || "line".equalsIgnoreCase(type)
        || "polygon".equalsIgnoreCase(type) || "other".equalsIgnoreCase(type);
  }

  /**
   * Applies an entity filter using only planar relations (relations with an area) on the given
   * MapReducer object. It uses the tags "type=multipolygon" and "type=boundary".
   */
  public <T extends OSHDBMapReducible> MapReducer<T> filterOnPlanarRelations(MapReducer<T> mapRed) {
    // further filtering to not look at all relations
    TagTranslator tt = DbConnData.tagTranslator;
    OSHDBTag typeMultipolygon = tt.getOSHDBTagOf("type", "multipolygon");
    OSHDBTag typeBoundary = tt.getOSHDBTagOf("type", "boundary");
    mapRed.osmEntityFilter(entity -> {
      return !entity.getType().equals(OSMType.RELATION)
          || entity.hasTagValue(typeMultipolygon.getKey(), typeMultipolygon.getValue())
          || entity.hasTagValue(typeBoundary.getKey(), typeBoundary.getValue());
    });
    return mapRed;
  }

  /**
   * Applies respective Puntal|Lineal|Polygonal filter(s) on features of the given MapReducer.
   * 
   * @return MapReducer with filtered geometries
   */
  @SuppressWarnings("unchecked") // unchecked to allow cast of (MapReducer<T>) to mapRed
  public <T extends OSHDBMapReducible> MapReducer<T> filterOnSimpleFeatures(MapReducer<T> mapRed,
      ProcessingData processingData) {
    MapReducer<OSMEntitySnapshot> mapReducer = (MapReducer<OSMEntitySnapshot>) mapRed;
    Set<SimpleFeatureType> simpleFeatureTypes = processingData.getSimpleFeatureTypes();
    boolean containsPoint = false;
    boolean containsLine = false;
    boolean containsPolygon = false;
    boolean containsOther = false;
    for (SimpleFeatureType type : simpleFeatureTypes) {
      if (type.equals(SimpleFeatureType.POINT)) {
        containsPoint = true;
      } else if (type.equals(SimpleFeatureType.LINE)) {
        containsLine = true;
      } else if (type.equals(SimpleFeatureType.POLYGON)) {
        containsPolygon = true;
      } else if (type.equals(SimpleFeatureType.OTHER)) {
        containsOther = true;
      }
    }
    final boolean hasPoly = containsPolygon;
    final boolean hasPoint = containsPoint;
    final boolean hasLine = containsLine;
    final boolean hasOther = containsOther;
    return (MapReducer<T>) mapReducer
        .filter((SerializablePredicate<OSMEntitySnapshot>) predicate -> {
          return (hasPoly && predicate.getGeometry() instanceof Polygonal)
              || (hasPoint && predicate.getGeometry() instanceof Puntal)
              || (hasLine && predicate.getGeometry() instanceof Lineal)
              || (hasOther && "GeometryCollection"
                  .equalsIgnoreCase(predicate.getGeometry().getGeometryType()));
        });
  }

  /**
   * Checks the provided time info on its temporal extent.
   * 
   * @param timeInfo time information to check
   * @throws NotFoundException if the given time is not completely within the timerange of the
   *         underlying data
   * @throws BadRequestException if the timestamps are not ISO-8601 conform.
   */
  protected void checkTemporalExtend(String... timeInfo)
      throws NotFoundException, BadRequestException {
    long start = 0;
    long end = 0;
    long timestampLong = 0;
    try {
      start = ISODateTimeParser.parseISODateTime(ExtractMetadata.fromTstamp).toEpochSecond();
      end = ISODateTimeParser.parseISODateTime(ExtractMetadata.toTstamp).toEpochSecond();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    for (String timestamp : timeInfo) {
      try {
        ZonedDateTime zdt = ISODateTimeParser.parseISODateTime(timestamp);
        timestampLong =
            DateTimeFormatter.ISO_DATE_TIME.parse(zdt.format(DateTimeFormatter.ISO_DATE_TIME))
                .getLong(ChronoField.INSTANT_SECONDS);
        if (timestampLong < start || timestampLong > end) {
          throw new NotFoundException(
              "The given time parameter is not completely within the timeframe ("
                  + ExtractMetadata.fromTstamp + " to " + ExtractMetadata.toTstamp
                  + ") of the underlying osh-data.");
        }
      } catch (NotFoundException e) {
        throw e;
      } catch (Exception e) {
        throw new BadRequestException(ExceptionMessages.TIME_FORMAT);
      }
    }
  }

  /**
   * Checks the provided time info on its ISO conformity.
   * 
   * @param timeInfo time information to check
   * @throws BadRequestException if the timestamps are not ISO-8601 conform.
   */
  protected void checkTimestampsOnIsoConformity(String... timeInfo) throws BadRequestException {
    for (String timestamp : timeInfo) {
      try {
        ISODateTimeParser.parseISODateTime(timestamp);
      } catch (Exception e) {
        throw new BadRequestException(ExceptionMessages.TIME_FORMAT);
      }
    }
  }

  /**
   * Checks the provided period on its ISO conformity. Throws a 400 BadRequestException if it is not
   * ISO conform.
   */
  protected void checkPeriodOnIsoConformity(String period) throws BadRequestException {
    try {
      ISODateTimeParser.parseISOPeriod(period);
    } catch (Exception e) {
      throw new BadRequestException(
          "The interval (period) of the provided time parameter is not ISO-8601 conform.");
    }
  }

  /**
   * Splits the given boundary parameter (bboxes, bcircles, or bpolys) on '|' to seperate the
   * different bounding objects.
   * 
   * @param boundaryParam <code>String</code> that contains the boundary parameter(s)
   * @return splitted boundaries
   */
  private String[] splitOnHyphen(String boundaryParam) {
    if (boundaryParam.contains("|")) {
      return boundaryParam.split("\\|");
    }
    return new String[] {boundaryParam};
  }

  /**
   * Splits the coordinates from the given boundaries array.
   * 
   * @param boundariesArray contains the boundaries without a custom id
   * @return <code>List</code> containing the splitted boundaries
   * @throws BadRequestException if the coordinates are invalid
   */
  private List<String> splitBoundariesWithoutIds(String[] boundariesArray,
      BoundaryType boundaryType) throws BadRequestException {
    List<String> boundaryParamValues = new ArrayList<>();
    for (int i = 0; i < boundariesArray.length; i++) {
      String[] coords = boundariesArray[i].split("\\,");
      for (String coord : coords) {
        boundaryParamValues.add(coord);
      }
      boundaryIds[i] = "boundary" + (i + 1);
    }
    checkBoundaryParamLength(boundaryParamValues, boundaryType);
    return boundaryParamValues;
  }

  /**
   * Splits the ids and the coordinates from the given bounding boxes array.
   * 
   * @param bboxesArray contains the bounding boxes having a custom id
   * @return <code>List</code> containing the splitted bounding boxes
   * @throws BadRequestException if the bboxes have invalid content
   */
  private List<String> splitBboxesWithIds(String[] bboxesArray) throws BadRequestException {
    List<String> boundaryParamValues = new ArrayList<>();
    for (int i = 0; i < bboxesArray.length; i++) {
      String[] coords = bboxesArray[i].split("\\,");
      if (coords.length != 4) {
        throw new BadRequestException(ExceptionMessages.BOUNDARY_PARAM_FORMAT);
      }
      if (coords[0].contains(":")) {
        String[] idAndCoordinate = coords[0].split(":");
        // extract the id
        boundaryIds[i] = idAndCoordinate[0];
        // extract the coordinates
        boundaryParamValues.add(idAndCoordinate[1]);
        boundaryParamValues.add(coords[1]);
        boundaryParamValues.add(coords[2]);
        boundaryParamValues.add(coords[3]);
      } else {
        throw new BadRequestException(ExceptionMessages.BOUNDARY_IDS_FORMAT);
      }
    }
    checkBoundaryParamLength(boundaryParamValues, BoundaryType.BBOXES);
    return boundaryParamValues;
  }

  /**
   * Splits the ids and the coordinates from the given bounding circles array.
   * 
   * @param bcirclesArray contains the bounding circles having a custom id
   * @return <code>List</code> containing the splitted bounding circles
   * @throws BadRequestException if the bcircles have invalid content
   */
  private List<String> splitBcirclesWithIds(String[] bcirclesArray) throws BadRequestException {
    List<String> boundaryParamValues = new ArrayList<>();
    for (int i = 0; i < bcirclesArray.length; i++) {
      String[] coords = bcirclesArray[i].split("\\,");
      if (coords.length != 3) {
        throw new BadRequestException(ExceptionMessages.BOUNDARY_PARAM_FORMAT);
      }
      String[] idAndCoordinate = coords[0].split(":");
      boundaryIds[i] = idAndCoordinate[0];
      // extract the coordinate
      boundaryParamValues.add(idAndCoordinate[1]);
      boundaryParamValues.add(coords[1]);
      // extract the radius
      boundaryParamValues.add(coords[2]);
    }
    checkBoundaryParamLength(boundaryParamValues, BoundaryType.BCIRCLES);
    return boundaryParamValues;
  }

  /**
   * Splits the ids and the coordinates from the given bounding polygons array.
   * 
   * @param bpolysArray contains the bounding polygons having a custom id
   * @return <code>List</code> containing the splitted bounding polygons
   * @throws BadRequestException if the bpolys have invalid content
   */
  private List<String> splitBpolysWithIds(String[] bpolysArray) throws BadRequestException {
    List<String> boundaryParamValues = new ArrayList<>();
    for (int i = 0; i < bpolysArray.length; i++) {
      String[] coords = bpolysArray[i].split("\\,");
      String[] idAndCoordinate = coords[0].split(":");
      // extract the id and the first coordinate
      boundaryIds[i] = idAndCoordinate[0];
      boundaryParamValues.add(idAndCoordinate[1]);
      // extract the other coordinates
      for (int j = 1; j < coords.length; j++) {
        if (coords[j].contains(":")) {
          throw new BadRequestException(ExceptionMessages.BOUNDARY_PARAM_FORMAT);
        }
        boundaryParamValues.add(coords[j]);
      }
    }
    checkBoundaryParamLength(boundaryParamValues, BoundaryType.BPOLYS);
    return boundaryParamValues;
  }

  /**
   * Checks the given boundaries list on their length. Bounding box and polygon list must be even,
   * bounding circle list must be divisable by three.
   * 
   * @param boundaries parameter to check the length
   * @throws BadRequestException if the length is not even or divisible by three
   */
  private void checkBoundaryParamLength(List<String> boundaries, BoundaryType boundaryType)
      throws BadRequestException {
    if ((boundaryType.equals(BoundaryType.BBOXES) || boundaryType.equals(BoundaryType.BPOLYS))
        && boundaries.size() % 2 != 0) {
      throw new BadRequestException(ExceptionMessages.BOUNDARY_PARAM_FORMAT);
    }
    if (boundaryType.equals(BoundaryType.BCIRCLES) && boundaries.size() % 3 != 0) {
      throw new BadRequestException(ExceptionMessages.BOUNDARY_PARAM_FORMAT);
    }
  }

  /** Internal helper method to get the toTimestamps from a timestampList. */
  private String[] getToTimestampsFromTimestamplist(String[] timeData) {
    toTimestamps = new String[timeData.length];
    for (int i = 0; i < timeData.length; i++) {
      try {
        toTimestamps[i] =
            ISODateTimeParser.parseISODateTime(timeData[i]).format(DateTimeFormatter.ISO_DATE_TIME);
      } catch (Exception e) {
        // time gets checked earlier already, so no exception should appear here
      }
    }
    return toTimestamps;
  }

  public Object[] getBoundaryIds() {
    return boundaryIds;
  }

  public String[] getToTimestamps() {
    return toTimestamps;
  }

  public void setBoundaryIds(Object[] boundaryIds) {
    this.boundaryIds = boundaryIds;
  }

  public void setToTimestamps(String[] toTimestamps) {
    this.toTimestamps = toTimestamps;
  }
}
