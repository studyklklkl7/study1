package com.tenminds.domain.common.pg.model;


import com.tenminds.domain.common.pg.entity.Bank;
import com.tenminds.domain.common.pg.entity.BankId;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@NoArgsConstructor
@ToString
public class BankReponseDto { 

	private Integer pgCd;
	
	private String pgBankCd;
	
	private String bankNm;
	
	private Integer wipetBankCd;
	
	
	public BankReponseDto(Bank bank){
		this.pgCd = bank.getPgCd();
		this.pgBankCd = bank.getPgBankCd();
		this.bankNm = bank.getBankNm();
		this.wipetBankCd = bank.getWipetBankCd();
		
	}


}