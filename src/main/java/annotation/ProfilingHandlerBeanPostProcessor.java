package annotation;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class ProfilingHandlerBeanPostProcessor implements BeanPostProcessor {

    private ProfilingController profilingController = new ProfilingController();

    /**
     * собираем список бинов для которых нужно будет сделать обработку
     * на этакпе postProcessBeforeInitialization поскольку ...
     * а на этапе postProcessAfterInitialization уже обрабатывать
     */
    private Map<String, Class> classMap = new HashMap<>();

    public ProfilingHandlerBeanPostProcessor() throws Exception {
        // стандартная джаа инсрумент не относящийся к спрингу
        MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
        platformMBeanServer.registerMBean(profilingController, new ObjectName("profiling", "name", "profiling controller"));
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();

        if (beanClass.isAnnotationPresent(Profiling.class)) {
            classMap.put(beanName, beanClass);
        }

        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class beanClass = classMap.get(beanName);

        if (beanClass != null) {
            return Proxy.newProxyInstance(
                    beanClass.getClassLoader(),
                    beanClass.getInterfaces(),
                    new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            if (profilingController.isEnabled()) {
                                System.out.println("Profiling start");

                                long before = System.nanoTime();
                                Object retVal = method.invoke(bean, args);
                                long after = System.nanoTime();

                                System.out.println(after - before);
                                System.out.println("Profiling stop");

                                return retVal;
                            } else {
                                return method.invoke(bean, args);
                            }
                        }
                    }
            );
        }

        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
