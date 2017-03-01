package com.witcher.videoplayerlib.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.witcher.videoplayerlib.R;
import com.witcher.videoplayerlib.entity.Definition;

import java.util.List;

/**
 * Created by witcher on 2017/3/1 0001.
 */

public class DefinitionAdapter extends BaseAdapter {

    private Context mContext;
    private List<Definition> list;
    private int mIntCurrentPosition;
    private OnItemClick onItemClick;


    public DefinitionAdapter(Context context, List<Definition> list) {
        mContext = context;
        this.list = list;
    }
    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public void setData(List<Definition> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_definition, null);
            holder = new ViewHolder();
            holder.mTvName = (TextView) convertView.findViewById(R.id.tv_name);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mTvName.setText(list.get(position).getName());
        if (position == mIntCurrentPosition) {
            holder.mTvName.setBackgroundResource(R.drawable.definition_true);
            holder.mTvName.setTextColor(mContext.getResources().getColor(R.color.main_pink));
        } else {
            holder.mTvName.setBackgroundResource(R.color.transparent100);
            holder.mTvName.setTextColor(mContext.getResources().getColor(R.color.white));
        }
        holder.mTvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntCurrentPosition = position;
                DefinitionAdapter.this.notifyDataSetChanged();
                if(onItemClick!=null){
                    onItemClick.onClick(position);
                }
            }
        });
        convertView.setTag(holder);
        return convertView;
    }

    public interface OnItemClick{
        void onClick(int position);
    }

    final class ViewHolder {
        TextView mTvName;
    }
}
