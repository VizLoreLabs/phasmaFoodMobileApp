package com.vizlore.phasmafood.bluetooth.events;

/**
 * Created by smedic on 1/14/18.
 */

import android.bluetooth.BluetoothDevice;

/**
 * Event container class.  Contains broadcast ACL action and {@link BluetoothDevice}.
 * <p>
 * Possible broadcast ACL action values are:
 * {@link BluetoothDevice#ACTION_ACL_CONNECTED},
 * {@link BluetoothDevice#ACTION_ACL_DISCONNECT_REQUESTED},
 * {@link BluetoothDevice#ACTION_ACL_DISCONNECTED}
 */
public class AclEvent {

	private String action;
	private BluetoothDevice bluetoothDevice;

	public AclEvent(String action, BluetoothDevice bluetoothDevice) {
		this.action = action;
		this.bluetoothDevice = bluetoothDevice;
	}

	public String getAction() {
		return action;
	}

	public BluetoothDevice getBluetoothDevice() {
		return bluetoothDevice;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		AclEvent that = (AclEvent) o;

		if (action != null && !action.equals(that.action)) return false;
		return !(bluetoothDevice != null ? !bluetoothDevice.equals(that.bluetoothDevice)
				: that.bluetoothDevice != null);
	}

	@Override
	public int hashCode() {
		int result = action != null ? action.hashCode() : 0;
		result = 31 * result + (bluetoothDevice != null ? bluetoothDevice.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "AclEvent{" +
				"action=" + action +
				", bluetoothDevice=" + bluetoothDevice +
				'}';
	}
}
