package org.heigit.bigspatialdata.ohsome.springBootWebAPI.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception class corresponding to the HTTP status code 401.
 *
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public UnauthorizedException(String message) {
		super(message);
	}

}
