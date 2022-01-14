package quoter;

public class TerminatorQuoter implements Quoter {
    private String message;

    /**
     * сеттер нужен только для xml конфигов
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void sayQuote() {
        System.out.println("message = " + this.message);
    }
}
