# CampKeeper — 系統功能規劃與 API 設計

> Side project：露營小幫手  
> 技術棧：Java 21 / Spring Boot 3.5 / PostgreSQL / pgvector / Spring AI / Docker / Telegram Bot

---

## 專案模組結構

```
CampKeeper/
├── backend-app/     # Spring Boot 主入口、Controller、Service
├── base/            # 共用工具、例外處理、常數
├── config/          # 所有設定、properties、profile
├── model/           # JPA Entity、Repository
├── common/          # 共用 adapter、converter
├── web-common/      # Web 層共用（Swagger、GlobalExceptionHandler）
└── config/src/main/resources/docker/  # Docker Compose
```

### 模組依賴關係

```
backend-app
  └── depends on → model
  └── depends on → config
  └── depends on → base
  └── depends on → common
  └── depends on → web-common

model
  └── depends on → base
```

---

## 程式碼結構

```
backend-app/src/main/java/.../backend/app/
├── ai/
│   ├── restcontroller/
│   │   └── AdminController.java             # 管理員操作（向量重建）
│   └── service/
│       └── CampsiteEmbeddingService.java    # embedding upsert / 語意搜尋 / 批次同步
├── config/
│   ├── GoogleGenAiEmbeddingConfig.java  # 覆寫 Spring AI 預設 client，修正 API version
│   ├── RestClientConfig.java            # RestClient bean 集中管理
│   ├── SecurityConfig.java              # Spring Security 設定
│   └── SwaggerConfig.java
├── restcontroller/
│   └── RestCampsiteController.java
├── service/
│   ├── CampsiteService.java
│   └── impl/
│       └── CampsiteServiceImpl.java
├── repository/
│   └── CampsiteRepository.java
├── converter/
│   └── CampsiteToDtoConverter.java
├── dto/
│   └── CampsiteDTO.java
└── external/
    └── icamping/                        # iCamping 外部 API 整合
        ├── client/
        │   └── ICampingClient.java
        ├── dto/
        │   ├── ICampingStore.java
        │   └── ICampingStoreListResponse.java
        └── variables/
            ├── ICampingApiPath.java     # API 路徑 enum
            └── ICampingApiKey.java      # API key enum

model/src/main/java/.../model/
├── entity/
│   └── Campsite.java
└── repository/
    └── CampsiteRepository.java
```

---

## Campsite API

| Method | Path | 說明 |
|--------|------|------|
| GET | `/api/v1/campsites` | 查詢所有營地（分頁 + 縣市篩選） |
| GET | `/api/v1/campsites/{id}` | 查詢單筆營地 |
| POST | `/api/v1/campsites/create` | 新增營地（自動生成 embedding） |
| PUT | `/api/v1/campsites/{id}` | 更新營地（自動更新 embedding） |
| DELETE | `/api/v1/campsites/{id}` | 刪除營地 |
| POST | `/api/v1/campsites/sync` | 從愛露營 API 同步所有營地資料（含 embedding） |
| GET | `/api/v1/campsites/search/ai` | **AI 語意搜尋**（自然語言查詢） |

> Context path: `/backend-app`，完整範例：`POST http://localhost:8080/backend-app/api/v1/campsites/sync`

### AI 語意搜尋

```
GET /api/v1/campsites/search/ai?query=寵物友善台中高山有電&topK=10
```

| 參數 | 必填 | 說明 |
|------|------|------|
| query | ✅ | 自然語言描述，例如「南投森林系近泡湯」 |
| topK | ❌ | 回傳筆數，預設 10 |

---

## Admin API

| Method | Path | 說明 |
|--------|------|------|
| POST | `/api/v1/admin/embeddings/sync` | 將 DB 所有營地重建向量索引 |

> 首次啟動或更換 embedding model 後需執行一次。

---

## Campsite Entity 欄位

| 欄位 | 類型 | 說明 |
|------|------|------|
| id | Long | 自增主鍵 |
| storeName | String | 愛露營唯一識別碼（upsert key） |
| name | String | 營地中文名稱 |
| area | String | 大區域（北部/中部/南部/東部） |
| city | String | 縣市 |
| district | String | 鄉鎮區 |
| altitude | Integer | 海拔（公尺） |
| hasPower | Boolean | 有無電源 |
| petFriendly | Boolean | 寵物友善 |
| facilities | TEXT | 設施列表（JSON array） |
| latitude / longitude | BigDecimal | 座標（愛露營 API 未提供，預留） |

---

## iCamping API 整合

資料來源使用逆向自 iCamping mobile web（`m.icamping.app`）的 guest API，不需要登入。

| 項目 | 說明 |
|------|------|
| 資料取得方式 | 呼叫 `ICampingApiPath.STORE_LIST` 取得全部營地 |
| 同步策略 | 手動觸發（`POST /sync`），以 `store_name` 為 key 做 upsert |
| 設施解析 | `facility[]` 陣列整體存 JSON，關鍵標籤（電源/寵物）另存 boolean |
| API 路徑管理 | `ICampingApiPath` enum（STORE_LIST / STORE_LIST_TOP / STUFF_LIST / EXTERNAL_LINK_LIST） |
| API Key 管理 | `ICampingApiKey` enum |
| RestClient 設定 | `RestClientConfig`（base-url、Origin/Referer header 集中管理） |
| 設定位置 | `config/src/main/resources/dev/application-dev.yml` |

> **注意：** Origin / Referer 必須為 `https://m.icamping.app`，使用其他 origin 會被 API 拒絕（403）。

---

## 技術架構總覽

| 層級 | 技術 |
|------|------|
| Backend API | Spring Boot 3.5 / Java 21 |
| ORM | Spring Data JPA / Hibernate |
| Database | PostgreSQL 15（`pgvector/pgvector:pg15` image） |
| Vector Store | pgvector — HNSW index，cosine distance，768 dims |
| Embedding Model | Google `gemini-embedding-001`（Matryoshka，截斷至 768 dims 輸出） |
| AI Framework | Spring AI 1.1.7 |
| HTTP Client | Spring RestClient（內建於 spring-boot-starter-web） |
| 推播通知 | Telegram Bot API（規劃中） |
| 容器化 | Docker Compose |
| 未來擴充 | K8s（流量成長後遷移） |

### Spring AI 向量搜尋流程

```
新增/更新/同步營地
  └── CampsiteServiceImpl
        └── CampsiteEmbeddingService.upsertEmbedding()
              └── gemini-embedding-001 → 768-dim vector
                    └── pgvector (HNSW) 儲存

AI 搜尋請求
  └── GET /search/ai?query=...
        └── CampsiteEmbeddingService.semanticSearchCamp()
              └── query → embedding → cosine similarity search
                    └── 回傳最相近的 Campsite 列表
```

---

## Config Profile 設定

```
config/src/main/resources/
├── application.yml              # 共用（app name）
├── dev/
│   └── application-dev.yml      # 本機開發 DB + 愛露營 API key
└── prod/
    └── application-prod.yml     # 正式環境 DB（環境變數）
```

```yaml
# dev/application-dev.yml
icamping:
  api:
    base-url: https://api-guest-prod-tier-1-wwclgij22a-an.a.run.app
    key: <firebase-web-api-key>
```