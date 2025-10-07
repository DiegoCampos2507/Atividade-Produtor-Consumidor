# 🐰 Atividade Produtor-Consumidor

> Sistema de mensageria distribuída utilizando RabbitMQ com padrão Producer-Consumer

[![Java](https://img.shields.io/badge/Java-21-orange)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.3-brightgreen)](https://spring.io/projects/spring-boot)
[![RabbitMQ](https://img.shields.io/badge/RabbitMQ-3.x-orange)](https://www.rabbitmq.com/)
[![Docker](https://img.shields.io/badge/Docker-Compose-blue)](https://docs.docker.com/compose/)

---

## 👥 Identificação do Grupo

| Nome Completo | RA |
|---------------|-----|
| **[NOME COMPLETO 1]** | **[RA 1]** |
| **Gustavo Silva Presilli** | **04241056** |
| **Diego Crispim S Campos** | **04241019** |

---

## 📖 Descrição do Projeto

Este projeto implementa um sistema de **mensageria distribuída** utilizando o padrão **Producer-Consumer** com **RabbitMQ** como message broker. O sistema é composto por duas aplicações principais:

### 🚀 **Produtor (Producer)**
- **Função**: Recebe números do usuário através de endpoints REST
- **Tecnologia**: Spring Boot com Spring AMQP
- **Porta**: `8080`
- **Responsabilidades**:
  - Validar números recebidos via HTTP
  - Publicar mensagens no RabbitMQ
  - Retornar confirmações de envio
  - Tratamento de erros e logs

### 📥 **Consumidor (Consumer)**
- **Função**: Consome mensagens do RabbitMQ e as processa
- **Tecnologia**: Spring Boot com Spring AMQP
- **Porta**: `8080` (mesma aplicação do Producer)
- **Responsabilidades**:
  - Escutar mensagens da fila RabbitMQ
  - Processar e armazenar mensagens recebidas
  - Disponibilizar endpoints para consulta
  - Manter histórico das mensagens

### 🐰 **RabbitMQ (Message Broker)**
- **Função**: Intermediar a comunicação entre Producer e Consumer
- **Tecnologia**: RabbitMQ via Docker
- **Porta**: `5672` (AMQP) e `15672` (Management UI)
- **Configuração**: Fanout Exchange para broadcast de mensagens

### 🎨 **Frontend Interativo**
- **Função**: Interface web para jogar
- **Tecnologia**: HTML5, CSS3, JavaScript Vanilla
- **Features**:
  - Input para número da sorte
  - Botões: Iniciar, Parar, Limpar
  - Exibição em tempo real dos números sorteados
  - Mensagens de carregamento animadas
  - Feedback visual de vitória/derrota

---

## 🛠️ Tecnologias Utilizadas

### **Backend**
- **Java 21** - Linguagem de programação
- **Spring Boot 3.3.3** - Framework principal
- **Spring AMQP** - Integração com RabbitMQ
- **Maven** - Gerenciamento de dependências

### **Mensageria**
- **RabbitMQ 3.x** - Message broker
- **AMQP Protocol** - Protocolo de mensageria

### **Frontend**
- **HTML5** - Estrutura
- **CSS3** - Estilização e animações
- **JavaScript (ES6+)** - Lógica e requisições AJAX
- **Fetch API** - Comunicação com backend

### **Containerização**
- **Docker** - Containerização do RabbitMQ
- **Docker Compose** - Orquestração de serviços

### **Desenvolvimento**
- **Jackson** - Serialização JSON
- **SLF4J + Logback** - Sistema de logs
- **Lombok** - Redução de boilerplate

---

## 🚀 Instruções para Subir o Ambiente

### **Pré-requisitos**
- ☑️ Java 21 ou superior
- ☑️ Maven 3.6+
- ☑️ Docker e Docker Compose
- ☑️ Git

### **1. Clonar o Repositório**
```bash
git clone https://github.com/DiegoCampos2507/Atividade-Produtor-Consumidor.git
cd Atividade-Produtor-Consumidor
```

### **2. Subir o RabbitMQ**
```bash
# Subir o RabbitMQ em background
docker compose up -d

# Verificar se está rodando
docker compose ps
```

### **3. Acessar o RabbitMQ Management**
- **URL**: http://localhost:15672
- **Usuário**: `guest`
- **Senha**: `guest`

### **4. Iniciar a Aplicação Spring Boot**
```bash
# Navegar para o diretório da aplicação
cd atividade_Produtor_Consumidor

# Executar a aplicação
mvn spring-boot:run

# Logs esperados
Started SpringRabbitApplication in X.XXX seconds
Listening on queue: fila-numero
Exchange configured: exchange-numero
```

### **5. Verificar se a Aplicação Subiu**
```bash
# Testar se a aplicação está rodando
curl http://localhost:8080/consumer/stats
```

**Resposta esperada:**
```json
{
  "totalMensagens": 0,
  "status": "Aguardando mensagens"
}
```

---

## 🎮 Como Jogar

### **Via Interface Web**

1. **Abra o arquivo**: `atividade_Produtor_Consumidor_Front-End/index.html` no navegador
   - Recomendado usar Live Server (altere para porta 3000) ou qualquer servidor HTTP (Durante o desenvolvimento foi usado o comando: python3 -m http.server 3000)
   
2. **Digite seu número da sorte**: Entre 1 e 100 no campo de input
   
3. **Clique em "Iniciar"**: O jogo começa a gerar números ao clicar no botão "Iniciar"
   
4. **Aguarde os resultados**: 
   - **"Gerando números sorteados..."** aparece (azul pulsante)
   - **"Processando números..."** enquanto envia ao backend
   - 10 números aleatórios são sorteados e exibidos
   - **Se você ganhar**: "Parabéns! Você ganhou com o número: X" (verde, negrito)
   - **Se perder**: "Que pena! Você não foi sorteado desta vez. Seu número era X." (vermelho, negrito)
   
5. **Limpar dados**: Use o botão "Limpar dados" para reiniciar completamente
   - Limpa todas as mensagens da tela
   - Remove dados do RabbitMQ
   - Reseta o estado do jogo
   - Exibe mensagem de confirmação em verde


### **Como o Consumidor Processa**
1. **Escuta Automática**: O consumidor fica escutando a fila RabbitMQ continuamente
2. **Processamento**: Cada mensagem recebida é formatada com timestamp
3. **Armazenamento**: Mensagens são armazenadas em lista thread-safe na memória
4. **Limite**: Mantém no máximo 100 mensagens (remove as mais antigas)
5. **Logs**: Registra todas as operações para debugging

---

## 📁 Estrutura de Pastas

```
Atividade-Produtor-Consumidor/
├── 📄 README.md                                    # 📚 Documentação principal (ESTE ARQUIVO)
├── 🐳 compose.yaml                                 # 🐋 Configuração Docker do RabbitMQ
│
├── 📁 atividade_Produtor_Consumidor/               # 🚀 Aplicação Spring Boot
│   ├── 📄 pom.xml                                  # Maven dependencies
│   ├── 📁 src/main/java/school/sptech/
│   │   ├── 🎮 SpringRabbitApplication.java         # Classe principal (@SpringBootApplication)
│   │   ├── ⚙️ RabbitTemplateConfiguration.java     # Configuração de Queue, Exchange e Binding
│   │   │
│   │   ├── 📁 controller/
│   │   │   ├── 📤 ProducerController.java          # Endpoints do Produtor
│   │   │   │   ├── POST /producer/send-number      # Envia número(s) ao RabbitMQ
│   │   │   │   └── DELETE /producer/clear          # Limpa fila + histórico
│   │   │   │
│   │   │   └── 📥 ConsumerController.java          # Endpoints do Consumidor
│   │   │       ├── GET /consumer/messages          # Lista mensagens recebidas
│   │   │       ├── GET /consumer/stats             # Estatísticas (total, status)
│   │   │       └── DELETE /consumer/messages       # Limpa apenas histórico
│   │   │
│   │   ├── 📁 service/
│   │   │   ├── 📨 PublisherService.java            # Lógica de envio ao RabbitMQ
│   │   │   │   ├── sendNumber(Integer)             # Publica mensagem no Exchange
│   │   │   │   └── clearAllData()                  # Limpa fila + histórico completo
│   │   │   │
│   │   │   └── 📩 ConsumerService.java             # Lógica de recebimento de mensagens
│   │   │       ├── @RabbitListener                 # Escuta automática da fila
│   │   │       ├── receiveMessage(String)          # Processa e armazena mensagem
│   │   │       ├── getUltimasMensagens()           # Retorna lista de mensagens
│   │   │       ├── getTotalMensagens()             # Contador de mensagens
│   │   │       └── limparMensagens()               # Limpa lista em memória
│   │   │
│   │   ├── 📁 dto/
│   │   │   ├── 📝 NumberRequestDto.java            # DTO para receber número(s)
│   │   │   └── 📋 MessageResponseDto.java          # DTO de resposta padronizada
│   │   │
│   │   └── 📁 config/
│   │       └── 🌐 CorsConfig.java                  # Configuração CORS (localhost:5500)
│   │
│   └── 📁 src/main/resources/
│       └── ⚙️ application.properties               # Configurações (RabbitMQ, porta, logs)
│
└── 📁 atividade_Produtor_Consumidor_Front-End/     # 🎨 Interface Web Interativa
    └── 🌐 index.html                               # Frontend completo do jogo
        ├── 🎯 HTML Structure                        # Estrutura da página (inputs, botões, div messages)
        ├── 🎨 CSS Styling                          # Estilos, cores, animações (pulse effect)
        └── 🔧 JavaScript Logic                     # Lógica completa do jogo
            ├── init()                              # Inicia o jogo (valida input, gera números)
            ├── stop()                              # Para o jogo (limpa intervalos)
            ├── geradorDeNumeros()                  # Gera 10 números aleatórios e envia
            ├── fetchMessages()                     # Busca mensagens do backend
            ├── clearMessages()                     # Limpa tudo (backend + frontend)
            └── Validações e Feedback Visual        # Mensagens de carregamento, vitória, derrota
```

---

## 🧪 Testando o Sistema Completo

### **Teste Rápido via cURL**
```bash
# 1. Enviar uma mensagem
curl -X POST http://localhost:8080/producer/send-number \
  -H "Content-Type: application/json" \
  -d '{"number": 999}'

# 2. Aguardar processamento (1-2 segundos)
sleep 2

# 3. Verificar se foi recebida
curl http://localhost:8080/consumer/messages
```

---

## 🔧 Possíveis Melhorias e Observações

### **Melhorias Técnicas**
- 🔐 **Autenticação**: Implementar JWT ou OAuth2
- 🗄️ **Persistência**: Salvar mensagens em banco de dados
- 📊 **Métricas**: Integrar com Prometheus/Grafana
- 🚀 **Performance**: Implementar pool de conexões
- 🔄 **Retry**: Políticas de reenvio para mensagens falhadas

### **Melhorias de Funcionalidade**
- 📱 **Frontend Avançado**: Interface React/Vue.js completa
- 🎯 **Filtros**: Busca e filtros nas mensagens
- 📈 **Dashboard**: Painel com estatísticas em tempo real
- 🔔 **Notificações**: WebSockets para updates em tempo real
- 📤 **Múltiplos Tipos**: Suporte a diferentes tipos de mensagem

### **Melhorias de Infraestrutura**
- 🐳 **Containerização Completa**: Dockerizar toda a aplicação
- ☸️ **Kubernetes**: Deploy em cluster
- 🔍 **Observabilidade**: Logs centralizados com ELK Stack
- 🔒 **Segurança**: HTTPS e certificados SSL
- 🌍 **Cloud**: Deploy em AWS/Azure/GCP

### **Observações de Desenvolvimento**
- ✅ **Thread Safety**: Lista thread-safe implementada
- 📝 **Logs Estruturados**: SLF4J configurado adequadamente
- 🛡️ **Validações**: Validação completa de entrada
- 🧪 **Testes**: Scripts e interface de teste incluídos
- 📚 **Documentação**: README e documentação da API completos

---

# ============================================
# Server Configuration
# ============================================
back-end server.port=8080
front-end server.port=3000

# README realizado com apoio de inteligência artificial para inclusão de mais conteúdos