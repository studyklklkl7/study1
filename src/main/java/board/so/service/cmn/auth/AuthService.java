package com.tenminds.service.cmn.auth;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.tenminds.common.configuration.database.DataSource;
import com.tenminds.common.configuration.database.DatabaseType;
import com.tenminds.common.configuration.database.Tx;
import com.tenminds.common.consts.LogMdc;
import com.tenminds.common.util.PagingUtil;
import com.tenminds.domain.cmn.auth.entity.Auth;
import com.tenminds.domain.cmn.auth.model.AuthCnd;
import com.tenminds.domain.cmn.auth.model.AuthDto;
import com.tenminds.domain.cmn.code.model.CodeDto;
import com.tenminds.domain.common.resolver.HttpHeader;
import com.tenminds.domain.common.security.LoginInfo;
import com.tenminds.repository.cmn.auth.AuthRepos;
import com.tenminds.repository.cmn.code.CodeQdslRepos;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "DY_LOGGER")
@RequiredArgsConstructor
@Service
public class AuthService {	
	
	private final AuthRepos authRepository;

	/*
	 * @Autowired AuthQdslRepos authQdslRepos;
	 */
	private final CodeQdslRepos codeQdslRepos;
	
	@DataSource(DatabaseType.DB_MARIA_WIPET_SVC)
	public Page<Auth> selectAuthList(Pageable pageable) throws Exception {
		
		//LogMdc.name.accept("Board");
		String StateCd = "1"; //0:비노출, 1:노출
		Page<Auth> list = authRepository.findAllByStateCd(StateCd, pageable);

		return list;
	}
	
	/*
	 * 페이징 리스트
	 */
	@DataSource(DatabaseType.DB_MARIA_WIPET_SVC)
	public PagingUtil authListByState(AuthCnd authCnd) throws Exception{
		
		return authRepository.authListByState(authCnd);
	}
	
	/*
	 * 코드 리스트
	*/
	@DataSource(DatabaseType.DB_MARIA_WIPET_SVC)
	public List<CodeDto> codeListBycatCdByuseType(int catCd, int useType) throws Exception{
		
		return codeQdslRepos.codeListBycatCdByuseType(catCd, useType);
	}
	
	/*
	 * 저장
	 */
	@Transactional(Tx.JPA)
	@DataSource(DatabaseType.DB_MARIA_WIPET_SVC)
	public String authCreateProcAjax(@ModelAttribute("authDto") AuthDto authDto, @AuthenticationPrincipal LoginInfo loginInfo, HttpHeader header, ModelMap model) throws Exception{
		
		LogMdc.name.accept("Auth");
		
		String result = "0"; //결과값
		
		try {
			String regMgrId = loginInfo.getManagerId(); //등록자 ID
			authDto.setRegMgrId(regMgrId); //set
	
			Auth auth = authDto.regToEntity(); //DTO -> Entity
			
			auth = authRepository.save(auth); //저장
			
			result = "1";
		} catch (Exception e) {
			// TODO: handle exception
			log.error("AuthService > authCreateProcAjax : " + e.toString());
		}		
		return result;
	}
	
	/*
	 * 조회
	 */
	@DataSource(DatabaseType.DB_MARIA_WIPET_SVC)
	public AuthDto authUpdateInfoByAuthCd(Integer authCd) throws Exception{
		
		LogMdc.name.accept("Auth");
		
		AuthDto authDto = null;
		try {
			//조회
			Auth auth = authRepository.findById(authCd).orElseThrow(()-> new IllegalArgumentException());
			//authDto = authRepository.authInfoByAuthcd(authCd); //query dsl
			//Auth auth = authRepository.findByAuthCd(authCd);
			
			//생성자
			authDto = new AuthDto(auth);
			authDto.setAuthCd(authCd);
	
		} catch (Exception e) {
			log.error("AuthService > authUpdateInfoByAuthCd : " + e.toString());
		}
		
		return authDto;
	}
	
	/*
	 * 수정
	 */
	@Transactional(Tx.JPA)
	@DataSource(DatabaseType.DB_MARIA_WIPET_SVC)
	public String authUpdateProcAjax(@ModelAttribute("authDto") AuthDto authDto, @AuthenticationPrincipal LoginInfo loginInfo, HttpHeader header, ModelMap model) throws Exception{
		
		LogMdc.name.accept("Auth");
		
		String result = "0"; //결과값
		
		try {
			Integer authCd = authDto.getAuthCd(); //id
			//해당 ID 조회
			Auth auth = authRepository.findById(authCd).orElseThrow(IllegalArgumentException::new);
			//Auth auth = authRepository.findById(authCd).orElseThrow(()-> new IllegalArgumentException());

			String lUptMgrId = loginInfo.getManagerId(); //수정자 ID
			authDto.setLUptMgrId(lUptMgrId); //set
//log.info("authDto 객체 =============" + authDto.toString());				
//log.info("authCd(수정처리) =============" + authDto.getAuthCd());	
			//auth = authDto.upToEntity(); //DTO -> Entity (Builder) : 생성자 생성시 업데이트 대상이 아닌 regdate등이 null로 나와서 업데이트 쿼리가 실행됨
 			auth.upToEntity(authDto);

	
//log.info("auth 객체 바뀜 =============" + auth.toString());
			
			//CRUD repo.
			authRepository.save(auth); //저장
			
			//queryDSL
			//authRepository.authUpdateProc(authDto);
			
			result = "1";
		} catch (Exception e) {
			// TODO: handle exception
			log.error("AuthService > authUpdateProcAjax : " + e.toString());
		}		
		return result;
	}
}

