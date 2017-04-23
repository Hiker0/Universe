package com.allen.test.info;

import com.allen.test.R;

import android.app.ListActivity;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AvailableFeatures extends ListActivity {
    PackageManager pm;
    FeatureInfo[] features;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        
        pm = getPackageManager();
        features=pm.getSystemAvailableFeatures();
        getListView().setAdapter(new FeatureAdapter());
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
    
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }
    
    class FeatureAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return features.length;
        }

        @Override
        public FeatureInfo getItem(int position) {
            return features[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(),
                        R.layout.item_list_feature, null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            FeatureInfo item = getItem(position);
            holder.iv_name.setText(item.name);
            
            //holder.iv_detail.setText();
            

            return convertView;
        }

        class ViewHolder {
            TextView iv_name;
            TextView iv_detail;

            public ViewHolder(View view) {
                iv_name = (TextView) view.findViewById(R.id.name);
                iv_detail = (TextView) view.findViewById(R.id.details);

                view.setTag(this);
            }
        }
    }

}
