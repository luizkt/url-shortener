package br.com.url.urlshortener.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisPassword
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory


@Configuration
@EnableCaching
@PropertySource("classpath:redis.properties")
class RedisConfig {
    @Autowired
    private val env: Environment? = null

    @Bean
    fun redisConnectionFactory(): LettuceConnectionFactory {
        val redisConf = RedisStandaloneConfiguration()

            redisConf.hostName = env?.getProperty("redis.host").toString()
            redisConf.port = env?.getProperty("redis.port").toString().toInt()
            redisConf.password = RedisPassword.of(env?.getProperty("redis.password"))

        return LettuceConnectionFactory(redisConf)
    }

    @Bean
    fun cacheManager(): RedisCacheManager {
        val rcm = RedisCacheManager.create(redisConnectionFactory())
        rcm.isTransactionAware = true
        return rcm
    }
}