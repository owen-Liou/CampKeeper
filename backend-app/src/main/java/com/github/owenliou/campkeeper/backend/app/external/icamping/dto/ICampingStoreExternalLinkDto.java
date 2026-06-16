package com.github.owenliou.campkeeper.backend.app.external.icamping.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ICampingStoreExternalLinkDto {

    private String link;
    private String name;
    private String sequence;

    @JsonProperty("store_name")
    private String storeName;
}
