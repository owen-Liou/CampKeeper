# CampKeeper 多模組架構指南

## 📋 專案概述

CampKeeper 採用標準的 Spring Boot 多模組架構，遵循分層設計原則：

```
CampKeeper (Parent POM)
├── config          🔧 配置中心、全域常量
├── base            🗂️ 核心實體層、通用服務
├── common           🛠️ 共用工具、異常、轉換
├── web-common       🎮 Web 層基類、REST 基礎
├── model            📊 數據模型（可選拆分）
└── backend-app      🚀 Spring Boot 啟動應用
```

---

## 🏗️ 四層架構設計

```
┌─────────────────────────────────────────────────────┐
│         backend-app (App Module)                    │
│  Spring Boot 啟動應用、路由、定時任務               │
└────────────┬────────────────────────────────────────┘
             │
┌─────────────▼────────────────────────────────────────┐
│   web-common (Web Layer)                            │
│  REST 基類、API 文檔、SSO 認證、文件下載            │
└────────────┬────────────────────────────────────────┘
             │
┌─────────────▼────────────────────────────────────────┐
│   個業務模組 (Service Layer)                         │
│  - CampsiteService extends CommonServiceImpl          │
│  - ReviewService extends CommonServiceImpl            │
│  - UserService extends CommonServiceImpl              │
└────────────┬────────────────────────────────────────┘
             │
┌─────────────▼────────────────────────────────────────┐
│   base (Base Layer)                                 │
│  CommonService 抽象、CRUD 實現、日期轉換器          │
└────────────┬────────────────────────────────────────┘
             │
         ┌───┴─────┬────────────┬─────────────┐
         │         │            │             │
┌────────▼──┐ ┌────▼───┐ ┌─────▼──┐ ┌──────▼─┐
│  common   │ │ config │ │ model  │ │PostgreSQL
│  工具庫   │ │ 配置   │ │ 實體   │ │ 數據庫
└───────────┘ └────────┘ └────────┘ └────────┘
```

---

## 🔄 數據流向示例：新增營地

```
1. 用戶發送請求
   POST /api/campstores
   {
     "name": "山林營地",
     "openDate": "2026-05-06",
     "isActive": true
   }
   ↓

2. backend-app 路由到 CampsiteController
   @RestController @RequestMapping("/api/campstores")
   ↓

3. CampsiteController extends AbstractRestController
   @PostMapping
   public Mono<ResponseEntity<BaseReply>> create(@RequestBody CampsiteDTO dto) {
       Campsite campstore = converter.reverse(dto);  // DTO → Entity
       Campsite saved = service.save(campstore);
       return ok(saved);  // 自動包裝為 HTTP 200 + BaseReply
   }
   ↓

4. CampsiteService extends CommonServiceImpl<Campsite, Long>
   public Campsite save(Campsite entity) {
       // 自動執行事務管理 @Transactional
       return repository.save(entity);
   }
   ↓

5. CustomRepository<Campsite, Long> extends JpaRepository
   直接調用 JPA 提供的 save() 方法
   ↓

6. Campsite 實體
   @Entity @Table(name = "campstores")
   @Convert(converter = DatePattern2Converter.class)
   LocalDate openDate;  // 數據庫: yyyy-MM-dd
   
   openDate 自動轉換為 yyyy-MM-dd 格式存儲
   ↓

7. PostgreSQL 數據庫
   INSERT INTO campstores(name, open_date, is_active, created_date, updated_date)
   VALUES('山林營地', '2026-05-06', 1, NOW(), NOW());
   
   ✅ 完成！
```

---

## 📚 模組功能對應表

| 模組 | 層級 | 核心功能 | 主要類別 |
|------|------|--------|---------|
| **base** | 業務邏輯層 | CRUD 通用實現、日期轉換 | CommonService、LoggedEntity |
| **config** | 配置層 | 環境管理、全域常量 | Profiles、DEFAULT_SETTINGS |
| **common** | 工具層 | 工具函數、異常、轉換 | JsonUtils、FileUtils、CustomResult |
| **web-common** | 表現層 | REST 基類、API 文檔 | AbstractRestController、SwaggerConfig |
| **backend-app** | 應用層 | 啟動入口、業務邏輯 | Application、@RestController |
| **model** | 數據層 | 實體定義（可選） | Entity、@Entity |

---

## 🎯 快速開發流程

### 步驟 1：定義實體（model or base）

```java
@Entity
@Table(name = "campstores")
@Data
@Builder
public class Campsite implements LoggedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    @Convert(converter = DatePattern2Converter.class)
    private LocalDate openDate;  // 自動格式化
    
    private LocalDateTime createdDate;   // 自動審計
    private LocalDateTime updatedDate;
}
```

### 步驟 2：創建 Repository

```java
@Repository
public interface CampsiteRepository extends CustomRepository<Campsite, Long> {
    List<Campsite> findByRegion(String region);
}
```

### 步驟 3：創建 Service

```java
@Service
public class CampsiteService extends CommonServiceImpl<Campsite, Long> {
    @Autowired
    private CampsiteRepository repository;
    
    // 自動獲得 CRUD 功能
    
    public List<Campsite> getByRegion(String region) {
        return repository.findByRegion(region);
    }
}
```

### 步驟 4：創建 DTO 轉換器

```java
@Component
public class CampsiteDtoConverter extends AbstractDtoConverter<Campsite, CampsiteDTO> {
    // 默認使用 ModelMapper 自動映射
}
```

### 步驟 5：創建 REST 控制器

```java
@RestController
@RequestMapping("/api/campstores")
public class CampsiteController extends AbstractRestController {
    
    @Autowired
    private CampsiteService service;
    
    @Autowired
    private CampsiteDtoConverter converter;
    
    @GetMapping("/{id}")
    public Mono<ResponseEntity<BaseReply>> get(@PathVariable Long id) {
        Campsite campstore = service.findById(id).orElseThrow();
        return ok(converter.convert(campstore));
    }
    
    @PostMapping
    public Mono<ResponseEntity<BaseReply>> create(@RequestBody CampsiteDTO dto) {
        Campsite campstore = converter.reverse(dto);
        return ok(service.save(campstore));
    }
}
```

### 步驟 6：啟動應用

```bash
# 本機開發
java -jar backend-app.jar --spring.profiles.active=dev

# Docker
docker run -e SPRING_PROFILES_ACTIVE=lab campkeeper-app
```

---

## 🌍 環境配置

### application.yml（backend-app/src/main/resources/）

```yaml
spring:
  application:
    name: backend-app
  profiles:
    active: dev
  datasource:
    url: jdbc:postgresql://localhost:5432/campkeeper
    username: postgres
    password: password
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false

server:
  port: 8080
  servlet:
    context-path: /backend-app

logging:
  level:
    root: INFO
    com.github.owenliou.campkeeper: DEBUG
```

---

## 📊 依賴樹

```
backend-app
├── web-common
│   ├── common
│   │   ├── base
│   │   │   ├── config
│   │   │   └── model
│   │   └── jackson、gson、commons-lang
│   └── spring-boot-starter-web、swagger-ui
├── config
└── base
```

---

## 💡 架構優勢

✅ **分層清晰**：每層職責明確  
✅ **高復用性**：通用層提取共用功能  
✅ **易於測試**：層與層之間依賴注入  
✅ **易於擴展**：新增業務邏輯無需修改基層  
✅ **規範統一**：日期格式、異常處理、API 回應統一  
✅ **多環境支持**：Profile 管理不同環境配置  

---

## 🚀 API 文檔訪問

啟動應用後，訪問以下地址查看 API 文檔：

```
Swagger UI:  http://localhost:8080/backend-app/swagger-ui.html
OpenAPI JSON: http://localhost:8080/backend-app/v3/api-docs
```

---

## 📖 詳細模組文檔

- [📘 Base 模組詳解](./MODULES_BASE.md) - 核心實體層、通用服務
- [⚙️ Config 模組詳解](./MODULES_CONFIG.md) - 環境配置、全域常量
- [🛠️ Common 模組詳解](./MODULES_COMMON.md) - 工具、異常、轉換
- [🎮 Web-Common 模組詳解](./MODULES_WEB_COMMON.md) - REST 基類、API 文檔

---

## ⚠️ 常見注意事項

1. **事務邊界**：@Service 層才標記 @Transactional
2. **日期格式**：使用 DEFAULT_SETTINGS 中定義的格式器
3. **異常捕獲**：Controller 層通過 @ExceptionHandler 統一轉換
4. **API Key**：通過 checkApiKey() 驗證敏感操作
5. **分頁查詢**：使用 Spring Data Pageable，自動映射到 BaseReply
6. **DTO 轉換**：使用 AbstractDtoConverter，支援雙向轉換

---

## 🔗 相關資源

- Spring Boot 官方文檔：https://spring.io/projects/spring-boot
- Spring Data JPA：https://spring.io/projects/spring-data-jpa
- Swagger/OpenAPI：https://swagger.io/
- PostgreSQL：https://www.postgresql.org/

---

## 📞 支持

如有問題，請參考各模組詳細文檔或查阅源代碼註解。

