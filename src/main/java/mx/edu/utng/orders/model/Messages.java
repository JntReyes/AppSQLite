package mx.edu.utng.orders.model;

/**
 * Created by jntreyes on 23/02/17.
 */

public class Messages {

        private String id;
        private String message;
        private String ip;

        public Messages(String id, String message, String ip) {
            this.id = id;
            this.message = message;
            this.ip = ip;
        }

        public Messages() {
        this("","","");
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "Messages{" +
                "id='" + id + '\'' +
                ", message='" + message + '\'' +
                ", ip='" + ip + '\'' +
                '}';
    }
}
