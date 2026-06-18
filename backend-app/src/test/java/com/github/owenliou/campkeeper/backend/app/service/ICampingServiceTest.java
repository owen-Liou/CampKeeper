package com.github.owenliou.campkeeper.backend.app.service;

import com.github.owenliou.campkeeper.backend.app.AbstractUnitTestCase;
import com.github.owenliou.campkeeper.backend.app.ai.service.impl.EmbeddingServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
class ICampingServiceTest extends AbstractUnitTestCase {

    @Autowired
    private ICampService iCampService;

    @Test
    void saveSnapshotTest() {
        iCampService.saveSnapshot();
    }

    @Test
    void syncFromICampingTest() {
        iCampService.saveSnapshot();
        int count = iCampService.syncAll();
        log.info("同步完成，共更新 {} 筆營地資料", count);
    }

}
