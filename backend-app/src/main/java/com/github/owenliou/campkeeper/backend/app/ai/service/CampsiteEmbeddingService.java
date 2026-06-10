package com.github.owenliou.campkeeper.backend.app.ai.service;

import com.github.owenliou.campkeeper.backend.app.repository.CampsiteRepository;
import com.github.owenliou.campkeeper.model.entity.Campsite;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Service for managing campsite embeddings using a vector store.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CampsiteEmbeddingService {

    private final VectorStore vectorStore;

    private final CampsiteRepository campsiteRepository;

    /**
     * Upserts the embedding for a given campsite into the vector store.
     * @param campsite The campsite to upsert the embedding for.
     */
    public void upsertEmbedding(Campsite campsite) {
        String docId = UUID.nameUUIDFromBytes(("campsite-" + campsite.getId()).getBytes()).toString();
        try {
            vectorStore.delete(List.of(docId));
            vectorStore.add(List.of(
                    Document.builder()
                            .id(docId)
                            .text(buildContent(campsite))
                            .metadata(Map.of("campsite_id", campsite.getId()))
                            .build()
            ));
        }catch (Exception e){
            log.error("upsertEmbedding error: {}", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    /**
     * Synchronize all campsite embeddings in batches with a 2-second delay between batches.
     * @return The total number of campsites processed.
     */
    public int syncAllEmbeddings() {
        List<Campsite> all = campsiteRepository.findAll();

        int batchSize = 50;
        for (int i = 0; i < all.size(); i += batchSize) {
            List<Campsite> batch = all.subList(i, Math.min(i + batchSize, all.size()));
            batch.forEach(this::upsertEmbedding);

            log.info("已同步 {}/{} 筆", Math.min(i + batchSize, all.size()), all.size());

            if (i + batchSize < all.size()) {
                try {
                    Thread.sleep(2000); // 每 50 筆等 2 秒
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        return all.size();
    }

    public List<Campsite> semanticSearch(String query, int topK) {
        List<Document> docs = vectorStore.similaritySearch(
            SearchRequest.builder().query(query).topK(topK).build()
        );
        List<Long> ids = docs.stream()
            .map(d -> ((Number) d.getMetadata().get("campsite_id")).longValue())
            .toList();
        return campsiteRepository.findAllById(ids);
    }

    private String buildContent(Campsite c) {
        return String.format(
            "營地：%s，位於%s%s，海拔%s公尺，%s，寵物友善：%s，供電：%s，衛浴：%s。%s",
            c.getName(),
            c.getCity() != null ? c.getCity() : "",
            c.getDistrict() != null ? c.getDistrict() : "",
            c.getAltitude() != null ? c.getAltitude() : "不明",
            c.getArea() != null ? c.getArea() : "",
            Boolean.TRUE.equals(c.getPetFriendly()) ? "是" : "否",
            Boolean.TRUE.equals(c.getHasPower()) ? "是" : "否",
            Boolean.TRUE.equals(c.getHasShower()) ? "是" : "否",
            c.getDescription() != null ? c.getDescription() : ""
        );
    }
}