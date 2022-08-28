package com.tenminds.repository.cmn.auth;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tenminds.domain.cmn.auth.entity.Auth;

public interface AuthRepos extends JpaRepository <Auth, Integer>, IAuthRepos {
	
	Page<Auth> findAllByStateCd(String StateCd, Pageable pageable);
	
	//Auth findByAuthCd(Integer authCd);
	

}
