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

/**
 * Controller responsável por receber números do usuário e enviar para o RabbitMQ
 */
@RestController
@RequestMapping("/producer")
@CrossOrigin(origins = "*")
public class ProducerController {

    private static final Logger logger = LoggerFactory.getLogger(ProducerController.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final PublisherService publisherService;

    public ProducerController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    /**
     * Endpoint POST para enviar um número via JSON no corpo da requisição
     * 
     * @param request DTO contendo o número a ser enviado
     * @return ResponseEntity com confirmação do envio
     */
    @PostMapping("/send-number")
    public ResponseEntity<MessageResponseDto> sendNumber(@RequestBody NumberRequestDto request) {
        logger.info("Recebida requisição POST para enviar número: {}", request);

        try {
            // Validação do número recebido
            if (request.getNumber() == null) {
                logger.warn("Número não fornecido na requisição");
                MessageResponseDto errorResponse = new MessageResponseDto(
                    false, 
                    "Número é obrigatório", 
                    null, 
                    LocalDateTime.now().format(DATE_FORMATTER)
                );
                return ResponseEntity.badRequest().body(errorResponse);
            }

            Integer number = request.getNumber();
            
            // Chama o serviço para enviar o número para o RabbitMQ
            publisherService.sendNumber(number);

            // Cria resposta de sucesso
            MessageResponseDto response = new MessageResponseDto(
                true,
                "Número enviado com sucesso para o RabbitMQ",
                number,
                LocalDateTime.now().format(DATE_FORMATTER)
            );

            logger.info("Número {} enviado com sucesso via POST", number);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Erro ao processar envio do número via POST: {}", e.getMessage(), e);
            
            MessageResponseDto errorResponse = new MessageResponseDto(
                false,
                "Erro interno do servidor: " + e.getMessage(),
                request.getNumber(),
                LocalDateTime.now().format(DATE_FORMATTER)
            );
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Endpoint GET para enviar um número via query parameter
     * 
     * @param number o número a ser enviado (query parameter)
     * @return ResponseEntity com confirmação do envio
     */
    @GetMapping("/send-number")
    public ResponseEntity<MessageResponseDto> sendNumberViaGet(@RequestParam("number") String numberStr) {
        logger.info("Recebida requisição GET para enviar número: {}", numberStr);

        try {
            // Validação e conversão do número
            Integer number;
            try {
                number = Integer.parseInt(numberStr);
            } catch (NumberFormatException e) {
                logger.warn("Número inválido fornecido: {}", numberStr);
                MessageResponseDto errorResponse = new MessageResponseDto(
                    false,
                    "Número deve ser um inteiro válido",
                    null,
                    LocalDateTime.now().format(DATE_FORMATTER)
                );
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // Chama o serviço para enviar o número para o RabbitMQ
            publisherService.sendNumber(number);

            // Cria resposta de sucesso
            MessageResponseDto response = new MessageResponseDto(
                true,
                "Número enviado com sucesso para o RabbitMQ",
                number,
                LocalDateTime.now().format(DATE_FORMATTER)
            );

            logger.info("Número {} enviado com sucesso via GET", number);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Erro ao processar envio do número via GET: {}", e.getMessage(), e);
            
            MessageResponseDto errorResponse = new MessageResponseDto(
                false,
                "Erro interno do servidor: " + e.getMessage(),
                null,
                LocalDateTime.now().format(DATE_FORMATTER)
            );
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Endpoint POST para enviar uma mensagem customizada
     * 
     * @param message a mensagem a ser enviada
     * @return ResponseEntity com confirmação do envio
     */
    @PostMapping("/send-message")
    public ResponseEntity<MessageResponseDto> sendCustomMessage(@RequestParam("message") String message) {
        logger.info("Recebida requisição para enviar mensagem customizada: {}", message);

        try {
            // Validação da mensagem
            if (message == null || message.trim().isEmpty()) {
                logger.warn("Mensagem vazia fornecida");
                MessageResponseDto errorResponse = new MessageResponseDto(
                    false,
                    "Mensagem não pode ser vazia",
                    null,
                    LocalDateTime.now().format(DATE_FORMATTER)
                );
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // Chama o serviço para enviar a mensagem para o RabbitMQ
            publisherService.sendMessage(message);

            // Cria resposta de sucesso
            MessageResponseDto response = new MessageResponseDto(
                true,
                "Mensagem '" + message + "' enviada com sucesso para o RabbitMQ",
                null,
                LocalDateTime.now().format(DATE_FORMATTER)
            );

            logger.info("Mensagem '{}' enviada com sucesso", message);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Erro ao processar envio da mensagem: {}", e.getMessage(), e);
            
            MessageResponseDto errorResponse = new MessageResponseDto(
                false,
                "Erro interno do servidor: " + e.getMessage(),
                null,
                LocalDateTime.now().format(DATE_FORMATTER)
            );
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}