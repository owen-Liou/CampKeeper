package com.github.owenliou.campkeeper.backend.app.repository;

import com.github.owenliou.campkeeper.base.model.repository.CustomRespository;
import com.github.owenliou.campkeeper.model.entity.Campstore;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * 營地數據存取層
 * 提供營地資料的數據庫操作接口
 */
@Repository
public interface CampsiteRepository extends CustomRespository<Campstore, Long> {

    Optional<Campstore> findByStoreName(String storeName);

    /**
     * 根據縣市查詢營地
     */
    List<Campstore> findByCity(String city);

    /**
     * 根據縣市和鄉鎮區查詢營地
     */
    List<Campstore> findByCityAndDistrict(String city, String district);

    @Query("SELECT c FROM Campstore c WHERE (:city IS NULL OR c.city = :city)")
    Page<Campstore> searchPaged(@Param("city") String city, Pageable pageable);

    /**
     * 複合條件查詢（使用 JPQL）
     */
    @Query("SELECT c FROM Campstore c WHERE " +
           "(:city IS NULL OR c.city = :city) AND " +
           "(:hasPower IS NULL OR c.hasPower = :hasPower) AND " +
           "(:hasShower IS NULL OR c.hasShower = :hasShower) AND " +
           "(:altitudeMin IS NULL OR c.altitude >= :altitudeMin) AND " +
           "(:altitudeMax IS NULL OR c.altitude <= :altitudeMax)")
    List<Campstore> findByCriteria(
        @Param("city") String city,
        @Param("hasPower") Boolean hasPower,
        @Param("hasShower") Boolean hasShower,
        @Param("altitudeMin") Integer altitudeMin,
        @Param("altitudeMax") Integer altitudeMax
    );
}

