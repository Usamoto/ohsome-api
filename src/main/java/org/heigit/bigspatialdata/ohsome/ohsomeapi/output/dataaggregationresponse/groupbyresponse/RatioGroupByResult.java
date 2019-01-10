package org.heigit.bigspatialdata.ohsome.ohsomeapi.output.dataaggregationresponse.groupbyresponse;

import org.heigit.bigspatialdata.ohsome.ohsomeapi.output.dataaggregationresponse.RatioResult;
import io.swagger.annotations.ApiModelProperty;


/**
 * Represents the ratio-groupBy result JSON object containing the groupBy value and the respective
 * {@link org.heigit.bigspatialdata.ohsome.ohsomeapi.output.dataaggregationresponse.RatioResult
 * RatioResult} objects. The RatioGroupByResult is only used in responses for /ratio/groupBy
 * requests.
 */
public class RatioGroupByResult {

  @ApiModelProperty(notes = "Object on which the ratio-results are grouped on", required = true)
  private Object groupByObject;
  @ApiModelProperty(notes = "RatioResult array holding timestamp, whole and part values",
      required = true)
  private RatioResult[] ratioResult;

  public RatioGroupByResult(Object groupByObject, RatioResult[] ratioResult) {
    this.groupByObject = groupByObject;
    this.ratioResult = ratioResult;
  }

  public Object getGroupByObject() {
    return groupByObject;
  }

  public RatioResult[] getRatioResult() {
    return ratioResult;
  }
}
