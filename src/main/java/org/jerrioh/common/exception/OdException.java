package org.jerrioh.common.exception;

import java.io.Serializable;

public class OdException extends Exception {
	private static final long serialVersionUID = 1L;
	private final OdResponseType odResponseType;
	private final Serializable data;

	public OdException(OdResponseType odResponseType) {
		super();
		this.odResponseType = odResponseType;
		this.data = null;
	}

	public OdException(OdResponseType odResponseType, Serializable data) {
		super();
		this.odResponseType = odResponseType;
		this.data = data;
	}

	public OdResponseType getOdResponseType() {
		return odResponseType;
	}

	public Serializable getData() {
		return data;
	}
}
