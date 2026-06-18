package com.github.owenliou.campkeeper.model.repository;

import com.github.owenliou.campkeeper.model.entity.Campstore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CampsiteRepository extends JpaRepository<Campstore, Long>,
        JpaSpecificationExecutor<Campstore> {

    List<Campstore> findByCity(String city);

    List<Campstore> findByCityAndDistrict(String city, String district);

}