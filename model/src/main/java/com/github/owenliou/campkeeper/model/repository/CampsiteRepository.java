package com.github.owenliou.campkeeper.model.repository;

import com.github.owenliou.campkeeper.model.entity.Campsite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CampsiteRepository extends JpaRepository<Campsite, Long>,
        JpaSpecificationExecutor<Campsite> {

    List<Campsite> findByCity(String city);

    List<Campsite> findByCityAndDistrict(String city, String district);

    List<Campsite> findByPetFriendlyTrue();
}