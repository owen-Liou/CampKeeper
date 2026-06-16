package com.github.owenliou.campkeeper.backend.app.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.owenliou.campkeeper.backend.app.ai.service.impl.EmbeddingServiceImpl;
import com.github.owenliou.campkeeper.backend.app.external.icamping.client.ICampingClient;
import com.github.owenliou.campkeeper.backend.app.external.icamping.dto.ICampingStoreListDto;
import com.github.owenliou.campkeeper.backend.app.service.CampsiteService;
import com.github.owenliou.campkeeper.backend.app.service.ICampService;
import com.github.owenliou.campkeeper.model.entity.Campsite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ICampServiceImpl implements ICampService {

    @Autowired
    private ICampingClient iCampingClient;

    @Autowired
    private CampsiteService campsiteService;

    @Autowired
    private EmbeddingServiceImpl embeddingService;

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
            Campsite campsite = campsiteService.getByStoreName(store.getStoreName());
            mapStoreToEntity(store, campsite);
            Campsite saved = campsiteService.save(campsite);
            embeddingService.upsertEmbedding(saved);
            count++;
        }
        return count;
    }

    private void mapStoreToEntity(ICampingStoreListDto store, Campsite campsite) {
        campsite.setStoreName(store.getStoreName());
        campsite.setName(store.getStoreAlias() != null ? store.getStoreAlias() : store.getStoreName());
        campsite.setCity(store.getCity());
        campsite.setDistrict(store.getDistrict());
        campsite.setArea(store.getArea());
        if (store.getAltitude() != null && !store.getAltitude().isBlank()) {
            try {
                campsite.setAltitude(Integer.parseInt(store.getAltitude()));
            } catch (NumberFormatException ignored) {
            }
        }
        List<String> facilities = store.getFacility();
        if (facilities != null) {
            campsite.setHasPower(facilities.contains("提供電源"));
            campsite.setPetFriendly(facilities.contains("寵物同行"));
            try {
                campsite.setFacilities(objectMapper.writeValueAsString(facilities));
            } catch (JsonProcessingException ignored) {
            }
        }
        campsite.setSourceUrl("https://m.icamping.app");
    }
}
