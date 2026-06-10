package com.github.owenliou.campkeeper.backend.app.ai.restcontroller;

import com.github.owenliou.campkeeper.backend.app.ai.service.CampsiteEmbeddingService;
import com.github.owenliou.campkeeper.common.CustomResult;
import com.github.owenliou.campkeeper.web.common.restcontroller.AbstractSyncRestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@Tag(name = "Admin API", description = "管理員操作 API")
public class AdminController extends AbstractSyncRestController {

    private final CampsiteEmbeddingService embeddingService;

    @PostMapping("/embeddings/sync")
    @Operation(summary = "重建向量索引", description = "將所有營地資料重新生成 embedding 並寫入 pgvector")
    public CustomResult<String> syncEmbeddings() {
        int count = embeddingService.syncAllEmbeddings();
        return CustomResult.result(true, "完成，共同步 " + count + " 筆向量資料");
    }
}