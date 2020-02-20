package br.com.url.urlshortener.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.JpaVendorAdapter
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.util.*
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("br.com.url.repository")
@PropertySource("classpath:database.properties")
public class JPAConfig {

    @Autowired
    private val env:Environment? = null

    @Bean(name = ["entityManagerFactory"])
    fun getEntityManagerFactoryBean(): LocalContainerEntityManagerFactoryBean {
        var lcemfb: LocalContainerEntityManagerFactoryBean = LocalContainerEntityManagerFactoryBean();
        lcemfb.setJpaVendorAdapter(getJpaVendorAdapter());
        lcemfb.setDataSource(getDataSource());  
        lcemfb.setPersistenceUnitName("myJpaPersistenceUnit");
        lcemfb.setPackagesToScan("br.com.fiap.entity");
        lcemfb.setJpaProperties(jpaProperties());
        return lcemfb;
    }

    @Bean
    fun getJpaVendorAdapter(): JpaVendorAdapter {
        var adapter: JpaVendorAdapter = HibernateJpaVendorAdapter();
        return adapter;
    }

    @Bean
    fun getDataSource(): DataSource {
        return DataSourceBuilder.create()
                .driverClassName(env?.getProperty("database.driverClassName"))
                .url(env?.getProperty("database.url"))
                .username(env?.getProperty("database.username"))
                .password(env?.getProperty("database.password"))
                .build();
    }

    @Bean(name = ["transactionManager"])
    fun txManager(): JpaTransactionManager? {
        var jpaTransactionManager: JpaTransactionManager? = getEntityManagerFactoryBean()?.getObject()?.let { JpaTransactionManager(it) };
        return jpaTransactionManager;
    }

    fun jpaProperties(): Properties {
        var properties: Properties = Properties();
        properties.put("hibernate.dialect", env?.getProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", env?.getProperty("hibernate.show_sql"));
        properties.put("hibernate.format_sql", env?.getProperty("hibernate.format_sql"));
        properties.put("hibernate.hbm2ddl.auto", env?.getProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.id.new_generator_mappings", env?.getProperty("hibernate.id.new_generator_mappings"));
        return properties;
    }
}