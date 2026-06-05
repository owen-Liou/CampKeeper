package com.github.owenliou.campkeeper.backend.app.external.icamping.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ICampingStoreListResponse {

    @JsonProperty("error_mesg")
    private String errorMessage;

    private String status;

    private List<ICampingStore> items;
}