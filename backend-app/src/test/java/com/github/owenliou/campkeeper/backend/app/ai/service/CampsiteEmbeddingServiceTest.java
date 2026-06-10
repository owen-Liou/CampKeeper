package com.github.owenliou.campkeeper.backend.app.ai.service;

import com.github.owenliou.campkeeper.backend.app.AbstractUnitTestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
class CampsiteEmbeddingServiceTest extends AbstractUnitTestCase {

    @Autowired
    private CampsiteEmbeddingService campsiteEmbeddingService;

    @Test
    void  syncAllEmbeddingsTest(){
        int count = campsiteEmbeddingService.syncAllEmbeddings();
        log.info("完成，共同步 {} 筆向量資料", count);
    }

}
