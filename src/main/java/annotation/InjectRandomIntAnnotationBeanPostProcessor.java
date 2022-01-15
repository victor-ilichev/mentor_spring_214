package annotation;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Random;

public class InjectRandomIntAnnotationBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            InjectRandomInt annotation = field.getAnnotation(InjectRandomInt.class);

            // значит аннотация над полем стояла
            if (annotation != null) {
                int min = annotation.min();
                int max = annotation.max();
                Random random = new Random();
                int result = min + random.nextInt(max - min);

                // поскольку поле скорее всего в private
                field.setAccessible(true);

                // можно сделать так, но придется обрабатывать exception
                // field.set(result);
                // и кидать exception мы тоже не можем по контракту BeanPostProcessor

                ReflectionUtils.setField(field, bean, result);
                // теперь прописываем в context.xml
                // чтобы о нашемпост процессоре узнал спринг
                // указываем его как обычный бин
                // ид можно неуказыват ьтак как пользоваться мы этим бином явно не станем
            }
        }
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
