package com.tenminds.service.common.pg;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tenminds.common.configuration.database.DataSource;
import com.tenminds.common.configuration.database.DatabaseType;
import com.tenminds.common.consts.LogMdc;
import com.tenminds.domain.common.pg.entity.Bank;
import com.tenminds.domain.common.pg.entity.BankId;
import com.tenminds.domain.common.pg.model.BankReponseDto;
import com.tenminds.repository.common.pg.BankRepos;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "DY_LOGGER")
@RequiredArgsConstructor
@Service
public class BankService {	
	
	//repository
	private final BankRepos bankRepository;
	
	@DataSource(DatabaseType.DB_MARIA_WIPET_SVC)
	public List<BankReponseDto> bankListByPgCdIsUseTest(Integer pgCd, Integer isUse) {
		LogMdc.name.accept("pg-bank");
		List<BankReponseDto> list = new ArrayList<>();
		try {
			//은행코드 리스트
			List<Bank> bankList =	bankRepository.findAllByPgCdAndIsUse(pgCd, isUse);

			BankReponseDto bankDto = null;

			for(Bank bank : bankList){

				bankDto = new BankReponseDto();
				bankDto.setPgBankCd(bank.getPgBankCd()); 	//pg사 은행코드
				bankDto.setBankNm(bank.getBankNm()); 		//은행명
				bankDto.setWipetBankCd(bank.getWipetBankCd()); //우리펫 은행코드

				list.add(bankDto);
			}
					
		} catch (Exception e) {
			log.error("BankService > bankListByPgCdIsUseTest : " + e.toString());
		}
		return list;
	}

	
	@DataSource(DatabaseType.DB_MARIA_WIPET_SVC)
	public List<BankReponseDto> bankListByPgCdIsUse(Integer pgCd, Integer isUse) {
		LogMdc.name.accept("pg-bank");
		List<BankReponseDto> list = new ArrayList<>();
		try {
			//은행코드 리스트
			 return bankRepository.findAllByPgCdAndIsUse(pgCd, isUse).stream()
					 	.map(Bank -> new BankReponseDto(Bank))
		                .collect(Collectors.toList());
					
		} catch (Exception e) {
			log.error("BankService > bankListByPgCdIsUse : " + e.toString());
		}
		return list;
	}
	
	
	
	
	
	
}

