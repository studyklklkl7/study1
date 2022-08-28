package com.tenminds.repository.cmn.auth;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tenminds.common.util.PagingUtil;
import com.tenminds.common.util.QdslUtil;
import com.tenminds.domain.cmn.auth.entity.Auth;
import com.tenminds.domain.cmn.auth.entity.QAuth;
import com.tenminds.domain.cmn.auth.model.AuthCnd;
import com.tenminds.domain.cmn.auth.model.AuthDto;
import com.tenminds.domain.cmn.auth.model.QAuthDto;
import com.tenminds.domain.cmn.userMgr.entity.QMgrInfo;
import com.tenminds.common.util.StringUtil;

//import static com.tenminds.domain.cmn.auth.entity.QAuth.auth;
//import static com.tenminds.domain.cmn.userMgr.entity.QMgrInfo.mgrInfo;


public class AuthReposImpl implements IAuthRepos {
	
	/*
	 * private final EntityManager em; private final JPAQueryFactory queryFactory;
	 * 
	 * public AuthQdslRepos(EntityManager em) { this.em = em; this.queryFactory =
	 * new JPAQueryFactory(em); }
	 */
	
	private final JPAQueryFactory queryFactory;
	
	public AuthReposImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }
	
	//QAuth auth = new QAuth("a");
	//QAuth auth = QAuth.auth; 
	//QMgrInfoEntity mInfo = QMgrInfoEntity.mgrInfoEntity;
	
	@Override
	public List<AuthDto> findAuthListBy(AuthCnd authCnd) throws Exception{
		
		List<AuthDto> authDtos = null;

		
		try {
			/*
			 * QAuth auth = QAuth.auth; QMgrInfo mgrInfo = QMgrInfo.mgrInfo;
			 */
			
			QAuth auth = new QAuth("auth"); 
			QMgrInfo mgrInfo = new QMgrInfo("mgrInfo");
			QMgrInfo mgrInfo2 = new QMgrInfo("mgrInfo2");
			
			//마리아 DB 함수 사용
	        StringTemplate regDateExpression = Expressions.stringTemplate("DATE_FORMAT({0}, '{1s}')", auth.regDate, ConstantImpl.create("%Y-%m-%d"));
			
			authCnd.of();
		
			
			//리스트
			authDtos = queryFactory
					.select(new QAuthDto(
							auth.authCd,
							auth.authName,
							auth.stateCd,
							auth.descript,
							regDateExpression.as("regDate"),
							auth.regMgrId,
							mgrInfo.name.as("regMgrName"),
							mgrInfo2.name.as("lUptMgrName")
					))
					.from(auth)
					.innerJoin(mgrInfo).on(auth.regMgrId.eq(mgrInfo.managerId))
					.innerJoin(mgrInfo2).on(auth.lUptMgrId.eq(mgrInfo2.managerId))
		            .where(
		            		QdslUtil.condition(authCnd.getSrchStateCd(), auth.stateCd::eq) //검색 파라미터중 stateCd가 있을 경우 auth.stateCd와 비교하고 없으면 null을 전달
		            )
					.fetch();
		}catch(Exception e){
			throw e;
		}
		return authDtos;
	}

	
	@Override
	public PagingUtil authListByState(AuthCnd authCnd) throws Exception{

		 
		//QAuthDto b = QAuthDto.;
		List<AuthDto> authDtos = null;
		Long cnt = 0L;
		String pagingFn = "";
		try {
			/*
			 * QAuth auth = QAuth.auth; QMgrInfo mgrInfo = QMgrInfo.mgrInfo;
			 */
			
			QAuth auth = new QAuth("auth"); 
			QMgrInfo mgrInfo = new QMgrInfo("mgrInfo");
			QMgrInfo mgrInfo2 = new QMgrInfo("mgrInfo2");
	
			//페이징 함수
			pagingFn = "_go_search";
			
			//빌더
			BooleanBuilder builder = new BooleanBuilder();
			
			//조건값 존재 여부
			if (StringUtils.hasText(authCnd.getSrchAuthName())) {
	            builder.and(auth.authName.contains(authCnd.getSrchAuthName()));
	            //builder.and(auth.authName.like("%"+authCnd.getSrchAuthName()));
	            //builder.and(auth.authName.startsWith(authCnd.getSrchAuthName()));
	        }
			
	        if (StringUtils.hasText(authCnd.getSrchStateCd())) {
	            builder.and(auth.stateCd.eq(authCnd.getSrchStateCd()));
	        }
	        
	        //마리아 DB 함수 사용
	        StringTemplate regDateExpression = Expressions.stringTemplate("DATE_FORMAT({0}, '{1s}')", auth.regDate, ConstantImpl.create("%Y-%m-%d"));

			
			//리스트와 총 건수를 가져오기 전에 한번 실행	
			authCnd.of();
	
			//리스트
			authDtos = queryFactory
					.select(new QAuthDto(
							auth.authCd,
							auth.authName,
							auth.stateCd,
							auth.descript,
							regDateExpression.as("regDate"),
							auth.regMgrId,
							mgrInfo.name.as("regMgrName"),
							mgrInfo2.name.as("lUptMgrName")
					))
					.from(auth)
					.innerJoin(mgrInfo).on(auth.regMgrId.eq(mgrInfo.managerId))
					.innerJoin(mgrInfo2).on(auth.lUptMgrId.eq(mgrInfo2.managerId))
		            .where(
		            		//statecdEq(authCnd.getStateCd())
		            		builder
		            )
		            .offset(authCnd.getOffset())
		            .limit(authCnd.getLimit())
		            .orderBy(QdslUtil.orderCondition(authCnd.getPageable(), Auth.class.getName() , "auth" ))
					.fetch();
			
			//카운트
			cnt = queryFactory.select(auth.count())	
			.from(auth)
			.innerJoin(mgrInfo).on(auth.regMgrId.eq(mgrInfo.managerId))
	        .where(
	        		//statecdEq(authCnd.getStateCd())
	        		builder
	        )
			.fetchOne();
			
		}catch(Exception e){
			throw e;
		} 	
		
		return new PagingUtil(authDtos, authCnd, cnt, pagingFn, true);
	}//authListByState end
	
	
	@Override
	public AuthDto authInfoByAuthcd(Integer authCd) throws Exception{
		AuthDto authDtos = null;
		try {
			
			  QAuth auth = QAuth.auth; 
			  //QMgrInfo mgrInfo = QMgrInfo.mgrInfo;
			 
			
			//빌더
			BooleanBuilder builder = new BooleanBuilder();
			
			//조건값 존재 여부
	        if (!StringUtil.isNull(authCd)) {
	            builder.and(auth.authCd.eq(authCd));
	        }

			authDtos = queryFactory
					.select(new QAuthDto(
							auth.authName,
							auth.stateCd,
							auth.descript
					))
					.from(auth)
		            .where(
		            		auth.authCd.eq(authCd)
		            )
					.fetchOne();
			
		} catch (Exception e) {
			throw e;
		}
		return authDtos;
	}//authInfoByAuthcd end
	
	
	@Override
	public Long authUpdateProc(AuthDto authDto) throws Exception{
		Long result = 0L; //결과값
		try {
			QAuth auth = QAuth.auth;
			
			result = queryFactory.update(auth)
					.where(auth.authCd.eq(authDto.getAuthCd()))
					.set(auth.authName, authDto.getAuthName())
					.set(auth.stateCd, authDto.getStateCd())
					.set(auth.descript, authDto.getDescript())
					.set(auth.lUptDate, LocalDateTime.now())
					.set(auth.lUptMgrId, authDto.getLUptMgrId())
					.execute();
		} catch (Exception e) {
			throw e;
		}
		return result;
	}//authUpdateProc end

}

