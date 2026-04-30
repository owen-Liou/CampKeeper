## Phase 1.1 — 營地資料庫

**目標：建立可搜尋的營地資訊平台**

### 功能清單

- 營地搜尋（縣市、海拔、設施、寵物友善）
- Google Maps 整合（latitude / longitude）
- 使用者收藏與評分（UGC 資料建立）
- 營地資料 CRUD API

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