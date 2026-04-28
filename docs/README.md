# CampKeeper — 系統功能規劃與 API 設計

> Side project：露營小幫手  
> 技術棧：Java 21 / Spring Boot 3.5 / PostgreSQL / Python / Docker / Telegram Bot

---

## 專案模組結構

```
CampKeeper/
├── backend-app/     # Spring Boot 主入口、Controller、Service
├── base/            # 共用工具、例外處理、常數
├── config/          # 所有設定、properties、profile
├── model/           # JPA Entity、Repository、DTO
├── camp-crawler/    # Python 爬蟲（獨立模組）
└── docker-compose.yml
```

### 模組依賴關係

```
backend-app
  └── depends on → model
  └── depends on → config
  └── depends on → base

model
  └── depends on → base
```

---

## 開發時程

| 時程 | Phase | 目標 |
|------|-------|------|
| Week 1–3 | Phase 1 | 營地資料庫 CRUD |
| Week 4–5 | Phase 2 | 爬蟲 + Telegram 通知 |
| Week 6+  | Phase 3 | 二手裝備市集 |

---

## Phase 1 — 營地資料庫

**目標：建立可搜尋的營地資訊平台**

### 功能清單

- 營地搜尋（縣市、海拔、設施、寵物友善）
- Google Maps 整合（latitude / longitude）
- 使用者收藏與評分（UGC 資料建立）
- 營地資料 CRUD API

### Entity — Campsite

```java
@Entity
@Table(name = "campsites")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Campsite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 20)
    private String city;        // 縣市

    @Column(length = 20)
    private String district;    // 鄉鎮區

    private Integer altitude;   // 海拔（公尺）

    @Column(precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(precision = 9, scale = 6)
    private BigDecimal longitude;

    @Column(name = "has_power")
    private Boolean hasPower = false;

    @Column(name = "has_shower")
    private Boolean hasShower = false;

    @Column(name = "pet_friendly")
    private Boolean petFriendly = false;

    @Column(name = "source_url", length = 255)
    private String sourceUrl;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
```

### Repository

```java
@Repository
public interface CampsiteRepository extends JpaRepository<Campsite, Long>,
        JpaSpecificationExecutor<Campsite> {

    List<Campsite> findByCity(String city);
    List<Campsite> findByCityAndDistrict(String city, String district);
    List<Campsite> findByPetFriendlyTrue();
}
```

> `JpaSpecificationExecutor` 預留複合條件查詢（縣市 + 有電 + 寵物友善同時篩選）

---

## CRUD API 設計

### API 總覽

| Method | 路徑 | 說明 |
|--------|------|------|
| GET    | `/api/v1/campsites` | 查詢營地列表（含篩選） |
| GET    | `/api/v1/campsites/{id}` | 查詢單筆營地 |
| POST   | `/api/v1/campsites` | 新增營地 |
| PUT    | `/api/v1/campsites/{id}` | 更新營地 |
| DELETE | `/api/v1/campsites/{id}` | 刪除營地 |

---

### GET `/api/v1/campsites` — 查詢列表

**Query Parameters**

| 參數 | 型別 | 說明 |
|------|------|------|
| `city` | String | 縣市篩選，例如 `台北市` |
| `district` | String | 鄉鎮區篩選 |
| `petFriendly` | Boolean | 是否寵物友善 |
| `hasPower` | Boolean | 是否有電 |
| `hasShower` | Boolean | 是否有衛浴 |
| `altitudeMin` | Integer | 最低海拔 |
| `altitudeMax` | Integer | 最高海拔 |
| `page` | Integer | 頁碼（預設 0） |
| `size` | Integer | 每頁筆數（預設 20） |

**Response 200**

```json
{
  "content": [
    {
      "id": 1,
      "name": "武陵農場露營區",
      "city": "台中市",
      "district": "和平區",
      "altitude": 1740,
      "latitude": 24.3622,
      "longitude": 121.2839,
      "hasPower": true,
      "hasShower": true,
      "petFriendly": false
    }
  ],
  "totalElements": 42,
  "totalPages": 3,
  "page": 0,
  "size": 20
}
```

---

### GET `/api/v1/campsites/{id}` — 查詢單筆

**Response 200**

```json
{
  "id": 1,
  "name": "武陵農場露營區",
  "description": "位於台中市和平區，海拔1740公尺...",
  "city": "台中市",
  "district": "和平區",
  "altitude": 1740,
  "latitude": 24.3622,
  "longitude": 121.2839,
  "hasPower": true,
  "hasShower": true,
  "petFriendly": false,
  "sourceUrl": "https://example.com/campsite",
  "createdAt": "2025-01-01T00:00:00",
  "updatedAt": "2025-01-01T00:00:00"
}
```

**Response 404**

```json
{
  "status": 404,
  "message": "Campsite not found with id: 1"
}
```

---

### POST `/api/v1/campsites` — 新增營地

**Request Body**

```json
{
  "name": "武陵農場露營區",
  "description": "位於台中市和平區...",
  "city": "台中市",
  "district": "和平區",
  "altitude": 1740,
  "latitude": 24.3622,
  "longitude": 121.2839,
  "hasPower": true,
  "hasShower": true,
  "petFriendly": false,
  "sourceUrl": "https://example.com/campsite"
}
```

**Validation 規則**

| 欄位 | 規則 |
|------|------|
| `name` | 必填，最長 100 字元 |
| `city` | 必填 |
| `latitude` | 範圍 -90 ~ 90 |
| `longitude` | 範圍 -180 ~ 180 |

**Response 201**

```json
{
  "id": 1,
  "name": "武陵農場露營區",
  ...
}
```

---

### PUT `/api/v1/campsites/{id}` — 更新營地

Request Body 與 POST 相同，全欄位更新。

**Response 200**：回傳更新後完整資料  
**Response 404**：找不到該 id

---

### DELETE `/api/v1/campsites/{id}` — 刪除營地

**Response 204**：No Content（刪除成功）  
**Response 404**：找不到該 id

---

## 程式碼結構

```
backend-app/src/main/java/com/github/owenliou/campkeeper/app/
├── controller/
│   └── CampsiteController.java
├── service/
│   ├── CampsiteService.java
│   └── impl/
│       └── CampsiteServiceImpl.java
└── dto/
    ├── CampsiteRequest.java
    └── CampsiteResponse.java

model/src/main/java/com/github/owenliou/campkeeper/model/
├── entity/
│   └── Campsite.java
└── repository/
    └── CampsiteRepository.java
```



---

## Phase 2 — 空位通知爬蟲

**目標：自動偵測空營位並推播通知**

### 功能清單

- Python 爬蟲定時抓取營地空位資訊
- 使用者訂閱指定營地 + 日期
- Spring Scheduler 定期比對，有空位即觸發
- Telegram Bot 推播通知

### 爬蟲架構

```
camp-crawler/
├── requirements.txt       # requests, beautifulsoup4, psycopg2-binary
├── main.py
└── scrapers/
    └── base_scraper.py
```


---

## Phase 3 — 二手裝備市集

**目標：關鍵字訂閱，裝備上架即通知**

### 功能清單

- 使用者自行刊登二手裝備（品項 / 價格 / 狀況 / 附圖）
- 關鍵字訂閱（睡袋、天幕...）
- 有新貼文符合關鍵字即 Telegram 推播
- Facebook 社團爬蟲（進階，Meta API 限制多，Phase 3 後期）

---

## 技術架構總覽

| 層級 | 技術 |
|------|------|
| Backend API | Spring Boot 3.5 / Java 21 |
| ORM | Spring Data JPA / Hibernate |
| Database | PostgreSQL 15 |
| 爬蟲 | Python / BeautifulSoup |
| 推播通知 | Telegram Bot API |
| 容器化 | Docker Compose |
| 未來擴充 | K8s（流量成長後遷移） |

---

## Config Profile 設定

```
config/src/main/resources/
├── application.yml          # 共用（app name）
├── dev/
│   └── application.yml      # 本機開發 DB
└── prod/
    └── application.yml      # 正式環境 DB（環境變數）
```

```yaml
# prod/application.yml
spring:
  datasource:
    url: jdbc:postgresql://camp-keeper-db:5432/camping
    username: ${DB_USER}
    password: ${DB_PASSWORD}
  jpa:
    show-sql: false
    hibernate:
      ddl-auto: validate
```