package com.jasu.loginregister.BruteForce;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.jasu.loginregister.Entity.DefinitionEntity.DEStateMessage.MAX_FAILED_LOGIN;

@Service
public class LoginAttemptService {
	
	private LoadingCache<String, Integer> attemptsCache;

	public LoginAttemptService() {
		attemptsCache = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.MINUTES)
				.build(new CacheLoader<String, Integer>() {
					public Integer load(String key) {
						return 0;
					}
				});
	}

	public void loginSucceeded(String key) {
		attemptsCache.invalidate(key);
	}

	public void loginFailed(String key) {
		int attempts;
		try {
			attempts = attemptsCache.get(key);
		} catch (ExecutionException e) {
			attempts = 0;
		}
		attempts++;
		attemptsCache.put(key, attempts);
	}

	public boolean isBlocked(String key) {
		try {
			return attemptsCache.get(key) >= MAX_FAILED_LOGIN;
		} catch (ExecutionException e) {
			return false;
		}
	}
}