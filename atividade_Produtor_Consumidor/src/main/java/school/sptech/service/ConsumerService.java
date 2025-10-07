package school.sptech.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerService.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final List<String> mensagensRecebidas = Collections.synchronizedList(new ArrayList<>());
    private int totalMensagens = 0;

    @RabbitListener(queues = "${broker.queue.name}")
    public void receiveMessage(@Payload String message) {
        String timestamp = LocalDateTime.now().format(DATE_FORMATTER);
        String mensagemFormatada = String.format("[%s] \"%s\"", timestamp, message);

        synchronized (mensagensRecebidas) {
            mensagensRecebidas.add(mensagemFormatada);
            totalMensagens++;
        }

        logger.info("Mensagem recebida do RabbitMQ: {} | Total de mensagens: {}", message, totalMensagens);
    }

    public List<String> getUltimasMensagens() {
        synchronized (mensagensRecebidas) {
            return new ArrayList<>(mensagensRecebidas);
        }
    }

    public int getTotalMensagens() {
        return totalMensagens;
    }

    public void limparMensagens() {
        synchronized (mensagensRecebidas) {
            mensagensRecebidas.clear();
            totalMensagens = 0;
        }
        logger.info("Todas as mensagens foram limpas do histórico");
    }
}