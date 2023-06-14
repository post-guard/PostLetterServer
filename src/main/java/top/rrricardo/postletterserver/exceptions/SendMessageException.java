package top.rrricardo.postletterserver.exceptions;

import top.rrricardo.postletterserver.models.Message;

public class SendMessageException extends Exception {
    private final Message message;

    public SendMessageException(Message message, String text) {
        super(text);
        this.message = message;
    }

    public SendMessageException(Message message, String text, Exception exception) {
        super(text, exception);
        this.message = message;
    }
}
