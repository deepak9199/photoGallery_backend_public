package com.photogallery.photogallery.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.photogallery.photogallery.Entity.PhotoGallery;

public interface PhotoGalleryRepository extends JpaRepository<PhotoGallery, Long> {
	List<PhotoGallery> findByName(String name);

	List<PhotoGallery> findByUserid(Long id);

	List<PhotoGallery> findByCategoryid(Long categoryid);
}
