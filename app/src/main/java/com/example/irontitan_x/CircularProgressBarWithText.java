package com.example.irontitan_x;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class CircularProgressBarWithText extends ConstraintLayout {
    private ProgressBar progressBar;
    private TextView textView;

    public CircularProgressBarWithText(Context context) {
        super(context);
        init();
    }

    public CircularProgressBarWithText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircularProgressBarWithText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.activity_circular_progress_bar_with_text, this);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.text_remaining);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // You can customize your drawing here if needed
    }

    public void setProgress(int progress) {
        progressBar.setProgress(progress);
    }

    public void setText(String text) {
        textView.setText(text);
    }
}
