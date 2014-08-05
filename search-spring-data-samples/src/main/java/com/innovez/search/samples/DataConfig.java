package com.innovez.search.samples;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories
@EnableTransactionManagement
public class DataConfig {
	@Bean(name="dataSource")
	public EmbeddedDatabase dataSource() {
		return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.HSQL).build();
	}
	
	@Bean(name="entityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		
		entityManagerFactoryBean.setDataSource(dataSource());
		
		HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
		jpaVendorAdapter.setDatabase(Database.HSQL);
		jpaVendorAdapter.setShowSql(true);
		
		entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
		
		return entityManagerFactoryBean;
	}
	
	@Bean(name="transactionManager")
	public JpaTransactionManager transactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
		return transactionManager;
	}
	
	@Bean
	public ResourceDatabasePopulator databasePopulator() {
		ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
		databasePopulator.addScript(new ClassPathResource("/META-INF/spring/currency.dml.sql"));
		databasePopulator.setContinueOnError(false);
		return databasePopulator;
	}
}
