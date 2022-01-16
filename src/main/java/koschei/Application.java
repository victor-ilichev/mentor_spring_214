package koschei;

import koschei.config.AppConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import quoter.Quoter;
import quoter.TerminatorQuoter;

public class Application {
    public static void main(String[] args) throws InterruptedException {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("context.xml");
//        while (true) {
//            Thread.sleep(1000);
            // не станет теперь работать поскольку здесь теперь прокси
            // applicationContext.getBean(TerminatorQuoter.class).sayQuote();
            // делаем точку останова
            // правой кнопкой на строке остановки -> Evaluate Expression
            // запускаем и видим что по нашему запросу ничего нет, там нул
            // так же можно запустить applicationContext.getBeanDefinitionNames()
            // и увидим список зарегистрированных бинов
            // вызываем эту команду и видим оригинальное название класса из-за проксирования
            // applicationContext.getBean(Quoter.class).getClass()
            // и видим имя jdk.proxy2.$Proxy8
            // делаем так же как в профилировании
            applicationContext.getBean(Quoter.class).sayQuote();
//        }
    }
}
