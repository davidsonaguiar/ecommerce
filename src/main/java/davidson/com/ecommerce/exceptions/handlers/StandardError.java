package davidson.com.ecommerce.exceptions.handlers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StandardError {
    private Integer status;
    private String message;
    private Long timestamp;

    public StandardError() {
        this.timestamp = System.currentTimeMillis();
    }

    public StandardError(Integer status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }
}
