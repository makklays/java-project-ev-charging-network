# Project EV Charging network ⛽☕🛻 - Java

A production-ready EV Charging network microservice built with **Java 17**, **Spring Boot 3**, **Domain-Driven Design (DDD)**, and **Hexagonal Architecture (Ports & Adapters)**.

The project name **"EV Charging Network"** describes a high-throughput, event-driven backend platform designed to manage smart charging infrastructure, session billing, and real-time IoT communications, reflecting a modern startup ecosystem.

This project showcases **Clean Architecture**, **Transactional Outbox** protection, and smart **fintech billing idempotency**.

<p align="left">
  <img src="doc/images/ev-chargin-network2.jpeg" width="400" alt="EV Charging network 1" />
  <img src="doc/images/ev-chargin-network1.jpeg" width="400" alt="EV Charging network 2" />
</p>

---

## ⛽ Architecture Blueprint

This project strictly follows **Hexagonal Architecture** principles combined with **DDD patterns**. The source code is organized into decoupled layers using standard Java packages to enforce strict boundaries between business logic and technical infrastructure:

1. **`domain` (The Core)**: 100% pure Java code. Contains Aggregate Roots (`UserWallet`, `LedgerAuditLog`, `ChargingSession`), Entities, Enums (`InvoiceStatus`), and immutable Domain Events (`WalletDepositedEvent`, `InvoicePaidEvent`). It has **zero dependencies** on Spring, Hibernate, or any external frameworks.
2. **`application`**: Orchestrates business workflows. Defines **Inbound Ports** (Use Cases interfaces like `DepositMoneyUseCase`) and coordinates transactions. It depends only on the domain layer.
3. **`infrastructure`**: Technology-specific layer. Contains **Inbound Adapters** (Spring Boot `@RestController` classes) and **Outbound Adapters** (Spring Data JPA Persistence adapters, Apache Kafka configuration).
    * **`building_blocks`**: A technical sub-package inside infrastructure that houses cross-cutting components shared across all business domains, specifically the **Transactional Outbox** engine (`OutboxEventPublisher`) and the global **Idempotency** interceptor (`IdempotencyInterceptor`).

---

## 🏪 Core Business Rules & Orchestration Engine 

* **Dynamic Tariff Calculation**: Evaluates active charging rates in real time based on a matrix of variables including 
time-of-day pricing (peak vs. off-peak), connector speed capabilities (AC vs. ultra-fast DC), and idle penalty fees 
applied when a vehicle remains plugged in after reaching full charge.
* **Smart Power Dispatching**: Automatically routes available electrical current across active connectors. If total demand 
exceeds station grid limits, the routing engine dynamically throttles power to non-priority sessions to ensure 
continuous, safe operation without tripping circuit breakers.
* **State Transition & Validation Rules**: Enforces strict state machines for both hardware connectors (e.g., Available -> 
Preparing -> Charging -> Finishing -> Faulted) and user billing sessions, dropping invalid or out-of-order telemetry 
packets to maintain absolute system consistency.
* **Payment Reservation & Invoice Routing**: Initiates a pre-authorization hold on the user's payment method upon plug-in. 
Once the session terminates, the routing engine routes final consumption metrics to the **billing worker**, settles the exact 
amount, releases remaining funds, and dispatches an electronic receipt asynchronously.
* **High-Performance Geo-Fencing:** Utilizes **PostGIS** (hibernate-spatial) to index and query geographic coordinates of 
charging hubs. Allows the system to execute fast spatial queries, calculating the nearest available connector relative 
to the EV driver's real-time **GPS** position, and filtering charging networks within a dynamic search radius.
* **Database Evolution & Cold Storage Archival:** Employs Flyway for version-controlled, production-safe database 
migrations. Historical ledger audit logs and closed charging sessions are periodically offloaded to **AWS S3** as cold 
storage, ensuring the main **PostgreSQL** instance remains highly optimized and performant.

--- 

## 🛠️ Tech Stack & Prerequisites

* **Backend**: Java 17 (Utilizing Record types and Pattern Matching for clean architecture)
* **Framework**: Spring Boot 3.3+ (Spring Web, Spring Security, Spring Data JPA, Spring Validation)
* **Message Broker**: Apache Kafka (For high-throughput, guaranteed At-Least-Once event delivery)
* **Databases & Spatial Extensions**: PostgreSQL 16+ with **PostGIS** (`hibernate-spatial`) for advanced geo-queries
* **Database Migrations**: Flyway (Database versioning)
* **Cloud Infrastructure**: AWS SDK for Java 2.x (Amazon S3 for long-term historical data archival)
* **Data Mapping**: MapStruct 1.5+ (For performance-focused, compilation-time entity-to-domain mapping)
* **Build System**: Apache Maven 3.9+
* **Testing**: JUnit 5 (with 100% Mock-free unit testing for the domain core)

---

## 🚀 Getting Started

### 1. Spin up the infrastructure
Before booting the application, start the required cloud-ready environment (PostgreSQL 16 and Apache Kafka) using 
Docker Compose:
```bash
docker compose up -d
```

### 2. Build the project
Run the following command from the root directory to compile the source code, run native checks, and bundle the asset:
```bash
mvn clean package
```

### 3. Run the application
Locate the compiled executable fat-JAR file in the main `target/` directory and boot up the platform engine:
```bash
java -jar target/java-project-ev-charging-network-0.0.1-SNAPSHOT.jar
```

