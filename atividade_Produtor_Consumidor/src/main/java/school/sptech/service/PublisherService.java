package school.sptech.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PublisherService {

    private static final Logger logger = LoggerFactory.getLogger(PublisherService.class);

    private final RabbitTemplate rabbitTemplate;
    private final ConsumerService consumerService;

    @Value("${broker.exchange.name}")
    private String exchangeName;

    @Value("${broker.queue.name}")
    private String queueName;

    @Autowired
    public PublisherService(RabbitTemplate rabbitTemplate, ConsumerService consumerService) {
        this.rabbitTemplate = rabbitTemplate;
        this.consumerService = consumerService;
    }

    public void sendNumber(Integer number) {
        try {
            String message = number.toString();

            logger.info("Enviando número {} para o exchange {}", number, exchangeName);

            rabbitTemplate.convertAndSend(exchangeName, "", message);

            logger.info("Número {} enviado com sucesso para o RabbitMQ", number);

        } catch (Exception e) {
            logger.error("Erro ao enviar número {} para o RabbitMQ: {}", number, e.getMessage(), e);
            throw new RuntimeException("Falha ao enviar mensagem para o RabbitMQ", e);
        }
    }

    public void clearAllData() {
        // Limpa as mensagens do histórico do ConsumerService
        consumerService.limparMensagens();

        // Consome todas as mensagens da fila até que ela fique vazia
        Object message;
        int count = 0;
        do {
            message = rabbitTemplate.receiveAndConvert(queueName);
            if (message != null) {
                count++;
                logger.info("Mensagem removida da fila: {}", message);
            }
        } while (message != null);

        logger.info("Todos os dados foram limpos. {} mensagens removidas da fila.", count);
    }
}