package school.sptech;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Serviço responsável por consumir mensagens do RabbitMQ
 */
@Service
public class ConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerService.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final int MAX_MESSAGES = 100; // Limite máximo de mensagens armazenadas

    // Lista thread-safe para armazenar as últimas mensagens recebidas
    private final List<String> ultimasMensagens;

    public ConsumerService() {
        this.ultimasMensagens = new CopyOnWriteArrayList<>();
    }

    /**
     * Retorna a lista das últimas mensagens recebidas
     * 
     * @return Lista imutável das mensagens ou mensagem padrão se vazia
     */
    public List<String> getUltimasMensagens() {
        if (ultimasMensagens.isEmpty()) {
            return Collections.singletonList("Nenhuma mensagem recebida ainda.");
        }
        
        // Retorna uma cópia imutável da lista para evitar modificações externas
        return Collections.unmodifiableList(new ArrayList<>(ultimasMensagens));
    }

    /**
     * Método listener que consome mensagens da fila RabbitMQ
     * 
     * @param message a mensagem recebida da fila
     */
    @RabbitListener(queues = "${broker.queue.name}")
    public void receive(String message) {
        try {
            String timestamp = LocalDateTime.now().format(DATE_FORMATTER);
            String formattedMessage = String.format("[%s] %s", timestamp, message);
            
            logger.info("Mensagem recebida do RabbitMQ: {}", message);
            
            // Adiciona a mensagem formatada à lista
            ultimasMensagens.add(formattedMessage);
            
            // Remove mensagens antigas se exceder o limite
            if (ultimasMensagens.size() > MAX_MESSAGES) {
                ultimasMensagens.remove(0); // Remove a mais antiga
                logger.debug("Limite de mensagens excedido. Mensagem mais antiga removida.");
            }
            
            logger.info("Mensagem processada e armazenada com sucesso: {}", formattedMessage);
            
        } catch (Exception e) {
            logger.error("Erro ao processar mensagem recebida '{}': {}", message, e.getMessage(), e);
        }
    }

    /**
     * Retorna o número total de mensagens recebidas
     * 
     * @return número de mensagens na lista
     */
    public int getTotalMensagens() {
        return ultimasMensagens.size();
    }

    /**
     * Limpa todas as mensagens armazenadas
     */
    public void limparMensagens() {
        ultimasMensagens.clear();
        logger.info("Todas as mensagens foram removidas da lista");
    }
}
