import java.io.Serializable;
import java.sql.Date;

import javax.print.attribute.standard.DateTimeAtCompleted;

public class MessageTo implements Serializable {

    private String uuid_reciver;
    private String uuid_sender;
    private String message;
    private String message_date;
    private boolean is_deleted;

    public MessageTo() {
        this.uuid_reciver = "";
        this.uuid_sender = "";
        this.message = "";
        this.message_date = null;
        this.is_deleted = false;
    }

    public MessageTo(String uuid_sender, String uuid_reciver, String message, String message_date, boolean isdelated) {
        this.uuid_reciver = uuid_reciver;
        this.uuid_sender = uuid_sender;
        this.message = message;
        this.message_date = message_date;
        this.is_deleted = isdelated;
    }

    public String getMessage() {
        return message;
    }

    public String getMessage_date() {
        return message_date;
    }

    public String getUuid_reciver() {
        return uuid_reciver;
    }

    public String getUuid_sender() {
        return uuid_sender;
    }

    public boolean getIsDelated() {
        return is_deleted;
    }

    public void setIs_deleted(boolean is_deleted) {
        this.is_deleted = is_deleted;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMessage_date(String message_date) {
        this.message_date = message_date;
    }

    public void setUuid_reciver(String uuid_reciver) {
        this.uuid_reciver = uuid_reciver;
    }

    public void setUuid_sender(String uuid_sender) {
        this.uuid_sender = uuid_sender;
    }

    @Override
    public String toString() {
        return "MessageTo{" +
                "uuid_reciver='" + uuid_reciver + '\'' +
                ", uuid_sender='" + uuid_sender + '\'' +
                ", message='" + message + '\'' +
                ", message_date=" + message_date +
                ", is_deleted=" + is_deleted +
                '}';
    }
}
