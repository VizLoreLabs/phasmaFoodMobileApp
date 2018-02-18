package com.vizlore.phasmafood.bluetooth.exceptions;

import java.io.IOException;

/**
 * Created by smedic on 2/17/18.
 */

public class ConnectionErrorException extends IOException {

	public ConnectionErrorException(String error) {
		super("Connection error: " + error);
	}

}
