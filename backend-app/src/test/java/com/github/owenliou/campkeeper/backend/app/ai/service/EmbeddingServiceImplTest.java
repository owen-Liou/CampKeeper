package com.github.owenliou.campkeeper.backend.app.ai.service;

import com.github.owenliou.campkeeper.backend.app.AbstractUnitTestCase;
import com.github.owenliou.campkeeper.model.entity.Campsite;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
class EmbeddingServiceImplTest extends AbstractUnitTestCase {

    @Autowired
    private EmbeddingService embeddingService;

    @Test
    void  syncCampsitesToVectorStoreTest(){
        int count = embeddingService.syncCampsitesToVectorStore();
        log.info("完成，共同步 {} 筆向量資料", count);
    }

    @Test
    void semanticSearchCampsitesTest(){
        String query = "適合親子露營的營地";
        int topK = 5;
        List<Campsite> campsites = embeddingService.semanticSearchCamp(query, topK);
        log.info("搜尋結果：");
        log.info("共找到 {} 筆相關營地", campsites.size());
        campsites.forEach(campsite -> log.info("營地ID: {}, 名稱: {}, 地址: {}", campsite.getId(), campsite.getName(), campsite.getCity()+campsite.getDistrict()));
    }

}
