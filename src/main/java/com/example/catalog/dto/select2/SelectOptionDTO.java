package com.example.catalog.dto.select2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SelectOptionDTO implements Comparable<SelectOptionDTO> {

    @EqualsAndHashCode.Include
    private String id;
    private String text;

    @Override
    public int compareTo(SelectOptionDTO o) {
        return this.text.compareTo(o.getText());
    }
}
