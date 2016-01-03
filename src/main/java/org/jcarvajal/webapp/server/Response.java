package org.jcarvajal.webapp.server;

/**
 * Response wrapper to handle a request.
 * @author JoseCH
 *
 */
public class Response {
	private byte[] response;
	private int code;
	private String redirect;
	
	public int getCode() {
		return code;
	}
	
	public void setCode(int code) {
		this.code = code;
	}
	
	public byte[] getResponse() {
		return response;
	}
	
	public void setResponse(byte[] response) {
		this.response = response;
	}
	
	public String getRedirect() {
		return redirect;
	}

	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}
}
