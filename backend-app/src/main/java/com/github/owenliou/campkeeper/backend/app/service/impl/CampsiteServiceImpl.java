package com.github.owenliou.campkeeper.backend.app.service.impl;

import com.github.owenliou.campkeeper.backend.app.ai.service.EmbeddingService;
import com.github.owenliou.campkeeper.backend.app.converter.CampsiteToDtoConverter;
import com.github.owenliou.campkeeper.backend.app.dto.CampsiteDto;
import com.github.owenliou.campkeeper.backend.app.repository.CampsiteRepository;
import com.github.owenliou.campkeeper.backend.app.service.AbstractService;
import com.github.owenliou.campkeeper.backend.app.service.CampsiteService;
import com.github.owenliou.campkeeper.common.exception.CampNotFoundException;
import com.github.owenliou.campkeeper.model.entity.Campstore;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * 營地業務邏輯層實現
 * 提供營地相關的業務操作
 */
@Service
@Transactional
public class CampsiteServiceImpl extends AbstractService<Campstore, Long> implements CampsiteService {

    @Autowired
    private CampsiteRepository repository;

    @Autowired
    private CampsiteToDtoConverter campsiteToDtoConverter;

    @Autowired
    private EmbeddingService embeddingService;


    @Override
    public Campstore createCampsite(CampsiteDto campsiteDto) {
        Campstore campstore = campsiteToDtoConverter.reverse(campsiteDto);
        syncCampsiteToVectorStore(campstore);
        return save(campstore);
    }

    @Override
    public Campstore getByCampsiteId(String campsiteId) {
        Long id = Long.parseLong(campsiteId);
        return findById(id).orElseThrow(() -> new CampNotFoundException("Campstore not found with id: " + id));
    }

    @Override
    public Page<Campstore> searchCampsites(String city, Pageable pageable) {
        return repository.searchPaged(city, pageable);
    }

    @Override
    public Campstore getByStoreName(String storeName) {
        return repository.findByStoreName(storeName).orElse(null);
    }


    @Override
    public Campstore updateCampsite(CampsiteDto campsiteDto) {
        Long id = campsiteDto.getId();
        Campstore existingCampSite = findById(id).orElseThrow(() -> new CampNotFoundException("Campstore not found with id: " + id));
        campsiteToDtoConverter.doUpdate(existingCampSite, campsiteDto);
        syncCampsiteToVectorStore(existingCampSite);
        return save(existingCampSite);
    }

    @Override
    public void deleteCampsite(String campsiteId) {
        Long id = Long.parseLong(campsiteId);
        Campstore existingCampSite = findById(id).orElseThrow(() -> new CampNotFoundException("Campstore not found with id: " + id));
        delete(existingCampSite);
    }

    public Document syncCampsiteToVectorStore(Campstore campstore){
        return embeddingService.upsertEmbedding(campstore);
    }

}

