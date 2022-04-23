package com.stalary.pf.recruit.repo;

import com.stalary.pf.recruit.data.entity.CompanyEntity;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CompanyRepo extends BaseRepo<CompanyEntity, Long> {

    List<CompanyEntity> findByNameIsLike(String key);
}
