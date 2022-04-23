package com.stalary.pf.user.repo;

import com.stalary.pf.user.data.entity.UserEs;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserEsRepo extends ElasticsearchCrudRepository<UserEs, Long> {

    List<UserEs> findByIntentionCompanyOrIntentionJob(String company, String job);
}
