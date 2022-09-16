package estudo.s.ipsumintegrationtest.constants;

public class ExpectedMessage {
    
    private Message message;
    private Object[] args;

    public ExpectedMessage(Message message, Object... args) {
        this.message = message;
        this.args  = args;
    }

    @Override
    public String toString() {
        return String.format(message.message, args);
    }

}
