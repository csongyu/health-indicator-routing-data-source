package xyz.csongyu.healthindicator;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DataSourceConfiguration {
    @Bean
    public DataSource dataSource() {
        final DriverManagerDataSource dataSourceA = new DriverManagerDataSource();
        dataSourceA.setDriverClassName("org.h2.Driver");
        dataSourceA.setUrl("jdbc:h2:mem:dba;DB_CLOSE_DELAY=-1");
        dataSourceA.setUsername("sa");
        dataSourceA.setPassword("sa");

        final DriverManagerDataSource dataSourceB = new DriverManagerDataSource();
        dataSourceB.setDriverClassName("org.h2.Driver");
        dataSourceB.setUrl("jdbc:h2:mem:dbb;DB_CLOSE_DELAY=-1");
        dataSourceB.setUsername("sa");
        dataSourceB.setPassword("sa");

        final Map<Object, Object> dataSources = new HashMap<>();
        dataSources.put(DataSourceEnum.DATA_SOURCE_A, dataSourceA);
        dataSources.put(DataSourceEnum.DATA_SOURCE_B, dataSourceB);

        final RoutingDataSource routingDataSource = new RoutingDataSource();
        routingDataSource.setTargetDataSources(dataSources);
        routingDataSource.setDefaultTargetDataSource(dataSources.get(DataSourceEnum.DATA_SOURCE_A));
        return routingDataSource;
    }
}
