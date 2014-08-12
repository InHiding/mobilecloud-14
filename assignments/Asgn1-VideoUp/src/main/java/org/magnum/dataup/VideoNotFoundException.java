package org.magnum.dataup;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="No such Video")  // 404
public class VideoNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5799787002794264506L;
	
	public VideoNotFoundException(String msg) {
		super(msg);
	}
}
