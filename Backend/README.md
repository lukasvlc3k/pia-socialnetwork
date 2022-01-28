# PIA Social Network - Server

As mentioned in the project README.md file, the Server is based on Spring Boot technology.
It provides a REST API for a client.


## Architecture
- Controllers (handle HTTP requests from clients)
- Services (provides methods needed to handle task, using repositories and entities)
- Entities (entities mapped to database objects - ORM)
- Repositories (Hibernate repositories providing abstract database operations)
- DTO (data transfer objects - used for communication between server and client)


## Authentication
Client sends a POST request containing email and password to an
/auth/login endpoint (AuthController). If the credentials are valid, server provides a JWT
(Json Web Token) with 8 hours validity, that you can use in future requests to authenticate the user.

### Endpoints available without authentication/authorization
As specified in a WebSecurityConfig file, any endpoint that matches these wildcards is available without authentication
and authorization:
- /auth/** (login and registration)
- /swagger-ui/** (OpenAPI UI)
- /v3/api-docs/** (OpenAPI files)
- /public/** (websockets and publicly available tools (e.g. email availability check))

In other cases the authentication is needed. When not provided, server replies with 401 Unauthorized Http status code.

### Password handling
All password are B-Crypted before storing to the database. 


## Chat
Real-time chat is handled by WebSockets.
If the chat message recipient is online, the message is sent to all his opened sessions. The message is then send back
to the sender (also to all his opened sessions to achieve consistency).

In any case the received chat message is stored in the database and there is an REST endpoint, that
client can use to get N last messages.

All received messages are stored to the database. The client has a possibility to load N last
messages.

## Online/Offline handling
When the client connects to the WebSocket and is successfully authenticated (via the same JWT as for REST requests),
the user is set as online and all his online friends are notified.
When the connection to this client is lost, all his friends are notified again to change user state.