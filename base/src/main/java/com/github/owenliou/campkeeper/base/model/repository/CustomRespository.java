package com.github.owenliou.campkeeper.base.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
// @RepositoryRestResource(exported = false) // 避免啟用 Spring Data REST 後， Repository 被轉成 API
public interface CustomRespository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

}
