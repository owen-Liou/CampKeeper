package com.github.owenliou.campkeeper.backend.app.ai.service;

import com.github.owenliou.campkeeper.model.entity.Campstore;
import org.springframework.ai.document.Document;

import java.util.List;

public interface EmbeddingService {

    /**
     * 同步營地資料到向量資料庫
     * @return
     */
    int syncCampsitesToVectorStore();

    /**
     * 模型 語意搜尋營地
     * @param query
     * @param topK
     * @return
     */
    List<Campstore> semanticSearchCamp(String query, int topK);

    /**
     * 新增或更新營地的 embedding 到向量資料庫
     * @param campstore
     * @return
     */
    Document upsertEmbedding(Campstore campstore);

}
