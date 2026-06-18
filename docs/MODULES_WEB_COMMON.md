# Web-Common 模組 - Web 層共用基類與配置

## 📋 模組概述
**Web-Common 模組** 提供 Web 層的抽象與配置：
- 🎮 **抽象 REST 控制器**：統一的 HTTP 回應處理
- 🔐 **SSO 與安全**：認證、授權、角色管理
- 📚 **Swagger 配置**：API 文檔自動生成
- 🛡️ **API 安全**：API Key 驗證、安全頭設定

---

## 🏗️ 核心類別列表

### 1️⃣ REST 層基類

#### `AbstractRestController` 抽象基類
**功能**：統一的 HTTP 回應格式、異常處理、API 驗證

```java
@RestController
public abstract class AbstractRestController {
    
    @Autowired
    protected PrivilegeService privilegeService;
    
    // 詳細方法見下表
}
```

#### `RestHomeController` 首頁控制器
**功能**：應用首頁、健康檢查端點

---

### 2️⃣ HTTP 回應方法

#### 成功回應（HTTP 200）

| 方法 | 參數 | 回應格式 |
|------|------|--------|
| `ok(Object body)` | 數據 | `BaseReply` |
| `ok(Page<?> p)` | 分頁數據 | `BaseReply` + 分頁信息 |
| `ok(Object body, Paging paging)` | 數據 + 分頁 | `BaseReply` + 自訂分頁 |
| `ok(CustomResult<T> data)` | 自訂結果 | `CustomResult<T>` |

**使用範例**：
```java
@GetMapping("/campstores/{id}")
public Mono<ResponseEntity<BaseReply>> getCampsite(@PathVariable Long id) {
    Campsite campstore = service.findById(id).orElse(null);
    return ok(campstore);  // HTTP 200 + BaseReply 包裝
}

@GetMapping("/campstores")
public Mono<ResponseEntity<BaseReply>> list(Pageable pageable) {
    Page<Campsite> page = service.findAll(pageable);
    return ok(page);  // 自動包含分頁信息
}
```

#### 錯誤回應

| 方法 | HTTP 狀態 | 用途 |
|------|----------|------|
| `badRequest(SysCode code)` | 400 | 請求格式/驗證錯誤 |
| `badRequest(SysCode code, String info)` | 400 | 同上 + 詳細信息 |
| `syncBadRequest(SysCode code, String info)` | 400 | 同步版本（無 Mono） |
| `unauthorized(SysCode code)` | 401 | 未授權/令牌過期 |
| `unauthorized(SysCode code, String info)` | 401 | 同上 + 詳細信息 |
| `notFound(String info)` | 404 | 資源不存在 |
| `forbidden(String info)` | 403 | 禁止訪問 |

**使用範例**：
```java
@DeleteMapping("/{id}")
public Mono<ResponseEntity<BaseReply>> delete(@PathVariable Long id) {
    if (!service.existsById(id)) {
        return badRequest(SysCode.NOT_FOUND, "記錄不存在");
    }
    service.deleteById(id);
    return ok("刪除成功");
}
```

#### 文件下載

| 方法 | 用途 |
|------|------|
| `downloadFile(byte[], MediaType, String)` | 下載文件 |

**使用範例**：
```java
@GetMapping("/{id}/export")
public ResponseEntity<Resource> exportCampsite(@PathVariable Long id) {
    byte[] data = service.exportAsExcel(id);
    return downloadFile(
        data, 
        MediaType.APPLICATION_OCTET_STREAM, 
        "campsite_" + id + ".xlsx"
    );
}
```

#### 重導向

| 方法 | 用途 |
|------|------|
| `redirect(HttpServletResponse, String)` | HTTP 302 重導向 |

**使用範例**：
```java
@GetMapping("/auth/callback")
public Mono<ResponseEntity<?>> handleCallback(
    HttpServletResponse response,
    @RequestParam String code
) {
    String redirectUrl = "/dashboard?token=" + getToken(code);
    return redirect(response, redirectUrl);
}
```

#### API 安全驗證

| 方法 | 功能 |
|------|------|
| `checkApiKey(HttpServletRequest)` | 驗證 x-api-key header |

**使用範例**：
```java
@PostMapping("/protected-action")
public Mono<ResponseEntity<BaseReply>> protectedAction(
    HttpServletRequest request
) {
    checkApiKey(request);  // 驗證失敗拋異常
    // 執行受保護的操作
    return ok("操作成功");
}
```

---

### 3️⃣ SSO 與認證

#### 數據模型

| 類別 | 功能 |
|------|------|
| `SsoUserDetails` | SSO 用戶詳情封裝 |
| `CustomAccessToken` | 訪問令牌 DTO（包含權限、過期時間） |
| `CustomIdToken` | ID 令牌 DTO（用戶身份信息） |
| `TokenResp` | 令牌響應（access_token、id_token） |
| `Role` | 角色定義（name、permissions） |

#### 服務層

| 類別 | 功能 |
|------|------|
| `PrivilegeService` | 權限檢查接口 |
| `PrivilegeServiceImpl` | 權限檢查實現 |

**功能示例**：
```java
@Autowired
private PrivilegeService privilegeService;

public void checkPermission() {
    // 檢查用戶是否有特定權限
    if (!privilegeService.hasPermission(userId, "EDIT_CAMPSITE")) {
        throw new ForbiddenException("No permission");
    }
}
```

---

### 4️⃣ API 文檔配置

#### `SwaggerConfiguration` Swagger 配置類
**功能**：OpenAPI 3.0 文檔自動生成

**配置內容**：
- API 標題、版本、描述
- 服務器地址（dev、prod）
- 安全方案（API Key、Bearer Token）
- 聯繫信息、許可證

**訪問 Swagger UI**：
```
http://localhost:8080/swagger-ui.html
```

**功能**：
- ✅ 自動展示所有 REST 端點
- ✅ 請求/回應示例
- ✅ 參數驗證
- ✅ 在線測試 API
- ✅ 自動遵循 OpenAPI 3.0 規範

---

## 💼 完整使用範例

### 範例 1：創建自訂控制器

```java
@RestController
@RequestMapping("/api/campstores")
@Tag(name = "Campsite API", description = "營地管理 API")
public class CampsiteController extends AbstractRestController {
    
    @Autowired
    private CampsiteService service;
    
    @Autowired
    private CampsiteDtoConverter converter;
    
    @GetMapping("/{id}")
    @Operation(summary = "獲取營地詳情")
    public Mono<ResponseEntity<BaseReply>> getCampsite(@PathVariable Long id) {
        Campsite campstore = service.findById(id)
            .orElseThrow(() -> new CampNotFoundException("Campsite not found"));
        return ok(converter.convert(campstore));
    }
    
    @GetMapping
    @Operation(summary = "分頁查詢營地")
    public Mono<ResponseEntity<BaseReply>> list(
        @ParameterObject Pageable pageable
    ) {
        Page<Campsite> page = service.findAll(pageable);
        return ok(page);
    }
    
    @PostMapping
    @Operation(summary = "創建營地")
    public Mono<ResponseEntity<BaseReply>> create(
        @RequestBody CampsiteDTO dto,
        HttpServletRequest request
    ) {
        checkApiKey(request);  // API Key 驗證
        Campsite campstore = converter.reverse(dto);
        return ok(service.save(campstore));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "刪除營地")
    public Mono<ResponseEntity<BaseReply>> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ok("刪除成功");
    }
    
    @ExceptionHandler(ApiKeyInvalidException.class)
    public ResponseEntity<BaseReply> handleApiKeyError(ApiKeyInvalidException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(new BaseReply(SysCode.UNAUTHORIZED, e.getMessage(), null, null));
    }
}
```

### 範例 2：受保護的操作

```java
@RestController
@RequestMapping("/api/admin")
public class AdminController extends AbstractRestController {
    
    @PostMapping("/users/{id}/disable")
    public Mono<ResponseEntity<BaseReply>> disableUser(
        @PathVariable Long id,
        HttpServletRequest request
    ) {
        // 1. 驗證 API Key
        checkApiKey(request);
        
        // 2. 檢查權限
        String userId = getUserId(request);
        if (!privilegeService.hasPermission(userId, "ADMIN_DELETE_USER")) {
            return forbidden("No admin permission");
        }
        
        // 3. 執行操作
        userService.disableUser(id);
        return ok("用戶已禁用");
    }
}
```

### 範例 3：異常統一處理

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(CampNotFoundException.class)
    public ResponseEntity<BaseReply> handleNotFound(CampNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new BaseReply(SysCode.NOT_FOUND, e.getMessage(), null, null));
    }
    
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<BaseReply> handleForbidden(ForbiddenException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(new BaseReply(SysCode.FORBIDDEN, e.getMessage(), null, null));
    }
    
    @ExceptionHandler(SsoTokenExpiredException.class)
    public ResponseEntity<BaseReply> handleTokenExpired(SsoTokenExpiredException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(new BaseReply(SysCode.UNAUTHORIZED, "Token expired", null, null));
    }
}
```

---

## 🌐 API 文檔示例

### Swagger UI 頁面
```
http://localhost:8080/swagger-ui.html
```

### API 文檔 JSON
```
http://localhost:8080/v3/api-docs
```

### 詳細 API 文檔
```
http://localhost:8080/v3/api-docs/Campsite%20API
```

---

## 🔗 依賴關係

```
backend-app（應用層）
    ↓
@RestController（業務控制器）
    ↓ extends
AbstractRestController（基類）
├── ✅ 統一回應格式
├── ✅ API Key 驗證
├── ✅ 文件下載
├── ✅ 異常轉換
└── ✅ SSO 集成
    ↓ depends on
common（工具、異常）
base（業務邏輯）
config（配置）
    ↓
Swagger UI（API 文檔）
```

---

## ⚠️ 注意事項

1. **事務邊界**：Service 層標記 `@Transactional`，Controller 不需要
2. **異常轉換**：通過 `@ExceptionHandler` 統一轉換為 HTTP 狀態碼
3. **分頁查詢**：使用 Spring Data `Pageable`，自動映射到 `BaseReply` 的 `Paging`
4. **非同步回應**：使用 `Mono<ResponseEntity<>>` 支援 Project Reactor 響應式編程
5. **API Key 安全**：不要在日誌中輸出 API Key 的完整值（見代碼中截取前 10 字符）
6. **CORS 配置**：若需要跨域請求，在 WebMvcConfigurer 中配置 addCorsMappings
7. **文件下載**：設定正確的 Content-Type 和 Content-Disposition header

---

## 🚀 快速啟動 REST 服務

```java
// 1. 創建實體
@Entity
public class Campsite implements LoggedEntity { ... }

// 2. 創建 Service
@Service
public class CampsiteService extends CommonServiceImpl<Campsite, Long> { ... }

// 3. 創建 DTO 轉換器
@Component
public class CampsiteDtoConverter extends AbstractDtoConverter<Campsite, CampsiteDTO> { ... }

// 4. 創建 REST 控制器
@RestController
@RequestMapping("/api/campstores")
public class CampsiteController extends AbstractRestController {
    @GetMapping("/{id}")
    public Mono<ResponseEntity<BaseReply>> get(@PathVariable Long id) {
        return ok(service.findById(id).orElse(null));
    }
}

// 5. 訪問 Swagger UI
// http://localhost:8080/swagger-ui.html
```

完成！

