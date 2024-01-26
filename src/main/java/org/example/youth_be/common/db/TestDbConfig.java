package org.example.youth_be.common.db;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Profile("test")
@Configuration
public class TestDbConfig {

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:h2:~/youth_test");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }
}