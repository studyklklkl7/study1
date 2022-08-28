package com.tenminds.domain.cmn.auth.model;


import com.tenminds.domain.common.paging.PagingRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
public class AuthCnd extends PagingRequest{
	
	private String srchAuthName;
	private String srchStateCd;

}
