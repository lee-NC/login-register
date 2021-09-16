package com.jasu.loginregister.Jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class JwtResponse {
	private String token;
	private String type = "Bearer";
	private String refreshToken;
	private Long id;
	private String avatar;
	private int numActive;
	private String fullName;

	public JwtResponse(String accessToken, String refreshToken, Long id, String fullName,int numActive, String avatar) {
		this.token = accessToken;
		this.refreshToken = refreshToken;
		this.id = id;
		this.fullName = fullName;
		this.numActive = numActive;
		this.avatar = avatar;
	}

}
