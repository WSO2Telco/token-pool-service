/*
 * Copyright (c) 2016, WSO2 Inc. (http://wso2.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wso2telco.dep.tpservice.rest;

import java.sql.SQLException;
import java.util.Iterator;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wso2telco.dep.tpservice.manager.WhoManager;
import com.wso2telco.dep.tpservice.model.AddNewSpDto;
import com.wso2telco.dep.tpservice.model.TokenDTO;
import com.wso2telco.dep.tpservice.model.WhoDTO;
import com.wso2telco.dep.tpservice.pool.PoolFactory;
import com.wso2telco.dep.tpservice.pool.TokenControllable;
import com.wso2telco.dep.tpservice.pool.TokenPool;
import com.wso2telco.dep.tpservice.util.exception.TokenException;
import com.wso2telco.dep.tpservice.util.exception.WhoException;

@Api(value = "/tokenservice", description = "This service provide a rest service for providing a valid token")
@Path("/tokenservice")
public class TokenPoolService {
	Logger log = LoggerFactory.getLogger(TokenPoolService.class);

	@GET
	@Path("/{ownerID}")
	@ApiOperation(value = "Get the valid token", notes = "API for return the contact by given the id", response = Response.class)
	public Response get(
			@ApiParam(value = "owner Id of token to fetch", required = true) @PathParam("ownerID") String id) {
		// TODO: Implementation for HTTP GET request

		try {
			TokenPool pool = PoolFactory.getInstance().getManagager().getOwnerController(id).getTokenPool();
			String token = pool.accqureToken().getAccessToken();
			return Response.status(Response.Status.OK).entity(token).build();
		} catch (TokenException e) {
			log.error("", e);
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(e.getErrorType().getCode() + ":" + e.getErrorType().getMessage()).build();
		}

	}
	/*
	 * @POST
	 * 
	 * @Path("/{ownerid}")
	 * 
	 * @ApiOperation(value = "New token details", notes =
	 * "API for update token create ")
	 * 
	 * @ApiResponses({
	 * 
	 * @ApiResponse(code = 201, message = "Successfully created the contact"),
	 * 
	 * @ApiResponse(code = 400, message = "Bad Request ") })
	 * 
	 * public void create(@ApiParam(value = "Owner of the token ", required =
	 * true) @PathParam("ownerid") String wonerid) {
	 * 
	 * try { PoolFactory.getInstance() .getManagager()
	 * .getImlimentation(wonerid); } catch (TokenException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } }
	 */

	@PUT
	@Path("/refresh/{ownerId}/{tokenID}")
	@ApiOperation(value = "Re -fresh  token ", notes = "Re genarate the token using refresh token ", response = Response.class)
	public Response put(@ApiParam(value = "token id to update ", required = true) @PathParam("ownerId") String ownerId,
			@ApiParam(value = "new refreshTime to update ", required = true) @PathParam("tokenID") String tokenID) {
		try {
			log.debug(" calling refresh request for :" + ownerId + " tokenID:" + tokenID);
			TokenControllable tokenPoolImpl = PoolFactory.getInstance()
														.getManagager()
														.getOwnerController(ownerId)
														.getTokenController(tokenID);
			TokenDTO obj = tokenPoolImpl.refreshToken(tokenID);
			return Response.status(Response.Status.OK).entity(obj.getAccessToken()).build();

		} catch (TokenException e) {
			// TODO Auto-generated catch block
			log.error("", e);
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(e.getErrorType().getCode() + ":" + e.getErrorType().getMessage()).build();
			// e.printStackTrace();
		}
	}

	@PUT
	@Path("restart/{ownerId}")
	@ApiOperation(value = "Re start the owner's pool ", notes = "This will restart the pool for given owner.All the values including owner details load from the persistance layer", response = Response.class)
	public Response restart(
			@ApiParam(value = "token id to update ", required = true) @PathParam("ownerId") String ownerId) {
		try {
			log.debug(" calling re -start   :" + ownerId);
			PoolFactory.getInstance().getManagager().restart(ownerId);

			return Response.status(Response.Status.OK).entity("Token pool restartd for :" + ownerId).build();

		} catch (TokenException e) {
			log.error("", e);
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(e.getErrorType().getCode() + ":" + e.getErrorType().getMessage()).build();
		}
	}
	
	
	@POST
	@Path("/refresh/{ownerId}/{tokenID}")
	@ApiOperation(value = "Add New Token to new Owner Id", notes = "Token pool will store the new token for the new owner ", response = Response.class)
	public Response add(@ApiParam(value = "Owner Id to  Add", required = true) @PathParam("ownerId") String ownerId,
			@ApiParam(value = "Token to Add", required = true) @PathParam("tokenID") String tokenID) {
		try {
			log.debug(" calling add token request for :" + ownerId + " tokenID:" + tokenID);
			TokenControllable tokenPoolImpl = PoolFactory.getInstance()
														.getManagager()
														.getOwnerController(ownerId)
														.getTokenController(tokenID);
			TokenDTO obj = tokenPoolImpl.refreshToken(tokenID);
			return Response.status(Response.Status.OK).entity(obj.getAccessToken()).build();

		} catch (TokenException e) {
			// TODO Auto-generated catch block
			log.error("", e);
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(e.getErrorType().getCode() + ":" + e.getErrorType().getMessage()).build();
			// e.printStackTrace();
		}
	}

	/*@DELETE
	@Path("/{ownerID}/{tokenID}")
	@ApiOperation(value = "Delete token ", notes = "Delete token ")
	public void delete(
			@ApiParam(value = "owner Id of the token ", required = true) @PathParam("ownerID") String ownerID,
			@ApiParam(value = "token Id  to delete ", required = true) @PathParam("tokenid") String tokenid) {

		try {
			TokenControllable tokenPoolImpl = PoolFactory.getInstance().getManagager().getOwnerController(ownerID);
			tokenPoolImpl.removeToken(tokenid);
		} catch (TokenException e) {
			log.warn("delete token ", e);
		}

	}*/
	
	@POST
	@Path("/add/sp")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Add New Mig Sp ", notes = "Token pool will store the new Mig sp ")
	public Response addMigSp(AddNewSpDto addNewSpDto ) {
		
		WhoManager whoManager = new WhoManager();
		try {
			whoManager.addOwnerWithTokens(addNewSpDto);
		} catch (SQLException e) {
			e.printStackTrace();
			return Response.status(Response.Status.EXPECTATION_FAILED).entity("Error").build();
		}
			return Response.status(Response.Status.CREATED).entity("Ok").build();
	}
}
