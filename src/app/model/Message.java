package app.model;

/**
 * Created by JackManWu on 2018/1/27.
 */
public class Message {
    private String to;

    private String content;

    public Message() {
    }

    public Message(String to, String content) {
        this.to = to;
        this.content = content;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
