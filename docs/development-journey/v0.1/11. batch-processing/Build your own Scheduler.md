Microservices basics and tutorials + Implementation.

--- 
# TODO

- [ ] **Tutorials**
	- [ ] [Guide for Beginners](https://www.youtube.com/watch?v=FDjmWxETjn8)
- [ ] **Basic Implementation
	- [ ] Delete devices last seen 90 days ago
	- [ ] Delete users inactive for 3 years
	- [ ] Delete revoked tokens (expired, revoked)
		- [ ] Verification Tokens
		- [ ] Refresh Tokens
	- [ ] Delete completed sessions
	- [ ] Sample Deletion


---
# General Notes

## Sample Batch Deletion Snippet:

```
// Scheduled job — run nightly
@Scheduled(cron = "0 0 2 * * *")  // 2am every day
@Transactional
public void cleanUpExpiredSessions() {
    sessionRepository.deleteByStatusAndLastActiveAtBefore(
        SessionStatus.REVOKED,
        Instant.now().minus(Duration.ofDays(30))
    );
}
```

---
# Daily Development Artifacts