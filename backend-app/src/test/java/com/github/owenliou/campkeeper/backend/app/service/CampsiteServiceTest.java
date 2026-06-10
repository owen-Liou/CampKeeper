package com.github.owenliou.campkeeper.backend.app.service;

import com.github.owenliou.campkeeper.backend.app.AbstractUnitTestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
class CampsiteServiceTest extends AbstractUnitTestCase {

    @Autowired
    private CampsiteService campsiteService;

    @Test
    void syncFromICampingTest() {
        int count = campsiteService.syncFromICamping();
        log.info("同步完成，共更新 {} 筆營地資料", count);
    }

}
