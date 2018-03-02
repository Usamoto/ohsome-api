package org.heigit.bigspatialdata.ohsome.ohsomeApi.output.dataAggregationResponse.metadata;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.annotations.ApiModelProperty;

/**
 * Represents the meta data JSON object containing the execution time, the unit and a description of
 * the values, which are in the
 * {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.output.dataAggregationResponse.result.Result
 * Result} objects, as well as the request URL.
 */
@JsonInclude(Include.NON_NULL)
public class Metadata {

  @ApiModelProperty(notes = "Time the server needed to execute the request", required = true,
      position = 0)
  private long executionTime;
  @ApiModelProperty(notes = "Unit of the value in the result object(s)", required = true,
      position = 1)
  private String unit;
  @ApiModelProperty(notes = "Text describing the result in a sentence", required = true,
      position = 3)
  private String description;
  @ApiModelProperty(notes = "Request URL to which this whole output JSON was generated",
      required = true, position = 4)
  private String requestUrl;

  public Metadata(long executionTime, String unit, String description, String requestUrl) {
    this.executionTime = executionTime;
    this.unit = unit;
    this.description = description;
    this.requestUrl = requestUrl;
  }

  public long getExecutionTime() {
    return executionTime;
  }

  public String getUnit() {
    return unit;
  }

  public String getDescription() {
    return description;
  }

  public String getRequestUrl() {
    return requestUrl;
  }

}