package xyz.csongyu.healthindicator;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

@Component
public class RoutingDataSourceHealthIndicator implements HealthIndicator {
    private final DataSource dataSource;

    private Map<Object, JdbcTemplate> jdbcTemplates;

    public RoutingDataSourceHealthIndicator(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    @SuppressWarnings("unchecked")
    public void init() throws NoSuchFieldException, IllegalAccessException {
        final Field field = AbstractRoutingDataSource.class.getDeclaredField("resolvedDataSources");
        field.setAccessible(true);
        final Map<Object, DataSource> resolvedDataSources = (Map<Object, DataSource>)field.get(this.dataSource);
        this.jdbcTemplates = resolvedDataSources.entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, entry -> new JdbcTemplate(entry.getValue())));
    }

    @Override
    public Health health() {
        final List<Health> results = this.jdbcTemplates.entrySet().stream().map(entry -> {
            final JdbcTemplate jdbcTemplate = entry.getValue();
            try {
                jdbcTemplate.queryForObject("SELECT 1 FROM DUAL", String.class);
                return Health.up().withDetail("dataSource", entry.getKey()).build();
            } catch (final DataAccessException e) {
                return Health.down().withDetail("dataSource", entry.getKey()).withException(e).build();
            }
        }).collect(Collectors.toList());

        final Map<String, String> details =
            results.stream().collect(Collectors.toMap(health -> String.valueOf(health.getDetails().get("dataSource")),
                health -> health.getStatus().getCode()));
        if (results.stream().anyMatch(health -> Status.DOWN.equals(health.getStatus()))) {
            return Health.down().withDetails(details).build();
        } else {
            return Health.up().withDetails(details).build();
        }
    }
}
