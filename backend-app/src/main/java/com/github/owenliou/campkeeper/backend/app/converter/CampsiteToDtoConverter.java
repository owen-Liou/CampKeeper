package com.github.owenliou.campkeeper.backend.app.converter;

import com.github.owenliou.campkeeper.backend.app.dto.CampsiteDto;
import com.github.owenliou.campkeeper.common.converter.AbstractDtoConverter;
import com.github.owenliou.campkeeper.model.entity.Campstore;
import org.springframework.stereotype.Component;

/**
 * 營地 DTO 轉換器
 * 負責 Campstore 實體與 CampsiteDto 之間的雙向轉換
 */
@Component
public class CampsiteToDtoConverter extends AbstractDtoConverter<Campstore, CampsiteDto> {
    
    /**
     * Entity → DTO
     * 從數據庫實體轉換為 API 回應 DTO
     */
    @Override
    protected CampsiteDto doConvert(Campstore source) {
        if (source == null) {
            return new CampsiteDto();
        }
        return CampsiteDto.builder()
            .id(source.getId())
            .name(source.getName())
            .description(source.getDescription())
            .city(source.getCity())
            .district(source.getDistrict())
            .altitude(source.getAltitude())
            .hasPower(source.getHasPower())
            .hasShower(source.getHasShower())
            .storeName(source.getStoreName())
            .area(source.getArea())
            .facilities(source.getFacilities())
            .sourceUrl(source.getSourceUrl())
            .createdAt(source.getCreatedAt())
            .updatedAt(source.getUpdatedAt())
            .build();
    }
    
    /**
     * DTO → Entity
     * 從 API 請求 DTO 轉換為數據庫實體
     */
    @Override
    protected Campstore doReverse(CampsiteDto target) {
        if (target == null) {
            return new Campstore();
        }
        return Campstore.builder()
            .id(target.getId())
            .name(target.getName())
            .description(target.getDescription())
            .city(target.getCity())
            .district(target.getDistrict())
            .altitude(target.getAltitude())
            .hasPower(target.getHasPower())
            .hasShower(target.getHasShower())
            .storeName(target.getStoreName())
            .area(target.getArea())
            .facilities(target.getFacilities())
            .sourceUrl(target.getSourceUrl())
            .build();
    }

    public void doUpdate(Campstore source, CampsiteDto target) {
        source.setName(target.getName());
        source.setDescription(target.getDescription());
        source.setCity(target.getCity());
        source.setDistrict(target.getDistrict());
        source.setAltitude(target.getAltitude());
        source.setHasPower(target.getHasPower());
        source.setHasShower(target.getHasShower());
        source.setStoreName(target.getStoreName());
        source.setArea(target.getArea());
        source.setFacilities(target.getFacilities());
        source.setSourceUrl(target.getSourceUrl());
    }
}



