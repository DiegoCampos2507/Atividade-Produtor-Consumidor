package school.sptech.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável por publicar mensagens no RabbitMQ
 */
@Service
public class PublisherService {

    private static final Logger logger = LoggerFactory.getLogger(PublisherService.class);

    private final RabbitTemplate rabbitTemplate;

    @Value("${broker.exchange.name}")
    private String exchangeName;

    public PublisherService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Envia um número como mensagem para o RabbitMQ
     * 
     * @param number o número a ser enviado
     * @throws RuntimeException se ocorrer erro no envio
     */
    public void sendNumber(Integer number) {
        try {
            // Converte o número para string para envio
            String message = number.toString();
            
            logger.info("Enviando número {} para o exchange {}", number, exchangeName);
            
            // Publica a mensagem no exchange configurado
            rabbitTemplate.convertAndSend(exchangeName, "", message);
            
            logger.info("Número {} enviado com sucesso para o RabbitMQ", number);
            
        } catch (Exception e) {
            logger.error("Erro ao enviar número {} para o RabbitMQ: {}", number, e.getMessage(), e);
            throw new RuntimeException("Falha ao enviar mensagem para o RabbitMQ", e);
        }
    }

    /**
     * Envia uma mensagem customizada para o RabbitMQ
     * 
     * @param message a mensagem a ser enviada
     * @throws RuntimeException se ocorrer erro no envio
     */
    public void sendMessage(String message) {
        try {
            logger.info("Enviando mensagem '{}' para o exchange {}", message, exchangeName);
            
            rabbitTemplate.convertAndSend(exchangeName, "", message);
            
            logger.info("Mensagem '{}' enviada com sucesso para o RabbitMQ", message);
            
        } catch (Exception e) {
            logger.error("Erro ao enviar mensagem '{}' para o RabbitMQ: {}", message, e.getMessage(), e);
            throw new RuntimeException("Falha ao enviar mensagem para o RabbitMQ", e);
        }
    }
}