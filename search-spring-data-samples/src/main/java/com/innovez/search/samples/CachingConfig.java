package com.innovez.search.samples;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * Search caching configuration.
 * 
 * @author zakyalvan
 */
@Configuration
@EnableCaching(mode=AdviceMode.ASPECTJ)
public class CachingConfig implements CachingConfigurer {
	@Override
	public CacheManager cacheManager() {
		EhCacheCacheManager cacheManager = new EhCacheCacheManager(ehCacheManagerFactoryBean().getObject());
		return cacheManager;
	}
	
	@Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
        EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        cacheManagerFactoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
        cacheManagerFactoryBean.setShared(true);
        return cacheManagerFactoryBean;
    }

	@Override
	public KeyGenerator keyGenerator() {
		SimpleKeyGenerator keyGenerator = new SimpleKeyGenerator();
		return keyGenerator;
	}

	@Override
	public CacheResolver cacheResolver() {
		return null;
	}

	@Override
	public CacheErrorHandler errorHandler() {
		return null;
	}
}