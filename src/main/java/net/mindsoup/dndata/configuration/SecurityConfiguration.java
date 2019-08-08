package net.mindsoup.dndata.configuration;

import net.mindsoup.dndata.providers.UIAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.stereotype.Component;

/**
 * Created by Valentijn on 8-8-2019
 */
@Component
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private UIAuthenticationProvider authProvider;

	@Autowired
	public SecurityConfiguration(UIAuthenticationProvider authenticationProvider) {
		authProvider = authenticationProvider;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.authorizeRequests()
				.antMatchers("/ui/**").authenticated().and()
				.formLogin()
				.loginPage("/ui/login.html").permitAll()
				.failureUrl("/ui/login.html?error")
				.usernameParameter("username")
				.passwordParameter("password")
				.defaultSuccessUrl("/ui/index");
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/static/**","/swagger-ui.html", "/data/**", "/user/**", "/publish/**", "/status/**", "/csrf");
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider);
	}
}
