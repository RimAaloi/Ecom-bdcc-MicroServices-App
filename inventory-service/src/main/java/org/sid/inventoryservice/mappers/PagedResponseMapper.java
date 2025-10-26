package org.sid.inventoryservice.mappers;

import org.sid.inventoryservice.records.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PagedResponseMapper {

    public <T> PagedResponse<T> toPagedResponse(Page<T> page) {
        return new PagedResponse<>(
                page.getContent(),
                page.getSize(),
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}