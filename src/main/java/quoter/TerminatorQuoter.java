package quoter;

import annotation.InjectRandomInt;
import annotation.PostProxy;
import annotation.Profiling;

import javax.annotation.PostConstruct;

@Profiling
public class TerminatorQuoter implements Quoter {

    @InjectRandomInt(min=2, max=7)
    private int repeat;

    private String message;

    @PostConstruct
    public void init() {
        // поставили аннотацию @PostConstruct
        // но она работать не будет)
        // надо добавить зависимость в проект в файл pom
        // и прописать бин <bean class="org.springframework.context.annotation.CommonAnnotationBeanPostProcessor" />
        System.out.println("PHASE 2 repeat: " + this.repeat);
    }

    public TerminatorQuoter() {
        System.out.println("PHASE 1 repeat: " + this.repeat);
    }

    /**
     * сеттер нужен только для xml конфигов
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    @PostProxy
    public void sayQuote() {
        System.out.println("PHASE 3");

        for (int i = 0; i < this.repeat; i++) {
            System.out.println("message = " + this.message);
        }
    }
}
