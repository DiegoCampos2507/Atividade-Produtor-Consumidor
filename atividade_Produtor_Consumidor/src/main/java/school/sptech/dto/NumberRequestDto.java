package school.sptech.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para receber o número do usuário via requisição HTTP
 */
public class NumberRequestDto {
    
    @JsonProperty("number")
    private Integer number;

    public NumberRequestDto() {
    }

    public NumberRequestDto(Integer number) {
        this.number = number;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "NumberRequestDto{" +
                "number=" + number +
                '}';
    }
}