# API Produtor-Consumidor RabbitMQ

Este projeto implementa uma API REST para enviar e receber mensagens através do RabbitMQ seguindo o padrão Producer-Consumer.

## Funcionalidades Implementadas

### 🚀 Producer (Envio de Mensagens)
- **POST /producer/send-number**: Envia um número via JSON
- **GET /producer/send-number**: Envia um número via query parameter  
- **POST /producer/send-message**: Envia uma mensagem customizada

### 📥 Consumer (Recebimento de Mensagens)
- **GET /consumer/messages**: Lista todas as mensagens recebidas
- **GET /consumer/stats**: Estatísticas do consumidor
- **DELETE /consumer/messages**: Limpa todas as mensagens armazenadas

## Como Usar

### 1. Enviar um Número (POST)

```bash
curl -X POST http://localhost:8080/producer/send-number \
  -H "Content-Type: application/json" \
  -d '{"number": 42}'
```

**Resposta:**
```json
{
  "success": true,
  "message": "Número enviado com sucesso para o RabbitMQ",
  "sentNumber": 42,
  "timestamp": "2024-10-06 14:30:15"
}
```

### 2. Enviar um Número (GET)

```bash
curl "http://localhost:8080/producer/send-number?number=123"
```

### 3. Enviar Mensagem Customizada

```bash
curl -X POST "http://localhost:8080/producer/send-message?message=Olá RabbitMQ"
```

### 4. Listar Mensagens Recebidas

```bash
curl http://localhost:8080/consumer/messages
```

**Resposta:**
```json
[
  "[2024-10-06 14:30:15] 42",
  "[2024-10-06 14:30:20] 123", 
  "[2024-10-06 14:30:25] Olá RabbitMQ"
]
```

### 5. Ver Estatísticas

```bash
curl http://localhost:8080/consumer/stats
```

**Resposta:**
```json
{
  "totalMensagens": 3,
  "status": "Ativo"
}
```

### 6. Limpar Mensagens

```bash
curl -X DELETE http://localhost:8080/consumer/messages
```

## Validações Implementadas

### ✅ Validação de Números
- Verifica se o número é um inteiro válido
- Rejeita valores nulos ou não numéricos
- Retorna erro 400 (Bad Request) em caso de dados inválidos

### ✅ Validação de Mensagens
- Verifica se a mensagem não está vazia
- Remove espaços em branco desnecessários

### ✅ Tratamento de Erros
- Logs detalhados para debugging
- Respostas JSON padronizadas para erros
- Tratamento de exceções do RabbitMQ

## Arquitetura

```
Controller → Service → RabbitMQ → Consumer Service
     ↓                                    ↓
   DTOs                             Lista Thread-Safe
```

### 📁 Estrutura de Pacotes
- `controller/` - Controllers REST
- `service/` - Lógica de negócio
- `dto/` - Data Transfer Objects
- `config/` - Configurações (CORS, RabbitMQ)

### 🔧 Tecnologias Utilizadas
- **Spring Boot 3.3.3** - Framework principal
- **Spring AMQP** - Integração com RabbitMQ
- **Jackson** - Serialização JSON
- **SLF4J + Logback** - Sistema de logs

## Exemplo de Integração Frontend

### JavaScript/Fetch API

```javascript
// Enviar número
async function enviarNumero(numero) {
  try {
    const response = await fetch('http://localhost:8080/producer/send-number', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ number: numero })
    });
    
    const result = await response.json();
    console.log('Número enviado:', result);
  } catch (error) {
    console.error('Erro:', error);
  }
}

// Buscar mensagens
async function buscarMensagens() {
  try {
    const response = await fetch('http://localhost:8080/consumer/messages');
    const mensagens = await response.json();
    console.log('Mensagens recebidas:', mensagens);
  } catch (error) {
    console.error('Erro:', error);
  }
}
```

## Executar o Projeto

1. Certifique-se que o RabbitMQ está rodando (via Docker Compose)
2. Execute o projeto Spring Boot:
   ```bash
   mvn spring-boot:run
   ```
3. A API estará disponível em `http://localhost:8080`

## Logs de Exemplo

```
2024-10-06 14:30:15 INFO  [ProducerController] - Recebida requisição POST para enviar número: NumberRequestDto{number=42}
2024-10-06 14:30:15 INFO  [PublisherService] - Enviando número 42 para o exchange example.fanout.exchange
2024-10-06 14:30:15 INFO  [PublisherService] - Número 42 enviado com sucesso para o RabbitMQ
2024-10-06 14:30:15 INFO  [ConsumerService] - Mensagem recebida do RabbitMQ: 42
```