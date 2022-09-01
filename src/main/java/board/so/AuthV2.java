package com.tenminds.domain.cmn.auth.entity;


import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.tenminds.domain.SeqGenerator;
import com.tenminds.domain.cmn.auth.model.AuthDto;
import com.tenminds.domain.common.paging.PagingDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Table(name="mgr_auth_c")
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Getter
@ToString
@EntityListeners(AuditingEntityListener.class)
public class Auth extends PagingDto{
	
	/*
	 * @Id
	 * 
	 * @GeneratedValue(strategy=GenerationType.IDENTITY)
	 */
	
	
	@Id
    @GenericGenerator(name = "idGenerator", strategy = "com.tenminds.domain.SeqGenerator",
			parameters = {
			        @Parameter(name = "seqName", value = "sq_test")
			        })
    @GeneratedValue(generator = "idGenerator")  
    @Column
	private Integer authCd;	
	
	@Column
	private String authName;
	
	@Column
	private String stateCd;
	
	@Column
	private String descript;
	
	@Column(nullable = false, updatable = false)
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	@CreatedDate
	private LocalDateTime regDate;
	//private LocalDateTime regDate = LocalDateTime.now();
	
	@Column(updatable = false)
	private String regMgrId;
	
	@Column
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	@LastModifiedDate
	private LocalDateTime lUptDate;
	
	@Column
	private String lUptMgrId;
	
	//@ManyToOne(fetch=FetchType.LAZY)
	//@JoinColumn(name="regMgrId", insertable = false, updatable = false)
	//private MgrInfo mgrInfo;
	
	//등록시
	@Builder(builderClassName = "regAuthBuilder", builderMethodName = "regAuthBuilder")
	public Auth(String authName, String stateCd, String descript, String regMgrId) {
		this.authName = authName;
		this.stateCd = stateCd;
		this.descript = descript;
		this.regMgrId = regMgrId;
	}
	
	//수정시(crud save 이용시 regdate등 값의 변경이 있어 값변경 없이 수정 할 시 업데이트 쿼리가 실행됨)
	@Builder(builderClassName = "upAuthBuilder", builderMethodName = "upAuthBuilder")
	public Auth(Integer authCd, String authName, String stateCd, String descript, String lUptMgrId) {
		this.authCd = authCd;
		this.authName = authName;
		this.stateCd = stateCd;
		this.descript = descript;
		this.lUptMgrId = lUptMgrId;
	}
	
	//수정시
	public void upToEntity(AuthDto authDto) {
		this.authCd = authDto.getAuthCd();
		this.authName = authDto.getAuthName();
		this.stateCd = authDto.getStateCd();
		this.descript = authDto.getDescript();
		this.lUptMgrId = authDto.getLUptMgrId();
	}
	 
	
}
