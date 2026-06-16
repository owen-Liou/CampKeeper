package com.github.owenliou.campkeeper.backend.app.external.icamping.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.owenliou.campkeeper.backend.app.external.icamping.dto.ICampingStoreListDto;
import lombok.Data;

import java.util.List;

@Data
public class ICampingStoreListResponse implements ICampingResponse<ICampingStoreListDto> {

    @JsonProperty("error_mesg")
    private String errorMessage;

    private String status;

    private List<ICampingStoreListDto> items;
}