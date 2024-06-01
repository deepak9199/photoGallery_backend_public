package com.photogallery.photogallery.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.photogallery.photogallery.Entity.PhotoCategory;

public interface PhotoCategoryRepository extends JpaRepository<PhotoCategory, Long> {
	List<PhotoCategory> findByUserid(Long userid);
}
