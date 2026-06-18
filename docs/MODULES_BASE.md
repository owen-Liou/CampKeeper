# Base 模組 - 核心資料庫層與業務邏輯層

## 📋 模組概述
**Base 模組** 是專案的核心層，提供：
- 🗂️ **JPA 實體基類與轉換器**：統一的數據庫映射
- 🔧 **通用服務層**：CRUD 操作的抽象實現
- 📅 **日期時間轉換**：統一的日期格式處理

---

## 🏗️ 模組結構與核心類別

### 1️⃣ 實體層（Model Domain）

#### `LoggedEntity` 接口
**功能**：提供記錄建立/修改時間的統一標準
```java
public interface LoggedEntity extends Serializable {
    LocalDateTime getCreatedDate();        // 資料建立時間
    void setCreatedDate(LocalDateTime);
    
    LocalDateTime getUpdatedDate();        // 資料最後修改時間
    void setUpdatedDate(LocalDateTime);
}
```

**使用場景**：
- 所有需要追蹤審計日誌的實體應實現此接口
- 自動記錄資料的創建與修改時間

#### `LoggedEntityImpl` 實現
統一的實現類，提供預設的 created/updated 時間戳

---

### 2️⃣ 數據存取層（Repository）

#### `CustomRepository<T, ID>` 接口
**功能**：延伸 Spring Data JPA Repository，提供通用查詢功能
```java
// 基於 JpaRepository，支援所有基本 CRUD 操作
List<T> findAll();
Page<T> findAll(Pageable pageable);
List<T> findAll(Sort sort);
```

---

### 3️⃣ 業務邏輯層（Service）

#### `CommonService<T, ID>` 接口
**功能**：提供通用的業務邏輯操作接口

| 功能群 | 方法 | 說明 |
|--------|------|------|
| **新增** | `save(S entity)` | 保存單一實體 |
| | `saveAll(Iterable<S>)` | 批量保存 |
| **查詢** | `findById(ID id)` | 根據 ID 查詢 |
| | `getOne(ID id)` | 獲取實體引用 |
| | `findAll()` | 查詢全部 |
| | `findAll(Pageable)` | 分頁查詢 |
| | `findAll(Sort)` | 排序查詢 |
| | `findAllById(Iterable<ID>)` | 批量查詢 |
| **刪除** | `deleteById(ID id)` | 根據 ID 刪除 |
| | `delete(T entity)` | 刪除實體 |
| | `deleteAll(Iterable)` | 批量刪除 |
| | `deleteAll()` | 全部刪除 |
| **其他** | `count()` | 計數 |
| | `existsById(ID)` | 檢查是否存在 |
| | `flush()` | 刷新到數據庫 |
| | `saveAndFlush(T)` | 保存並同步 |

#### `CommonServiceImpl<T, ID>` 實現
**功能**：通用業務邏輯的預設實現

```java
@Service
@Transactional
public abstract class CommonServiceImpl<T, ID extends Serializable> 
    implements CommonService<T, ID> {
    
    @Autowired
    private CustomRepository<T, ID> repository;
    
    // 所有通用操作都代理給 repository
}
```

---

### 4️⃣ 數據庫轉換層（Converter）

提供 JPA `@Convert` 註解用的轉換器，實現 Java 類型 ↔ 數據庫類型的轉換

#### 日期/時間轉換器

| 轉換器 | 格式 | 用途 |
|--------|------|------|
| `DatePattern1Converter` | `yyyy/MM/dd` | 日期（斜線） |
| `DatePattern2Converter` | `yyyy-MM-dd` | 日期（連字符） |
| `DatePattern3Converter` | `yyyyMMdd` | 日期（緊湊格式） |
| `DateTimePattern1Converter` | `yyyy/MM/dd HH:mm:ss` | 日期時間（斜線） |
| `DateTimePattern2Converter` | `yyyy-MM-dd HH:mm:ss` | 日期時間（連字符） |

**使用方式**：在實體上標記轉換器
```java
@Entity
@Table(name = "campstores")
public class Campsite {
    @Convert(converter = DatePattern2Converter.class)
    private LocalDate openDate;  // 數據庫存儲為 yyyy-MM-dd
}
```

#### 布林值轉換器

| 轉換器 | 轉換方式 |
|--------|--------|
| `BooleanToIntConverter` | `true → 1`, `false → 0` |
| `BooleanToStringConverter` | `true → "Y"`, `false → "N"` |
| `BooleanToStringIntConverter` | `true → "1"`, `false → "0"` |

#### 其他轉換器
- `IntToStringConverter`：整數 ↔ 字符串

---

## 💡 使用範例

### 範例 1：創建自訂服務層

```java
@Service
public class CampsiteService extends CommonServiceImpl<Campsite, Long> {
    
    @Autowired
    private CampsiteRepository repository;
    
    // 自動獲得所有 CRUD 功能
    
    // 可添加自訂方法
    public List<Campsite> findByRegion(String region) {
        return repository.findByRegion(region);
    }
}
```

### 範例 2：創建實體

```java
@Entity
@Table(name = "campstores")
@Data
@Builder
public class Campsite implements LoggedEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Convert(converter = DatePattern2Converter.class)
    private LocalDate openDate;
    
    @Convert(converter = BooleanToIntConverter.class)
    private Boolean isActive;
    
    @Lob
    private String description;
    
    private LocalDateTime createdDate;      // 自動追蹤
    private LocalDateTime updatedDate;      // 自動追蹤
}
```

### 範例 3：在控制器中使用

```java
@RestController
@RequestMapping("/api/campstores")
public class CampsiteController {
    
    @Autowired
    private CampsiteService service;
    
    @GetMapping("/{id}")
    public ResponseEntity<Campsite> get(@PathVariable Long id) {
        return ResponseEntity.ok(
            service.findById(id).orElse(null)
        );
    }
    
    @PostMapping
    public ResponseEntity<Campsite> create(@RequestBody Campsite campstore) {
        return ResponseEntity.ok(
            service.save(campstore)
        );
    }
}
```

---

## 📊 依賴關係

```
CommonService (接口)
    ↓
CommonServiceImpl (實現)
    ↓ depends on
CustomRepository (數據訪問)
    ↓
JPA Entity (with Converters)
    ↓
LoggedEntity (審計時間戳)
    ↓ implements
具體實體類 (Campsite, etc.)
```

---

## ⚠️ 注意事項

1. **事務管理**：`CommonServiceImpl` 已標記 `@Transactional`，無需子類重複標記
2. **泛型限制**：ID 必須實現 `Serializable`
3. **代理調用**：所有數據庫操作通過 repository 代理，確保一致性
4. **日期轉換**：務必在實體上指定正確的 `@Convert` 註解
5. **LazyInit**：實體關聯應謹慎使用 FetchType.LAZY
6. **線程安全**：DateTimeFormatter 是線程安全的，可靜態共享

---

## 🔗 相關模組依賴

- ✅ 依賴：`config`（日期設定）
- ✅ 被依賴：`backend-app`、`common`、`web-common`

