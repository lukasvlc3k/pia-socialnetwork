# PIA Social Network

PIA Social Network is a simple social network web application that allows users to post and read text posts, make friendships and chat in real time.

The application was created as a semestral work for KIV/PIA (University of West Bohemia), 2022 and consists of two main parts: (backend) server (developed in Spring boot technology) and (client) frontend (made with React.js and Next.js framework).

## Running the application

1. git clone https://github.com/lukasvlc3k/pia-socialnetwork.git
2. cd ./pia-socialnetwork
3. docker-compose up

The frontend (client) will be accessible at http://localhost:3000. Backend http://localhost:8080
Swagger (OpenAPI): http://localhost:8080/swagger-ui/index.html

A new admin account will be created during first app start, the credentials will be logged to console (log level Info)

## Core (mandatory) features

- login
- registration
- friendship management allowing users to find a new friend by name, send friend requests and accept, reject or block them
- real-time messaging between two friends
- creating posts and announcments (admin feature, announcment = post visible for all users, not only friends)

## Bonus features

- entropy based password strength evaluation
- storing chat messages in the database, loading the last 10 messages when opening a new chat window
- showing older posts by scrolling to the bottom of the page (loading starts when scrollbar in 3/4 for smoother experience)
- instant check of email availability on the registration screen

## Technology stack

### Server

- Java 17, gradle
- Spring Boot
- JPA Hibernate ORM
- JsonWebToken (jjwt)
- Spring Websocket

### Database

- PostgreSQL database

### Client

- React 17.0.2
- Next.js 12.0.7
- Typescript 4.5.4
- Bootstrap 5 (React-Bootstrap)
- Moment.js (time utils)
- SweetAlert2 (notifications)
- Sockjs client (web sockets)
- sanitize-html
- emittery (event bus)
- axios

### Utils

- Open API Client Generator (Typescript + axios)

## Swagger (OpenAPI) documentation

http://localhost:8080/swagger-ui/index.html

## More details

More details about server and client implementation in README.md in a server/client directory.
