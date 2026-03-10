Basic Authorization for User.

--- 
# TODO

- [ ] **Authorization Fundamentals**
	- [ ] [Spring Security Basics](https://www.toptal.com/developers/spring/spring-security-tutorial)
	- [ ] [Role-based Authentication](https://www.geeksforgeeks.org/advance-java/spring-security-role-based-authentication/)
	- [ ] [Spring Security Registration: Roles and Priveleges](https://www.baeldung.com/role-and-privilege-for-spring-security-registration)
- [ ] **Authorization Model + Implementation**
	- [ ] Role-Based (RBAC) 
		- [ ] USER
		- [ ] ADMIN
		- [ ] MODERATOR
	- [ ] Attribute-based (ABAC)
		- [ ] user.department == "HR"
		- [ ] user.id == resource.ownerID
	- [ ] Scope-based (ABAC)
		- [ ] read:users 
		- [ ] write:orders
		- [ ] Users should use /me for current user operations and /users/id for admin-level
		- [ ] Implement Roles
		- [x] Implement DataInitializer we should have 1 administrator 
		- [ ] Implement only being able to access own data [refer here](https://stackoverflow.com/questions/51712724/how-to-allow-a-user-only-access-their-own-data-in-spring-boot-spring-security)
		- [ ] Only connected users can update their own data (profile, password, etc). Admin can update anyone's profile.  
		- [ ] We should check the ID on the endpoint and check if the connected user has the same ID.
		- [ ] This is a multitenant app so we should look into MultiTenant application implementations. [[MultiTenant Ref]]

---
# General Notes

---
# Daily Development Artifacts