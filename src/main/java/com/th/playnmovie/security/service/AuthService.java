package com.th.playnmovie.security.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.th.playnmovie.mapper.UserMapper;
import com.th.playnmovie.security.jwt.JwtTokenProvider;
import com.th.playnmovie.security.model.Permission;
import com.th.playnmovie.security.model.User;
import com.th.playnmovie.security.repository.PermissionRepository;
import com.th.playnmovie.security.repository.UserRepository;
import com.th.playnmovie.security.vo.AccountCredentialsVo;
import com.th.playnmovie.security.vo.TokenVo;
import com.th.playnmovie.security.vo.UserVo;

@Service
public class AuthService {

	private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
	
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtTokenProvider tokenProvider;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private PermissionRepository permissionRepository;
	
	public ResponseEntity<TokenVo> signIn(AccountCredentialsVo credentials) {
    try {
        logger.debug("Attempting to authenticate user: {}", credentials.getUsername());
        var username = credentials.getUsername();
        var password = credentials.getPassword();

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password));

        var user = userRepository.findByUsername(username);
        if (user == null) {
            logger.warn("Username {} not found after authentication.", username);
            throw new UsernameNotFoundException("Username " + username + " not found!");
        }

        var tokenResponse = tokenProvider.createAccessToken(username, user.getRoles());
        logger.info("User {} authenticated successfully.", username);
        return ResponseEntity.ok(tokenResponse);

    } catch (BadCredentialsException e) {
        logger.error("Invalid username/password supplied for user: {}", credentials.getUsername());
        throw e;
    } catch (Exception e) {
        logger.error("Authentication error for user {}: {}", credentials.getUsername(), e.getMessage());
        throw new BadCredentialsException("Invalid username/password supplied!");
    }
}
	
	public ResponseEntity<TokenVo> refreshToken(String username, String refreshToken) {
		logger.debug("Refreshing token for user: {}", username);

		var user = userRepository.findByUsername(username);
		
		if (user == null) {
			logger.warn("Refresh token failed: user '{}' not found.", username);
			throw new UsernameNotFoundException("Username " + username + " not found!");
		}

		var tokenResponse = tokenProvider.refreshToken(refreshToken);
		logger.info("Token refreshed successfully for user '{}'", username);

		return ResponseEntity.ok(tokenResponse);
    }
	
	public ResponseEntity<UserVo> createUser(User user){
		
		logger.debug("Creating new user: {}", user.getUsername());

		User mainUser = new User();
		
		mainUser.setAccountNonExpired(true);
		mainUser.setAccountNonLocked(true);
		mainUser.setCredentialsNonExpired(true);
		mainUser.setEnabled(true);
		mainUser.setPassword(passwordEncoder.encode(user.getPassword()));
		mainUser.setUsername(user.getUsername());
		mainUser.setFullName(user.getFullName());
		
		Permission permission = permissionRepository.findByDescription("COMMON_USER");
		mainUser.setPermission(List.of(permission));
		
		User userPersistence = userRepository.save(mainUser);
		logger.info("User '{}' created successfully.", userPersistence.getUsername());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.tokenResponseVo(userPersistence));
	}
}