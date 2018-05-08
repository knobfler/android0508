package com.sample.contentproviderexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InputActivity extends AppCompatActivity {

    private EditText editText;
    private EditText editText2;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        editText = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText2);

        button = (Button) findViewById(R.id.button);

        Intent i = getIntent();
        String country = i.getStringExtra("country");
        String capital = i.getStringExtra("capital");

        editText.setText(country);
        editText2.setText(capital);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = getIntent();
                i.putExtra("country", editText.getText().toString());
                i.putExtra("capital", editText2.getText().toString());
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(KeyEvent.KEYCODE_BACK == keyCode) {
            setResult(RESULT_CANCELED, getIntent());
        }
        return super.onKeyDown(keyCode, event);
    }
}
