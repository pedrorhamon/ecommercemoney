package com.starking.money.config;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import lombok.var;


@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UserDetailsService userDetailsService;	
     
    @Override
	public void configure(HttpSecurity http) throws Exception {
	    
	    http.authorizeRequests()
	            .antMatchers("/categorias").permitAll()
	            .anyRequest().authenticated()
	        .and()
	            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	        .and()
	            .csrf().disable()
	            .oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtAuthenticationConverter());
	}

	@Bean
	public JwtDecoder jwtDecoder() {
	    var secretKey = new SecretKeySpec("3032885ba9cd6621bcc4e7d6b6c35c2b".getBytes(), "HmacSHA256");
	
	    return NimbusJwtDecoder.withSecretKey(secretKey).build();
	}

	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {        
	    return super.authenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}

	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
			List<String> authorities = jwt.getClaimAsStringList("authorities");
	
			if (authorities == null) {
				authorities = Collections.emptyList();
			}
	
			JwtGrantedAuthoritiesConverter scopesAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
			Collection<GrantedAuthority> grantedAuthorities = scopesAuthoritiesConverter.convert(jwt);
	
			grantedAuthorities.addAll(authorities.stream()
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList()));
	
			return grantedAuthorities;
		});
	
		return jwtAuthenticationConverter;
	}
}