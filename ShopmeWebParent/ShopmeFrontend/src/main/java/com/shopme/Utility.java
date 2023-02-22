package com.shopme;

import com.shopme.security.oauth.CustomerOAuth2User;
import com.shopme.setting.EmailSettingUtils;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import java.util.Properties;

public class Utility {
	// return the url of the application , use in verify hyperlink url
	public static String getSiteURL(HttpServletRequest request) {
		String siteURL = request.getRequestURL().toString();
		
		return siteURL.replace(request.getServletPath(), "");
	}
	
	public static JavaMailSenderImpl prepareMailSender(EmailSettingUtils settings) {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		
		mailSender.setHost(settings.getHost());
		mailSender.setPort(settings.getPort());
		mailSender.setUsername(settings.getUsername());
		mailSender.setPassword(settings.getPassword());
		
		Properties mailProperties = new Properties();
		mailProperties.setProperty("mail.smtp.auth", settings.getSmtpAuth());
		mailProperties.setProperty("mail.smtp.starttls.enable", settings.getSmtpSecured());
		
		mailSender.setJavaMailProperties(mailProperties);
		
		return mailSender;
	}

	public static String getEmailOfAuthenticatedCustomer(HttpServletRequest request) {
		Object principal = request.getUserPrincipal(); // get the principal object , can be UsernamePasswordAuthenticationToken
// when login with email and password or Oauth2AuthenticationToken when login with google account
		if(principal == null) // if customer has not login
			return	null;
		String customerEmail = null;

		if (principal instanceof UsernamePasswordAuthenticationToken
				|| principal instanceof RememberMeAuthenticationToken) {
			customerEmail = request.getUserPrincipal().getName();
		} else if (principal instanceof OAuth2AuthenticationToken oauth2Token) {
			CustomerOAuth2User oauth2User = (CustomerOAuth2User) oauth2Token.getPrincipal();
			customerEmail = oauth2User.getEmail();
		}

		return customerEmail;
	}
}
