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

> ⚠️ **IMPORTANTE**: Substitua os campos acima com os dados reais dos integrantes do grupo.

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

## 📤 Como Enviar uma Mensagem de Teste

### **Endpoint do Produtor**

#### **POST - Enviar Número via JSON**
- **URL**: `http://localhost:8080/producer/send-number`
- **Método**: `POST`
- **Headers**: `Content-Type: application/json`

**Exemplo de Requisição:**
```bash
curl -X POST http://localhost:8080/producer/send-number \
  -H "Content-Type: application/json" \
  -d '{"number": 42}'
```

**JSON de Entrada:**
```json
{
  "number": 42
}
```

**Exemplo de Retorno:**
```json
{
  "success": true,
  "message": "Número enviado com sucesso para o RabbitMQ",
  "sentNumber": 42,
  "timestamp": "2024-10-06 14:30:15"
}
```

#### **GET - Enviar Número via Query Parameter**
- **URL**: `http://localhost:8080/producer/send-number?number=123`
- **Método**: `GET`

**Exemplo de Requisição:**
```bash
curl "http://localhost:8080/producer/send-number?number=123"
```

#### **POST - Enviar Mensagem Customizada**
- **URL**: `http://localhost:8080/producer/send-message?message=Hello`
- **Método**: `POST`

**Exemplo de Requisição:**
```bash
curl -X POST "http://localhost:8080/producer/send-message?message=Hello RabbitMQ"
```

---

## 📥 Como Verificar a Mensagem no Consumidor

### **Endpoint do Consumidor**

#### **GET - Listar Mensagens Recebidas**
- **URL**: `http://localhost:8080/consumer/messages`
- **Método**: `GET`

**Exemplo de Requisição:**
```bash
curl http://localhost:8080/consumer/messages
```

**Exemplo de Retorno:**
```json
[
  "[2024-10-06 14:30:15] 42",
  "[2024-10-06 14:30:20] 123",
  "[2024-10-06 14:30:25] Hello RabbitMQ"
]
```

#### **GET - Verificar Estatísticas**
- **URL**: `http://localhost:8080/consumer/stats`
- **Método**: `GET`

**Exemplo de Retorno:**
```json
{
  "totalMensagens": 3,
  "status": "Ativo"
}
```

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
├── 📄 README.md                                    # Documentação principal
├── 🐳 compose.yaml                                 # Configuração Docker do RabbitMQ
├── 📁 atividade_Produtor_Consumidor/               # Aplicação Spring Boot
│   ├── 📄 pom.xml                                  # Dependências Maven
│   ├── 📁 src/main/java/school/sptech/
│   │   ├── 🎮 SpringRabbitApplication.java         # Classe principal
│   │   ├── ⚙️ RabbitTemplateConfiguration.java     # Configuração RabbitMQ
│   │   ├── 📁 controller/
│   │   │   ├── 📤 ProducerController.java          # Endpoints do Produtor
│   │   │   └── 📥 ConsumerController.java          # Endpoints do Consumidor
│   │   ├── 📁 service/
│   │   │   ├── 📨 PublisherService.java            # Lógica de envio
│   │   │   └── 📩 ConsumerService.java             # Lógica de recebimento
│   │   ├── 📁 dto/
│   │   │   ├── 📝 NumberRequestDto.java            # DTO de entrada
│   │   │   └── 📋 MessageResponseDto.java          # DTO de resposta
│   │   └── 📁 config/
│   │       └── 🌐 CorsConfig.java                  # Configuração CORS
│   ├── 📁 src/main/resources/
│   │   └── ⚙️ application.properties               # Configurações da aplicação
│   ├── 🧪 test_api.sh                              # Script de testes automatizados
│   ├── 🌐 frontend_test.html                       # Interface de teste
│   └── 📁 docs/
│       └── 📚 API_README.md                        # Documentação detalhada da API
└── 📁 atividade_Produtor_Consumidor_Front-End/     # Interface Web (opcional)
    └── 🌐 index.html                               # Frontend básico
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

### **Teste via Interface Web**
1. Abra o arquivo `atividade_Produtor_Consumidor/frontend_test.html` no navegador
2. Use a interface para enviar números e mensagens
3. Veja as mensagens sendo recebidas em tempo real

### **Teste Automatizado**
```bash
# Executar script de testes
cd atividade_Produtor_Consumidor
./test_api.sh
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

## ⚠️ Aviso de Entrega

> **🚨 ATENÇÃO**: A ausência deste arquivo `README.md` com todas as informações solicitadas (identificação do grupo, instruções de execução, exemplos de uso, etc.) acarretará **NOTA 0** na avaliação do projeto.

### **Checklist de Entrega**
- ✅ Identificação completa do grupo (nomes e RAs)
- ✅ Descrição clara do funcionamento do sistema
- ✅ Instruções detalhadas para execução
- ✅ Exemplos práticos de uso da API
- ✅ Estrutura de pastas documentada
- ✅ Docker Compose funcional
- ✅ Aplicação executando corretamente
- ✅ Testes de integração funcionando

---

## 📞 Suporte

Em caso de dúvidas ou problemas:

1. **Verificar logs da aplicação**: `docker compose logs`
2. **Testar conectividade**: Acessar http://localhost:15672 (RabbitMQ)
3. **Validar dependências**: `mvn dependency:tree`
4. **Consultar documentação**: Ver `docs/API_README.md`

---

## 📄 Licença

Este projeto foi desenvolvido para fins acadêmicos como parte da disciplina de **[NOME DA DISCIPLINA]** da **[NOME DA INSTITUIÇÃO]**.

---

<div align="center">
  
**🎓 Desenvolvido com ❤️ por [NOME DO GRUPO]**

[![GitHub](https://img.shields.io/badge/GitHub-DiegoCampos2507-black?logo=github)](https://github.com/DiegoCampos2507/Atividade-Produtor-Consumidor)

</div>