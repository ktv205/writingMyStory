package com.example.writingmystory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
public class MainActivity extends ActionBarActivity {
	StringBuilder sb;
	EditText edit;
	TextView text;
	int flag=0;
	final static String MYSTORY="myStory";
	final static String COUNT="counter";
	final static int DEFAULT=0;
	FileInputStream fr;
	FileOutputStream fos;
	SharedPreferences shared;
	SharedPreferences.Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);  
		edit=(EditText)findViewById(R.id.writestory);
		text=(TextView)findViewById(R.id.seewrittenstory);

	}
	@Override
	protected void onResume() {	
		super.onResume();
		edit.setText("");
		shared=getSharedPreferences(COUNT, Context.MODE_PRIVATE);
		editor=shared.edit();
		editor.putInt("counting", 1+shared.getInt("counting", DEFAULT));
		editor.commit();

		if(shared.getInt("counting", DEFAULT)==1){
			text.setText("Your story will appear here");	

		}else{
			edit.setHint("continue your story");
			setText();

		}

		TextWatcher textWatcher=new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				flag=1;
				text.setText(sb+edit.getText().toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		};
		edit.addTextChangedListener(textWatcher);


	}
	public void setText(){
		String line="";
		BufferedReader bufferedReader = null;
		try {
			fr=openFileInput(MYSTORY);
			InputStreamReader inputStreamReader = new InputStreamReader(fr);
			bufferedReader = new BufferedReader(inputStreamReader);
			sb= new StringBuilder();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		text.setText(sb);
		try {
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	protected void onPause() {	
		super.onPause();
		String savingStory="";
		Log.i("testing", "in on text changed");
		if(flag==1){
			try {
				fos = openFileOutput(MYSTORY, Context.MODE_PRIVATE);
			} catch (FileNotFoundException e) {

				e.printStackTrace();
			}

			savingStory=text.getText().toString();
			try {
				fos.write(savingStory.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}else{
			try {
				fos = openFileOutput(MYSTORY, Context.MODE_APPEND);
			} catch (FileNotFoundException e) {

				e.printStackTrace();
			}
		}
		try {

			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
