package com.vizlore.phasmafood.bluetooth.events;

/**
 * Created by smedic on 1/14/18.
 */

import android.bluetooth.BluetoothDevice;

/**
 * Event container class.  Contains bond state (whether the device is unbonded, bonding, or bonded),
 * previous bond state, and {@link BluetoothDevice}.
 * <p>
 * Possible state values are:
 * {@link BluetoothDevice#BOND_NONE},
 * {@link BluetoothDevice#BOND_BONDING},
 * {@link BluetoothDevice#BOND_BONDED}
 */
public class BondStateEvent {

	private int state;
	private int previousState;
	private BluetoothDevice bluetoothDevice;

	public BondStateEvent(int state, int previousState, BluetoothDevice bluetoothDevice) {
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

		BondStateEvent that = (BondStateEvent) o;

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
		return "BondStateEvent{" +
				"state=" + state +
				", previousState=" + previousState +
				", bluetoothDevice=" + bluetoothDevice +
				'}';
	}
}