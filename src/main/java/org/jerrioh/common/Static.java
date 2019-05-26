package org.jerrioh.common;

public abstract class Static {
	protected Static() {
		this.throwIllegalStateException();
	}
	
	private void throwIllegalStateException() {
		throw new IllegalStateException("Static class");
	}

}
