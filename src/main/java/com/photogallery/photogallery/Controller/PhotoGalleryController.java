package com.photogallery.photogallery.Controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.photogallery.photogallery.Entity.PhotoGallery;
import com.photogallery.photogallery.Repository.PhotoGalleryRepository;
import com.photogallery.photogallery.Request.PhotoGalleryRequest;
import com.photogallery.photogallery.Response.ApiResponse;
import com.photogallery.photogallery.Security.Services.UserDetailsImpl;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static java.nio.file.Paths.get;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class PhotoGalleryController {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PhotoGalleryRepository photorepository;

	@Value("${file.upload-dir}")
	String FILE_DIRECTORY;

	@PostMapping("/photo")
	@PreAuthorize("hasRole('ADMIN')")
	public ApiResponse<?> create(@RequestParam("file") MultipartFile file,
			@RequestParam("categoryid") Long categoryid) {
		PhotoGalleryRequest photorequest = new PhotoGalleryRequest();
		UserDetailsImpl authuserDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		try {
			if (!file.isEmpty()) {
				File myFile = new File(FILE_DIRECTORY + file.getOriginalFilename());
				if (file.getContentType().equals("image/jpeg")) {
					photorequest.setName(file.getOriginalFilename());
					PhotoGallery photo = modelMapper.map(photorequest, PhotoGallery.class);
					photo.setUserid(authuserDetails.getId());
					photo.setCategoryid(categoryid);
					myFile.createNewFile();
					FileOutputStream fos = new FileOutputStream(myFile);
					fos.write(file.getBytes());
					fos.close();
					return ApiResponse.success(photorepository.save(photo));
				} else {
					return ApiResponse.failure("only image/jpeg file supported");
				}

			} else {
				return ApiResponse.failure("File is Empty");
			}

		} catch (Exception e) {
			System.out.println(e);
		}

		return ApiResponse.failure("enternal Server Error Please try again");
	}

	@DeleteMapping("/photo/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ApiResponse<?> remove(@PathVariable Long id) {
		Optional<PhotoGallery> photo = photorepository.findById(id);
		String filename = photo.get().getName();
		Path filePath = get(FILE_DIRECTORY).toAbsolutePath().normalize().resolve(filename);
		try {
			if (Files.exists(filePath)) {
				File myFile = new File(FILE_DIRECTORY + filename);
				boolean success = myFile.delete();
				if (success) {
					photorepository.deleteById(id);
					return ApiResponse.success();
				}
			} else {
				photorepository.deleteById(id);
				return ApiResponse.success();
			}

		} catch (Exception e) {
			System.out.println(e);
		}

		return ApiResponse.failure("Internal server error");
	}

	@GetMapping("/photo")
	@PreAuthorize("hasRole('ADMIN')")
	public ApiResponse<?> listAll() {
		UserDetailsImpl authuserDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		List<PhotoGallery> photo = photorepository.findByUserid(authuserDetails.getId());
		return ApiResponse.success(photo);
	}

	@GetMapping("photo/{filename}")
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

	@GetMapping("/photoById/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ApiResponse<?> listbyid(@PathVariable Long id) {
		List<PhotoGallery> photo = photorepository.findByCategoryid(id);
		if (!photo.isEmpty()) {
			return ApiResponse.success(photo);
		}
		return ApiResponse.failure("Data not found");
	}
}
