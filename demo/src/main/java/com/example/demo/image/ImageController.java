package com.example.demo.image;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;

import com.example.demo.image.storageService.StorageServiceException;

@Controller
public class ImageController {
	
	private final ImageDBRepository repository;
		
	@Autowired
	public ImageController(ImageDBRepository repository) {
		this.repository = repository;
	}
	
	@GetMapping("/images")
	public ModelAndView findAllImages(ModelAndView mav) {
		
		List<Image> images = (List<Image>) repository.findAll();
		for(Image image : images) {
			image.setImageFileURL(MvcUriComponentsBuilder.fromMethodName(ImageController.class, "serveFile", image.getId())
					.build().toUriString());
		}
		mav.addObject("images", images).setViewName("pictures");
		return mav;
	}
	
	@GetMapping(value="files/{id}",produces = MediaType.IMAGE_JPEG_VALUE)
	@ResponseBody
	public ResponseEntity<ByteArrayResource> serveFile(@PathVariable Long id) {
		
		Image image = repository.findById(id).orElseThrow(() -> new StorageServiceException("파일을 찾을 수 없습니다"));
		String filename = "";
		try {
			filename = URLEncoder.encode(image.getImageFilename(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			new StorageServiceException("파일명을 불러 올 수 없습니다");
		}
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"").body(new ByteArrayResource(image.getImageFile()));
	}
	
	@PostMapping("/images")
	public RedirectView saveImage(@RequestParam(value="file") MultipartFile imageFile, @RequestParam(value="imageTitle")String imageTitle) {
		
		try {
			repository.save(new Image(imageTitle, imageFile.getOriginalFilename(), imageFile.getBytes()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			new StorageServiceException("파일을 저장 하지 못했습니다");
		}
		return new RedirectView("images");
	}
	
	@GetMapping("/images/{id}")
	public RedirectView deleteImage(@PathVariable Long id) {
		try {
			repository.deleteById(id);
		} catch(IllegalArgumentException e) {		
			new StorageServiceException("파일을 찾을 수 없습니다");
		}
		return new RedirectView("images");
	}
	
}
