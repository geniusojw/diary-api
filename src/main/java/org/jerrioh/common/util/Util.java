package org.jerrioh.common.util;

public abstract class Util {
	protected Util() {
		this.throwIllegalStateException();
	}
	
	private void throwIllegalStateException() {
		throw new IllegalStateException("Utility class");
	}
	
	private void setUp() {
		throw new IllegalStateException("Utility class");
	}
}
