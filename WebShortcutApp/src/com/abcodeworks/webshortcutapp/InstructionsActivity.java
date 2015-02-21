package com.abcodeworks.webshortcutapp;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class InstructionsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_instructions);
        TextView mTextview = (TextView)findViewById(R.id.activity_instructions_text);
        mTextview.setText(
               Html.fromHtml(
                   getString(R.string.instructions_html)));
    }
}
