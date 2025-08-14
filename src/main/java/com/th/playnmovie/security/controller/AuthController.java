package com.th.playnmovie.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.th.playnmovie.security.model.User;
import com.th.playnmovie.security.service.AuthService;
import com.th.playnmovie.security.vo.AccountCredentialsVo;
import com.th.playnmovie.security.vo.TokenVo;
import com.th.playnmovie.security.vo.UserVo;

import io.micrometer.common.util.StringUtils;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	@PostMapping("/signin")
	public ResponseEntity<?> signin(@RequestBody AccountCredentialsVo credentials){
		
		if(credentialsAreInvalid(credentials)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid client request");
		}
		ResponseEntity<TokenVo> vo = authService.signIn(credentials);
		
		return ResponseEntity.ok().body(vo);
	}
	@PutMapping("/refresh/{username}")
	public ResponseEntity<?> refreshToken(@PathVariable("username")String username, @RequestHeader("Authorization") String refreshToken){
		
		if(paramenterAreInvalid(username, refreshToken)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid client request");
		}
		ResponseEntity<TokenVo> vo = authService.refreshToken(username, refreshToken);
		
		return ResponseEntity.ok().body(vo);
	}
	@PostMapping("/register")
	public ResponseEntity<UserVo> registerUser(@RequestBody User user){
		
		ResponseEntity<UserVo> vo = authService.createUser(user);
		return vo;
	}
	

	private static boolean credentialsAreInvalid(AccountCredentialsVo credentials) {
		return (credentials == null || StringUtils.isBlank(credentials.getUsername()) || StringUtils.isBlank(credentials.getPassword()));
	}
	
	private static boolean paramenterAreInvalid(String username, String password) {
		return (StringUtils.isBlank(username) || StringUtils.isBlank(password));

	}
}
