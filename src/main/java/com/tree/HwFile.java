package com.tree;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class HwFile {

    private String id;
    private String name;
    private String description;

    @JsonProperty(value = "parent_id")
    private String parentId;

    @JsonProperty(value = "created_by")
    private String createdBy;

    @JsonProperty(value = "created_date")
    private String createdDate;//string is ok for the hw scope
}
