package com.github.owenliou.campkeeper.backend.app.external.icamping.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.owenliou.campkeeper.backend.app.external.icamping.dto.ICampingStoreExternalLinkDto;
import lombok.Data;

import java.util.List;

@Data
public class ICampingStoreExternalLinkResponse implements ICampingResponse<ICampingStoreExternalLinkDto> {

    @JsonProperty("error_mesg")
    private String errorMessage;

    private String status;

    private List<ICampingStoreExternalLinkDto> items;
}