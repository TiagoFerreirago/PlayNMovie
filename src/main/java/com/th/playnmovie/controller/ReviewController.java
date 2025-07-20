package com.th.playnmovie.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.th.playnmovie.dto.ReviewDto;
import com.th.playnmovie.model.TypeEnum;
import com.th.playnmovie.service.ReviewService;

@RestController
@RequestMapping("/review")
public class ReviewController {

	@Autowired
	private ReviewService reviewService;
	
	@GetMapping
	public ResponseEntity<List<ReviewDto>>findAllByItemAndType(@PathVariable Long itemId,@PathVariable TypeEnum type){
		List<ReviewDto> findAll = reviewService.findAllByItemAndType(itemId, type);
		return ResponseEntity.ok().body(findAll);
	}
	
	@PostMapping
	public ResponseEntity<ReviewDto> create(@RequestBody ReviewDto reviewDto){
		ReviewDto review = reviewService.create(reviewDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(review);
	}
	
	@PutMapping
	public ResponseEntity<ReviewDto> update(@RequestBody ReviewDto reviewDto){
		ReviewDto review = reviewService.update(reviewDto);
		return ResponseEntity.ok().body(review);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		reviewService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
