package com.github.owenliou.campkeeper.backend.app.restcontroller;

import com.github.owenliou.campkeeper.backend.app.converter.CampsiteToDtoConverter;
import com.github.owenliou.campkeeper.backend.app.dto.CampsiteDto;
import com.github.owenliou.campkeeper.backend.app.service.CampsiteService;
import com.github.owenliou.campkeeper.common.CustomResult;
import com.github.owenliou.campkeeper.model.entity.Campstore;
import com.github.owenliou.campkeeper.web.common.restcontroller.AbstractSyncRestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@Tag(name = "Campstore API", description = "營地管理 API")
public class RestCampsiteController extends AbstractSyncRestController {

    private final CampsiteService campsiteService;

    private final CampsiteToDtoConverter campsiteToDtoConverter;

    @GetMapping
    @Operation(summary = "查詢營地列表", description = "支援分頁與縣市篩選，預設每頁 20 筆")
    public CustomResult<Page<CampsiteDto>> getAll(
            @Parameter(description = "縣市篩選，例如：台中市") @RequestParam(required = false) String city,
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        Page<CampsiteDto> page = campsiteService.searchCampsites(city, pageable).map(campsiteToDtoConverter::convert);
        return CustomResult.result(true, page);
    }


    /**
     * 根據 ID 查詢單筆營地
     */
    @GetMapping("/{id}")
    @Operation(summary = "查詢單筆營地", description = "根據營地 ID 查詢詳細資訊")
    public CustomResult<CampsiteDto> getById(@Parameter(description = "營地 ID") @PathVariable String id) {
        Campstore campstore = campsiteService.getByCampsiteId(id);
        return CustomResult.result(true, campsiteToDtoConverter.convert(campstore));
    }

    /**
     * 新增營地
     */
    @PostMapping("/create")
    @Operation(summary = "新增營地", description = "添加新的營地資訊")
    public CustomResult<CampsiteDto> create(@RequestBody CampsiteDto campsiteDto) {
        Campstore campstore = campsiteService.createCampsite(campsiteDto);
        return CustomResult.result(true, campsiteToDtoConverter.convert(campstore));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新營地", description = "根據營地 ID 更新營地資訊")
    public CustomResult<CampsiteDto> update(@PathVariable String id, @RequestBody CampsiteDto campsiteDto) {
        campsiteDto.setId(Long.parseLong(id));
        Campstore campstore = campsiteService.updateCampsite(campsiteDto);
        return CustomResult.result(true, campsiteToDtoConverter.convert(campstore));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "刪除營地", description = "根據營地 ID 刪除營地資訊")
    public void delete(@PathVariable String id) {
        campsiteService.deleteCampsite(id);
    }

}

