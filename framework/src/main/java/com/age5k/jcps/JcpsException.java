package com.age5k.jcps;

/**
 * 
 * @author Wu
 *
 */
public class JcpsException extends RuntimeException {

	public JcpsException() {
		super();
	}

	public JcpsException(String message, Throwable cause) {
		super(message, cause);
	}

	public JcpsException(String message) {
		super(message);
	}

	public JcpsException(Throwable cause) {
		super(cause);
	}

	public static RuntimeException toRtException(Throwable t) {

		if (t instanceof RuntimeException) {
			return (RuntimeException) t;
		} else {
			return new JcpsException(t);
		}
	}

	public static JcpsException toRtException(String string) {
		//
		return new JcpsException(string);
	}

}
