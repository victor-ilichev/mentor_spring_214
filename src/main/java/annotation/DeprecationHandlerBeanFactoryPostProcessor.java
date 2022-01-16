package annotation;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * листенер работает на этапе когда все уже создано
 * все бины и все прокси и все инит методы отработали
 *
 * а этот работает на этапе когда есть тольео бин дефинишены
 */
public class DeprecationHandlerBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        for (String definitionName : configurableListableBeanFactory.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = configurableListableBeanFactory.getBeanDefinition(definitionName);
            String beanClassName = beanDefinition.getBeanClassName();
            try {
                Class<?> beanClass = Class.forName(beanClassName);
                DeprecatedClass annotation = beanClass.getAnnotation(DeprecatedClass.class);

                if (annotation != null) {
                    beanDefinition.setBeanClassName(annotation.newImpl().getName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
