package com.tenminds.repository.common.pg;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tenminds.domain.common.pg.entity.Bank;
import com.tenminds.domain.common.pg.entity.BankId;

public interface BankRepos extends JpaRepository <Bank, BankId>, IBankRepos {
	
	List<Bank> findAllByPgCdAndIsUse(Integer pgCd, Integer isUse);

}