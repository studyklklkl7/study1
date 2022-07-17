package board.configuration;

import java.nio.charset.Charset;

import javax.servlet.Filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import board.board.interceptor.LoggerInterceptor;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer{
	
	@Bean
	public CommonsMultipartResolver multipartResolver(){
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
		commonsMultipartResolver.setDefaultEncoding("UTF-8");
		commonsMultipartResolver.setMaxUploadSizePerFile(5 * 1024 * 1024); //5 * 1024 * 1024 (5mb)
		return commonsMultipartResolver;
	}
	
	
	@Override
	public void addInterceptors(InterceptorRegistry registry){
		registry.addInterceptor(new LoggerInterceptor());
		
		/*addPathPatterns(), excludePathPatterns() 메서드로 요청 주소의 패턴과 제외할 요청 주소의 패턴 지정 가능, */
	}
	
	/*
	 * @Bean public Filter characterEncodingFilter() { CharacterEncodingFilter
	 * characterEncodingFilter = new CharacterEncodingFilter();
	 * characterEncodingFilter.setEncoding("UTF-8");
	 * characterEncodingFilter.setForceEncoding(true);
	 * 
	 * return characterEncodingFilter;
	 * 
	 * }
	 * 
	 * @Bean public HttpMessageConverter<String> responseBodyConverter(){ return new
	 * StringHttpMessageConverter(Charset.forName("UTF-8")); }
	 */
}
