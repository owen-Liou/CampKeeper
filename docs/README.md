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