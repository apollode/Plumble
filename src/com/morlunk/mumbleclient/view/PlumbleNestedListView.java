package com.morlunk.mumbleclient.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.morlunk.mumbleclient.view.PlumbleNestedAdapter.NestMetadataType;
import com.morlunk.mumbleclient.view.PlumbleNestedAdapter.NestPositionMetadata;

public class PlumbleNestedListView extends ListView implements OnItemClickListener {

	public interface OnNestedChildClickListener {
		public void onNestedChildClick(AdapterView<?> parent, View view, int groupPosition, int childPosition, long id);
	}
	public interface OnNestedGroupClickListener {
		public void onNestedGroupClick(AdapterView<?> parent, View view, int groupPosition, long id);
	}
	
	private PlumbleNestedAdapter mNestedAdapter;
	private OnNestedChildClickListener mChildClickListener;
	private OnNestedGroupClickListener mGroupClickListener;

	//private boolean mMaintainPosition;
	
	public PlumbleNestedListView(Context context) {
		this(context, null);
	}
	
	public PlumbleNestedListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		setOnItemClickListener(this);
		/*
		TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PlumbleNestedListView, 0, 0);
		try {
			mMaintainPosition = array.getBoolean(R.styleable.PlumbleNestedListView_maintainPosition, false);
		} finally {
			array.recycle();
		}
		*/
	}
	
	public void setAdapter(PlumbleNestedAdapter adapter) {
		super.setAdapter(adapter);
		mNestedAdapter = adapter;
	}
	
	public void expandGroup(int groupPosition) {
		mNestedAdapter.expandGroup(groupPosition);
	}
	
	public void collapseGroup(int groupPosition) {
		mNestedAdapter.collapseGroup(groupPosition);
	}

	public OnNestedChildClickListener getOnChildClickListener() {
		return mChildClickListener;
	}

	public void setOnChildClickListener(
			OnNestedChildClickListener mChildClickListener) {
		this.mChildClickListener = mChildClickListener;
	}
	
	public OnNestedGroupClickListener getOnGroupClickListener() {
		return mGroupClickListener;
	}

	public void setOnGroupClickListener(
			OnNestedGroupClickListener mGroupClickListener) {
		this.mGroupClickListener = mGroupClickListener;
	}

	@Override
	public void setOnItemClickListener(OnItemClickListener listener) {
		if(listener != this)
	        throw new RuntimeException(
	                "For PlumbleNestedListView, please use the child and group equivalents of setOnItemClickListener.");
		super.setOnItemClickListener(listener);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		NestPositionMetadata metadata = mNestedAdapter.visibleMeta.get(position);
		if(metadata.type == NestMetadataType.META_TYPE_GROUP && mGroupClickListener != null) {
			mGroupClickListener.onNestedGroupClick(parent, view, metadata.groupPosition, id);
		} else if(metadata.type == NestMetadataType.META_TYPE_ITEM && mChildClickListener != null)
			mChildClickListener.onNestedChildClick(parent, view, metadata.groupPosition, metadata.childPosition, id);
	}
}
