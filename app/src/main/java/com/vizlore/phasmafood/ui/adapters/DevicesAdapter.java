package com.vizlore.phasmafood.ui.adapters;

import android.arch.lifecycle.LiveData;
import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.vizlore.phasmafood.R;
import com.vizlore.phasmafood.utils.SingleLiveEvent;

import java.util.List;

import static com.vizlore.phasmafood.ui.adapters.DevicesAdapter.AdapterListType.PAIRED_DEVICES;

/**
 * Created by smedic on 1/18/18.
 */

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.ViewHolder> {

	private static final String TAG = "SMEDIC";

	private SingleLiveEvent<Pair<Integer, AdapterAction>> clickEventLiveData = new SingleLiveEvent<>();
	private List<BluetoothDevice> devices;
	private AdapterListType adapterListType = PAIRED_DEVICES;

	public enum AdapterListType {
		PAIRED_DEVICES,
		AVAILABLE_DEVICES
	}

	public enum AdapterAction {
		ACTION_STATUS,
		ACTION_PAIR,
		ACTION_CONFIG
	}

	public DevicesAdapter(List<BluetoothDevice> devices) {
		this.devices = devices;
	}

	public DevicesAdapter(AdapterListType adapterListType, List<BluetoothDevice> devices) {
		this.adapterListType = adapterListType;
		this.devices = devices;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_device, parent, false);
		return new ViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
		BluetoothDevice device = devices.get(position);

		if (device != null && device.getName() != null) {
			holder.name.setText(!device.getName().isEmpty() ? device.getName() : "No name");
		} else {
			holder.name.setText("No name");
		}

		if (adapterListType == PAIRED_DEVICES) {
			holder.status.setText("Connect");
		} else if (adapterListType == AdapterListType.AVAILABLE_DEVICES) {
			holder.status.setText("Pair");
		}

		if (adapterListType == PAIRED_DEVICES) {
			holder.config.setText("Disconnect");
		} else if (adapterListType == AdapterListType.AVAILABLE_DEVICES) {
			holder.config.setText("Config");
		}

		holder.status.setOnClickListener(view -> post(new Pair<>(position, AdapterAction.ACTION_PAIR)));

		holder.config.setOnClickListener(view -> post(new Pair<>(position, AdapterAction.ACTION_CONFIG)));
	}

	@Override
	public int getItemCount() {
		return devices.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		public TextView name;
		public Button status;
		public Button config;

		public ViewHolder(View view) {
			super(view);
			name = view.findViewById(R.id.title);
			status = view.findViewById(R.id.status);
			config = view.findViewById(R.id.config);
		}
	}

	public LiveData<Pair<Integer, AdapterAction>> getClickEventObservable() {
		return clickEventLiveData;
	}

	public void post(Pair<Integer, AdapterAction> actionId) {
		clickEventLiveData.setValue(actionId);
	}
}
