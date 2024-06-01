package com.photogallery.photogallery.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.photogallery.photogallery.Entity.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

	List<Profile> findByUserid(Long userid);

}