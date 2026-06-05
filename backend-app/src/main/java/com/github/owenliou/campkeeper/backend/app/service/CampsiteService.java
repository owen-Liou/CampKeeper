package com.github.owenliou.campkeeper.backend.app.service;

import com.github.owenliou.campkeeper.backend.app.dto.CampsiteDTO;
import com.github.owenliou.campkeeper.base.service.CommonService;
import com.github.owenliou.campkeeper.model.entity.Campsite;

/**
 * 營地業務邏輯層接口
 */
public interface CampsiteService extends CommonService<Campsite, Long> {

    Campsite getByCampsiteId(String campsiteId);

    Campsite createCampsite(CampsiteDTO campsiteDto);

    Campsite updateCampsite(CampsiteDTO campsiteDto);

    void deleteCampsite(String campsiteId);

    int syncFromICamping();

}
