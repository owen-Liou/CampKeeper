package com.github.owenliou.campkeeper.backend.app.service;

import com.github.owenliou.campkeeper.backend.app.dto.CampsiteDto;
import com.github.owenliou.campkeeper.base.service.CommonService;
import com.github.owenliou.campkeeper.model.entity.Campsite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 營地業務邏輯層接口
 */
public interface CampsiteService extends CommonService<Campsite, Long> {

    Campsite getByCampsiteId(String campsiteId);

    Campsite createCampsite(CampsiteDto campsiteDto);

    Campsite updateCampsite(CampsiteDto campsiteDto);

    void deleteCampsite(String campsiteId);

    Page<Campsite> searchCampsites(String city, Pageable pageable);

    Campsite getByStoreName(String storeName);

}
