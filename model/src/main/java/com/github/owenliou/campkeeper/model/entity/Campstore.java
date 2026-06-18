package com.github.owenliou.campkeeper.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 營地實體類別，對應資料庫中的campstore表。
 * 包含營地的基本資訊，如名稱、描述、位置、設施等。
 * 同時包含資料來源URL和時間戳記，以便追蹤資料的來源和更新狀態。
 */
@Entity
@Table(name = "campstore")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Campstore {

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

    @Column(name = "has_power")
    private Boolean hasPower = false;       // 有無電

    @Column(name = "has_shower")
    private Boolean hasShower = false;      // 有無衛浴


    @Column(name = "store_name", unique = true, length = 50)
    private String storeName;   // 愛露營唯一識別碼（用於 upsert）

    @Column(length = 20)
    private String area;        // 大區域（北部/中部/南部/東部）

    @Column(columnDefinition = "TEXT")
    private String facilities;  // 設施列表（JSON array）

    @Column(name = "source_url", length = 255)
    private String sourceUrl;   // 資料來源（爬蟲用）

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