package com.github.owenliou.campkeeper.backend.app.ai.service;

import com.github.owenliou.campkeeper.backend.app.AbstractUnitTestCase;
import com.github.owenliou.campkeeper.backend.app.ai.service.impl.EmbeddingServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
class EmbeddingServiceImplTest extends AbstractUnitTestCase {

    @Autowired
    private EmbeddingServiceImpl embeddingServiceImpl;

    @Test
    void  syncCampsitesToVectorStoreTest(){
        int count = embeddingServiceImpl.syncCampsitesToVectorStore();
        log.info("完成，共同步 {} 筆向量資料", count);
    }

}
