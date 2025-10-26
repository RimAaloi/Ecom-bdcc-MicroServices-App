package org.sid.customerservice.records;
import java.util.List;

public record PagedResponse<T>(
        List<T> content,
        int size,
        int currentPage,
        long totalItems,
        int totalPages
) {
}