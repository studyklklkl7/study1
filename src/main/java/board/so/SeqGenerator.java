package com.tenminds.domain;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import com.tenminds.common.consts.LogMdc;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "DY_LOGGER")
public class SeqGenerator implements IdentifierGenerator, Configurable {
	
	private String seqName;
	
	  @Override
	    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {

	        // TODO Auto-generated method stub
		  	seqName = params.getProperty("seqName");
	    }
	   

	    @Override
	    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
	    	LogMdc.name.accept("Auth");
	    	// String prefix =  LocalDate.now().toString();
	    	 LocalDate date = LocalDate.now(); //오늘 날짜 LocalDate 객체 생성
	    	 DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
	    	 String prefix = date.format(dateTimeFormatter); //오늘날짜
	    	 
	         // 이 Connection 은 Hibernate 가 관리하기 때문에 직접 close는 하지 말것.
	         Connection connection = session.connection(); 
	         try {
	  
	             PreparedStatement ps = connection.prepareStatement("SELECT nextval("+seqName+") as nextval");
	  
	             ResultSet rs = ps.executeQuery();
	             if (rs.next()) {
	                 int id = rs.getInt("nextval");
	                 String code = prefix + id;
	                 log.info("code" + prefix +" ::: "+ id);
	                 return code;
	             }
	  
	         } catch (SQLException e) {
	             //log.error(e);
	             throw new HibernateException("Unable to generate Stock Code Sequence");
	         }
	         return null;

	    }


	
}
