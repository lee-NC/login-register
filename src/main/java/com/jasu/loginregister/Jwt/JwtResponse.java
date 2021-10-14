package com.jasu.loginregister.Jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class JwtResponse {
	private String token;
	private String type = "Bearer";
	private Long userId;
	private String refreshToken;
	private String avatar;
	private int numActive;
	private String fullName;
	private Long coin;

	public JwtResponse(String accessToken,Long userId, String refreshToken, String fullName,int numActive, String avatar,Long coin) {
		this.token = accessToken;
		this.userId = userId;
		this.refreshToken = refreshToken;
		this.coin = coin;
		this.fullName = fullName;
		this.numActive = numActive;
		this.avatar = avatar;
	}

}
