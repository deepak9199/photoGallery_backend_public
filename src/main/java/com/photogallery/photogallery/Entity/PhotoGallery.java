package com.photogallery.photogallery.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "photogallery")
@Data
@NoArgsConstructor
public class PhotoGallery {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "userid must not be Null")
	@Min(value = 1, message = "userid must not be 0")
	private Long userid;

	@NotNull(message = "categoryid must not be Null")
	@Min(value = 1, message = "categoryid must not be 0")
	private Long categoryid;

	@NotBlank(message = "category must not be empty")
	@Size(max = 500)
	private String name;

}
