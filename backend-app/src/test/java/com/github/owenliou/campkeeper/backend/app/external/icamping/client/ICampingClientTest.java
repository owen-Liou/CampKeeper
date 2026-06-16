package com.github.owenliou.campkeeper.backend.app.external.icamping.client;

import com.github.owenliou.campkeeper.backend.app.AbstractUnitTestCase;
import com.github.owenliou.campkeeper.backend.app.external.icamping.dto.ICampingStoreExternalLinkDto;
import com.github.owenliou.campkeeper.backend.app.external.icamping.dto.ICampingStoreListDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
class ICampingClientTest extends AbstractUnitTestCase {

    @Autowired
    private ICampingClient iCampingClient;

    @Test
    void testFetchAllStores() {
        List<ICampingStoreListDto> stores = iCampingClient.fetchAllStores();
        log.info("從愛露營 API 取得的營地數量: {}", stores.size());
        assertNotNull(stores);
        assertFalse(stores.isEmpty(), "應該至少有一筆營地資料");
    }

    @Test
    void testFetchStoreLinksByStoreName() {
        List<ICampingStoreExternalLinkDto>  stores = iCampingClient.fetchStoreLinksByStoreName("kusu628");
        log.info("從愛露營 API 取得的外部連結數量: {}", stores.size());
        stores.forEach(store -> {
            log.info("外部連結 - 名稱: {}, 連結: {},序號 : {}, 營地 : {}", store.getName(), store.getLink(),store.getSequence(),store.getStoreName());
        });
        assertNotNull(stores);
    }
}
