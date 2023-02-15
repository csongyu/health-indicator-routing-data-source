package xyz.csongyu.healthindicator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, JdbcTemplateAutoConfiguration.class})
public class HealthIndicatorRoutingDataSourceApplication {
    public static void main(final String[] args) {
        SpringApplication.run(HealthIndicatorRoutingDataSourceApplication.class, args);
    }
}
