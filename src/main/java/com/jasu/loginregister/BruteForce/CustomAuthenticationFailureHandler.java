package com.jasu.loginregister.BruteForce;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private LoginAttemptService loginAttemptService;

	@Autowired
	private MessageSource messages;

	@Autowired
	private LocaleResolver localeResolver;

	@Override
	public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException exception) throws IOException, ServletException {
		setDefaultFailureUrl("/login?error=true");

		super.onAuthenticationFailure(request, response, exception);

		final Locale locale = localeResolver.resolveLocale(request);

		String errorMessage = messages.getMessage("message.badCredentials", null, locale);

		if (exception.getMessage()
				.equalsIgnoreCase("blocked")) {
			errorMessage = messages.getMessage("auth.message.blocked", null, locale);
		}

		request.getSession()
				.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, errorMessage);
	}
	
}
