package com.github.owenliou.campkeeper.backend.app.service;

import com.github.owenliou.campkeeper.backend.app.dto.CampsiteDto;
import com.github.owenliou.campkeeper.base.service.CommonService;
import com.github.owenliou.campkeeper.model.entity.Campstore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 營地業務邏輯層接口
 */
public interface CampsiteService extends CommonService<Campstore, Long> {

    Campstore getByCampsiteId(String campsiteId);

    Campstore createCampsite(CampsiteDto campsiteDto);

    Campstore updateCampsite(CampsiteDto campsiteDto);

    void deleteCampsite(String campsiteId);

    Page<Campstore> searchCampsites(String city, Pageable pageable);

    Campstore getByStoreName(String storeName);

}
