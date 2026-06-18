package com.github.owenliou.campkeeper.backend.app.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.owenliou.campkeeper.backend.app.ai.service.EmbeddingService;
import com.github.owenliou.campkeeper.backend.app.external.icamping.client.ICampingClient;
import com.github.owenliou.campkeeper.backend.app.external.icamping.dto.ICampingStoreExternalLinkDto;
import com.github.owenliou.campkeeper.backend.app.external.icamping.dto.ICampingStoreListDto;
import com.github.owenliou.campkeeper.backend.app.external.icamping.snapshot.ICampingSnapshotService;
import com.github.owenliou.campkeeper.backend.app.repository.CampstoreLinkRepository;
import com.github.owenliou.campkeeper.backend.app.service.CampsiteService;
import com.github.owenliou.campkeeper.backend.app.service.ICampService;
import com.github.owenliou.campkeeper.model.entity.Campstore;
import com.github.owenliou.campkeeper.model.entity.CampstoreLink;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ICampServiceImpl implements ICampService {

    @Autowired
    private ICampingClient iCampingClient;

    @Autowired
    private CampsiteService campsiteService;

    @Autowired
    private EmbeddingService embeddingService;

    @Autowired
    private CampstoreLinkRepository campstoreLinkRepository;

    @Autowired
    private CampStoreLinkServiceImpl campStoreLinkService;

    @Autowired
    private ICampingSnapshotService snapshotService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 打一次 API，將營地列表存成 JSON snapshot
     */
    @Override
    public void saveSnapshot() {
        List<ICampingStoreListDto> stores = iCampingClient.fetchAllStores();
        snapshotService.saveStores(stores);
        log.info("Stores snapshot saved: {} records", stores.size());
    }

    /**
     * 對每個已存在的營地打 API 取外部連結，存成 JSON snapshot
     * 需先執行 syncAll() 以確保 DB 有營地資料
     */
    @Override
    public void saveLinksSnapshot() {
        List<Campstore> all = campsiteService.findAll();
        Map<String, List<ICampingStoreExternalLinkDto>> linksMap = new HashMap<>();

        for (Campstore campstore : all) {
            String storeName = campstore.getStoreName();
            if (storeName == null) continue;

            List<ICampingStoreExternalLinkDto> links = iCampingClient.fetchStoreLinksByStoreName(storeName);
            linksMap.put(storeName, links);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        snapshotService.saveLinks(linksMap);
        log.info("Links snapshot saved: {} stores", linksMap.size());
    }

    /**
     * 從 snapshot 檔同步營地資料到 DB
     */
    @Override
    public int syncAll() {
        List<ICampingStoreListDto> stores = snapshotService.loadStores();
        int count = 0;
        for (ICampingStoreListDto store : stores) {
            if (store.getStoreName() == null) continue;
            Campstore campstore = campsiteService.getByStoreName(store.getStoreName());
            if (campstore == null) {
                campstore = new Campstore();
            }
            mapStoreToEntity(store, campstore);
            Campstore saved = campsiteService.save(campstore);
            embeddingService.upsertEmbedding(saved);
            count++;
        }
        return count;
    }

    /**
     * 從 snapshot 檔同步外部連結到 DB
     */
    @Override
    @Transactional
    public int syncAllLinks() {
        Map<String, List<ICampingStoreExternalLinkDto>> linksMap = snapshotService.loadLinks();
        int count = 0;

        for (Map.Entry<String, List<ICampingStoreExternalLinkDto>> entry : linksMap.entrySet()) {
            String storeName = entry.getKey();
            campstoreLinkRepository.deleteByStoreName(storeName);

            for (ICampingStoreExternalLinkDto dto : entry.getValue()) {
                CampstoreLink link = CampstoreLink.builder()
                        .storeName(storeName)
                        .name(dto.getName())
                        .link(dto.getLink())
                        .sequence(parseSequence(dto.getSequence()))
                        .build();
                campstoreLinkRepository.save(link);
                count++;
            }
        }
        return count;
    }

    private Integer parseSequence(String sequence) {
        if (sequence == null || sequence.isBlank()) return 0;
        try {
            return Integer.parseInt(sequence);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void mapStoreToEntity(ICampingStoreListDto store, Campstore campstore) {
        campstore.setStoreName(store.getStoreName());
        campstore.setName(store.getStoreAlias() != null ? store.getStoreAlias() : store.getStoreName());
        campstore.setCity(store.getCity());
        campstore.setDistrict(store.getDistrict());
        campstore.setArea(store.getArea());
        if (store.getAltitude() != null && !store.getAltitude().isBlank()) {
            try {
                campstore.setAltitude(Integer.parseInt(store.getAltitude()));
            } catch (NumberFormatException ignored) {
            }
        }
        List<String> facilities = store.getFacility();
        if (facilities != null) {
            campstore.setHasPower(facilities.contains("提供電源"));
            try {
                campstore.setFacilities(objectMapper.writeValueAsString(facilities));
            } catch (JsonProcessingException ignored) {
            }
        }
        campstore.setSourceUrl("https://m.icamping.app");
    }
}
