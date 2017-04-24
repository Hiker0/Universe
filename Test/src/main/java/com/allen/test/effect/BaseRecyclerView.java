package com.allen.test.effect;

import java.util.Random;

import com.allen.test.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class BaseRecyclerView extends Activity implements OnClickListener {
	Button addButton ,removeButton;
	MyRecyclerAdapter myAdapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_recycler_view);
        
        addButton=(Button) this.findViewById(R.id.btn_add);
        addButton.setOnClickListener(this);
        removeButton=(Button) this.findViewById(R.id.btn_remove);
        removeButton.setOnClickListener(this);
        
        RecyclerView recycler = (RecyclerView) this.findViewById(R.id.recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,OrientationHelper.HORIZONTAL, false);
        myAdapter = new MyRecyclerAdapter(this,10);
        recycler.setAdapter(myAdapter);
        
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setLayoutManager(layoutManager);
    }
    
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		if(id == R.id.btn_add){
			myAdapter.addData(2);
		}else if(id == R.id.btn_remove){
			myAdapter.removeData(2);
		}
	}

     class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {
         

         private Context mContext;
         private LayoutInflater inflater;
         private int number;
         MyRecyclerAdapter(Context context,int num){
                this. mContext=context;
                inflater=LayoutInflater. from(mContext);
                number = num;
         }
         
         @Override
         public int getItemCount() {
               
                return number;
         }
         
         public void addData(int position) {
             //mDatas.add(position, "Insert One");
        	 number= number+1;
             notifyItemInserted(position);
         }

         public void removeData(int position) {
             //mDatas.remove(position);
        	 if(number > position){
        		 number= number-1;
        	 }
             notifyItemRemoved(position);
         }


         @Override
         public void onBindViewHolder(MyViewHolder holder, final int position) {
               
               holder.t_name.setText("gmail");
               holder.t_size.setText("( "+position+" )");
               holder.t_image.setImageResource(R.drawable.animated_android);
               
               Random r = new Random();
               int i = r.nextInt(100);
               ViewGroup.LayoutParams lp = holder.t_image.getLayoutParams();
               lp.width = 200+i;
               lp.height= 200;
               holder.t_image.setLayoutParams(lp);
               holder.t_image.setBackgroundColor(0xff556600);
         }


         @Override
         public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
               
               View view = inflater.inflate(R.layout.image_item2,parent, false);
               MyViewHolder holder= new MyViewHolder(view);
                return holder;
         }
         
         class MyViewHolder extends ViewHolder{
               
               TextView t_name;
               TextView t_size;
               ImageView t_image;

                public MyViewHolder(View view) {
                     super(view);
                     t_name=(TextView) view.findViewById(R.id.id_name);
                     t_size=(TextView) view.findViewById(R.id.id_size);
                     t_image=(ImageView) view.findViewById(R.id.id_image);
               }
               
         }

    }

}

