package com.monitor.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Thing {
    private Integer id;
    private String name;
    private String description;
}
