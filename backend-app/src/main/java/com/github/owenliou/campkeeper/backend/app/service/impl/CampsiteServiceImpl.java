package com.github.owenliou.campkeeper.backend.app.service.impl;

import com.github.owenliou.campkeeper.backend.app.converter.CampsiteToDtoConverter;
import com.github.owenliou.campkeeper.backend.app.dto.CampsiteDTO;
import com.github.owenliou.campkeeper.backend.app.repository.CampsiteRepository;
import com.github.owenliou.campkeeper.backend.app.service.AbstractService;
import com.github.owenliou.campkeeper.backend.app.service.CampsiteService;
import com.github.owenliou.campkeeper.common.exception.CampNotFoundException;
import com.github.owenliou.campkeeper.model.entity.Campsite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    @Override
    public Campsite getByCampsiteId(String campsiteId) {
        Long id = Long.parseLong(campsiteId);
        return findById(id).orElseThrow(() -> new CampNotFoundException("Campsite not found with id: " + id));
    }

    @Override
    public Campsite createCampsite(CampsiteDTO campsiteDto) {
        Campsite campsite = campsiteToDtoConverter.reverse(campsiteDto);
        return save(campsite);
    }

    @Override
    public Campsite updateCampsite(CampsiteDTO campsiteDto) {
        Long id = campsiteDto.getId();
        Campsite existingCampSite = findById(id).orElseThrow(() -> new CampNotFoundException("Campsite not found with id: " + id));
        return save(existingCampSite);
    }

    @Override
    public void deleteCampsite(String campsiteId) {
        Long id = Long.parseLong(campsiteId);
        Campsite existingCampSite = findById(id).orElseThrow(() -> new CampNotFoundException("Campsite not found with id: " + id));
        delete(existingCampSite);

    }
}

