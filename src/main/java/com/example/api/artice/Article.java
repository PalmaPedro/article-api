package com.example.api.artice;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Article {
    private int id;
    private String description;
    private double weight;
    private double volume;
}
