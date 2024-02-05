package sellyourunhappiness.api.config.bean;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import sellyourunhappiness.api.config.filter.JwtTokenFilter;

@Configuration
public class JwtFilterConfiguration {
	@Bean
	public FilterRegistrationBean<JwtTokenFilter> jwtTokenFilterRegistrationBean(JwtTokenFilter jwtTokenFilter) {
		FilterRegistrationBean<JwtTokenFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(jwtTokenFilter);
		registrationBean.addUrlPatterns("/api/*");
		return registrationBean;
	}
}
