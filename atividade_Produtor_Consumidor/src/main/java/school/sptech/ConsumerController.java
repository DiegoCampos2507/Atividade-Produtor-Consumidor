package school.sptech;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller responsável por expor endpoints relacionados ao consumidor de mensagens
 */
@RestController
@RequestMapping("/consumer")
@CrossOrigin(origins = "*")
public class ConsumerController {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerController.class);

    private final ConsumerService consumerService;

    public ConsumerController(ConsumerService consumerService) {
        this.consumerService = consumerService;
    }

    /**
     * Endpoint para obter todas as mensagens recebidas
     * 
     * @return Lista das últimas mensagens recebidas
     */
    @GetMapping("/messages")
    public ResponseEntity<List<String>> getUltimasMensagens() {
        logger.info("Requisição recebida para obter últimas mensagens");
        
        List<String> mensagens = consumerService.getUltimasMensagens();
        
        logger.info("Retornando {} mensagem(ns)", mensagens.size());
        return ResponseEntity.ok(mensagens);
    }

    /**
     * Endpoint para obter estatísticas do consumidor
     * 
     * @return Informações sobre o total de mensagens recebidas
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getEstatisticas() {
        logger.info("Requisição recebida para obter estatísticas");
        
        int totalMensagens = consumerService.getTotalMensagens();
        
        Map<String, Object> stats = Map.of(
            "totalMensagens", totalMensagens,
            "status", totalMensagens > 0 ? "Ativo" : "Aguardando mensagens"
        );
        
        logger.info("Retornando estatísticas: {}", stats);
        return ResponseEntity.ok(stats);
    }

    /**
     * Endpoint para limpar todas as mensagens armazenadas
     * 
     * @return Confirmação da limpeza
     */
    @DeleteMapping("/messages")
    public ResponseEntity<Map<String, String>> limparMensagens() {
        logger.info("Requisição recebida para limpar mensagens");
        
        consumerService.limparMensagens();
        
        Map<String, String> response = Map.of(
            "message", "Todas as mensagens foram removidas com sucesso",
            "status", "success"
        );
        
        logger.info("Mensagens limpas com sucesso");
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint legado mantido para compatibilidade
     * 
     * @deprecated Use /messages em vez disso
     */
    @GetMapping
    @Deprecated
    public ResponseEntity<List<String>> getUltimaMensagem() {
        logger.warn("Uso de endpoint deprecated detectado. Use /consumer/messages");
        return getUltimasMensagens();
    }
}
