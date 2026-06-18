# CampKeeper — 產品規劃路線圖

> 露營小幫手 side project，目標平台：手機 APP（後端 REST API + Flutter 前端）

---

## 核心理念

**不需要登入也能使用大部分功能**，降低使用門檻，從資訊查詢出發，逐步引入社群與個人化功能。

---

## 功能路線圖

### Phase 1 — 營地資料庫（進行中）

目標：讓使用者能瀏覽、搜尋台灣各地營地資訊

- [x] 營地 CRUD API
- [x] 串接愛露營 API，一鍵同步全台營地資料（`POST /api/v1/campstores/sync`）
- [x] 營地列表查詢 API（`GET /api/v1/campstores`）
- [ ] 搜尋 / 篩選（縣市、海拔、設施、寵物友善）
- [ ] 分頁查詢
- [ ] 地圖整合（Google Maps / OpenStreetMap）

---

### Phase 2 — 營地空位查詢（規劃中）

目標：查詢指定營地的帳位資訊與空位狀況

- [ ] 串接愛露營 API 取得帳位列表（`/api/guest/v1/stuff/store_name/list`）
- [ ] 空位查詢 API（指定營地 + 日期）
- [ ] （進階）Telegram Bot 空位通知

> 資料來源改用愛露營 guest API，不需要 Python 爬蟲

---

### Phase 3 — 用戶系統（規劃中）

目標：引入帳號機制，解鎖個人化功能

- [ ] 用戶註冊 / 登入（JWT）
- [ ] Google OAuth / LINE Login（台灣用戶友善）
- [ ] 收藏營地
- [ ] 空位訂閱通知（需帳號才能綁定推播）

---

### Phase 4 — 露營日記（遠期）

目標：讓使用者記錄每次露營體驗

- [ ] 建立露營日記（文字、日期、營地關聯）
- [ ] 上傳照片 / 影片（雲端儲存：S3 或 MinIO）
- [ ] 個人行程記錄頁

---

### Phase 5 — 社群功能（遠期）

目標：建立露營者社群

- [ ] 公開日記 / 心得分享
- [ ] 評分與留言
- [ ] 標籤 / 探索頁

---

## 暫不列入規劃

| 功能 | 原因 |
|------|------|
| 二手裝備市集 | 台灣露營二手主要在 FB 社團，爬蟲受 Meta 限制多，技術挑戰高 |

---

## 技術棧

| 層級 | 技術 |
|------|------|
| Backend API | Java 21 / Spring Boot 3.5 |
| Database | PostgreSQL 15 |
| 外部資料來源 | 愛露營 guest API（Spring RestClient） |
| 推播 | Telegram Bot API |
| 容器化 | Docker Compose |
| 前端（規劃） | Flutter（iOS / Android 同一份程式碼） |
| 媒體儲存（Phase 4） | MinIO / AWS S3 |

---

## 目前進度

```
Phase 1 ██████░░░░ 60%  （CRUD + 愛露營 sync API 完成，搜尋篩選待實作）
Phase 2 ░░░░░░░░░░  0%  （改以愛露營 API 為資料來源，不需 Python 爬蟲）
Phase 3 ░░░░░░░░░░  0%
Phase 4 ░░░░░░░░░░  0%
Phase 5 ░░░░░░░░░░  0%
```
