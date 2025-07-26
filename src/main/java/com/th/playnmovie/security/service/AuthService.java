package com.th.playnmovie.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.th.playnmovie.security.jwt.JwtTokenProvider;
import com.th.playnmovie.security.repository.UserRepository;
import com.th.playnmovie.security.vo.AccountCredentialsVo;
import com.th.playnmovie.security.vo.TokenVo;

@Service
public class AuthService {

	
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtTokenProvider tokenProvider;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public ResponseEntity<TokenVo> signIn(AccountCredentialsVo credentials) {
	    try {
	        System.out.println("Attempting to authenticate user: " + credentials.getUsername());
	        var username = credentials.getUsername();
	        var password = credentials.getPassword();

	        // Buscar o usuário manualmente para debugar a senha
	        var user = userRepository.findByUsername(username);

	        if (user != null) {
	            System.out.println("Senha codificada no banco: " + user.getPassword());
	            System.out.println("Senha raw digitada: " + password);
	            System.out.println("Senha bate? " + passwordEncoder.matches(password, user.getPassword()));
	        } else {
	            System.out.println("Usuário não encontrado antes da autenticação.");
	        }

	        // Tentar autenticar
	        authenticationManager.authenticate(
	            new UsernamePasswordAuthenticationToken(username, password));

	        // Gerar token se autenticação foi bem-sucedida
	        if (user != null) {
	            System.out.println("User authenticated, creating token...");
	            var tokenResponse = tokenProvider.createAccessToken(username, user.getRoles());
	            return ResponseEntity.ok(tokenResponse);
	        } else {
	            throw new UsernameNotFoundException("Username " + username + " not found!");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new BadCredentialsException("Invalid username/password supplied!");
	    }
	}
	
	public ResponseEntity<TokenVo> refreshToken(String username, String refreshToken) {
		var user = userRepository.findByUsername(username);
		
		var tokenResponse = new TokenVo();
		if (user != null) {
			tokenResponse = tokenProvider.refreshToken(refreshToken);
		} else {
			throw new UsernameNotFoundException("Username " + username + " not found!");
		}
		return ResponseEntity.ok(tokenResponse);
	}
}
