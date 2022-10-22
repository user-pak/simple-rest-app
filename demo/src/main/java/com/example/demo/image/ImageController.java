package com.example.demo.image;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;

import com.example.demo.image.storageService.StorageService;
import com.example.demo.image.storageService.StorageServiceException;

@Controller
public class ImageController {
	
	private final StorageService service;
	private final ImageRepository repository;
		
	@Autowired
	public ImageController(StorageService service, ImageRepository repository) {
		this.service = service;
		this.repository = repository;
	}
	
	@GetMapping("/images")
	public ModelAndView findAllImages(ModelAndView mav) {
		
		List<Image> images = (List<Image>) repository.findAll();
		for(Image image : images) {
			image.setImageFilename(MvcUriComponentsBuilder.fromMethodName(ImageController.class, "serveFile", image.getImageFilename())
					.build().toUriString());
		}
		mav.addObject("images", images).setViewName("pictures");
		return mav;
	}
	
	@GetMapping("files/{filename}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
		
		Resource imageFile = service.loadAsResource(filename);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imageFile.getFilename() + "\"").body(imageFile);
	}
	
	@PostMapping("/images")
	public RedirectView saveImage(@RequestParam(value="imageTitle")String imageTitle, @RequestParam(value="file") MultipartFile file) {
		
		service.store(file);
		repository.save(new Image(imageTitle, file.getOriginalFilename()));
		return new RedirectView("images");
	}
	
}
