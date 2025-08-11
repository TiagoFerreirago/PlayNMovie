package com.th.playnmovie.mapper;

import com.th.playnmovie.security.vo.UserVo;
import com.th.playnmovie.security.model.User;

public class UserMapper {

	
	public static UserVo tokenResponseVo(User user) {
		
		UserVo userDto = new UserVo();
		userDto.setEnabled(user.getEnabled());
		userDto.setFullName(user.getFullName());
		userDto.setId(user.getId());
		userDto.setUsername(user.getUsername());
		
		return userDto;
	}
	
	public static User tokenResponse(UserVo userVo) {
		
		User user = new User();
		user.setEnabled(userVo.getEnabled());
		user.setFullName(userVo.getFullName());
		user.setId(userVo.getId());
		user.setUsername(userVo.getUsername());
		
		return user;
	}
}
