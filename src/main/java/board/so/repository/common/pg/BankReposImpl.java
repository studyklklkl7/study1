package com.tenminds.repository.common.pg;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tenminds.common.util.PagingUtil;
import com.tenminds.common.util.QdslUtil;
import com.tenminds.common.util.StringUtil;
import com.tenminds.domain.cmn.auth.entity.Auth;
import com.tenminds.domain.cmn.auth.entity.QAuth;
import com.tenminds.domain.cmn.auth.model.AuthCnd;
import com.tenminds.domain.cmn.auth.model.AuthDto;
import com.tenminds.domain.cmn.auth.model.QAuthDto;
import com.tenminds.domain.cmn.userMgr.entity.QMgrInfo;

//import static com.tenminds.domain.cmn.auth.entity.QAuth.auth;
//import static com.tenminds.domain.cmn.userMgr.entity.QMgrInfo.mgrInfo;


public class BankReposImpl implements IBankRepos {
	
	/*
	 * private final EntityManager em; private final JPAQueryFactory queryFactory;
	 * 
	 * public AuthQdslRepos(EntityManager em) { this.em = em; this.queryFactory =
	 * new JPAQueryFactory(em); }
	 */
	
	private final JPAQueryFactory queryFactory;
	
	public BankReposImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }
	
	
}