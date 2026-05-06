# Config 模組 - 配置中心與工具函數庫

## 📋 模組概述
**Config 模組** 是專案的配置管理層，提供：
- ⚙️ **環境配置管理**：Profile 識別與環境判斷
- 🎯 **全域常量定義**：日期格式、時區、ID 規則等
- 🛠️ **工具函數庫**：日期轉換、時間操作等
- 🔧 **Spring 自動配置**：Bean 自動註冊

---

## 🏗️ 核心類別列表

### 1️⃣ 環境管理

#### `Profiles` 組件
**功能**：Profile 識別與切換，支援多環境判斷

```java
@Component
@Slf4j
public class Profiles {
    // Profile 常量定義
    public static final String DEV = "dev";           // 開發
    public static final String DEV_LAB = "dev-lab";   // 開發-實驗室
    public static final String LAB = "lab";           // 實驗室
    public static final String PROD = "prod";         // 產線
    public static final String SANDBOX = "sandbox";   // 沙箱
    public static final String STAGE = "stage";       // 預發佈
    public static final String STAGE_GCP = "stage-gcp";  // GCP 預發佈
    public static final String TEST = "test";         // 測試
    
    // 複合條件
    public static final String NOT_PRODS = "!prod";   // 非產線
    public static final String PRODS = "prod";        // 產線
    public static final String NOT_TEST = "!test";    // 非測試
    
    private final ConfigurableEnvironment env;
    
    // 構造注入環境變量
    public Profiles(ConfigurableEnvironment env) {
        this.env = env;
    }
    
    // 判斷是否匹配某個 Profile
    public boolean isCurrentProfileMatch(String... profiles) {
        return env.matchesProfiles(profiles);
    }
    
    // 獲取當前活躍的所有 Profile
    public String activeProfiles() {
        return String.join(", ", env.getActiveProfiles());
    }
}
```

**支援的 Profile**：
```
dev, dev-lab, lab, prod, sandbox, stage, stage-gcp, test
```

---

### 2️⃣ 全域常量

#### `DEFAULT_SETTINGS` 接口
**功能**：定義全專案通用的常量和格式

```java
public interface DEFAULT_SETTINGS {
    // 時區設定
    String DEFAULT_TIMEZONE = "Asia/Taipei";
    int DEFAULT_OFFSET = 8;  // UTC+8
    
    // ===== 日期格式 =====
    String DATE_PATTERN_1 = "yyyy/MM/dd";
    DateTimeFormatter DATE_FORMATTER_1 = DateTimeFormatter.ofPattern(DATE_PATTERN_1);
    
    String DATE_PATTERN_2 = "yyyy-MM-dd";
    DateTimeFormatter DATE_FORMATTER_2 = DateTimeFormatter.ofPattern(DATE_PATTERN_2);
    
    String DATE_PATTERN_3 = "yyyyMMdd";
    DateTimeFormatter DATE_FORMATTER_3 = DateTimeFormatter.ofPattern(DATE_PATTERN_3);
    
    String DATE_PATTERN_4 = "yyyy-MM";
    DateTimeFormatter DATE_FORMATTER_4 = DateTimeFormatter.ofPattern(DATE_PATTERN_4);
    
    // ===== 日期時間格式 =====
    String DATE_TIME_PATTERN_1 = "yyyy/MM/dd HH:mm:ss";
    DateTimeFormatter DATE_TIME_FORMATTER_1 = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN_1);
    
    String DATE_TIME_PATTERN_2 = "yyyy-MM-dd HH:mm:ss";
    DateTimeFormatter DATE_TIME_FORMATTER_2 = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN_2);
    
    // ===== 時間格式 =====
    String TIME_PATTERN_1 = "HH:mm:ss";
    DateTimeFormatter TIME_FORMATTER_1 = DateTimeFormatter.ofPattern(TIME_PATTERN_1);
    
    // ===== 星期格式 =====
    String WEEK_PATTERN_1 = "e";  // 1-7：日-六
    DateTimeFormatter WEEK_FORMATTER_1 = DateTimeFormatter.ofPattern(WEEK_PATTERN_1);
    
    // ===== 民國日期格式 =====
    String ROC_DATE_PATTERN_1 = "%03d年%02d月%02d日";
    String ROC_DATE_TIME_PATTERN_1 = "%03d年%02d月%02d日 %02d:%02d";
}
```

#### `IDs` 常量接口
**功能**：定義 ID 生成與編碼規則
```java
public interface IDs {
    // ID 生成規則定義（詳見源代碼）
}
```

---

### 3️⃣ 工具函數庫

#### `DateUtils` 工具類
**功能**：日期計算、時間戳轉換、時區操作

常用方法：
- `LocalDate/LocalDateTime` 轉 `String`
- `String` 轉 `LocalDate/LocalDateTime`
- 時間戳轉換
- 時區操作

---

### 4️⃣ 自動配置

#### `CampKeeperConfigAutoConfiguration` 配置類
**功能**：Spring Boot 自動配置，自動註冊 Bean
```java
@Configuration
public class CampKeeperConfigAutoConfiguration {
    // 自動注冊 Profiles、DateUtils 等 Bean
}
```

---

## 🌍 DEFAULT_SETTINGS 完整常量表

### 時區與區域設定
```
DEFAULT_TIMEZONE = "Asia/Taipei"           // 台灣時區
DEFAULT_OFFSET = 8                          // UTC+8 偏移
```

### 日期格式（DATE_PATTERN_x）

| 常量 | 格式 | 用途 | 示例 |
|-----|------|------|------|
| `DATE_PATTERN_1` | `yyyy/MM/dd` | 日期（斜線）| 2026/05/06 |
| `DATE_PATTERN_2` | `yyyy-MM-dd` | 日期（連字符）| 2026-05-06 |
| `DATE_PATTERN_3` | `yyyyMMdd` | 日期（緊湊）| 20260506 |
| `DATE_PATTERN_4` | `yyyy-MM` | 年月 | 2026-05 |

### 日期時間格式（DATE_TIME_PATTERN_x）

| 常量 | 格式 |
|-----|------|
| `DATE_TIME_PATTERN_1` | `yyyy/MM/dd HH:mm:ss` |
| `DATE_TIME_PATTERN_2` | `yyyy-MM-dd HH:mm:ss` |

### 時間格式
```
TIME_PATTERN_1 = "HH:mm:ss"                 // 時分秒
WEEK_PATTERN_1 = "e"                        // 星期（1-7，日-六）
```

### 民國日期格式
```
ROC_DATE_PATTERN_1 = "%03d年%02d月%02d日"
    // 示例：115年05月06日

ROC_DATE_TIME_PATTERN_1 = "%03d年%02d月%02d日 %02d:%02d"
    // 示例：115年05月06日 15:30
```

---

## 💼 使用範例

### 範例 1：識別當前環境

```java
@Service
public class MyService {
    
    @Autowired
    private Profiles profiles;
    
    public void doSomething() {
        // 獲取所有活躍 Profile
        String activeProfiles = profiles.activeProfiles();  // "dev, lab"
        log.info("Active profiles: {}", activeProfiles);
        
        // 檢查是否在產線環境
        if (profiles.isCurrentProfileMatch("prod")) {
            // 產線特殊邏輯
            log.warn("Running in production!");
        }
        
        // 檢查是否不在產線（複合條件）
        if (profiles.isCurrentProfileMatch("!prod")) {
            // 非產線邏輯
        }
    }
}
```

### 範例 2：使用全域日期格式

```java
@Entity
public class Campsite {
    
    @Convert(converter = DatePattern2Converter.class)
    private LocalDate openDate;  // 數據庫存儲為 yyyy-MM-dd
    
    public void displayDate() {
        // 使用全域日期格式器
        String formatted = DEFAULT_SETTINGS.DATE_FORMATTER_2.format(openDate);
        System.out.println(formatted);  // 2026-05-06
    }
}
```

### 範例 3：時區處理

```java
@Service
public class DateService {
    
    public LocalDateTime getCurrentTimeInTaipei() {
        ZoneId taipei = ZoneId.of(DEFAULT_SETTINGS.DEFAULT_TIMEZONE);
        return LocalDateTime.now(taipei);
    }
}
```

### 範例 4：民國年轉換

```java
@Service
public class ROCDateService {
    
    public String toROCDate(LocalDate date) {
        int rocYear = date.getYear() - 1911;
        return String.format(
            DEFAULT_SETTINGS.ROC_DATE_PATTERN_1,
            rocYear, 
            date.getMonthValue(), 
            date.getDayOfMonth()
        );
    }
}
```

---

## 🚀 啟動時指定 Profile

### 方式 1：本機開發（Command Line）
```bash
java -jar camp-app.jar --spring.profiles.active=dev
```

### 方式 2：Docker 環境變數
```yaml
version: '3'
services:
  app:
    image: campkeeper:latest
    environment:
      - SPRING_PROFILES_ACTIVE=lab
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/camp
```

### 方式 3：application.yml 設定
```yaml
spring:
  profiles:
    active: dev
```

### 方式 4：IDE（IntelliJ IDEA）
```
Run → Edit Configurations → Program Arguments:
--spring.profiles.active=dev
```

---

## 📊 環境矩陣

| 環境 | Profile | 用途 | 日誌等級 | 特點 |
|-----|---------|------|--------|------|
| 本機開發 | dev | 開發機 | DEBUG | 快速迭代 |
| 開發實驗室 | dev-lab | 實驗新功能 | DEBUG | 模擬環境 |
| 測試環境 | lab | 質量測試 | INFO | 數據隔離 |
| 沙箱 | sandbox | 外部測試 | INFO | 模擬真實 |
| 預發佈 | stage | 上線前測試 | INFO | 準正式 |
| GCP 預發佈 | stage-gcp | 雲端測試 | INFO | 云原生 |
| 產線 | prod | 生產環境 | WARN | 高可用 |
| 單元測試 | test | 測試運行 | DEBUG | 隔離+快速 |

---

## 🔗 依賴關係

```
所有模組
    ↓
Config 配置管理
├── Profiles（環境識別）
├── DEFAULT_SETTINGS（全域常量）
├── IDs（ID 規則）
└── DateUtils（日期工具）
    ↓
Spring Boot Auto-Configuration
    ↓
Bean 自動注冊
```

---

## ⚠️ 注意事項

1. **時區一致性**：所有日期操作應使用 `DEFAULT_SETTINGS.DEFAULT_TIMEZONE`
2. **線程安全**：`DateTimeFormatter` 是線程安全的，可靜態共享
3. **Profile 命名**：遵循小寫連字符規則（如 `dev-lab`）
4. **環境變數**：Docker 使用 `SPRING_` 前綴的環境變數
5. **優先級**：Command Line > 環境變數 > application.yml > 預設值

