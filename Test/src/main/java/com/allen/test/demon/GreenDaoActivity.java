package com.allen.test.demon;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.allen.test.DaoApplication;
import com.allen.test.R;
import com.allen.test.data.greendao.DaoSession;
import com.allen.test.data.greendao.User;
import com.allen.test.data.greendao.UserDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johnson on 2017-04-24.
 */
public class GreenDaoActivity extends Activity {
    UserDao mDao;
    private QueryBuilder<User> usersQuery;
    private ListView mListView;
    private Button mBtnAdd;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_green_dao);
        DaoSession daoSession = ((DaoApplication) getApplication()).getDaoSession();
        mDao = daoSession.getUserDao();
        usersQuery =  mDao.queryBuilder().orderAsc(UserDao.Properties.Id);
        mListView =(ListView) findViewById(R.id.list);
        mBtnAdd = (Button)findViewById(R.id.btn_add);
        mBtnAdd.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                addUser();
            }
        });
        userAdapter = new UserAdapter();
        updateUserss();
        mListView.setAdapter(userAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               User user = userAdapter.getItem(position);
                Long userId = user.getId();
                mDao.deleteByKey(userId);
                updateUserss();
            }
        });
    }
    private void updateUserss() {
        List<User> notes = usersQuery.list();
        userAdapter.setUser(notes);
    }

    private void addUser() {

        User user = new User();
        user.setUsername("123");
        user.setNickname("qwe");
        mDao.insert(user);
        updateUserss();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    class UserAdapter extends BaseAdapter{
        List<User> notes = new ArrayList<>();
        UserAdapter(){

        }

        void setUser(List<User>  notes){
            this.notes = notes;
            notifyDataSetChanged();
        }
        @Override
        public int getCount() {
            return notes.size();
        }

        @Override
        public User getItem(int position) {
            return notes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(GreenDaoActivity.this);
            ViewHolder holder;
            View view;
            if(convertView != null){
                view = (View) convertView;
                holder = (ViewHolder) convertView.getTag();
            }else{
                view = inflater.inflate(R.layout.item_list_user, null);
                holder = new ViewHolder();
                holder.name = (TextView)view.findViewById(R.id.tv_name);
                holder.id = (TextView)view.findViewById(R.id.tv_id);
                holder.nickname = (TextView)view.findViewById(R.id.tv_nickname);
                view.setTag(holder);
            }
            User user= getItem(position);
            holder.setName(user.getUsername());
            holder.setId(""+user.getId());
            holder.setNickName(user.getNickname());
            return view;
        }

        class ViewHolder {
            TextView name;
            TextView id;
            TextView nickname;

            void setName(String text) {
                name.setText(text);
            }
            void setId(String text) {
                id.setText(text);
            }
            void setNickName(String text) {
                nickname.setText(text);
            }
        }
    }
}
