package com.example.imageservice.repository;

import com.example.imageservice.domain.ImageUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlRepository extends JpaRepository<ImageUrl, Long> {

    Optional<ImageUrl> findBystoredName(String storedName);

}
