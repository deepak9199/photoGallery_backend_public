package com.photogallery.photogallery.Controller;

import static java.nio.file.Paths.get;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.photogallery.photogallery.Entity.PhotoCategory;
import com.photogallery.photogallery.Entity.PhotoGallery;
import com.photogallery.photogallery.Repository.PhotoCategoryRepository;
import com.photogallery.photogallery.Repository.PhotoGalleryRepository;
import com.photogallery.photogallery.Request.PhotoCategoryRequest;
import com.photogallery.photogallery.Response.ApiResponse;
import com.photogallery.photogallery.Security.Services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class PhotoCategoryController {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PhotoCategoryRepository PhotoCategoryRepository;

	@Autowired
	private PhotoGalleryRepository photorepository;

	@Value("${file.upload-dir}")
	String FILE_DIRECTORY;

	@PostMapping("/PhotoCategory")
	@PreAuthorize("hasRole('ADMIN')")
	public ApiResponse<?> create(@Valid @RequestBody PhotoCategoryRequest PhotoCategoryRequest) {
		UserDetailsImpl authuserDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		PhotoCategory PhotoCategory = modelMapper.map(PhotoCategoryRequest, PhotoCategory.class);
		PhotoCategory.setUserid(authuserDetails.getId());
		System.out.println(PhotoCategory);
		return ApiResponse.success(PhotoCategoryRepository.save(PhotoCategory));
	}

	@PutMapping("/PhotoCategory/{id}")
	public ApiResponse<?> update(@PathVariable Long id, @Valid @RequestBody PhotoCategoryRequest Itemrequest) {
		UserDetailsImpl authuserDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		PhotoCategory existingItem = PhotoCategoryRepository.findById(id).get();
		PhotoCategory Item = modelMapper.map(Itemrequest, PhotoCategory.class);
		Item.setUserid(authuserDetails.getId());
		Item.setId(existingItem.getId());
		return ApiResponse.success(PhotoCategoryRepository.save(Item));
	}

	@GetMapping("/PhotoCategory")
	@PreAuthorize("hasRole('ADMIN')")
	public ApiResponse<?> listAll() {
		UserDetailsImpl authuserDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		List<PhotoCategory> PhotoCategory = PhotoCategoryRepository.findByUserid(authuserDetails.getId());
		return ApiResponse.success(PhotoCategory);
	}

	@DeleteMapping("/PhotoCategory/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ApiResponse<?> remove(@PathVariable Long id) {
		List<PhotoGallery> photoList = photorepository.findByCategoryid(id);
		if (!photoList.isEmpty()) {
			PhotoCategoryRepository.deleteById(id);
			for (int i = 0; i < photoList.size(); i++) {
				Optional<PhotoGallery> photo = photorepository.findById(photoList.get(i).getId());
				String filename = photo.get().getName();
				Path filePath = get(FILE_DIRECTORY).toAbsolutePath().normalize().resolve(filename);
				try {
					if (Files.exists(filePath)) {
						File myFile = new File(FILE_DIRECTORY + filename);
						boolean success = myFile.delete();
						if (success) {
							photorepository.deleteById(id);
						}
					}
				} catch (Exception e) {
					System.out.println(e);
				}
			}
			return ApiResponse.success();

		} else {
			PhotoCategoryRepository.deleteById(id);
			return ApiResponse.success();
		}

	}

}
