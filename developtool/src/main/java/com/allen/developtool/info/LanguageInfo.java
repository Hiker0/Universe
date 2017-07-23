package com.allen.developtool.info;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.allen.developtool.R;

public class LanguageInfo extends Activity {
    
    TextView countryView;
    TextView langageView; 
    ListView availableList;
    ArrayList<Locale> mList;
    ListAdapter mAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_info);
        countryView = (TextView) this.findViewById(R.id.current_country);
        langageView = (TextView) this.findViewById(R.id.current_language);
        availableList = (ListView) this.findViewById(R.id.list);
        
        Locale locale = this.getResources().getConfiguration().locale;
        countryView.setText("Country : "+locale.getDisplayCountry()+"("+locale.getCountry()+")");
        langageView.setText("Language: "+locale.getDisplayLanguage()+"("+locale.getLanguage()+")");
        mList=new ArrayList<Locale>();
        Locale[] locales = Locale.getAvailableLocales();
        for(Locale mlocale :locales){
            mList.add(mlocale);
        }
        mAdapter = new LocaleAdapter();
        availableList.setAdapter(mAdapter);
        
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
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

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
    
    class LocaleAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Locale getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(),
                        R.layout.item_list_locale, null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            Locale item = getItem(position);
            holder.tv_name.setText(item.toString());
            holder.tv_self.setText(item.getDisplayLanguage(item));
            holder.tv_country.setText(item.getDisplayCountry());
            holder.tv_language.setText(item.getDisplayLanguage());
            
            return convertView;
        }

        class ViewHolder {
            TextView tv_name;
            TextView tv_self;
            TextView tv_country;
            TextView tv_language;

            public ViewHolder(View view) {
                tv_country = (TextView) view.findViewById(R.id.country);
                tv_name = (TextView) view.findViewById(R.id.name);
                tv_language = (TextView) view.findViewById(R.id.language);
                tv_self = (TextView) view.findViewById(R.id.self);
                view.setTag(this);
            }
        }
    }
}
