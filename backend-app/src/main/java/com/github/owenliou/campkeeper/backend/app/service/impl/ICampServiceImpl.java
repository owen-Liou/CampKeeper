package com.github.owenliou.campkeeper.backend.app.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.owenliou.campkeeper.backend.app.ai.service.EmbeddingService;
import com.github.owenliou.campkeeper.backend.app.ai.service.impl.EmbeddingServiceImpl;
import com.github.owenliou.campkeeper.backend.app.external.icamping.client.ICampingClient;
import com.github.owenliou.campkeeper.backend.app.external.icamping.dto.ICampingStoreExternalLinkDto;
import com.github.owenliou.campkeeper.backend.app.external.icamping.dto.ICampingStoreListDto;
import com.github.owenliou.campkeeper.backend.app.repository.CampstoreLinkRepository;
import com.github.owenliou.campkeeper.backend.app.service.CampsiteService;
import com.github.owenliou.campkeeper.backend.app.service.ICampService;
import com.github.owenliou.campkeeper.model.entity.Campstore;
import com.github.owenliou.campkeeper.model.entity.CampstoreLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

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

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 取得愛露營所有營地資料，並同步到本地數據庫
     * @return
     */
    @Override
    public int syncAll() {
        List<ICampingStoreListDto> stores = iCampingClient.fetchAllStores();
        int count = 0;
        for (ICampingStoreListDto store : stores) {
            if (store.getStoreName() == null) continue;
            Campstore campstore = campsiteService.getByStoreName(store.getStoreName());
            if(campstore == null) {
                campstore = new Campstore();
            }
            mapStoreToEntity(store, campstore);
            Campstore saved = campsiteService.save(campstore);
            embeddingService.upsertEmbedding(saved);
            count++;
        }
        return count;
    }

    @Override
    @Transactional
    public int syncAllLinks() {
        List<Campstore> all = campsiteService.findAll();
        int count = 0;
        for (Campstore campstore : all) {
            String storeName = campstore.getStoreName();
            if (storeName == null) continue;

            List<ICampingStoreExternalLinkDto> links = iCampingClient.fetchStoreLinksByStoreName(storeName);
            campstoreLinkRepository.deleteByStoreName(storeName);

            for (ICampingStoreExternalLinkDto dto : links) {
                CampstoreLink link = CampstoreLink.builder()
                        .storeName(storeName)
                        .name(dto.getName())
                        .link(dto.getLink())
                        .sequence(parseSequence(dto.getSequence()))
                        .build();
                campstoreLinkRepository.save(link);
                count++;
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
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
