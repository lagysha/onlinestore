package io.teamchallenge.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PageableDto<T> {
    private List<T> page;
    private long totalElements;
    private int currentPage;
    private int totalPages;
}
