# 📚 **DOCUMENTAÇÃO COMPLETA DA API MEETIX**

## 🌐 **INFORMAÇÕES GERAIS**

- **Base URL**: `http://localhost:8081`
- **Porta**: `8081`
- **Framework**: Spring Boot 3.5.5
- **Banco de Dados**: PostgreSQL 17.6
- **Autenticação**: JWT (JSON Web Token)
- **Formato de Resposta**: JSON
- **Encoding**: UTF-8

---

## 🔐 **AUTENTICAÇÃO**

### **Token JWT**
- **Algoritmo**: HS256
- **Expiração**: 24 horas
- **Header**: `Authorization: Bearer {token}`
- **Claim Principal**: `email` do usuário

### **Como usar o Token**
1. Faça login ou registro em `/auth/login` ou `/auth/register`
2. Copie o token retornado
3. Adicione em todas as requisições autenticadas:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

## 📋 **ENDPOINTS DE AUTENTICAÇÃO**

### **BASE**: `/auth`

---

#### **1. 🔓 POST /auth/login** - Fazer Login

**URL Completa**: `http://localhost:8081/auth/login`

**Headers**:
```
Content-Type: application/json
```

**Body (JSON)**:
```json
{
  "email": "joao@exemplo.com",
  "password": "senha123"
}
```

**Exemplo de Requisição (cURL)**:
```bash
curl -X POST http://localhost:8081/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@exemplo.com",
    "password": "senha123"
  }'
```

**Resposta de Sucesso (200)**:
```json
{
  "success": true,
  "message": "Login realizado com sucesso",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2FvQGV4ZW1wbG8uY29tIiwiaWF0IjoxNjk2Nzg5MjAwLCJleHAiOjE2OTY4NzU2MDB9.K7BwF5H9nXqJ2M8L1P4R5S6T7U8V9W0X1Y2Z3A4B5C6",
    "userId": "550e8400-e29b-41d4-a716-446655440001",
    "email": "joao@exemplo.com",
    "firstName": "João",
    "lastName": "Silva"
  }
}
```

**Resposta de Erro (401)**:
```json
{
  "success": false,
  "message": "Credenciais inválidas",
  "data": null
}
```

---

#### **2. 📝 POST /auth/register** - Criar Nova Conta

**URL Completa**: `http://localhost:8081/auth/register`

**Headers**:
```
Content-Type: application/json
```

**Body (JSON)**:
```json
{
  "firstName": "João",
  "lastName": "Silva",
  "email": "joao@exemplo.com",
  "password": "senha123",
  "instagram": "@joao_silva",
  "university": "UFPE",
  "course": "Engenharia de Software"
}
```

**Exemplo de Requisição (cURL)**:
```bash
curl -X POST http://localhost:8081/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "João",
    "lastName": "Silva",
    "email": "joao@exemplo.com",
    "password": "senha123",
    "instagram": "@joao_silva",
    "university": "UFPE",
    "course": "Engenharia de Software"
  }'
```

**Resposta de Sucesso (201)**:
```json
{
  "success": true,
  "message": "Usuário registrado com sucesso",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2FvQGV4ZW1wbG8uY29tIiwiaWF0IjoxNjk2Nzg5MjAwLCJleHAiOjE2OTY4NzU2MDB9.K7BwF5H9nXqJ2M8L1P4R5S6T7U8V9W0X1Y2Z3A4B5C6",
    "userId": "550e8400-e29b-41d4-a716-446655440001",
    "email": "joao@exemplo.com",
    "firstName": "João",
    "lastName": "Silva"
  }
}
```

**Resposta de Erro (409)**:
```json
{
  "success": false,
  "message": "Email já está em uso",
  "data": null
}
```

---

#### **3. ✅ GET /auth/validate** - Validar Token

**URL Completa**: `http://localhost:8081/auth/validate`

**Headers**:
```
Authorization: Bearer {token}
```

**Exemplo de Requisição (cURL)**:
```bash
curl -X GET http://localhost:8081/auth/validate \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

**Resposta de Sucesso (200)**:
```json
{
  "success": true,
  "message": "Token válido",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userId": "550e8400-e29b-41d4-a716-446655440001",
    "email": "joao@exemplo.com",
    "firstName": "João",
    "lastName": "Silva"
  }
}
```

**Resposta de Erro (401)**:
```json
{
  "success": false,
  "message": "Token inválido",
  "data": null
}
```

---

#### **4. 🚪 POST /auth/logout** - Fazer Logout

**URL Completa**: `http://localhost:8081/auth/logout`

**Headers**:
```
Authorization: Bearer {token}
```

**Exemplo de Requisição (cURL)**:
```bash
curl -X POST http://localhost:8081/auth/logout \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

**Resposta de Sucesso (200)**:
```json
{
  "success": true,
  "message": "Logout realizado com sucesso. Remova o token do lado cliente.",
  "data": null
}
```

---

#### **5. 💚 GET /auth/health** - Status do Serviço

**URL Completa**: `http://localhost:8081/auth/health`

**Headers**: Nenhum necessário

**Exemplo de Requisição (cURL)**:
```bash
curl -X GET http://localhost:8081/auth/health
```

**Resposta (200)**:
```json
{
  "success": true,
  "message": "Serviço de autenticação está funcionando",
  "timestamp": 1696789200000
}
```

---

## 👥 **ENDPOINTS DE USUÁRIOS**

### **BASE**: `/users`

**⚠️ IMPORTANTE**: Todos os endpoints de usuários exigem autenticação via token JWT.

---

#### **6. 👤 GET /users/me** - Ver Meu Perfil (RECOMENDADO)

**URL Completa**: `http://localhost:8081/users/me`

**Headers**:
```
Authorization: Bearer {token}
```

**Exemplo de Requisição (cURL)**:
```bash
curl -X GET http://localhost:8081/users/me \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

**Resposta de Sucesso (200)**:
```json
{
  "success": true,
  "message": "Perfil encontrado",
  "data": {
    "id_user": "550e8400-e29b-41d4-a716-446655440001",
    "firstName": "João",
    "lastName": "Silva",
    "email": "joao@exemplo.com",
    "instagram": "@joao_silva",
    "university": "UFPE",
    "course": "Engenharia de Software",
    "createdAt": "2024-10-08T10:30:00.000+00:00",
    "updatedAt": "2024-10-08T10:30:00.000+00:00"
  }
}
```

**Resposta de Erro (404)**:
```json
{
  "success": false,
  "message": "Perfil não encontrado",
  "data": null
}
```

---

#### **7. ✏️ PUT /users/me** - Editar Meu Perfil (RECOMENDADO)

**URL Completa**: `http://localhost:8081/users/me`

**Headers**:
```
Authorization: Bearer {token}
Content-Type: application/json
```

**Body (JSON)** - Todos os campos são opcionais:
```json
{
  "firstName": "João Carlos",
  "lastName": "Silva Santos",
  "email": "joao.carlos@exemplo.com",
  "password": "novaSenha123",
  "instagram": "@joao_carlos_dev",
  "university": "UFPE",
  "course": "Ciência da Computação"
}
```

**Exemplo de Requisição (cURL)**:
```bash
curl -X PUT http://localhost:8081/users/me \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "João Carlos",
    "lastName": "Silva Santos",
    "password": "novaSenha123"
  }'
```

**Resposta de Sucesso (200)**:
```json
{
  "success": true,
  "message": "Seu perfil foi atualizado com sucesso",
  "data": {
    "id_user": "550e8400-e29b-41d4-a716-446655440001",
    "firstName": "João Carlos",
    "lastName": "Silva Santos",
    "email": "joao@exemplo.com",
    "instagram": "@joao_silva",
    "university": "UFPE",
    "course": "Engenharia de Software",
    "createdAt": "2024-10-08T10:30:00.000+00:00",
    "updatedAt": "2024-10-08T15:45:00.000+00:00"
  }
}
```

---

#### **8. 🗑️ DELETE /users/me** - Deletar Minha Conta (RECOMENDADO)

**URL Completa**: `http://localhost:8081/users/me`

**Headers**:
```
Authorization: Bearer {token}
```

**Exemplo de Requisição (cURL)**:
```bash
curl -X DELETE http://localhost:8081/users/me \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

**Resposta de Sucesso (200)**:
```json
{
  "success": true,
  "message": "Sua conta foi deletada com sucesso. Você será desconectado.",
  "data": null
}
```

**🚨 IMPORTANTE**: Ao deletar a conta:
- Todos os eventos organizados pelo usuário são deletados (CASCADE)
- Todas as participações em eventos são removidas (CASCADE)
- O token JWT se torna inválido

---

#### **9. 📋 GET /users** - Listar Todos Usuários

**URL Completa**: `http://localhost:8081/users`

**Headers**:
```
Authorization: Bearer {token}
```

**Exemplo de Requisição (cURL)**:
```bash
curl -X GET http://localhost:8081/users \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

**Resposta de Sucesso (200)**:
```json
{
  "success": true,
  "message": "Usuários listados com sucesso",
  "data": [
    {
      "id_user": "550e8400-e29b-41d4-a716-446655440001",
      "firstName": "João",
      "lastName": "Silva",
      "email": "joao@exemplo.com",
      "instagram": "@joao_silva",
      "university": "UFPE",
      "course": "Engenharia de Software"
    },
    {
      "id_user": "550e8400-e29b-41d4-a716-446655440002",
      "firstName": "Maria",
      "lastName": "Santos",
      "email": "maria@exemplo.com",
      "instagram": "@maria_santos",
      "university": "UFC",
      "course": "Design"
    }
  ]
}
```

---

#### **10. 🔍 GET /users/{id}** - Buscar Usuário por ID

**URL Completa**: `http://localhost:8081/users/{id}`

**Parâmetros de Path**:
- `id` (UUID): ID do usuário

**Headers**:
```
Authorization: Bearer {token}
```

**Exemplo de Requisição (cURL)**:
```bash
curl -X GET http://localhost:8081/users/550e8400-e29b-41d4-a716-446655440001 \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

**Resposta de Sucesso (200)**:
```json
{
  "success": true,
  "message": "Usuário encontrado",
  "data": {
    "id_user": "550e8400-e29b-41d4-a716-446655440001",
    "firstName": "João",
    "lastName": "Silva",
    "email": "joao@exemplo.com",
    "instagram": "@joao_silva",
    "university": "UFPE",
    "course": "Engenharia de Software"
  }
}
```

**Resposta de Erro (404)**:
```json
{
  "success": false,
  "message": "Usuário não encontrado",
  "data": null
}
```

---

#### **11. 📧 GET /users/email/{email}** - Buscar Usuário por Email

**URL Completa**: `http://localhost:8081/users/email/{email}`

**Parâmetros de Path**:
- `email` (String): Email do usuário

**Headers**:
```
Authorization: Bearer {token}
```

**Exemplo de Requisição (cURL)**:
```bash
curl -X GET http://localhost:8081/users/email/joao@exemplo.com \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

**Resposta**: Mesmo formato do endpoint anterior.

---

#### **12. ✏️ PUT /users/{id}** - Editar Usuário por ID (COM VALIDAÇÃO)

**URL Completa**: `http://localhost:8081/users/{id}`

**⚠️ REGRA DE NEGÓCIO**: Só funciona se o ID corresponder ao usuário autenticado.

**Parâmetros de Path**:
- `id` (UUID): ID do usuário

**Headers**:
```
Authorization: Bearer {token}
Content-Type: application/json
```

**Body (JSON)**:
```json
{
  "firstName": "João Carlos",
  "lastName": "Silva Santos",
  "email": "joao.carlos@exemplo.com",
  "password": "novaSenha123",
  "instagram": "@joao_carlos_dev",
  "university": "UFPE",
  "course": "Ciência da Computação"
}
```

**Exemplo de Requisição (cURL)**:
```bash
curl -X PUT http://localhost:8081/users/550e8400-e29b-41d4-a716-446655440001 \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "João Carlos",
    "password": "novaSenha123"
  }'
```

**Resposta de Sucesso (200)**:
```json
{
  "success": true,
  "message": "Sua conta foi atualizada com sucesso",
  "data": {
    "id_user": "550e8400-e29b-41d4-a716-446655440001",
    "firstName": "João Carlos",
    "lastName": "Silva",
    "email": "joao@exemplo.com"
  }
}
```

**Resposta de Erro - Tentativa de Editar Conta de Outro (403)**:
```json
{
  "success": false,
  "message": "Você só pode editar sua própria conta",
  "data": null
}
```

---

#### **13. 🗑️ DELETE /users/{id}** - Deletar Usuário por ID (COM VALIDAÇÃO)

**URL Completa**: `http://localhost:8081/users/{id}`

**⚠️ REGRA DE NEGÓCIO**: Só funciona se o ID corresponder ao usuário autenticado.

**Parâmetros de Path**:
- `id` (UUID): ID do usuário

**Headers**:
```
Authorization: Bearer {token}
```

**Exemplo de Requisição (cURL)**:
```bash
curl -X DELETE http://localhost:8081/users/550e8400-e29b-41d4-a716-446655440001 \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

**Resposta de Sucesso (200)**:
```json
{
  "success": true,
  "message": "Sua conta foi deletada com sucesso",
  "data": null
}
```

**Resposta de Erro - Tentativa de Deletar Conta de Outro (403)**:
```json
{
  "success": false,
  "message": "Você só pode deletar sua própria conta",
  "data": null
}
```

---

#### **14. 👨‍💼 POST /users** - Criar Usuário (ADMINISTRATIVO)

**URL Completa**: `http://localhost:8081/users`

**⚠️ NOTA**: Para registros normais, use `/auth/register`. Este endpoint é para uso administrativo.

**Headers**:
```
Authorization: Bearer {token}
Content-Type: application/json
```

**Body (JSON)**:
```json
{
  "firstName": "Admin",
  "lastName": "User",
  "email": "admin@exemplo.com",
  "password": "adminSenha123",
  "instagram": "@admin_user",
  "university": "UFPE",
  "course": "Administração"
}
```

**Resposta de Sucesso (201)**:
```json
{
  "success": true,
  "message": "Usuário criado com sucesso",
  "data": {
    "id_user": "550e8400-e29b-41d4-a716-446655440003",
    "firstName": "Admin",
    "lastName": "User",
    "email": "admin@exemplo.com"
  }
}
```

---

## 🎉 **ENDPOINTS DE EVENTOS**

### **BASE**: `/events`

**✅ IMPORTANTE**: Endpoints de visualização de eventos são PÚBLICOS e não requerem autenticação.
**⚠️ NOTA**: Criação/edição/exclusão de eventos ainda não possui autenticação (será implementada em versões futuras).

---

#### **15. 🎪 POST /events** - Criar Evento

**URL Completa**: `http://localhost:8081/events`

**Headers**:
```
Content-Type: application/json
```

**Body (JSON)**:
```json
{
  "eventType": "WORKSHOP",
  "title": "Workshop de Spring Boot",
  "description": "Aprenda a criar APIs REST com Spring Boot",
  "startDateTime": "2024-11-01T10:00:00",
  "endDateTime": "2024-11-01T18:00:00",
  "location": "UFPE - Centro de Informática",
  "imgUrl": "https://exemplo.com/imagem.jpg",
  "eventUrl": "https://meetix.com/events/workshop-spring",
  "remote": false,
  "maxAttendees": 50,
  "isPaid": true,
  "price": 99.90,
  "organizerId": "550e8400-e29b-41d4-a716-446655440001",
  "generateCertificate": true
}
```

**Tipos de Evento Disponíveis**:
- `WORKSHOP`
- `CONFERENCE`
- `MEETUP`
- `SEMINAR`
- `HACKATHON`

**Exemplo de Requisição (cURL)**:
```bash
curl -X POST http://localhost:8081/events \
  -H "Content-Type: application/json" \
  -d '{
    "eventType": "WORKSHOP",
    "title": "Workshop de Spring Boot",
    "description": "Aprenda a criar APIs REST com Spring Boot",
    "startDateTime": "2024-11-01T10:00:00",
    "endDateTime": "2024-11-01T18:00:00",
    "location": "UFPE - Centro de Informática",
    "remote": false,
    "maxAttendees": 50,
    "isPaid": false,
    "price": 0.00,
    "organizerId": "550e8400-e29b-41d4-a716-446655440001",
    "generateCertificate": true
  }'
```

**Resposta de Sucesso (201)**:
```json
{
  "id_event": "660e8400-e29b-41d4-a716-446655440001",
  "eventType": "WORKSHOP",
  "title": "Workshop de Spring Boot",
  "description": "Aprenda a criar APIs REST com Spring Boot",
  "startDateTime": "2024-11-01T10:00:00",
  "endDateTime": "2024-11-01T18:00:00",
  "location": "UFPE - Centro de Informática",
  "imgUrl": null,
  "eventUrl": null,
  "remote": false,
  "maxAttendees": 50,
  "isPaid": false,
  "price": 0.00,
  "organizerId": "550e8400-e29b-41d4-a716-446655440001",
  "generateCertificate": true,
  "createdAt": "2024-10-08T15:30:00.000+00:00",
  "updatedAt": "2024-10-08T15:30:00.000+00:00"
}
```

---

#### **16. 📋 GET /events** - Listar Todos Eventos

**URL Completa**: `http://localhost:8081/events`

**Headers**: Nenhum necessário

**Exemplo de Requisição (cURL)**:
```bash
curl -X GET http://localhost:8081/events
```

**Resposta de Sucesso (200)**:
```json
[
  {
    "id_event": "660e8400-e29b-41d4-a716-446655440001",
    "eventType": "WORKSHOP",
    "title": "Workshop de Spring Boot",
    "description": "Aprenda a criar APIs REST com Spring Boot",
    "startDateTime": "2024-11-01T10:00:00",
    "endDateTime": "2024-11-01T18:00:00",
    "location": "UFPE - Centro de Informática",
    "remote": false,
    "maxAttendees": 50,
    "isPaid": false,
    "price": 0.00,
    "organizerId": "550e8400-e29b-41d4-a716-446655440001",
    "generateCertificate": true
  },
  {
    "id_event": "660e8400-e29b-41d4-a716-446655440002",
    "eventType": "MEETUP",
    "title": "Meetup de React",
    "description": "Discussões sobre as novidades do React",
    "startDateTime": "2024-11-05T19:00:00",
    "endDateTime": "2024-11-05T22:00:00",
    "location": "Online",
    "remote": true,
    "maxAttendees": 100,
    "isPaid": false,
    "price": 0.00,
    "organizerId": "550e8400-e29b-41d4-a716-446655440002",
    "generateCertificate": false
  }
]
```

---

#### **17. 🔍 GET /events/{id}** - Buscar Evento por ID

**URL Completa**: `http://localhost:8081/events/{id}`

**Parâmetros de Path**:
- `id` (UUID): ID do evento

**Headers**: Nenhum necessário

**Exemplo de Requisição (cURL)**:
```bash
curl -X GET http://localhost:8081/events/660e8400-e29b-41d4-a716-446655440001
```

**Resposta de Sucesso (200)**:
```json
{
  "id_event": "660e8400-e29b-41d4-a716-446655440001",
  "eventType": "WORKSHOP",
  "title": "Workshop de Spring Boot",
  "description": "Aprenda a criar APIs REST com Spring Boot",
  "startDateTime": "2024-11-01T10:00:00",
  "endDateTime": "2024-11-01T18:00:00",
  "location": "UFPE - Centro de Informática",
  "remote": false,
  "maxAttendees": 50,
  "isPaid": false,
  "price": 0.00,
  "organizerId": "550e8400-e29b-41d4-a716-446655440001",
  "generateCertificate": true
}
```

**Resposta de Erro (404)**:
```
HTTP 404 Not Found
(Sem body)
```

---

#### **18. ✏️ PUT /events/{id}** - Editar Evento

**URL Completa**: `http://localhost:8081/events/{id}`

**Parâmetros de Path**:
- `id` (UUID): ID do evento

**Headers**:
```
Content-Type: application/json
```

**Body (JSON)** - Todos os campos são obrigatórios:
```json
{
  "eventType": "CONFERENCE",
  "title": "Conferência de Spring Boot Avançado",
  "description": "Técnicas avançadas de Spring Boot para produção",
  "startDateTime": "2024-11-01T09:00:00",
  "endDateTime": "2024-11-01T17:00:00",
  "location": "UFPE - Auditório Principal",
  "imgUrl": "https://exemplo.com/nova-imagem.jpg",
  "eventUrl": "https://meetix.com/events/conference-spring",
  "remote": false,
  "maxAttendees": 80,
  "isPaid": true,
  "price": 149.90,
  "organizerId": "550e8400-e29b-41d4-a716-446655440001",
  "generateCertificate": true
}
```

**Exemplo de Requisição (cURL)**:
```bash
curl -X PUT http://localhost:8081/events/660e8400-e29b-41d4-a716-446655440001 \
  -H "Content-Type: application/json" \
  -d '{
    "eventType": "CONFERENCE",
    "title": "Conferência de Spring Boot Avançado",
    "description": "Técnicas avançadas de Spring Boot para produção",
    "startDateTime": "2024-11-01T09:00:00",
    "endDateTime": "2024-11-01T17:00:00",
    "location": "UFPE - Auditório Principal",
    "remote": false,
    "maxAttendees": 80,
    "isPaid": true,
    "price": 149.90,
    "organizerId": "550e8400-e29b-41d4-a716-446655440001",
    "generateCertificate": true
  }'
```

**Resposta de Sucesso (200)**:
```json
{
  "id_event": "660e8400-e29b-41d4-a716-446655440001",
  "eventType": "CONFERENCE",
  "title": "Conferência de Spring Boot Avançado",
  "description": "Técnicas avançadas de Spring Boot para produção",
  "startDateTime": "2024-11-01T09:00:00",
  "endDateTime": "2024-11-01T17:00:00",
  "location": "UFPE - Auditório Principal",
  "remote": false,
  "maxAttendees": 80,
  "isPaid": true,
  "price": 149.90,
  "organizerId": "550e8400-e29b-41d4-a716-446655440001",
  "generateCertificate": true,
  "updatedAt": "2024-10-08T16:00:00.000+00:00"
}
```

**Resposta de Erro (404)**:
```
HTTP 404 Not Found
(Sem body)
```

---

#### **19. 🗑️ DELETE /events/{id}** - Deletar Evento

**URL Completa**: `http://localhost:8081/events/{id}`

**Parâmetros de Path**:
- `id` (UUID): ID do evento

**Headers**: Nenhum necessário

**Exemplo de Requisição (cURL)**:
```bash
curl -X DELETE http://localhost:8081/events/660e8400-e29b-41d4-a716-446655440001
```

**Resposta de Sucesso (204)**:
```
HTTP 204 No Content
(Sem body)
```

---

## 📊 **CÓDIGOS DE STATUS HTTP**

### **Códigos de Sucesso**
- **200 OK**: Operação realizada com sucesso
- **201 Created**: Recurso criado com sucesso (registro/criação)
- **204 No Content**: Operação realizada sem conteúdo de retorno (delete)

### **Códigos de Erro do Cliente**
- **400 Bad Request**: Dados inválidos na requisição
- **401 Unauthorized**: Token inválido, expirado ou ausente
- **403 Forbidden**: Usuário não tem permissão para a operação
- **404 Not Found**: Recurso não encontrado
- **409 Conflict**: Conflito (ex: email já em uso)

### **Códigos de Erro do Servidor**
- **500 Internal Server Error**: Erro interno do servidor

---

## 🛡️ **REGRAS DE SEGURANÇA**

### **Autenticação Obrigatória**
Endpoints que exigem token JWT:
- Todos os endpoints `/users/*` (gerenciamento de usuários)
- Futuramente: criação/edição/exclusão de eventos (POST/PUT/DELETE `/events/*`)

### **Endpoints Públicos (sem autenticação)**
- `/auth/*` - Autenticação e registro
- `GET /events` - Listar todos eventos
- `GET /events/{id}` - Ver evento específico
- `/health` - Health checks

### **Autorização por Propriedade**
Usuários só podem:
- ✅ Ver/editar/deletar sua própria conta
- ❌ Modificar contas de outros usuários (retorna 403 Forbidden)

### **Validação de Token**
- Token deve estar presente no header `Authorization`
- Formato: `Bearer {token}`
- Token deve ser válido e não expirado
- Token identifica o usuário pelo claim `email`

---

## 📝 **EXEMPLOS DE FLUXOS COMPLETOS**

### **Fluxo 1: Registro e Login**
```bash
# 1. Registrar nova conta
curl -X POST http://localhost:8081/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "João",
    "lastName": "Silva",
    "email": "joao@exemplo.com",
    "password": "senha123",
    "university": "UFPE",
    "course": "Engenharia"
  }'

# 2. Fazer login (alternativo, registro já retorna token)
curl -X POST http://localhost:8081/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@exemplo.com",
    "password": "senha123"
  }'

# 3. Usar o token retornado em outras requisições
```

### **Fluxo 2: Gerenciar Perfil**
```bash
# 1. Ver meu perfil
curl -X GET http://localhost:8081/users/me \
  -H "Authorization: Bearer {TOKEN_AQUI}"

# 2. Editar meu perfil
curl -X PUT http://localhost:8081/users/me \
  -H "Authorization: Bearer {TOKEN_AQUI}" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "João Carlos",
    "password": "novaSenha123"
  }'

# 3. Deletar minha conta
curl -X DELETE http://localhost:8081/users/me \
  -H "Authorization: Bearer {TOKEN_AQUI}"
```

### **Fluxo 3: Gerenciar Eventos**
```bash
# 1. Criar evento
curl -X POST http://localhost:8081/events \
  -H "Content-Type: application/json" \
  -d '{
    "eventType": "WORKSHOP",
    "title": "Meu Workshop",
    "description": "Descrição do workshop",
    "startDateTime": "2024-11-01T10:00:00",
    "endDateTime": "2024-11-01T18:00:00",
    "location": "Local do evento",
    "remote": false,
    "maxAttendees": 30,
    "isPaid": false,
    "price": 0.00,
    "organizerId": "{MEU_USER_ID}",
    "generateCertificate": true
  }'

# 2. Listar todos eventos
curl -X GET http://localhost:8081/events

# 3. Ver evento específico
curl -X GET http://localhost:8081/events/{EVENT_ID}

# 4. Editar evento
curl -X PUT http://localhost:8081/events/{EVENT_ID} \
  -H "Content-Type: application/json" \
  -d '{ ... }'

# 5. Deletar evento
curl -X DELETE http://localhost:8081/events/{EVENT_ID}
```

---

## 🚨 **TRATAMENTO DE ERROS COMUNS**

### **Token JWT Expirado**
```bash
# Erro retornado:
{
  "timestamp": "2024-10-08T17:00:00.000+00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource",
  "path": "/users/me"
}

# Solução: Fazer login novamente
curl -X POST http://localhost:8081/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "seu@email.com", "password": "sua_senha"}'
```

### **Tentativa de Editar Conta de Outro Usuário**
```bash
# Erro retornado:
{
  "success": false,
  "message": "Você só pode editar sua própria conta",
  "data": null
}

# Solução: Use /users/me ou verifique se está usando o ID correto
```

### **Email Já em Uso no Registro**
```bash
# Erro retornado:
{
  "success": false,
  "message": "Email já está em uso",
  "data": null
}

# Solução: Use um email diferente ou faça login se já tem conta
```

---

## 🔧 **CONFIGURAÇÃO DO AMBIENTE**

### **Banco de Dados**
```properties
# application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/meetixdev
spring.datasource.username=joaolucasdev
spring.datasource.password=1618
server.port=8081
```

### **Testando se API está funcionando**
```bash
# Health check do serviço de autenticação
curl -X GET http://localhost:8081/auth/health

# Resposta esperada:
{
  "success": true,
  "message": "Serviço de autenticação está funcionando",
  "timestamp": 1696789200000
}
```

---

## 📱 **RECOMENDAÇÕES PARA FRONTEND**

### **Endpoints Recomendados para uso seguro:**
1. **`GET /users/me`** - Sempre use este para ver perfil do usuário logado
2. **`PUT /users/me`** - Sempre use este para editar perfil
3. **`DELETE /users/me`** - Sempre use este para deletar conta

### **Armazenamento do Token:**
- ✅ **localStorage** para aplicações web
- ✅ **AsyncStorage** para React Native
- ✅ **Secure Storage** para aplicações móveis nativas
- ❌ **Não armazenar em cookies** sem configuração adequada

### **Tratamento de Expiração:**
```javascript
// Exemplo de interceptor para renovar token
axios.interceptors.response.use(
  response => response,
  error => {
    if (error.response?.status === 401) {
      // Token expirado - redirecionar para login
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);
```

---

## 🚀 **PRÓXIMOS DESENVOLVIMENTOS**

### **Funcionalidades Planejadas:**
1. **EventParticipantController** - Gestão de participações em eventos
2. **Autenticação em EventController** - Proteção dos endpoints de eventos
3. **Upload de Imagens** - Endpoint para upload de imagens de eventos e perfis
4. **Sistema de Notificações** - WebSocket para notificações em tempo real
5. **Sistema de Avaliações** - Avaliação de eventos pelos participantes

### **Melhorias de Segurança:**
1. **Rate Limiting** - Limitação de requisições por IP/usuário
2. **Refresh Tokens** - Tokens de renovação automática
3. **Logout com Blacklist** - Invalidação real de tokens no servidor
4. **Auditoria Completa** - Log detalhado de todas as operações

---

## 📞 **SUPORTE E CONTATO**

### **Time de Desenvolvimento:**
- **Projeto**: Meetix Backend API
- **Versão**: 1.0.0
- **Framework**: Spring Boot 3.5.5
- **Documentação**: Atualizada em 08/10/2025

### **Recursos Adicionais:**
- **Logs da Aplicação**: Disponíveis no console durante desenvolvimento
- **Banco de Dados**: PostgreSQL com hibernate DDL auto
- **Ambiente**: Desenvolvimento (localhost:8081)

---

**🎉 Esta é a documentação completa da API Meetix! Todos os endpoints estão prontos para uso e testados.** 

**Para começar, faça um registro em `/auth/register` e use o token retornado para acessar os endpoints protegidos!** 🚀