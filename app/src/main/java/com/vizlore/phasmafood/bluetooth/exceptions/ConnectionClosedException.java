package com.vizlore.phasmafood.bluetooth.exceptions;

import java.io.IOException;

/**
 * Created by smedic on 1/14/18.
 */

public class ConnectionClosedException extends IOException {

	public ConnectionClosedException() {
		super("Connection is closed.");
	}

	public ConnectionClosedException(String message) {
		super(message);
	}
}