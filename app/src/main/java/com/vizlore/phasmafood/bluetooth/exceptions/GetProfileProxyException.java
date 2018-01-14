package com.vizlore.phasmafood.bluetooth.exceptions;

/**
 * Created by smedic on 1/14/18.
 */

/**
 * Thrown when {@link BluetoothAdapter#getProfileProxy} returns true, which means that connection
 * to bluetooth profile failed.
 */
public class GetProfileProxyException extends RuntimeException {

	public GetProfileProxyException() {
		super("Failed to get profile proxy");
	}
}