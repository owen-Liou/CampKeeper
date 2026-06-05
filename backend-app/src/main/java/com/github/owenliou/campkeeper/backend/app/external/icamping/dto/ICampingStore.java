package com.github.owenliou.campkeeper.backend.app.external.icamping.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ICampingStore {

    @JsonProperty("store_name")
    private String storeName;

    @JsonProperty("store_alias")
    private String storeAlias;

    private Boolean status;

    private String altitude;

    private String area;

    private String city;

    private String district;

    private List<String> facility;
}
