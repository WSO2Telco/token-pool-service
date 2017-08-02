package com.wso2telco.dep.tpservice.dao;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.CreateSqlObject;
import org.skife.jdbi.v2.sqlobject.Transaction;
import org.skife.jdbi.v2.sqlobject.mixins.GetHandle;

import com.wso2telco.dep.tpservice.model.TokenDTO;
import com.wso2telco.dep.tpservice.model.WhoDTO;

abstract class OwnerHandler implements GetHandle {

	@CreateSqlObject
	abstract PersistableWho persistableWho();
	@CreateSqlObject
	abstract PersistableToken  persistableToken();
	
	public WhoDTO create(final WhoDTO whoDTO) {
		persistableWho().create(whoDTO.getOwnerId(), whoDTO.getTokenUrl(),whoDTO.getDefaultConnectionRestTime(),1,whoDTO.getRetryAttmpt() , whoDTO.getMaxRetryCount(),whoDTO.getRetryDelay());
		int did = persistableWho().getWhoDid(whoDTO.getOwnerId());
		whoDTO.setId(did);
		return whoDTO;

	}
	@Transaction
	public WhoDTO createWhoAndTokens(final WhoDTO whoDTO,final List<TokenDTO> tokenDTOs) {
		persistableWho().create(whoDTO.getOwnerId(), whoDTO.getTokenUrl(),whoDTO.getDefaultConnectionRestTime(),1,whoDTO.getRetryAttmpt() , whoDTO.getMaxRetryCount(),whoDTO.getRetryDelay());
		int did = persistableWho().getWhoDid(whoDTO.getOwnerId());
		for (TokenDTO tokenDTO : tokenDTOs) {
			persistableToken().insert(did,tokenDTO.getTokenAuth(),tokenDTO.getTokenValidity(), true, tokenDTO.getAccessToken(), tokenDTO.getRefreshToken());
		}	
		return whoDTO;

	}

}
