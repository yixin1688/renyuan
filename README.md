## 员工档案管理（Vue + Java + MySQL）

一个最小可运行的员工档案管理示例：前端 Vue3（Vite）+ Element Plus，后端 Spring Boot（Java）+ JPA。
默认支持本地开发模式，也支持使用 Docker 进行前后端与 MySQL 的整体展示部署。

### 目录结构

- `backend/`：Spring Boot 后端（员工 CRUD API）
- `frontend/`：Vue3 前端（列表/新增/编辑/删除）
- `docker-compose.yml`：前端 + 后端 + MySQL 整体编排
- `backend/Dockerfile`：后端镜像构建
- `frontend/Dockerfile`：前端镜像构建

### 1) Docker 一键启动展示环境

要求：本机已安装 Docker 与 Docker Compose。

在项目根目录执行：

```bash
docker compose up --build -d
```

启动后访问：

- 前端展示页：`http://localhost`
- 后端接口：`http://localhost:8081/api/employees`
- MySQL：`localhost:3306`

停止服务：

```bash
docker compose down
```

### 2) 单独启动 MySQL（可选）

在项目根目录执行：

```bash
docker compose up -d
```

MySQL 默认：
- host: `127.0.0.1`
- port: `3306`
- database: `employee_app`
- user: `app`
- password: `app123456`

### 3) 启动后端

要求：JDK 8+，Maven 3.9+。

默认使用 H2：

```bash
cd backend
mvn spring-boot:run
```

如需使用 MySQL：

```bash
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

后端默认地址：`http://localhost:8081`

H2 控制台：`http://localhost:8081/h2-console`

### 4) 启动前端

要求：Node.js 18+。

```bash
cd frontend
npm install
npm run dev
```

前端默认地址：`http://localhost:5173`

### API（后端）

- `GET /api/employees`：分页列表（支持按姓名/工号模糊搜索）
- `GET /api/employees/{id}`：详情
- `POST /api/employees`：新增
- `PUT /api/employees/{id}`：更新
- `DELETE /api/employees/{id}`：删除

### 已实现页面

- 员工列表
- 关键词搜索
- 新增员工
- 编辑员工
- 删除员工
- 分页浏览

### 容器化说明

- 前端容器基于 `nginx`，会自动把 `/api` 请求转发到后端容器
- 后端容器使用 `Spring Boot` 打包后的 jar 启动
- Docker 编排默认使用 MySQL，不使用 H2

