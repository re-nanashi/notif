# 🚀 Notif: Order Tracking and Notification System

## Overview

Notif is a SaaS platform that provides structured order tracking for
businesses that sell primarily through Facebook Messenger.

Many small businesses in the Philippines operate entirely inside
Messenger without a formal order management system. There is no tracking
page, no structured status lifecycle, and no automated Notification
system. All updates are handled manually through chat.

Notif introduces a structured order lifecycle and automated
Messenger Notifications while preserving the Messenger-first selling
experience.

------------------------------------------------------------------------

# Problem

Small and medium-sized businesses selling via Messenger face:

-   No centralized order tracking
-   Manual and repetitive status updates
-   High customer inquiry volume ("Ano na po status?")
-   Errors in order updates
-   Lack of transparency for customers

There is no "Shopee-style tracking page" for Messenger-based sales.

------------------------------------------------------------------------

# Solution

Notif provides:

-   Web-based order management dashboard
-   Unique tracking number per order
-   Public order tracking page
-   Automated Messenger status Notifications
-   Structured order lifecycle management

### Workflow

1.  Business creates an order in the dashboard

2.  System generates a tracking number

3.  Customer receives confirmation via Messenger

4.  Business updates order status

5.  Customer automatically receives Messenger updates

6.  Customer can visit:

    https://yourdomain.com/track/{trackingNumber}

to view real-time order status

------------------------------------------------------------------------

# Architecture (Current Phase)

## Backend

-   Java
-   Spring Boot (Spring MVC)
-   PostgreSQL
-   JPA / Hibernate
-   REST APIs
-   Messenger Graph API Integration

## Architecture Style

-   Modular Monolith -> Microservices (future)
-   Domain-separated modules
-   Clean architecture boundaries
-   Synchronous REST communication

------------------------------------------------------------------------

# Core Modules

-   Order Management Module
-   Tracking Module
-   Notification Module
-   Audit / Logging Module

------------------------------------------------------------------------

# Order Lifecycle Example

-   PENDING
-   CONFIRMED
-   IN_PROGRESS
-   READY_FOR_PICKUP
-   OUT_FOR_DELIVERY
-   COMPLETED
-   CANCELLED

Each state transition: - Is logged - Triggers automated Messenger
Notification - Is auditable

------------------------------------------------------------------------

# Engineering Philosophy

This project serves two primary purposes:

## 1. Production SaaS Platform

-   To be used in my own business
-   To be offered to other small business owners

## 2. Engineering Mastery Project

-   Build core features manually to understand internals
-   Gradually migrate to industry-standard tools
-   Practice architectural evolution

Strategy: - Modular Monolith → Gradual Microservices Extraction - Custom
Auth → OAuth2 / Keycloak - On-Prem → Cloud (AWS) - Custom Gateway →
Industry-grade API Gateway - Sync Communication → Event-driven
(Kafka/RabbitMQ)

------------------------------------------------------------------------

# Performance & Scalability Considerations

-   Thread pool tuning
-   Asynchronous Notification dispatch
-   Idempotent status transitions
-   Optimistic locking on updates
-   Indexed tracking numbers
-   DTO projections to reduce memory footprint
-   Pagination for search endpoints

------------------------------------------------------------------------

# Roadmap / Feature Checklist

## SaaS Features (Planned)

-   [ ] Multi-tenant support
-   [ ] SMS fallback
-   [ ] Email Notifications
-   [ ] Analytics dashboard
-   [ ] Customer order history
-   [ ] Payment integration
-   [ ] Shipping integration
-   [ ] Subscription billing
-   [ ] Custom branded tracking pages

------------------------------------------------------------------------

# Versioning & Changelog

## v0.1.0 -- Initial Foundation (WIP)

-   Modular monolith structure
-   Order creation APIs
-   Tracking number generation
-   Basic Messenger Notification integration

------------------------------------------------------------------------

# Long-Term Vision

Notif aims to become the order infrastructure layer for
Messenger-first businesses --- bringing structured commerce capabilities
to informal digital sellers.

------------------------------------------------------------------------

#### 📜 License
This project is licensed under the [MIT License](LICENSE).
