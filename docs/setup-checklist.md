## 🔑 1. Project Foundations
- [x] Choose stack (e.g., React + Node/Express, Next.js + Django/FastAPI, etc.) - [[300 Software Development/Projects/Notif/Project Overview|Project Overview]]
- [x] Initialize [version control](https://www.atlassian.com/git/tutorials) (`git init`, GitHub repo)
- [x] Add `.gitignore` for your language/framework
- [x] Decide repo structure (monorepo or separate frontend + backend)

---
## 💻 2. Backend Setup

- [x] Select backend framework (Express, Django, FastAPI, etc.)
- [x] Setup routing (REST or GraphQL)
- [x] Setup database (Postgres, MySQL, Mongo, etc.)
- [x] Integrate ORM/ODM (Prisma, Sequelize, SQLAlchemy, Django ORM)
- [x] Add config management (`.env`, `.env.example`)
- [ ] Implement authentication (JWT, session, or OAuth)
- [ ] Add error handling middleware
- [ ] Write basic tests (unit + integration)
- [ ] Generate API docs (Swagger / OpenAPI / Postman collection)

---
## 🎨 3. Frontend Setup

- [ ] Choose frontend framework (React, Next.js, Vue, Svelte, etc.)
- [ ] Create folder structure (`components`, `pages`, `hooks`, `styles`)
- [ ] Setup global styles/theme (Tailwind, SCSS, styled-components)
- [ ] Configure API client (Axios, fetch wrapper, React Query, etc.)
- [ ] Setup state management (Context API, Redux, Zustand, etc.)
- [ ] Handle authentication (tokens, refresh flow)
- [ ] Add error + loading states UI
- [ ] Write basic tests (unit with Jest, integration with Cypress/Playwright)

---
## 🧪 4. Quality & Automation

- [ ] Setup linter + formatter (ESLint, Prettier, Black, Flake8)
- [ ] Create [CI workflow](https://www.youtube.com/watch?v=iuNNbW7DvBU) (GitHub Actions / GitLab CI) → runs tests + lint
- [ ] Configure pre-commit hooks (Husky, lint-staged, pre-commit)
- [ ] Add code coverage reports

---
## 🐳 5. Environment & Deployment

- [x] Create Dockerfile for backend
- [ ] Create Dockerfile for frontend (or deploy with Vercel/Netlify)
- [x] Setup Docker Compose (backend + db + frontend if needed)
- [ ] Choose deployment platform:
	- [ ] Free/simple → Vercel, Netlify, Render, Railway
	- [ ] Advanced → AWS, GCP, Azure, DigitalOcean
- [ ] Setup staging & production environments (optional)

---
## 📊 6. Monitoring & Security

- [ ] Add logging (winston, loguru, etc.)
- [ ] Integrate error tracking (Sentry free tier)
- [ ] Enable dependency scanning (Dependabot, npm audit, pip-audit)
- [ ] Secure secrets (never commit `.env`)

---
## 📄 7. Documentation & Polish

- [ ] Write README with:
	- [ ] App description & features
	- [ ] Tech stack
	- [ ] Setup instructions
	- [ ] Example `.env`
	- [ ] Screenshots or GIF demo
- [ ] Add LICENSE (MIT for open source)
- [ ] Create CONTRIBUTING guide (optional)
- [ ] Maintain CHANGELOG (optional)
- [ ] Use project board/issues (optional)

---
# 🎯 Priority Levels

- ✅ **Must-have for portfolio**: Clean repo, README, 
	- [ ] lint + CI
	- [ ] auth + error handling
	- [ ] live demo
- ✨ **Nice-to-have**: 
	- [ ] Live demo
	- [ ] Docker
	- [ ] Automated deployment 
	- [ ] Monitoring/logging 
	- [ ] API docs

---