package ch.antbou.boundary.out;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErrorResponse {
    private String type = "error";
    private String message;

    public ErrorResponse(String message) {
        this.type = "error";
        this.message = message;
    }
}
