
**Notif** is a social media application designed to help people become more mindful of how limited their time is. The app uses a life expectancy countdown to remind users that life is finite and encourages them to create meaningful experiences with the people they care about.

Users can build personal and shared **bucket lists**, track experiences by posting photos or videos, and collaborate with friends to complete activities together. Notif also recommends places and activities nearby—such as cafés, gyms, or local experiences—when users need inspiration or help deciding what to do next.

---
# Technical Description

**Notif** is a social media platform that encourages intentional living by combining a life expectancy countdown with collaborative bucket lists. Users can create personal or shared goals, document experiences through media posts, and receive location-based recommendations for activities and places nearby.

##### Notif Stack
- Frontend: 
	- React 
	- Tailwind CSS
- Backend: SpringBoot
- Database: PostgreSQL
- API: REST
- Deployment: To be determined

---
# Backend Features

- Authentication
- Social graph (friends)
- Media uploads
- Notifications
- Recommendation systems from processed data
- Location-based queries
- Event feeds

##### Some features to look into:
- Real-time data pipeline feeding a metrics API
- Task scheduler service (like a mini Airflow clone)

---
# Feature Roadmap

v0.1: 
- [x] Basic User Management System + Registration
- [ ] Custom Authentiation
- [ ] 1 Core Module (+ implement logging)
- [ ] 1 Core Module (+ implement authorization)
- [ ] Custom API Gateway (+ implement simple load balancing)
- [ ] Custom Cache Layer (+ implement in-memory rate-limiter)
- [ ] OpenAPI Integration
- [ ] Github Actions Workflow Integration
- [ ] Testing Integration (JUnit, Mockito)
- [ ] Batch Processing + Job Scheduling

v0.2: 
- [ ] API Gateway (Kong Gateway)
- [ ] Migrate to Microservices (gRPC)
- [ ] KeyCloak Authentication
- [ ] Messaging Queues (RabbitMQ)
- [ ] Build your own Kafka

v0.3: 
- [ ] Distributed Rate-limiting in API Gateway (e.g., Kong, AWS API Gateway) or Reverse Proxy (e.g., Ngix) backed by a distributed in-memory cache (Redis)
- [ ] Notification module (web-sockets)
- [ ] Analytics (Kafka)

v0.4: 
- [ ] Implement a file storage solution
- [ ] Cloudification (AWS)
- [ ] Build your own CDN

v0.5:
- [ ] Basic Front-end

v1.0:
- [ ] MVP

---
# TODOs

### Main System Development
#### v0.1
- [x] **01. User Management System**
	- [x] [[Basic User Management and Registration]]
- [ ] **02. Custom Authentication**
	- [x] [[Basic Token Management]]
	- [ ] [[Multi-user and Multi-device Token Management]]
	- [ ] [[Robust Auth Implementation]] + [[Multi-user per device, Multi-device per user,  refresh token implementation]]
	- [ ] [[Authorization Model]]
	- [ ] [[Authentication Flows + Basic Account Security]]
	- [ ] [[Session Management and Admin Capabilities]]
	- [ ] [[OAuth and Standards Support]]
- [ ] **03. Core Module (+ implement logging)**
	- [ ] Modify error messages to only return generic messages. Logging errors should be internal.
- [ ] **04. Core Module (+ implement authorization)**
	- [ ] Implementing Role Repositories vs Enums
- [ ] **05. Custom API Gateway (+ implement simple load balancing)**
	- [ ] [[Build your own API Gateway]]
- [ ] **06. Custom Cache Layer (+ implement in-memory rate-limiter)**
	- [ ] [[Build your own Redis]]
- [ ] **07. OpenAPI Integration**
- [ ] **08. Github Actions Workflow Integration**
- [ ] **09. OpenAPI Integration**
- [ ] **10. Testing Integration (JUnit, Mockito)**
- [ ] **11. Batch Processing**
	- [ ] [[Build your own Scheduler]]
#### v0.2
- [ ] **01. API Gateway (Kong, Nginx, Spring Cloud Gateway)**
- [ ] **02. Migrate to Microservices (gRPC)**
	- [ ] [[Microservices Fundamentals]]
- [ ] **03. Keycloack Authentication**
- [ ] **04. Messaging Queues (RabbitMQ)**
- [ ] **05. Custom Event Processing**
	- [ ] [[Build your own Kafka]]
#### v0.5
- [ ]  **01. Basic Frontend**
	- [ ] [Silent Authentication](https://auth0.com/docs/authenticate/login/configure-silent-authentication)
	- [ ] Chat Feature (WebSockets)
	
### Best Practices
- [ ] [JWT Security Best Practices](https://curity.io/resources/learn/jwt-best-practices/)
- [ ] Rotate the secret key (gracefully handle rotation)
	- Better if we already implemented Microservices
- [ ] Rate limit
	- [ ] Logging In
	- [ ] Verification Email Request
### Backlogs
- [ ] Update LocalDateTime.now() -> Instant.now()
- [ ] Do not call save if a JPA managed entity
- [ ] Comment Exceptions in core-module
- [ ] VerificationToken should be hashed
- [ ] User soft deletion
- [ ] Add "instance": "path" to ApiError
- [ ] Implement better UI for emails.
- [ ] mitigate XFF errors on our edge Proxy, have a trusted list of proxies on our server

--- 
