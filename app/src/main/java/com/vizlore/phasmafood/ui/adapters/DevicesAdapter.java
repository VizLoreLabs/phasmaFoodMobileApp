package com.vizlore.phasmafood.ui.adapters;

import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.vizlore.phasmafood.R;

import java.util.List;

/**
 * Created by smedic on 1/18/18.
 */

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.ViewHolder> {

	private static final String TAG = "SMEDIC";

	private List<BluetoothDevice> devices;

	public DevicesAdapter(List<BluetoothDevice> devices) {
		this.devices = devices;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_device, parent, false);
		return new ViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		BluetoothDevice device = devices.get(position);
		holder.name.setText(device.getName());

		holder.status.setOnClickListener(view -> {
			// TODO: 1/18/18  
		});

		holder.config.setOnClickListener(view -> {
			// TODO: 1/18/18  
		});
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
}
