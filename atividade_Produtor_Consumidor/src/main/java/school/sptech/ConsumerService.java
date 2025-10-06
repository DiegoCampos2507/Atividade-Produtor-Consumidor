package school.sptech;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ConsumerService {

    private List<String> ultimasMensagens;

    public ConsumerService(List<String> ultimasMensagem) {
        this.ultimasMensagens = ultimasMensagem;
    }

    public List<String> getUltimasMensagens() {
        return ultimasMensagens != null ? ultimasMensagens : Collections.singletonList("Nenhuma mensagem recebida ainda.");
    }

    @RabbitListener(queues = "${broker.queue.name}")
    public void receive(String message) {
        System.out.println("Received message: " + message);
        ultimasMensagens.add(message);
    }
}
