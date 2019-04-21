package org.jerrioh.common.util;

import org.jerrioh.common.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OdLogger extends Util {
	private static Logger logger = LoggerFactory.getLogger(OdLogger.class);

	public static void debug(String format) {
		getLogger().debug(format);
	}

	public static void debug(String format, Object arg) {
		getLogger().debug(format, arg);
	}

	public static void debug(String format, Object arg1, Object arg2) {
		getLogger().debug(format, arg1, arg2);
	}

	public static void debug(String format, Object... arguments) {
		getLogger().debug(format, arguments);
	}

	public static void debug(String msg, Throwable t) {
		getLogger().info(msg, t);
	}

	public static void info(String format) {
		getLogger().info(format);
	}

	public static void info(String format, Object arg) {
		getLogger().info(format, arg);
	}

	public static void info(String format, Object arg1, Object arg2) {
		getLogger().info(format, arg1, arg2);
	}

	public static void info(String format, Object... arguments) {
		getLogger().info(format, arguments);
	}

	public static void info(String msg, Throwable t) {
		getLogger().info(msg, t);
	}

	public static void error(String format) {
		getLogger().error(format);
	}

	public static void error(String format, Object arg) {
		getLogger().error(format, arg);
	}

	public static void error(String format, Object arg1, Object arg2) {
		getLogger().error(format, arg1, arg2);
	}

	public static void error(String format, Object... arguments) {
		getLogger().error(format, arguments);
	}

	public static void error(String msg, Throwable t) {
		getLogger().error(msg, t);
	}

	private static Logger getLogger() {
		Class<?> clazz = OdLogger.class;
		try {
			clazz = getLoggerClass();
		} catch (ClassNotFoundException e) {
			logger.error("logger class not found", e);
		}
		return LoggerFactory.getLogger(clazz);
	}

	private static Class<?> getLoggerClass() throws ClassNotFoundException {
		StackTraceElement[] stackTrace = new Throwable().getStackTrace();
		for (StackTraceElement element : stackTrace) {
			String className = element.getClassName();
			int lastIndexOf = className.lastIndexOf(Constant.DOT);
			String substring = className.substring(lastIndexOf + 1, className.length());

			if (!OdLogger.class.getSimpleName().equals(substring)) {
				return Class.forName(className);
			}
		}
		throw new ClassNotFoundException("finally not found");
	}
}
