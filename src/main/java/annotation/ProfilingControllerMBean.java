package annotation;

/**
 * указываем методы которы мы хотим чтобы были доступны в jmx консуле
 */
public interface ProfilingControllerMBean {
    void setEnabled(boolean enabled);
}
