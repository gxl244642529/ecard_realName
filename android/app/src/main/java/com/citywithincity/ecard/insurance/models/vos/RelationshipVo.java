package com.citywithincity.ecard.insurance.models.vos;

import java.io.Serializable;

public class RelationshipVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String relation;
	public int id;
	
	@Override
	public String toString() {
		return relation;
	}
}
