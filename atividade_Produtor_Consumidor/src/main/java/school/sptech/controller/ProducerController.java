package school.sptech.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.dto.MessageResponseDto;
import school.sptech.dto.NumberRequestDto;
import school.sptech.service.PublisherService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/producer")
@CrossOrigin(origins = "http://localhost:5500")
public class ProducerController {

    private static final Logger logger = LoggerFactory.getLogger(ProducerController.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final PublisherService publisherService;

    public ProducerController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @PostMapping("/send-number")
    public ResponseEntity<MessageResponseDto> sendNumber(@RequestBody Map<String, Object> request) {
        logger.info("Recebida requisição POST para enviar número(s): {}", request);

        try {
            // Verifica se é um array de números ou um único número
            if (request.containsKey("numbers")) {
                List<Integer> numbers = (List<Integer>) request.get("numbers");
                for (Integer number : numbers) {
                    publisherService.sendNumber(number);
                }

                MessageResponseDto response = new MessageResponseDto(
                        true,
                        numbers.size() + " números enviados com sucesso para o RabbitMQ",
                        null,
                        LocalDateTime.now().format(DATE_FORMATTER)
                );
                return ResponseEntity.ok(response);
            } else if (request.containsKey("number")) {
                Integer number = (Integer) request.get("number");
                publisherService.sendNumber(number);

                MessageResponseDto response = new MessageResponseDto(
                        true,
                        "Número enviado com sucesso para o RabbitMQ",
                        number,
                        LocalDateTime.now().format(DATE_FORMATTER)
                );
                return ResponseEntity.ok(response);
            } else {
                MessageResponseDto errorResponse = new MessageResponseDto(
                        false,
                        "Número é obrigatório",
                        null,
                        LocalDateTime.now().format(DATE_FORMATTER)
                );
                return ResponseEntity.badRequest().body(errorResponse);
            }

        } catch (Exception e) {
            logger.error("Erro ao processar envio do número: {}", e.getMessage(), e);

            MessageResponseDto errorResponse = new MessageResponseDto(
                    false,
                    "Erro interno do servidor: " + e.getMessage(),
                    null,
                    LocalDateTime.now().format(DATE_FORMATTER)
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Map<String, String>> clearAllData() {
        logger.info("Requisição recebida para limpar todos os dados enviados");

        try {
            publisherService.clearAllData();

            Map<String, String> response = Map.of(
                    "message", "Todos os dados foram removidos com sucesso",
                    "status", "success"
            );

            logger.info("Dados limpos com sucesso");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erro ao limpar os dados: {}", e.getMessage(), e);

            Map<String, String> errorResponse = Map.of(
                    "message", "Erro ao limpar os dados",
                    "status", "error"
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}