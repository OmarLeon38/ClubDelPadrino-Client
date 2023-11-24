package com.upao.cliente.clubdelpadrino_client.pruebaGrid;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

public class CustomGridView extends GridView {

    public CustomGridView(Context context) {
        super(context);
        init();
    }

    public CustomGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        // Permite el desplazamiento horizontal
        setHorizontalScrollBarEnabled(true);
        setVerticalScrollBarEnabled(false);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // Permite que el padre (ScrollView) maneje el desplazamiento
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.onInterceptTouchEvent(ev);
    }
}

