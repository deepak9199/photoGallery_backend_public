package com.photogallery.photogallery.Entity;

import com.photogallery.photogallery.Request.PhotoGalleryRequest;

import lombok.Data;

@Data
public class Gallery {
	private String category;
	private PhotoGalleryRequest photoname[];
}
