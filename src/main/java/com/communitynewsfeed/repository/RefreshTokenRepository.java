package com.communitynewsfeed.repository;

import com.communitynewsfeed.domain.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    Optional<RefreshToken> findByKey(String key);
    Optional<RefreshToken> deleteByKey(String key);

}
