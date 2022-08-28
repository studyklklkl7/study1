package com.tenminds.common.util;

import java.util.List;

import org.springframework.data.domain.PageImpl;

import com.tenminds.domain.common.paging.PagingDto;
import com.tenminds.domain.common.paging.PagingRequest;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/***********************************************************************
 * Simple Paging Util */
/************************************************************************
 * @author kkkmmm
 * @version 0.1
 * constructor 
 * 	- pageImpl : PageImpl객체 
 * 	- callNm : 호출 클래스명 or 함수명 ( default : move-page)
 *  - callFunction : 호출 타입 ( false : 클래스 , true : 함수 , default : false ) 
 *  - pageSize : 한번에 보여줄 페이지 넘버 갯수 ( default : 10 )
 * 
 * {note}
 * org.springframework.data.domain.PageImpl 이용한 간단 페이징 유틸
 * Querydsl과 mustache 환경에서 테스트  *  
 * 
 */
@ToString
@Setter
@Getter
public class PagingUtil {	

	private PageImpl<?> pageImpl;			// PageImpl 객체
	private String pagingHtml;				// pagingHtml;
	private String callNm = "move-page";	// 함수명
	private int pageSize = 10;				// 한 페이지당 사이즈
	private boolean callFunction = false;
	private Long total = 0L;

	public PagingUtil(List<?> content, PagingRequest pagingReq, long total) {
		this.pageImpl = new PageImpl<>(content, pagingReq.getPageable(), total);
		init();
	}
	
	public PagingUtil(List<?> content, PagingRequest pagingReq, long total,String callNm) {
		this.pageImpl = new PageImpl<>(content, pagingReq.getPageable(), total);
		this.callNm = callNm;
		init();
	}
	
	public PagingUtil(List<?> content, PagingRequest pagingReq, long total,String callNm,boolean callFunction) {
		this.pageImpl = new PageImpl<>(content, pagingReq.getPageable(), total);
		this.total = total;
		this.callNm = callNm;
		this.callFunction = callFunction;
		init();
	}
	
	//초기화
	private void init() {
		Long index = pageImpl.getPageable().getOffset()+1;
		for(Object pagingDto : pageImpl.getContent()) {
			((PagingDto)pagingDto).setIndex(index++);
		}
		
		int pageNumber = pageImpl.getPageable().getPageNumber();
		int totalPage = pageImpl.getTotalPages();
		StringBuffer htmlSb = new StringBuffer();
		
		int pageOffset = (pageNumber/pageSize) * pageSize;
		int pageLen = (pageNumber/pageSize == totalPage/pageSize ? totalPage : ((pageNumber/pageSize)+1) * pageSize);
		boolean isPrev = pageNumber/pageSize > 0;
		boolean isNext = pageNumber/pageSize < totalPage/pageSize;
		
		
		//***html area
		htmlSb.append("<div class='page_sample'><ul class='pagination justify-content-center'>"); 
		
		//prev-page
		if(isPrev) {
			int prevPage = (((pageNumber/pageSize)-1) * pageSize) + pageSize;
			if(!callFunction)
				htmlSb.append("<li class='page-item'><a class='page-link "+callNm+"' href='javascript:void(0);' aria-label='Previous' page-number='"+prevPage+"'><span aria-hidden='true'>&laquo;</span></a></li>");
			else
				htmlSb.append("<li class='page-item'><a class='page-link' href='javascript:"+callNm+"("+prevPage+");' aria-label='Previous' page-number='"+prevPage+"'><span aria-hidden='true'>&laquo;</span></a></li>");
		}
		else {
			htmlSb.append("<li class='page-item disabled'><a class='page-link' href='javascript:void(0);' aria-label='Previous'><span aria-hidden='true'>&laquo;</span></a></li>");
		}
		
		//number-page
		int numberPage = 0;
		for(int i = pageOffset; i < pageLen; i++) {		
			numberPage = i+1;
			
			if(pageImpl.getPageable().getPageNumber() == i)
				htmlSb.append("<li class='page-item active' aria-current='page'><a class='page-link' href='javascript:void(0);'>"+numberPage+"</a></li>");
			else {
				if(!callFunction)
					htmlSb.append("<li class='page-item'><a class='page-link "+callNm+"' href='javascript:void(0);' page-number='"+numberPage+"'>"+numberPage+"</a></li>");
				else
					htmlSb.append("<li class='page-item'><a class='page-link' href='javascript:"+callNm+"("+numberPage+");' page-number='"+numberPage+"'>"+numberPage+"</a></li>");
			}
		}
		
		//next-page
		if(isNext) {
			int nextPage = (((pageNumber/pageSize)+1) * pageSize) + 1;
			if(!callFunction)
				htmlSb.append("<li class='page-item'><a class='page-link "+callNm+"' href='javascript:void(0);' aria-label='Next' page-number='"+nextPage+"'><span aria-hidden='true'>&raquo;</span></a></li>");
			else
				htmlSb.append("<li class='page-item'><a class='page-link' href='javascript:"+callNm+"("+nextPage+");' aria-label='Next' page-number='"+nextPage+"'><span aria-hidden='true'>&raquo;</span></a></li>");
		}
		else {
			htmlSb.append("<li class='page-item disabled'><a class='page-link' href='javascript:void(0);' aria-label='Next'><span aria-hidden='true'>&raquo;</span></a></li>");
		}
		
		htmlSb.append("</ul></div>");
		//***html area
		
		pagingHtml = htmlSb.toString();
	}	
	
}
