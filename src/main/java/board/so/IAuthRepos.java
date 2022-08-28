package com.tenminds.repository.cmn.auth;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tenminds.common.util.PagingUtil;
import com.tenminds.domain.cmn.auth.model.AuthCnd;
import com.tenminds.domain.cmn.auth.model.AuthDto;

public interface IAuthRepos{
	
	//테스트용
	List<AuthDto> findAuthListBy(AuthCnd authCnd) throws Exception;

	//페이징 리스트
	PagingUtil authListByState(AuthCnd authCnd)throws Exception;
	
	//상세 페이지
	AuthDto authInfoByAuthcd(Integer authCd)throws Exception;
	
	//수정처리
	Long authUpdateProc(AuthDto authDto) throws Exception;
	
}
