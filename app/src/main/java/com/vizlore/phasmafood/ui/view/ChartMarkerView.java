package com.vizlore.phasmafood.ui.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.vizlore.phasmafood.R;

/**
 * @author Stevan Medic
 * <p>
 * Created on Nov 2018
 */
public class ChartMarkerView extends MarkerView {

	private TextView value;

	public ChartMarkerView(@NonNull Context context) {
		super(context, R.layout.chart_marker_layout);
		value = findViewById(R.id.value);
	}

	// callbacks every time the MarkerView is redrawn, can be used to update user-interface
	@Override
	public void refreshContent(Entry e, Highlight highlight) {
		final String val = getContext().getString(R.string.waveMeasurementMarker,
			String.valueOf(e.getX()), String.valueOf(e.getY()));
		value.setText(val);
	}

	@Override
	public MPPointF getOffset() {
		return new MPPointF(-(getWidth() / 2), -getHeight());
	}
}