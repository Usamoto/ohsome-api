package org.heigit.ohsome.ohsomeapi.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

/** Holds helper methods that are used within the various test classes. */
public class Helper {
  private static String portPost = TestProperties.PORT2;
  private static String server = TestProperties.SERVER;

  /** Method to get post response body as String */
  static String getPostResponseBody(String urlParams, MultiValueMap<String, String> map) {
    TestRestTemplate restTemplate = new TestRestTemplate();
    ResponseEntity<String> response =
        restTemplate.postForEntity(server + portPost + urlParams, map, String.class);
    String responseBody = response.getBody();
    return responseBody;
  }

  /** Method to create CSV parser, skip comment headers */
  public static CSVParser csvParser(String responseBody) throws IOException {
    CSVFormat csvFormat =
        CSVFormat.DEFAULT.withFirstRecordAsHeader().withDelimiter(';').withCommentMarker('#');
    CSVParser csvParser = CSVParser.parse(responseBody, csvFormat);
    return csvParser;
  }

  /** Method to get CSV entries */
  public static List<CSVRecord> getCsvRecords(String responseBody) throws IOException {
    CSVParser csvParser = csvParser(responseBody);
    List<CSVRecord> records = csvParser.getRecords();
    return records;
  }

  /** Method to get CSV headers */
  public static Map<String, Integer> getCsvHeaders(String responseBody) throws IOException {
    CSVParser csvParser = csvParser(responseBody);
    Map<String, Integer> headers = csvParser.getHeaderMap();
    return headers;
  }
}
