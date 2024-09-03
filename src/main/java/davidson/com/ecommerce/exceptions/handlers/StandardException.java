package davidson.com.ecommerce.exceptions.handlers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StandardException {
    private Integer status;
    private String message;
    private Long timestamp;

    public StandardException() {
        this.timestamp = System.currentTimeMillis();
    }

    public StandardException(Integer status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }
}
