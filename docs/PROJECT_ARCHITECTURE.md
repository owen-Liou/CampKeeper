# CampKeeper 專案架構文件

> 產生日期：2026-05-05

---

## 📋 專案基本資訊

| 項目 | 內容 |
|------|------|
| **專案名稱** | CampKeeper |
| **Group ID** | `com.github.owenliou` |
| **版本** | `1.0.1` |
| **打包方式** | Maven 多模組 (pom) |
| **Java 版本** | 21 |
| **Spring Boot 版本** | 3.3.10 |
| **編碼** | UTF-8 |

---

## 🏗️ 模組架構

```
CampKeeper (Parent POM)
├── config          ← 配置中心：環境變數、資料庫設定、常數定義
├── model           ← 資料模型：JPA Entity、DTO
├── base            ← 核心邏輯：通用 Service、Repository、工具類
├── web-common      ← Web 共用：REST Controller 基底類、SSO、Security
└── backend-app     ← 啟動模組：Spring Boot Application 入口
```

---

## 📦 模組說明

### 1. `config` - 配置中心
| 項目 | 說明 |
|------|------|
| **用途** | 集中管理環境設定、常數、DB 連線資訊 |
| **依賴** | 無模組依賴（獨立模組） |
| **主要內容** | `DEFAULT_SETTINGS`（日期格式、時區）、`Profiles`（環境判斷） |

**主要依賴：**
- `spring-boot-starter`
- `lombok`
- `postgresql`

---

### 2. `model` - 資料模型
| 項目 | 說明 |
|------|------|
| **用途** | 定義 JPA Entity、DTO、資料傳輸物件 |
| **依賴** | 無模組依賴（獨立模組） |
| **主要內容** | Entity 類、資料庫欄位映射 |

**主要依賴：**
- `spring-boot-starter-data-jpa`
- `lombok`

---

### 3. `base` - 核心邏輯
| 項目 | 說明 |
|------|------|
| **用途** | 通用邏輯、Service 基底類、Repository 介面、工具類 |
| **依賴** | `config` |
| **主要內容** | `CommonService`、`CommonServiceImpl`、`CustomRepository`、Converter |

**主要依賴：**
- `spring-boot-starter`
- `spring-boot-starter-data-jpa`
- `spring-boot-starter-web`
- `spring-boot-starter-webflux`
- `spring-data-rest-core`
- `httpclient5`
- `gson`
- `commons-lang3`
- `commons-beanutils`
- `commons-io`
- `modelmapper`
- `zstd-jni`

---

### 4. `web-common` - Web 共用元件
| 項目 | 說明 |
|------|------|
| **用途** | REST Controller 基底類、SSO 服務、Security 設定、API 文件 |
| **依賴** | `config` |
| **主要內容** | `AbstractRestController`、`PrivilegeService`、Swagger UI |

**主要依賴：**
- `spring-boot-starter-security`
- `springdoc-openapi-starter-webmvc-ui` (Swagger UI)
- `jackson-dataformat-msgpack`
- `tomcat-embed-core`
- `mydata-common` (local jar)
- `CGVerify` (local jar)

---

### 5. `backend-app` - 啟動模組
| 項目 | 說明 |
|------|------|
| **用途** | Spring Boot Application 入口、定時任務、API 端點 |
| **依賴** | `model`, `base`, `config`, `web-common` |
| **主要內容** | Application 主類、Controller、排程任務 |

**主要依賴：**
- `spring-boot-starter`
- `spring-boot-starter-web`
- `modelmapper`
- 所有內部模組

---

## 🔗 模組依賴關係圖

```
                    ┌─────────────┐
                    │   config    │  ← 配置中心（獨立）
                    └──────┬──────┘
                           │
           ┌───────────────┼───────────────┐
           │               │               │
           ▼               │               ▼
    ┌─────────────┐        │        ┌─────────────┐
    │    base     │        │        │ web-common  │
    └──────┬──────┘        │        └──────┬──────┘
           │               │               │
           │               │               │
           │      ┌────────┴────────┐      │
           │      │      model      │      │
           │      └────────┬────────┘      │
           │               │               │
           └───────────────┼───────────────┘
                           │
                           ▼
                   ┌─────────────┐
                   │ backend-app │  ← 啟動模組
                   └─────────────┘
```

---

## ⚙️ 版本管理（dependencyManagement）

Parent POM 統一管理以下依賴版本：

| 依賴 | 版本 |
|------|------|
| Lombok | 1.18.42 |
| Spring Boot | 3.5.7 |
| PostgreSQL | 42.7.3 |
| Commons BeanUtils | 1.11.0 |
| Commons Collections4 | 4.5.0 |
| Commons Lang3 | 3.20.0 |
| Commons IO | 2.21.0 |
| ModelMapper | 3.2.0 |
| JSpecify | 0.3.0 |
| Zstd-JNI | 1.5.7-5 |
| SpringDoc OpenAPI | 2.8.8 |
| Jackson MessagePack | 0.9.9 |

---

## 🌍 環境 Profiles

| Profile | 說明 | 預設 |
|---------|------|------|
| `dev` | 開發環境，啟用 devtools、actuator | ✅ |
| `prod` | 正式環境 | |

---

## 📁 自定義 JAR 存放位置

本專案使用 **本地 Maven Repository** 存放自定義 JAR：

```
CampKeeper/
└── lib/
    └── local/
        ├── mydata-common/
        │   └── 0.5.39-RELEASE/
        │       ├── mydata-common-0.5.39-RELEASE.jar
        │       └── mydata-common-0.5.39-RELEASE.pom
        └── CGVerify/
            └── 1.0.2/
                ├── CGVerify-1.0.2.jar
                └── CGVerify-1.0.2.pom
```

Parent POM 設定：
```xml
<repositories>
    <repository>
        <id>project-local</id>
        <url>file://${maven.multiModuleProjectDirectory}/lib/local</url>
    </repository>
</repositories>
```

---

## 🚀 建置與執行

### 編譯全部模組
```bash
mvn clean install
```

### 僅編譯指定模組
```bash
mvn clean install -pl backend-app -am
```

### 執行 Spring Boot 應用
```bash
cd backend-app
mvn spring-boot:run
```

### 指定 Profile 執行
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

---

## 📂 目錄結構總覽

```
CampKeeper/
├── pom.xml                      ← Parent POM
├── lib/
│   └── local/                   ← 自定義 JAR (Maven Repo 格式)
├── docs/                        ← 文件
│   ├── PROJECT_ARCHITECTURE.md  ← 本文件
│   ├── README.md
│   ├── SQL.md
│   └── Phase1營地資料庫/
├── base/
│   ├── pom.xml
│   └── src/main/java/com/github/owenliou/campkeeper/base/
│       ├── model/               ← Repository、Converter
│       └── service/             ← CommonService
├── config/
│   ├── pom.xml
│   └── src/main/java/com/github/owenliou/campkeeper/
│       └── variables/           ← DEFAULT_SETTINGS、Profiles
├── model/
│   ├── pom.xml
│   └── src/main/java/           ← JPA Entity
├── web-common/
│   ├── pom.xml
│   └── src/main/java/com/github/owenliou/campkeeper/web/common/
│       ├── restcontroller/      ← AbstractRestController
│       └── sso/                 ← PrivilegeService
└── backend-app/
    ├── pom.xml
    └── src/
        ├── main/
        │   ├── java/            ← Application 主類、Controller
        │   └── resources/
        │       └── application.yml
        └── test/
```

---

## 📝 注意事項

1. **避免循環依賴**：`config` 不應依賴 `base`，否則會造成 Maven reactor 循環。
2. **版本統一**：子模組使用 `${project.version}` 引用其他模組，確保版本一致。
3. **本地 JAR**：放在 `lib/local` 並遵循 Maven Repository 結構（groupId/artifactId/version）。
4. **Profile 切換**：使用 `@profiles.active@` 佔位符，Maven build 時自動替換。

---

## 📚 相關文件

- [README.md](README.md)
- [SQL.md](SQL.md)
- [profile.md](profile.md)
- [Phase1 營地資料庫](Phase1營地資料庫/)
- [Phase2 空位通知爬蟲](Phase2空位通知爬蟲/)
- [Phase3 二手裝備市場](Phase3二手裝備市場/)

