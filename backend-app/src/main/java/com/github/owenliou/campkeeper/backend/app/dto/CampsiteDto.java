package com.github.owenliou.campkeeper.backend.app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 營地 DTO（Data Transfer Object）
 * 用於 API 請求/回應的數據傳輸
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CampsiteDto {

    private Long id;

    private String name;

    private String description;

    private String city;        // 縣市

    private String district;    // 鄉鎮區

    private Integer altitude;   // 海拔（公尺）

    private BigDecimal latitude;    // 緯度

    private BigDecimal longitude;   // 經度

    @JsonProperty("hasPower")
    private Boolean hasPower;       // 有無電

    @JsonProperty("hasShower")
    private Boolean hasShower;      // 有無衛浴

    @JsonProperty("petFriendly")
    private Boolean petFriendly;    // 寵物友善

    private String storeName;   // 愛露營識別碼

    private String area;        // 大區域

    private String facilities;  // 設施列表（JSON array）

    private String sourceUrl;   // 資料來源

    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;
}

