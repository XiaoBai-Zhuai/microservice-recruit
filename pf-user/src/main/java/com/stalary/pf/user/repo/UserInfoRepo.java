package com.stalary.pf.user.repo;

import com.stalary.pf.user.data.entity.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserInfoRepo extends JpaRepository<UserInfoEntity, Long> {

    UserInfoEntity findByUserId(Long userId);

    List<UserInfoEntity> findByIntentionCompanyContainsOrIntentionJobContains(String company, String job);
}
