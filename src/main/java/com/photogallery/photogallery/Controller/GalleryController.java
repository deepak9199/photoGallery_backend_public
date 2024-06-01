package com.photogallery.photogallery.Controller;

import static java.nio.file.Paths.get;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.photogallery.photogallery.Entity.Gallery;
import com.photogallery.photogallery.Entity.PhotoCategory;
import com.photogallery.photogallery.Entity.PhotoGallery;
import com.photogallery.photogallery.Repository.PhotoCategoryRepository;
import com.photogallery.photogallery.Repository.PhotoGalleryRepository;
import com.photogallery.photogallery.Request.PhotoGalleryRequest;
import com.photogallery.photogallery.Request.UserIDRequest;
import com.photogallery.photogallery.Response.ApiResponse;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class GalleryController {

	@Autowired
	private PhotoGalleryRepository photorepository;

	@Autowired
	private PhotoCategoryRepository PhotoCategoryRepository;

	@Value("${file.upload-dir}")
	String FILE_DIRECTORY;

	@PostMapping("gallery")
	public ApiResponse<?> getgallery(@Valid @RequestBody UserIDRequest useridreuest) {
		List<PhotoCategory> PhotoCategory = PhotoCategoryRepository.findByUserid(useridreuest.getUserid());
//		System.out.println(PhotoCategory);
		if (!PhotoCategory.isEmpty()) {
			int i = 0;
			List<Gallery> galleryList = new ArrayList<Gallery>();
			for (i = 0; i < PhotoCategory.size(); i++) {
				Gallery gallery = new Gallery();
				List<PhotoGalleryRequest> PhotoGalleryRequestArrayList = new ArrayList<PhotoGalleryRequest>();
				gallery.setCategory(PhotoCategory.get(i).getCategory());
				List<PhotoGallery> PhotoGallery = photorepository.findByCategoryid(PhotoCategory.get(i).getId());
//				System.out.println(PhotoGallery.size());
				if (!PhotoGallery.isEmpty()) {
					int j = 0;
					for (j = 0; j < PhotoGallery.size(); j++) {
						PhotoGalleryRequest PhotoGalleryRequest = new PhotoGalleryRequest();
						PhotoGalleryRequest.setCategoryid(PhotoGallery.get(j).getCategoryid());
						PhotoGalleryRequest.setName(PhotoGallery.get(j).getName());
						PhotoGalleryRequestArrayList.add(PhotoGalleryRequest);
					}
					gallery.setPhotoname(PhotoGalleryRequestArrayList
							.toArray(new PhotoGalleryRequest[PhotoGalleryRequestArrayList.size()]));

				} else {
					System.out.println("not photo found");
				}
				galleryList.add(gallery);
			}
			return ApiResponse.success(galleryList);

		} else {
			return ApiResponse.failure("No Photo Category Found");
		}
	}

	@GetMapping("gallery/{filename}")
	public ResponseEntity<Resource> download(@PathVariable("filename") String filename) throws IOException {
		Path filePath = get(FILE_DIRECTORY).toAbsolutePath().normalize().resolve(filename);
		if (!Files.exists(filePath)) {
			throw new FileNotFoundException(filename + " was not found on the server");
		}
		Resource resource = new UrlResource(filePath.toUri());
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("File-Name", filename);
		httpHeaders.add(CONTENT_DISPOSITION, "attachment;File-Name=" + resource.getFilename());
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
				.headers(httpHeaders).body(resource);
	}
}
