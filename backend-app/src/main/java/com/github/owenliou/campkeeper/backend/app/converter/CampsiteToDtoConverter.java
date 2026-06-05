package com.github.owenliou.campkeeper.backend.app.converter;

import com.github.owenliou.campkeeper.backend.app.dto.CampsiteDTO;
import com.github.owenliou.campkeeper.common.converter.AbstractDtoConverter;
import com.github.owenliou.campkeeper.model.entity.Campsite;
import org.springframework.stereotype.Component;

/**
 * 營地 DTO 轉換器
 * 負責 Campsite 實體與 CampsiteDTO 之間的雙向轉換
 */
@Component
public class CampsiteToDtoConverter extends AbstractDtoConverter<Campsite, CampsiteDTO> {
    
    /**
     * Entity → DTO
     * 從數據庫實體轉換為 API 回應 DTO
     */
    @Override
    protected CampsiteDTO doConvert(Campsite source) {
        if (source == null) {
            return new CampsiteDTO();
        }
        return CampsiteDTO.builder()
            .id(source.getId())
            .name(source.getName())
            .description(source.getDescription())
            .city(source.getCity())
            .district(source.getDistrict())
            .altitude(source.getAltitude())
            .latitude(source.getLatitude())
            .longitude(source.getLongitude())
            .hasPower(source.getHasPower())
            .hasShower(source.getHasShower())
            .petFriendly(source.getPetFriendly())
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
    protected Campsite doReverse(CampsiteDTO target) {
        if (target == null) {
            return new Campsite();
        }
        return Campsite.builder()
            .id(target.getId())
            .name(target.getName())
            .description(target.getDescription())
            .city(target.getCity())
            .district(target.getDistrict())
            .altitude(target.getAltitude())
            .latitude(target.getLatitude())
            .longitude(target.getLongitude())
            .hasPower(target.getHasPower())
            .hasShower(target.getHasShower())
            .petFriendly(target.getPetFriendly())
            .storeName(target.getStoreName())
            .area(target.getArea())
            .facilities(target.getFacilities())
            .sourceUrl(target.getSourceUrl())
            .build();
    }
}



