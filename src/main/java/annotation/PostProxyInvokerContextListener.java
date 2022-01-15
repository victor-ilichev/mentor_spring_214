package annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

public class PostProxyInvokerContextListener implements ApplicationListener<ContextRefreshedEvent> {
    // инжект фактори это вроде бы пздц
    // но тут это нормально посколькув этом случае это уместно
    // псскольку это аппликаион листенер спринга
    @Autowired
    private ConfigurableListableBeanFactory factory;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        String[] names = applicationContext.getBeanDefinitionNames();

        // мы не можем просто найти бин по имени или по классу потому-что
        // на этом этапе мы можем получит ьпрокси объект
        // но объект может быть и lazy что тоже не правильно
        for (String name : names) {
            BeanDefinition beanDefinition = factory.getBeanDefinition(name);
            String originalClassName = beanDefinition.getBeanClassName();

            try {
                Class<?> originalClass = Class.forName(originalClassName);
                for (Method method : originalClass.getMethods()) {
                    if (method.isAnnotationPresent(PostProxy.class)) {
                        Object bean = applicationContext.getBean(name);

                        // .getClass() = $Proxy7
                        Method currentMethod = bean.getClass().getMethod(method.getName(), method.getParameterTypes());
                        currentMethod.invoke(bean);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
