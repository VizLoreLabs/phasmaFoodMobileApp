package com.vizlore.phasmafood.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vizlore.phasmafood.R;
import com.vizlore.phasmafood.model.results.BitmapWrapper;

import java.util.List;

/**
 * @author Stevan Medic
 * <p>
 * Created on Sep 2019
 */
public class ZipImagesAdapter extends RecyclerView.Adapter<ZipImagesAdapter.ViewHolder> {

	private List<BitmapWrapper> images;
	private OnImageClickListener onImageClickListener;

	public ZipImagesAdapter(List<BitmapWrapper> bitmaps) {
		this.images = bitmaps;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.zip_image_layout, parent, false);
		return new ViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
		final BitmapWrapper bitmapWrapper = images.get(position);
		holder.imageTitle.setText(bitmapWrapper.getBitmapName());
		holder.imageView.setImageBitmap(bitmapWrapper.getBitmap());
		holder.imageView.setOnClickListener(view -> {
			if (onImageClickListener != null) {
				onImageClickListener.onImageClick(bitmapWrapper);
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
		void onImageClick(BitmapWrapper bitmap);
	}
}
