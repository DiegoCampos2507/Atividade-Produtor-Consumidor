package school.sptech.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para resposta de confirmação do envio da mensagem
 */
public class MessageResponseDto {
    
    @JsonProperty("success")
    private boolean success;
    
    @JsonProperty("message")
    private String message;
    
    @JsonProperty("sentNumber")
    private Integer sentNumber;
    
    @JsonProperty("timestamp")
    private String timestamp;

    public MessageResponseDto() {
    }

    public MessageResponseDto(boolean success, String message, Integer sentNumber, String timestamp) {
        this.success = success;
        this.message = message;
        this.sentNumber = sentNumber;
        this.timestamp = timestamp;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getSentNumber() {
        return sentNumber;
    }

    public void setSentNumber(Integer sentNumber) {
        this.sentNumber = sentNumber;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "MessageResponseDto{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", sentNumber=" + sentNumber +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}