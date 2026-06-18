# Common 模組 - 共用工具與異常處理

## 📋 模組概述
**Common 模組** 提供跨專案的共用功能：
- 🛠️ **工具函數庫**：字符串、JSON、文件、IP 等操作
- ⚠️ **異常體系**：統一的異常定義與處理
- 🔄 **序列化適配**：JSON 日期時間適配
- 📦 **轉換框架**：DTO 轉換的抽象實現

---

## 🏗️ 核心類別分類

### 1️⃣ 回應格式

#### `CustomResult<R>` 通用回應類
**功能**：統一的 API 回應格式，包含成功標誌和數據

```java
@Data
@AllArgsConstructor(staticName = "result")
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomResult<R> {
    private boolean success;    // 是否成功
    private R result;           // 回應數據
    
    public boolean isNotSuccess() {
        return !success;
    }
}
```

**使用範例**：
```java
// 成功
CustomResult.result(true, campsiteData);

// 失敗
CustomResult.result(false, null);

// JSON 序列化
{
  "success": true,
  "result": { ... }
}
```

---

### 2️⃣ 工具函數庫

#### 字符串操作
| 工具類 | 功能 |
|--------|------|
| `CustomStringUtils` | 自訂字符串工具（格式化、驗證） |
| `StrUtils` | 基礎字符串處理（Null 安全、分割） |

#### JSON 與序列化（Gson 基礎）
| 工具類 | 功能 |
|--------|------|
| `JsonUtils` | Gson 序列化/反序列化（支援日期適配） |
| **適配器** | 下表 ↓ |

**日期/時間適配器**：

| 適配器名稱 | 功能 | 轉換方向 |
|-----------|------|--------|
| `LocalDateSerializeAdapter` | LocalDate → JSON | Java → JSON |
| `LocalDateDeserializeAdapter` | JSON → LocalDate | JSON → Java |
| `LocalDateTimeSerializeAdapter` | LocalDateTime → JSON | Java → JSON |
| `LocalDateTimeDeserializeAdapter` | JSON → LocalDateTime | JSON → Java |
| `LocalDateTimeToMillisecondSerializeAdapter` | LocalDateTime → 毫秒戳 | Java → JSON |
| `LocalDateTimeToMillisecondDeserializeAdapter` | 毫秒戳 → LocalDateTime | JSON → Java |

**使用範例**：
```java
// JSON 序列化示例
Campsite campstore = new Campsite();
campstore.setOpenDate(LocalDate.now());
String json = JsonUtils.toJson(campstore);
// JSON 中自動轉換為日期格式

// JSON 反序列化示例
String json = "{\"openDate\": \"2026-05-06\", ...}";
Campsite campstore = JsonUtils.fromJson(json, Campsite.class);
// 自動轉換為 LocalDate
```

#### 文件操作
| 工具類 | 功能 |
|--------|------|
| `FileUtils` | 文件讀寫、ClassPath 資源、目錄管理 |

**常用方法**：
```java
// 從 ClassPath 讀取文件
String content = FileUtils.resourceToString("/templates/test.html");

// 保存文件到本地
File file = FileUtils.saveToFile(data, "/tmp", "file.txt");

// 創建目錄
boolean success = FileUtils.createFolder("/tmp/folder");

// 讀取資源流
InputStream stream = FileUtils.resourceToInputStream("/config.json");
```

#### 網絡操作
| 工具類 | 功能 |
|--------|------|
| `IpAddressUtils` | IP 地址識別、獲取客戶端 IP |
| `HttpClientUtils` | HTTP 客戶端（基於 Apache HttpClient5） |

**使用範例**：
```java
// 獲取客戶端 IP
String clientIp = IpAddressUtils.getClientIp(request);

// HTTP 請求
String response = HttpClientUtils.doGet("https://api.example.com/data");
String postResponse = HttpClientUtils.doPost(url, data);
```

#### 實體/數據操作
| 工具類 | 功能 |
|--------|------|
| `EntityUtils` | 實體反射操作、字段提取、值轉換 |
| `StringCompressor` | 字符串壓縮/解壓（GZIP、Zstd） |

**字符串壓縮範例**：
```java
// GZIP 壓縮
String originalText = "這是需要壓縮的文本";
String compressed = StringCompressor.compress(originalText);

// GZIP 解壓
String decompressed = StringCompressor.decompressToString(compressed);

// Zstd 壓縮（效率更高）
String zstdCompressed = StringCompressor.compressZstd(originalText);
String zstdDecompressed = StringCompressor.decompressZstdToString(zstdCompressed);
```

#### 日誌操作
| 工具類 | 功能 |
|--------|------|
| `LogUtils` | 動態日誌輸出（根據 Profile 決定是否輸出） |

**使用範例**：
```java
// 僅在開發環境輸出
LogUtils.devLogInfo("Debug info: {}", value);

// 僅在非生產環境輸出
LogUtils.notProdLogWarn("Warning: {}", message);
```

---

### 3️⃣ 異常體系

#### 基礎異常類
| 異常類 | HTTP 狀態 | 用途 |
|--------|---------|------|
| `RestCustomException` | 自訂 | 自訂 REST 異常（包含 HTTP 狀態碼） |
| `CampNotFoundException` | 404 | 端點不存在 |
| `ForbiddenException` | 403 | 禁止訪問 |
| `DtoConverterException` | 500 | DTO 轉換錯誤 |

**使用範例**：
```java
@ExceptionHandler(CampNotFoundException.class)
public ResponseEntity<ErrorResponse> handleNotFound(CampNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ErrorResponse("Endpoint not found", e.getMessage()));
}
```

#### SSO 相關異常

| 異常類 | HTTP 狀態 | 用途 |
|--------|---------|------|
| `SysCodeException` | 400 | 帶系統碼的業務異常 |
| `SsoTokenExpiredException` | 401 | SSO 令牌過期 |
| `ApiKeyInvalidException` | 401 | API 金鑰無效 |
| `TokenRequestException` | 401 | 令牌請求失敗 |

**使用範例**：
```java
public void validateToken(String token) {
    if (isExpired(token)) {
        throw new SsoTokenExpiredException("Token expired");
    }
    if (isInvalid(token)) {
        throw new ApiKeyInvalidException("Invalid API key");
    }
}
```

---

### 4️⃣ DTO 轉換框架

#### 抽象轉換基類
| 類別 | 功能 |
|------|------|
| `AbstractDtoConverter<S, T>` | DTO 轉換抽象基類 |
| `CustomDtoConverter<S, T>` | 轉換接口 |

**功能特性**：
- ✅ 支援單一對象轉換
- ✅ 支援 List<S> ↔ List<T> 轉換
- ✅ 支援反向轉換
- ✅ 基於 ModelMapper 自動映射
- ✅ 支援自訂轉換邏輯

**使用範例 1：基本 DTO 轉換**
```java
@Component
public class CampsiteDtoConverter extends AbstractDtoConverter<Campsite, CampsiteDTO> {
    
    // 繼承後自動獲得 convert() 和 reverse() 方法
    // 默認使用 ModelMapper 自動映射
}

// 使用
@Autowired
private CampsiteDtoConverter converter;

public void test() {
    Campsite campstore = new Campsite(...);
    CampsiteDTO dto = converter.convert(campstore);
    
    // 反向轉換
    Campsite restored = converter.reverse(dto);
}
```

**使用範例 2：自訂轉換邏輯**
```java
@Component
public class ReviewDtoConverter extends AbstractDtoConverter<Review, ReviewDTO> {
    
    @Autowired
    private UserService userService;
    
    @Override
    protected ReviewDTO doConvert(Review source) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(source.getId());
        dto.setRating(source.getRating());
        // 自訂邏輯：獲取用戶信息
        User author = userService.findById(source.getAuthorId());
        dto.setAuthorName(author.getName());
        return dto;
    }
    
    @Override
    protected Review doReverse(ReviewDTO target) {
        Review review = new Review();
        review.setId(target.getId());
        review.setRating(target.getRating());
        // 自訂反向邏輯
        return review;
    }
}
```

**使用範例 3：List 轉換**
```java
// 轉換列表
List<Campsite> campstores = campsiteService.findAll();
List<CampsiteDTO> dtos = campstores.stream()
    .map(converter::convert)
    .collect(Collectors.toList());
```

---

## 💼 整合使用範例

### 完整場景：API 端點實現

```java
@RestController
@RequestMapping("/api/campstores")
public class CampsiteController {
    
    @Autowired
    private CampsiteService service;
    
    @Autowired
    private CampsiteDtoConverter converter;
    
    @GetMapping("/{id}")
    public CustomResult<CampsiteDTO> getCampsite(@PathVariable Long id) {
        try {
            Campsite campstore = service.findById(id)
                .orElseThrow(() -> new CampNotFoundException("Campsite not found"));
            CampsiteDTO dto = converter.convert(campstore);
            return CustomResult.result(true, dto);
        } catch (CampNotFoundException e) {
            return CustomResult.result(false, null);
        }
    }
    
    @PostMapping("/{id}/export")
    public ResponseEntity<byte[]> export(@PathVariable Long id) throws IOException {
        Campsite campstore = service.findById(id).orElseThrow();
        String json = JsonUtils.toJson(campstore);
        
        // 壓縮數據
        String compressed = StringCompressor.compressZstd(json);
        
        return ResponseEntity.ok()
            .header("Content-Type", "application/octet-stream")
            .body(compressed.getBytes(StandardCharsets.UTF_8));
    }
}
```

---

## 📊 依賴關係

```
所有模組
    ↓
Common 工具庫
├── JsonUtils + Adapters（日期序列化）
├── FileUtils（文件操作）
├── StringCompressor（數據壓縮）
├── HttpClientUtils（網絡操作）
├── EntityUtils（反射操作）
├── CustomResult（API 回應）
├── 異常體系（統一錯誤處理）
└── DTO 轉換框架（跨層數據轉換）
    ↓
web-common 和 backend-app
```

---

## ⚠️ 注意事項

1. **序列化**：Gson 已自動配置日期適配器，無需額外設定
2. **DTO 轉換**：使用 Stream API 轉換列表時，優先用 ModelMapper 的 map 方法
3. **文件操作**：ClassPath 資源適用於打包後的靜態文件
4. **壓縮選擇**：Zstd 壓縮率和速度優於 GZIP，推薦用於緩存
5. **異常設計**：為異常提供有意義的錯誤訊息，便於調試

