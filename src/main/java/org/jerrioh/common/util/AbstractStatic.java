package org.jerrioh.common.util;

public abstract class AbstractStatic {
	protected AbstractStatic() {
		this.throwIllegalStateException();
	}
	
	private void throwIllegalStateException() {
		throw new IllegalStateException("Utility class");
	}
}
