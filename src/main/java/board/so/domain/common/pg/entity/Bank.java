package com.tenminds.domain.common.pg.entity;


import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@NoArgsConstructor
@Entity
@ToString
@Table(name="pg_bank_c")
@IdClass(BankId.class)
public class Bank{
	
	
	/*
	 * @EmbeddedId private PgCdPgBankCdId pgCdPgBankCdId; //복합키 (PG사 코드, PG사 은행코드)
	 */	
	@Id
	@Column
	private Integer pgCd;
	
	@Id
	@Column
	private String pgBankCd;
	
	@Column
	private String bankNm;
	
	@Column
	private Integer isUse;
	
	@Column
	private Integer wipetBankCd;
	 
	
}