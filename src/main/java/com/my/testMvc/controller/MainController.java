package com.my.testMvc.controller;

import com.my.testMvc.dto.ItemDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {
    @GetMapping({"main", "/"})
    public String mainPage() {
        return "mainPage";
    }

    @GetMapping("/gugudan")
    public String gugu() {
        return "/test/gugu";
    }

    @GetMapping("/itemlist")
    public String item(Model model) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            ItemDto item = new ItemDto(
                    "테스트 상품" + i,
                    "상품 상세 설명" + i,
                    i * 1000,
                    LocalDate.of(2023,6,30).atStartOfDay()
            );
            itemDtoList.add(item);
        }
        model.addAttribute("items", itemDtoList);
        return "/test/item";
    }
}
