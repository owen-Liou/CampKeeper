package com.github.owenliou.campkeeper.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor(staticName = "result")
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomResult<R> {

    private boolean success;
    private R result;

    public boolean isNotSuccess() {
        return !success;
    }

/*
    public boolean hasValue() {
        if (result instanceof Collection<?>) {
            return success && ((result != null) && !((Collection<?>) result).isEmpty());
        }
        return success && (result != null);
    }
*/
/*
    public static <R> Optional<CustomResult<R>> fromString(ObjectMapper objectMapper, String json) {
        TypeReference<CustomResult<R>> typeReference = new TypeReference<>() {
        };
        try {
            return Optional.ofNullable(objectMapper.readValue(json, typeReference));
        } catch (JsonProcessingException e) {
            throw new DtoConverterException("%s, 將 JSON 轉換異動通知(%s) 失敗：%s".formatted(e.getLocalizedMessage(), CustomResult.class.getSimpleName(), json));
        }
    }
*/

}
