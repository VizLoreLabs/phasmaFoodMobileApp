package com.vizlore.phasmafood.bluetooth.events;

/**
 * Created by smedic on 1/14/18.
 */

import android.bluetooth.BluetoothDevice;

/**
 * Event container class.  Contains connection state (whether the device is disconnected,
 * connecting, connected, or disconnecting), previous connection state, and {@link BluetoothDevice}.
 * <p>
 * Possible state values are:
 * {@link BluetoothAdapter#STATE_DISCONNECTED},
 * {@link BluetoothAdapter#STATE_CONNECTING},
 * {@link BluetoothAdapter#STATE_CONNECTED},
 * {@link BluetoothAdapter#STATE_DISCONNECTING}
 */
public class ConnectionStateEvent {

	private int state;
	private int previousState;
	private BluetoothDevice bluetoothDevice;

	public ConnectionStateEvent(int state, int previousState, BluetoothDevice bluetoothDevice) {
		this.state = state;
		this.previousState = previousState;
		this.bluetoothDevice = bluetoothDevice;
	}

	public int getState() {
		return state;
	}

	public int getPreviousState() {
		return previousState;
	}

	public BluetoothDevice getBluetoothDevice() {
		return bluetoothDevice;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ConnectionStateEvent that = (ConnectionStateEvent) o;

		if (state != that.state) return false;
		if (previousState != that.previousState) return false;
		return !(bluetoothDevice != null ? !bluetoothDevice.equals(that.bluetoothDevice)
				: that.bluetoothDevice != null);
	}

	@Override
	public int hashCode() {
		int result = state;
		result = 31 * result + previousState;
		result = 31 * result + (bluetoothDevice != null ? bluetoothDevice.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "ConnectionStateEvent{"
				+ "state="
				+ state
				+ ", previousState="
				+ previousState
				+ ", bluetoothDevice="
				+ bluetoothDevice
				+ '}';
	}
}