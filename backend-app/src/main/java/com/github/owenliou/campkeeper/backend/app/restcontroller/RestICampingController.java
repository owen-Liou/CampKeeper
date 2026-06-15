package com.github.owenliou.campkeeper.backend.app.restcontroller;

import com.github.owenliou.campkeeper.backend.app.service.ICampService;
import com.github.owenliou.campkeeper.common.CustomResult;
import com.github.owenliou.campkeeper.web.common.restcontroller.AbstractSyncRestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 營地管理 REST 控制器
 * 提供營地資料的 CRUD API 操作
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/icamping")
@Tag(name = "Campsite API", description = "愛露營 API")
public class RestICampingController extends AbstractSyncRestController {

    private final ICampService iCampService;

    @PostMapping("/sync")
    @Operation(summary = "同步iCamping資料", description = "從iCamping API 匯入最新營地資料")
    public CustomResult<String> sync() {
        int count = iCampService.syncAll();
        return CustomResult.result(true, "同步完成，共更新 " + count + " 筆營地資料");
    }

}

