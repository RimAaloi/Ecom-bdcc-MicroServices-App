package org.sid.inventoryservice.records;
import java.util.List;

public record PagedResponse<T>(
        List<T> content,
        int size,
        int currentPage,
        long totalItems,
        int totalPages
) {
}