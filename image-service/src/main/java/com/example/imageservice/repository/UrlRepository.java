package com.example.imageservice.repository;

import com.example.imageservice.domain.ImageUrl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlRepository extends JpaRepository<ImageUrl, Long> {

}
