package com.vizlore.phasmafood.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vizlore.phasmafood.R;

import java.io.File;
import java.util.List;

/**
 * @author Stevan Medic
 * <p>
 * Created on Sep 2019
 */
public class ZipImagesAdapter extends RecyclerView.Adapter<ZipImagesAdapter.ViewHolder> {

	private List<File> images;
	private OnImageClickListener onImageClickListener;

	public ZipImagesAdapter(List<File> devices) {
		this.images = devices;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.zip_image_layout, parent, false);
		return new ViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
		final File file = images.get(position);
		Picasso.get().load(new File(file.getAbsolutePath())).into(holder.imageView);
		holder.imageTitle.setText(file.getName());
		holder.itemView.setOnClickListener(view -> {
			if (onImageClickListener != null) {
				onImageClickListener.onImageClick(file.getAbsolutePath());
			}
		});
	}

	@Override
	public int getItemCount() {
		return images.size();
	}

	class ViewHolder extends RecyclerView.ViewHolder {
		ImageView imageView;
		TextView imageTitle;

		ViewHolder(View view) {
			super(view);
			imageView = view.findViewById(R.id.zipImage);
			imageTitle = view.findViewById(R.id.zipImageTitle);
		}
	}

	public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
		this.onImageClickListener = onImageClickListener;
	}

	public interface OnImageClickListener {
		void onImageClick(String path);
	}
}
