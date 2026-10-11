package com.prepwise.dto.response.resume;

import com.prepwise.entity.KeywordStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeywordItemResponse {
    private String keyword;
    private KeywordStatus status;
}
