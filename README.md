# 问答论坛 (QA Forum)

一个基于Spring Boot构建的在线问答平台，用户可以在上面进行提问和讨论。

## 🚀 功能特性

### 核心功能
- ✅ 用户注册与登录（Spring Security）
- ✅ 创建讨论话题（多行文本，支持正常换行显示）
- ✅ 查看所有讨论信息列表
- ✅ 查看讨论详情及相关回复
- ✅ 对讨论话题进行回复（可以多个回复）
- ✅ 每个讨论话题是一个独立的thread

### 技术特性
- ✅ 基于Spring MVC架构
- ✅ 使用Spring IoC容器
- ✅ 无I/O操作（符合要求）
- ✅ 合适的数据库存储方案（开发环境：H2，生产环境：MySQL）
- ✅ 安全登录机制
- ✅ 页面美化（Bootstrap 5 + Font Awesome）

## 🛠️ 技术栈

### 后端
- **Spring Boot 2.7.0**
- Spring MVC
- Spring Data JPA
- Spring Security
- Hibernate
- H2 Database（开发）
- MySQL（生产）
- Java 17

### 前端
- Thymeleaf模板引擎
- Bootstrap 5
- Font Awesome 6
- JavaScript

### 开发工具
- Maven
- Spring Boot DevTools
- IntelliJ IDEA

## 📁 项目结构
📁 项目结构
源代码结构
config/ - 配置类

SecurityConfig.java - Spring Security安全配置

controller/ - 控制器层

HomeController.java - 首页控制器

UserController.java - 用户控制器（登录、注册）

DiscussionController.java - 讨论控制器

service/ - 服务层

UserService.java - 用户服务

DiscussionService.java - 讨论服务

repository/ - 数据访问层

UserRepository.java - 用户数据访问

DiscussionRepository.java - 讨论数据访问

ReplyRepository.java - 回复数据访问

model/ - 实体类

User.java - 用户实体

Discussion.java - 讨论实体

Reply.java - 回复实体

dto/ - 数据传输对象

RegisterDTO.java - 注册数据传输对象

资源文件结构
templates/ - 页面模板

layouts/base.html - 基础布局模板

user/login.html - 登录页面

user/register.html - 注册页面

discussions/list.html - 讨论列表页面

discussions/view.html - 讨论详情页面

discussions/new.html - 新建讨论页面

index.html - 首页

static/ - 静态资源

css/custom.css - 自定义样式

配置文件

application.properties - 主配置文件

application-dev.properties - 开发环境配置

application-prod.properties - 生产环境配置
## 🗄️ 数据库设计

### 用户表 (users)
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    avatar VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
##讨论表 (discussions)
CREATE TABLE discussions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    user_id BIGINT NOT NULL,
    view_count INTEGER DEFAULT 0,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
回复表 (replies)
CREATE TABLE replies (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    content TEXT NOT NULL,
    discussion_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (discussion_id) REFERENCES discussions(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

环境要求
JDK 17+

Maven 3.6+

MySQL 8.0+（生产环境）

Git
