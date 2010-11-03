package net.raescott.logging;

/**
 * Simple Spring Bean which has little functionality.  Meant to illustrate AOP
 * logging concepts.
 * @author Richard Scott Smith <rsmith@harriscomputer.com>
 */
public class MyClassToLog {
	private String attribute1;
	private String attribute2;

	/**
	 * @return the attribute1
	 */
	public String getAttribute1() {
		return attribute1;
	}

	/**
	 * @param attribute1 the attribute1 to set
	 */
	public void setAttribute1(String attribute1) {
		this.attribute1 = attribute1;
	}

	/**
	 * @return the attribute2
	 */
	public String getAttribute2() {
		return attribute2;
	}

	/**
	 * @param attribute2 the attribute2 to set
	 */
	public void setAttribute2(String attribute2) {
		this.attribute2 = attribute2;
	}

	@Override
	public String toString() {
		return attribute1 + " " + attribute2;
	}
}
