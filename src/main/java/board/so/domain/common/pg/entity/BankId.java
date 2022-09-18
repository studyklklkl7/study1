package com.tenminds.domain.common.pg.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BankId implements Serializable {
	@Column
	private Integer pgCd;
	
	@Column
	private String pgBankCd;
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankId contCustAgreeId = (BankId) o;
        return pgBankCd == contCustAgreeId.pgBankCd && Objects.equals(pgCd, contCustAgreeId.pgBankCd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pgCd, pgBankCd);
    }
    
}
