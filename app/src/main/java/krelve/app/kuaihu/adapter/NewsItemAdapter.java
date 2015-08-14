package krelve.app.kuaihu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import krelve.app.kuaihu.R;
import krelve.app.kuaihu.model.StoriesEntity;
import krelve.app.kuaihu.util.Constant;

/**
 * Created by wwjun.wang on 2015/8/14.
 */
public class NewsItemAdapter extends BaseAdapter {
    private List<StoriesEntity> entities;
    private Context context;
    private ImageLoader mImageloader;

    public NewsItemAdapter(Context context, List<StoriesEntity> items) {
        this.context = context;
        entities = items;
        mImageloader = ImageLoader.getInstance();
    }

    @Override
    public int getCount() {
        return entities.size();
    }

    @Override
    public Object getItem(int position) {
        return entities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.news_item, parent, false);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.iv_title = (ImageView) convertView.findViewById(R.id.iv_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        StoriesEntity entity = entities.get(position);
        viewHolder.tv_title.setText(entity.getTitle());
        if (entity.getImages() != null) {
            viewHolder.iv_title.setVisibility(View.VISIBLE);
            mImageloader.displayImage(entity.getImages().get(0), viewHolder.iv_title);
        }else{
            viewHolder.iv_title.setVisibility(View.GONE);
        }
        return convertView;

    }

    public static class ViewHolder {
        TextView tv_title;
        ImageView iv_title;
    }

}