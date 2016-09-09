/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2016 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.pocketmusic.ui;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.TableRow;

import org.catrobat.catroid.R;
import org.catrobat.catroid.pocketmusic.note.MusicalBeat;
import org.catrobat.catroid.pocketmusic.note.trackgrid.GridRow;

import org.catrobat.catroid.pocketmusic.note.trackgrid.GridRowPosition;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TrackRowView extends TableRow {

	public static final int QUARTER_COUNT = 4;
	private final MusicalBeat beat;
	private List<NoteView> noteViews = new ArrayList<>(QUARTER_COUNT);
	private boolean isBlackRow;

	public TrackRowView(Context context) {
		this(context, MusicalBeat.BEAT_4_4, false, null);
	}

	public TrackRowView(Context context, MusicalBeat beat, boolean isBlackRow, GridRow gridRow) {
		super(context);
		this.beat = beat;
		this.setBlackRow(isBlackRow);
		initializeRow();
		setWeightSum(QUARTER_COUNT);
		updateGridRow(gridRow);
	}

	public void updateGridRow(GridRow gridRow) {
		if (gridRow == null) {
			return;
		}
		for (int i = 0; i < gridRow.getGridRowPositions().get(0).size(); i++) {
			GridRowPosition position = gridRow.getGridRowPositions().get(0).get(i);
			if (position != null) {
				BigDecimal divident = new BigDecimal(position.getNoteLength().toMilliseconds(1));
				BigDecimal divisor = new BigDecimal(beat.getNoteLength().toMilliseconds(1));
				long length = divident.divide(divisor, BigDecimal.ROUND_HALF_UP).longValue();
				for (int j = position.getColumnStartIndex(); j < position.getColumnStartIndex() + length; j++) {
					NoteView noteView = noteViews.get(j);
					if (!noteView.isToggled()) {
						noteView.onClick(null);
					}
				}
			}
		}
	}

	private void initializeRow() {
		LayoutParams params = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);
		params.leftMargin = params.topMargin = params.rightMargin = params.bottomMargin = 2;
		int noteColor;
		if (isBlackRow) {
			noteColor = ContextCompat.getColor(getContext(), R.color.light_grey);
		} else {
			noteColor = ContextCompat.getColor(getContext(), R.color.white);
		}
		for (int i = 0; i < QUARTER_COUNT; i++) {
			noteViews.add(new NoteView(getContext(), noteColor));
			addView(noteViews.get(i), params);
		}
	}

	public List<NoteView> getNoteViews() {
		return noteViews;
	}

	public int getTactCount() {
		return QUARTER_COUNT;
	}

	public void setBlackRow(boolean blackRow) {
		isBlackRow = blackRow;
	}
}
