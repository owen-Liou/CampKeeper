package com.github.owenliou.campkeeper.backend.app.restcontroller;

import com.github.owenliou.campkeeper.backend.app.converter.CampsiteToDtoConverter;
import com.github.owenliou.campkeeper.backend.app.dto.CampsiteDTO;
import com.github.owenliou.campkeeper.backend.app.ai.service.CampsiteEmbeddingService;
import com.github.owenliou.campkeeper.backend.app.service.CampsiteService;
import com.github.owenliou.campkeeper.common.CustomResult;
import com.github.owenliou.campkeeper.model.entity.Campsite;
import java.util.List;
import com.github.owenliou.campkeeper.web.common.restcontroller.AbstractSyncRestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

/**
 * 營地管理 REST 控制器
 * 提供營地資料的 CRUD API 操作
 */
@RestController
@RequestMapping("/api/v1/campsites")
@Tag(name = "Campsite API", description = "營地管理 API")
public class RestCampsiteController extends AbstractSyncRestController {

    @Autowired
    private CampsiteService campsiteService;

    @Autowired
    private CampsiteToDtoConverter campsiteToDtoConverter;

    @Autowired
    private CampsiteEmbeddingService embeddingService;

    @GetMapping
    @Operation(summary = "查詢營地列表", description = "支援分頁與縣市篩選，預設每頁 20 筆")
    public CustomResult<Page<CampsiteDTO>> getAll(
            @Parameter(description = "縣市篩選，例如：台中市") @RequestParam(required = false) String city,
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        Page<CampsiteDTO> page = campsiteService.searchCampsites(city, pageable).map(campsiteToDtoConverter::convert);
        return CustomResult.result(true, page);
    }

    @PostMapping("/sync")
    @Operation(summary = "同步iCamping資料", description = "從iCamping API 匯入最新營地資料")
    public CustomResult<String> sync() {
        int count = campsiteService.syncFromICamping();
        return CustomResult.result(true, "同步完成，共更新 " + count + " 筆營地資料");
    }

    /**
     * 根據 ID 查詢單筆營地
     */
    @GetMapping("/{id}")
    @Operation(summary = "查詢單筆營地", description = "根據營地 ID 查詢詳細資訊")
    public CustomResult<CampsiteDTO> getById(@Parameter(description = "營地 ID") @PathVariable String id) {
        Campsite campsite = campsiteService.getByCampsiteId(id);
        return CustomResult.result(true, campsiteToDtoConverter.convert(campsite));
    }

    /**
     * 新增營地
     */
    @PostMapping("/create")
    @Operation(summary = "新增營地", description = "添加新的營地資訊")
    public CustomResult<CampsiteDTO> create(@RequestBody CampsiteDTO campsiteDto) {
        Campsite campsite = campsiteService.createCampsite(campsiteDto);
        return CustomResult.result(true, campsiteToDtoConverter.convert(campsite));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新營地", description = "根據營地 ID 更新營地資訊")
    public CustomResult<CampsiteDTO> update( @PathVariable String id, @RequestBody CampsiteDTO campsiteDto) {
        campsiteDto.setId(Long.parseLong(id));
        Campsite campsite = campsiteService.updateCampsite(campsiteDto);
        return CustomResult.result(true, campsiteToDtoConverter.convert(campsite));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "刪除營地", description = "根據營地 ID 刪除營地資訊")
    public void delete(@PathVariable String id) {
        campsiteService.deleteCampsite(id);
    }

    @GetMapping("/search/ai")
    @Operation(summary = "AI 自然語言搜尋", description = "用自然語言描述想找的營地，例如：寵物友善台中高山有電")
    public CustomResult<List<CampsiteDTO>> aiSearch(
            @Parameter(description = "自然語言查詢") @RequestParam String query,
            @Parameter(description = "回傳筆數") @RequestParam(defaultValue = "10") int topK) {
        List<CampsiteDTO> result = embeddingService.semanticSearch(query, topK)
            .stream()
            .map(campsiteToDtoConverter::convert)
            .toList();
        return CustomResult.result(true, result);
    }

}

