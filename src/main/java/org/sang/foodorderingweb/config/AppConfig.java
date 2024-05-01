package org.sang.foodorderingweb.config;

import jakarta.servlet.http.HttpServletRequest;
import java.security.Security;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class AppConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers("/api/admin/**").hasAnyRole("RESTAURANT_OWNER", "ADMIN")
						.requestMatchers("/api/**").authenticated()
						.anyRequest().permitAll()
				).addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)
				.csrf(AbstractHttpConfigurer::disable)
				.cors(cors -> cors.configurationSource(corsConfigurationSource()));
		return http.build();
	}

	private CorsConfigurationSource corsConfigurationSource() {
		return new CorsConfigurationSource() {
			@Override
			public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
				CorsConfiguration cfg = new CorsConfiguration();
				cfg.setAllowedOrigins(List.of(
						"http://localhost:3000"
				));// Xác định danh sách các nguồn (origins) mà các yêu cầu CORS được chấp nhận
				cfg.setAllowedMethods(Collections.singletonList(
						"*"));//Xác định danh sách các phương thức HTTP được phép truy cập từ các nguồn khác nhau
				cfg.setAllowCredentials(
						true);//Xác định xem có cho phép gửi thông tin xác thực (credentials) như cookies, token từ client đến server không
				cfg.setAllowedHeaders(
						Collections.singletonList("*"));//Xác định danh sách các headers được phép trong yêu cầu CORS
				cfg.setExposedHeaders(
						List.of("Authorization"));//Xác định danh sách các headers mà client được phép truy cập sau khi yêu cầu CORS
				cfg.setMaxAge(
						3600L);//Xác định thời gian tối đa mà các cấu hình CORS được lưu trữ trong bộ nhớ cache của trình duyệt
				return cfg;
			}
		};
	}
	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
}
