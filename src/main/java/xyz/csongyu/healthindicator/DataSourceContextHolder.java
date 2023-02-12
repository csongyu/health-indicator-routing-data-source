package xyz.csongyu.healthindicator;

public class DataSourceContextHolder {
    private static final ThreadLocal<DataSourceEnum> threadLocal = new ThreadLocal<>();

    public static DataSourceEnum getDataSourceContext() {
        return threadLocal.get();
    }

    public static void setDataSourceContext(final DataSourceEnum dataSourceEnum) {
        threadLocal.set(dataSourceEnum);
    }

    public static void clearDataSourceContext() {
        threadLocal.remove();
    }
}
