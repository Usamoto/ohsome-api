package org.heigit.bigspatialdata.ohsome.ohsomeApi.output.dataAggregationResponse;

import org.heigit.bigspatialdata.ohsome.ohsomeApi.output.dataAggregationResponse.metadata.Metadata;
import org.heigit.bigspatialdata.ohsome.ohsomeApi.output.dataAggregationResponse.result.GroupByResult;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.annotations.ApiModelProperty;

/**
 * Represents the whole JSON response object for the data aggregation response using the
 * /groupBy/key resource. It contains an optional
 * {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.output.dataAggregationResponse.metadata.Metadata
 * Metadata}, the requested
 * {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.output.dataAggregationResponse.result.GroupByResult
 * GroupByResult} and an identifier of the object plus the corresponding
 * {@link org.heigit.bigspatialdata.ohsome.ohsomeApi.output.dataAggregationResponse.result.Result
 * Result} objects.
 */
@JsonInclude(Include.NON_NULL)
public class GroupByKeyResponse {

  @ApiModelProperty(notes = "License of the included data", required = true, position = 0)
  private String license;
  @ApiModelProperty(notes = "Copyright of the used data", required = true, position = 1)
  private String copyright;
  @ApiModelProperty(notes = "Metadata describing the output", position = 2)
  private Metadata metadata;
  @ApiModelProperty(notes = "GroupByResult array holding the respective objects "
      + "with their timestamp-value pairs", required = true)
  private GroupByResult[] groupByKeyResult;

  public GroupByKeyResponse(String license, String copyright, Metadata metadata,
      GroupByResult[] groupByKeyResult) {
    this.license = license;
    this.copyright = copyright;
    this.metadata = metadata;
    this.groupByKeyResult = groupByKeyResult;
  }

  public String getLicense() {
    return license;
  }

  public String getCopyright() {
    return copyright;
  }

  public Metadata getMetadata() {
    return metadata;
  }

  public GroupByResult[] getGroupByKeyResult() {
    return groupByKeyResult;
  }

}