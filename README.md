# Java microservices demo (Spring Boot)
What are microservices?

Microservices is an architecture where you build an application as a set of small, independent services, each responsible for one business capability (users, orders, payments, and so on). Each service:

runs as its own process, with its own code and its own data
talks to other services over the network, usually HTTP/REST or a message queue
can be developed, deployed, and scaled independently

The opposite is a monolith, where everything lives in one codebase and is deployed as one unit.

Why people use them: you can scale only the busy part (say, orders), different teams can own different services, and a crash in one service doesn’t have to take down everything. The trade-off: more moving parts, network failures to handle, and harder debugging and deployment.

Service	Port	What it does
user-service	8081	Owns user data (/users, /users/{id})
order-service	8082	Owns orders. For /orders/{id} it calls user-service over HTTP using RestClient (in UserClient.java)
api-gateway	8080	The single entry point. Clients call /api/... and it forwards to the right service
Setup
Install JDK 17+ and Maven (check with java -version and mvn -v).
In VS Code, install the Extension Pack for Java and the Spring Boot Extension Pack.
Run

Open three terminals in VS Code and run one command in each, starting with the user service:

cd user-service  && mvn spring-boot:run
cd order-service && mvn spring-boot:run
cd api-gateway   && mvn spring-boot:run

You can also open each *Application.java file and click Run above the main method.

Try it in your browser
http://localhost:8080/api/users
http://localhost:8080/api/users/1
http://localhost:8080/api/orders
http://localhost:8080/api/orders/101 returns the order combined with user details, fetched from a different service
See the benefit

Swagger UI is available for each service:

- API gateway: http://localhost:8080/swagger-ui/index.html
- User service: http://localhost:8081/swagger-ui/index.html
- Order service: http://localhost:8082/swagger-ui/index.html

Each service's OpenAPI JSON is available at `/v3/api-docs` on the same host and port.

Stop user-service (Ctrl+C) and reload /api/orders/101. The order service doesn’t crash. It returns the order with a "User service unavailable" warning. Asking for a user that doesn’t exist, such as /api/users/99, returns a clean 404 JSON error.

Things to know
Each service has its own in-memory data. In a real system each would have its own database.
Service URLs live in application.properties and can be overridden with environment variables like USER_SERVICE_URL, so nothing is hardcoded.
The gateway here is a simple hand-written Spring controller to keep the example easy to read. Production systems often use Spring Cloud Gateway instead.
There’s a small JUnit test in user-service. Run it with cd user-service && mvn test.

Requires JDK 21 (LTS) and Maven 3.8+ (`java -version`, `mvn -v`).

| Service       | Port | Role                                   |
|---------------|------|----------------------------------------|
| api-gateway   | 8080 | Single entry point, routes to services |
| user-service  | 8081 | Owns user data                         |
| order-service | 8082 | Owns orders, calls user-service        |

Run each in its own terminal:

    cd user-service  && mvn spring-boot:run
    cd order-service && mvn spring-boot:run
    cd api-gateway   && mvn spring-boot:run

Try:

    http://localhost:8080/api/users
    http://localhost:8080/api/users/1
    http://localhost:8080/api/orders
    http://localhost:8080/api/orders/101

Stop user-service (Ctrl+C) and call /api/orders/101 again: order-service still responds.

Run the unit tests: `cd user-service && mvn test`
