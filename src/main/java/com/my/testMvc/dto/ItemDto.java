package com.my.testMvc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ItemDto {
    private String itemName;
    private String itemDetail;
    private Integer price;
    private LocalDateTime regTime;
}
