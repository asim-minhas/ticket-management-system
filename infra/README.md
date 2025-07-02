# 📦 Ticket Management System - Infrastructure Setup

This directory contains the infrastructure configuration for the Ticket Management System microservices project. All services are containerized using **Docker** and orchestrated via **Docker Compose** for local development and testing.

---

## 📑 Overview

The system includes:
- **6 Spring Boot microservices**
- **6 MongoDB instances** (one per service)
- **MySQL instance** for relational data
- **RabbitMQ** as a message broker
- **Docker volumes** for persistent storage
- **Docker networks** for inter-service communication

---

## 📁 Project Structure
infra-service/
├── docker-compose.yml # Main compose configuration
├── .env # Environment variable definitions
├── README.md # Infrastructure usage guide

---

## 📌 Services Defined

| Service Name          | Description                           | Port(s)            |
|:---------------------|:--------------------------------------|:------------------|
| **auth-service**        | Authentication and user management    | 9000               |
| **ticketing-service**   | Ticket management service             | 9001               |
| **notification-service**| Notification delivery service         | 9002               |
| **blocker-service**     | Blocker tracking and management       | 9003               |
| **reporting-service**   | SLA reporting and analytics           | 9004               |
| **client-service**      | Client management service             | 9005               |
| **MongoDB (x6)**         | One database container per service    | 27017–27022        |
| **MySQL**               | Relational database                   | 3306               |
| **RabbitMQ**            | Message broker and management console | 5672 / 15672       |

---

## 📦 Volumes

Named Docker volumes are defined for all database services to persist data between container restarts.

---

## 🌐 Networks

All services are connected via a custom **`ticket-system-network`** Docker network for inter-service communication.

---

## 📖 Usage

### 📥 Build and Run Services

```bash
docker-compose --env-file .env up --build -d
```
This command:
- Builds Docker images for app services
- Pulls images for infrastructure services
- Starts containers in detached mode
- Injects environment variables from `.env`
- Runs healthchecks for infrastructure containers
