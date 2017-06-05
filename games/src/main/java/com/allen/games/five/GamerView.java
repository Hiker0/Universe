package com.allen.games.five;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.allen.games.R;

public class GamerView extends RelativeLayout {

    public GamerView(Context context) {
        this(context, null, 0);
        // TODO Auto-generated constructor stub
    }

    
    public GamerView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
        // TODO Auto-generated constructor stub
    }

    public GamerView(Context context, AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
        LayoutInflater.from(context).inflate(R.layout.gamer_view, this);
        
    }

}
