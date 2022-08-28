package com.tenminds.domain.cmn.auth.model;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.querydsl.core.annotations.QueryProjection;
import com.tenminds.domain.cmn.auth.entity.Auth;
import com.tenminds.domain.common.paging.PagingDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@NoArgsConstructor
@ToString
public class AuthDto extends PagingDto{ 
	
	private Integer authCd;	

	private String authName;

	private String stateCd;

	private String descript;

	private LocalDate regDate;

	private String regMgrId;
	
	private String regMgrName;
	
	private String lUptMgrId;
	
	private String lUptMgrName;
	 
	
	@QueryProjection
	public AuthDto(Integer authCd, String authName, String stateCd, String descript, String regDate, String regMgrId, String regMgrName, String lUptMgrName) {
	    this.authCd = authCd;
	    this.authName = authName;
	    this.stateCd = stateCd;
	    this.descript = descript;
	    this.regDate = LocalDate.parse(regDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	    this.regMgrId = regMgrId;
	    this.regMgrName = regMgrName;
	    this.lUptMgrName = lUptMgrName;
	}
	
	@QueryProjection
	public AuthDto(String authName, String stateCd, String descript) {
	    this.authName = authName;
	    this.stateCd = stateCd;
	    this.descript = descript;
	}


	/*
	 * @Builder public AuthDto(String authName, String stateCd, String descript,
	 * String regMgrId) { this.authName = authName; this.stateCd = stateCd;
	 * this.descript = descript; this.regMgrId = regMgrId; }
	 */
	
	//dto -> entity(resquest dto 로 받은 객체를 entity 화하여 저장하는 용도)
	public Auth regToEntity() {
		return Auth.regAuthBuilder()
				.authName(authName)
				.stateCd(stateCd)
				.descript(descript)
				.regMgrId(regMgrId)
				.build();
	}
	
	//dto -> entity(resquest dto 로 받은 객체를 entity 화하여 수정하는 용도)
	public Auth upToEntity() {
		return Auth.upAuthBuilder()
		.authCd(authCd)
		.authName(authName)
		.stateCd(stateCd)
		.descript(descript)
		.lUptMgrId(lUptMgrId)
		.build();
	}

	
	//entity -> dto (조회시 이용)
	public AuthDto(Auth auth){
		this.authName = auth.getAuthName();
		this.stateCd = auth.getStateCd();
		this.descript = auth.getDescript();
	}
	
	/*
	 * @Builder public AuthDto(Integer authCd, String authName, String stateCd,
	 * String descript, String lUptMgrId) { this.authCd = authCd; this.authName =
	 * authName; this.stateCd = stateCd; this.descript = descript; this.lUptMgrId =
	 * lUptMgrId; }
	 */
	

}
