package com.tenminds.domain.common.paging;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PagingRequest {
	private int page = 1;
    private int size = 10;
    private int pageSize = 10;
    private Pageable pageable;
    private Long offset;
    private int limit;
    private Sort sort;
    
    //private Direction direction = Direction.DESC;

    public void setPage(int page) {
        this.page = page <= 0 ? 1 : page;
    }

    public void setSize(int size) {
        int DEFAULT_SIZE = 10;
        int MAX_SIZE = 50;
        this.size = size > MAX_SIZE ? DEFAULT_SIZE : size;
    }
    
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize <= 0 ? 1 : pageSize;
    }
    
    public void setSort(Sort sort) {
        this.sort = sort;
    }
    
    public Long getOffset() {
    	return offset;
    }
    public int getLimit() {
    	return limit;
    }
    
    public int getPage() {
    	return page;
    }
    
    public Pageable getPageable() {
    	return pageable;
    } 
    /*
    public void setDirection(Direction direction) {
        this.direction = direction;
    }*/

    public PageRequest of() {
    	PageRequest pageRequest = null;
    	if(sort != null)
    		pageRequest = PageRequest.of(page - 1, size, sort);
    	else
    		pageRequest = PageRequest.of(page - 1, size);
    	
    	pageable = pageRequest;
    	offset = pageable.getOffset();
    	limit = pageable.getPageSize();
    	sort = pageable.getSort();
        return pageRequest;
    }
}
