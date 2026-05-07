package com.github.owenliou.campkeeper.backend.app.restcontroller;

import com.github.owenliou.campkeeper.backend.app.converter.CampsiteToDtoConverter;
import com.github.owenliou.campkeeper.backend.app.dto.CampsiteDTO;
import com.github.owenliou.campkeeper.backend.app.service.CampsiteService;
import com.github.owenliou.campkeeper.common.CustomResult;
import com.github.owenliou.campkeeper.common.exception.CampNotFoundException;
import com.github.owenliou.campkeeper.model.entity.Campsite;
import com.github.owenliou.campkeeper.web.common.restcontroller.AbstractSyncRestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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
    public CustomResult<Void> delete(@PathVariable String id) {
        campsiteService.deleteCampsite(id);

        // 成功執行到這，代表刪除完成
        return CustomResult.result(true, null);
    }

}

