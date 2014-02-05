/*Copyright © 2011, Continua Health Alliance.
 *All rights reserved.
 *Terms and conditions of use are stated in the Reference Code License Agreement.
 */

package org.continuaalliance.mcesl.settings;


/*******************************
Developed by Vignet for Continua.
*******************************/

public class InvalidSettingsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String exceptionMsg = "";

	public InvalidSettingsException(String exceptionMsg) {
		this.exceptionMsg = exceptionMsg;
	}

	public String toString() {
		return exceptionMsg;
	}
}
