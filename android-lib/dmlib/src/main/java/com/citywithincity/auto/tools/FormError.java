package com.citywithincity.auto.tools;

public class FormError extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FormError(String error) {
		super(error);
	}
}