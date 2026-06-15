package com.github.owenliou.campkeeper.backend.app.ai.restcontroller;

import com.github.owenliou.campkeeper.backend.app.ai.service.EmbeddingService;
import com.github.owenliou.campkeeper.backend.app.converter.CampsiteToDtoConverter;
import com.github.owenliou.campkeeper.backend.app.dto.CampsiteDTO;
import com.github.owenliou.campkeeper.common.CustomResult;
import com.github.owenliou.campkeeper.web.common.restcontroller.AbstractSyncRestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;


import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ai")
@Tag(name = "Admin API", description = "管理員操作 API")
public class RestApiController extends AbstractSyncRestController {

    private final EmbeddingService embeddingService;

    private final CampsiteToDtoConverter campsiteToDtoConverter;

    @GetMapping("/search")
    @Operation(summary = "AI 自然語言搜尋", description = "用自然語言描述想找的營地，例如：寵物友善台中高山有電")
    public CustomResult<List<CampsiteDTO>> aiSearch(@Parameter(description = "自然語言查詢") @RequestParam String query,
                                                    @Parameter(description = "回傳筆數") @RequestParam(defaultValue = "10") int topK) {
        List<CampsiteDTO> result = embeddingService.semanticSearchCamp(query, topK)
                .stream()
                .map(campsiteToDtoConverter::convert)
                .toList();
        return CustomResult.result(true, result);
    }
}