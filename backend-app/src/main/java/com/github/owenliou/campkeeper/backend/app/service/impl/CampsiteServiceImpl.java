package com.github.owenliou.campkeeper.backend.app.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.owenliou.campkeeper.backend.app.converter.CampsiteToDtoConverter;
import com.github.owenliou.campkeeper.backend.app.dto.CampsiteDTO;
import com.github.owenliou.campkeeper.backend.app.external.icamping.client.ICampingClient;
import com.github.owenliou.campkeeper.backend.app.external.icamping.dto.ICampingStore;
import com.github.owenliou.campkeeper.backend.app.repository.CampsiteRepository;
import com.github.owenliou.campkeeper.backend.app.service.AbstractService;
import com.github.owenliou.campkeeper.backend.app.ai.service.impl.EmbeddingServiceImpl;
import com.github.owenliou.campkeeper.backend.app.service.CampsiteService;
import com.github.owenliou.campkeeper.common.exception.CampNotFoundException;
import com.github.owenliou.campkeeper.model.entity.Campsite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 營地業務邏輯層實現
 * 提供營地相關的業務操作
 */
@Service
@Transactional
public class CampsiteServiceImpl extends AbstractService<Campsite, Long> implements CampsiteService {

    @Autowired
    private CampsiteRepository repository;

    @Autowired
    private CampsiteToDtoConverter campsiteToDtoConverter;

    @Autowired
    private ICampingClient iCampingClient;

    @Autowired
    private EmbeddingServiceImpl embeddingService;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public Campsite getByCampsiteId(String campsiteId) {
        Long id = Long.parseLong(campsiteId);
        return findById(id).orElseThrow(() -> new CampNotFoundException("Campsite not found with id: " + id));
    }

    @Override
    public Campsite createCampsite(CampsiteDTO campsiteDto) {
        Campsite campsite = campsiteToDtoConverter.reverse(campsiteDto);
        Campsite saved = save(campsite);
        embeddingService.upsertEmbedding(saved);
        return saved;
    }

    @Override
    public Campsite updateCampsite(CampsiteDTO campsiteDto) {
        Long id = campsiteDto.getId();
        Campsite existingCampSite = findById(id).orElseThrow(() -> new CampNotFoundException("Campsite not found with id: " + id));
        existingCampSite.setName(campsiteDto.getName());
        existingCampSite.setDescription(campsiteDto.getDescription());
        existingCampSite.setCity(campsiteDto.getCity());
        existingCampSite.setDistrict(campsiteDto.getDistrict());
        existingCampSite.setAltitude(campsiteDto.getAltitude());
        existingCampSite.setLatitude(campsiteDto.getLatitude());
        existingCampSite.setLongitude(campsiteDto.getLongitude());
        existingCampSite.setHasPower(campsiteDto.getHasPower());
        existingCampSite.setHasShower(campsiteDto.getHasShower());
        existingCampSite.setPetFriendly(campsiteDto.getPetFriendly());
        existingCampSite.setStoreName(campsiteDto.getStoreName());
        existingCampSite.setArea(campsiteDto.getArea());
        existingCampSite.setFacilities(campsiteDto.getFacilities());
        existingCampSite.setSourceUrl(campsiteDto.getSourceUrl());
        Campsite saved = save(existingCampSite);
        embeddingService.upsertEmbedding(saved);
        return saved;
    }

    @Override
    public void deleteCampsite(String campsiteId) {
        Long id = Long.parseLong(campsiteId);
        Campsite existingCampSite = findById(id).orElseThrow(() -> new CampNotFoundException("Campsite not found with id: " + id));
        delete(existingCampSite);
    }

    @Override
    public Page<Campsite> searchCampsites(String city, Pageable pageable) {
        return repository.searchPaged(city, pageable);
    }

    @Override
    public int syncFromICamping() {
        List<ICampingStore> stores = iCampingClient.fetchAllStores();
        int count = 0;
        for (ICampingStore store : stores) {
            if (store.getStoreName() == null) continue;
            Campsite campsite = repository.findByStoreName(store.getStoreName())
                    .orElse(new Campsite());
            mapStoreToEntity(store, campsite);
            Campsite saved = save(campsite);
            embeddingService.upsertEmbedding(saved);
            count++;
        }
        return count;
    }

    private void mapStoreToEntity(ICampingStore store, Campsite campsite) {
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

