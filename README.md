# Project EV Charging network ⛽☕🛻 - Java

A production-ready EV Charging network microservice built with **Java 17**, **Spring Boot 3**, **Domain-Driven Design (DDD)**, and **Hexagonal Architecture (Ports & Adapters)**.

The project name **"EV Charging Network"** describes a high-throughput, event-driven backend platform designed to manage smart charging infrastructure, session billing, and real-time IoT communications, reflecting a modern startup ecosystem.

<p align="left">
  <img src="doc/images/ev-chargin-network2.jpeg" width="400" alt="EV Charging network 1" />
  <img src="doc/images/ev-chargin-network1.jpeg" width="400" alt="EV Charging network 2" />
</p>

---

## ⛽ Architecture Blueprint

This project strictly follows **Hexagonal Architecture** principles combined with **DDD patterns**. The source code is decoupled into three isolated Maven modules to enforce architectural boundaries at the compilation level:

1. **`domain` (The Core)**: 100% pure Java 17 code. Contains Aggregate Roots, Entities, Value Objects, and core business rules. Zero dependencies on Spring, Hibernate, or any external framework.
2. **`application` (Use Cases / Ports)**: Orchestrates business workflows. Defines **Inbound Ports** (Use Cases API) and **Outbound Ports** (SPI for DB and Gateways). Depends only on the `domain` module.
3. **`infrastructure` (Adapters)**: Technology-specific layer. Contains **Inbound Adapters** (Spring REST Controllers) and **Outbound Adapters** (Spring Data JPA, PostgreSQL, HTTP Clients).

---

## 🏪 Core Business Rules & Routing Engine

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
Once the session terminates, the routing engine routes final consumption metrics to the billing worker, settles the exact 
amount, releases remaining funds, and dispatches an electronic receipt asynchronously.

--- 

## 🛠️ Tech Stack & Prerequisites

* **Backend**: Java 17 (Utilizing Record types and Pattern Matching)
* **Framework**: Spring Boot 3.3+ (Spring Web, Spring Security, Spring Data JPA)
* **Database**: PostgreSQL 16+
* **Build System**: Apache Maven 3.9+
* **Testing**: JUnit 5 (with 100% Mock-free unit testing for the domain core)

---

## 🚀 Getting Started

### 1. Build the project
Run the following command from the root directory to compile all sub-modules:
```bash
mvn clean package
```

### 2. Run the application
Locate the executable JAR in the infrastructure module and run it:
```bash
java -jar infrastructure/target/infrastructure-1.0.0-SNAPSHOT.jar
```

