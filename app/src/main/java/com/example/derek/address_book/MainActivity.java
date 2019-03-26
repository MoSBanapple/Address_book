package com.example.derek.address_book;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    EditText nameInput;
    EditText addressInput;
    TextView outputText;
    TextView addresses;
    String long_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameInput = (EditText)findViewById(R.id.NameInput);
        addressInput = (EditText)findViewById(R.id.AddressInput);
        outputText = (TextView)findViewById(R.id.OutputText);
        addresses = (TextView)findViewById(R.id.addressList);
    }

    public void onClick(View view) {

//        try {
            switch (view.getId()) {

                case R.id.AddButton:
                    new RequestTask().execute(nameInput.getText().toString(), addressInput.getText().toString(), "add");
                    //outputText.setText(request(nameInput.getText().toString(), addressInput.getText().toString(), "add"));
                    break;

                case R.id.DeleteButton:
                    new RequestTask().execute(nameInput.getText().toString(), addressInput.getText().toString(), "delete");
                    //outputText.setText(request(nameInput.getText().toString(), addressInput.getText().toString(), "delete"));
                    break;

                case R.id.SearchButton:
                    new RequestTask().execute(nameInput.getText().toString(), addressInput.getText().toString(), "find");
                    //outputText.setText(request(nameInput.getText().toString(), addressInput.getText().toString(), "find"));
                    break;

                case R.id.ListButton:
                    new ListTask().execute("blank", "blank", "list");

//                    new RequestTask().execute(nameInput.getText().toString(), addressInput.getText().toString(), "list");
                    break;

                default:
                    break;
            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    public void list(String input) throws IOException {
        Intent intent = new Intent(this, List.class);
        intent.putExtra(EXTRA_MESSAGE, input);

        startActivity(intent);
    }

//    public void toast(String str){
//        Context context = getApplicationContext();
//        int duration = Toast.LENGTH_SHORT;
//
//        Toast toast = Toast.makeText(context, str, duration);
//        toast.show();
//    }

    class RequestTask extends AsyncTask<String, String, String> {
        protected String doInBackground(String... inputs) {
            String output = "";
            String name = inputs[0];
            String address = inputs[1];
            String action = inputs[2];
            String url = "https://testproject-175503.appspot.com";
            String params = "name=" + name + "&address=" + address + "&action=" + action;
            try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection)obj.openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();
//            OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream());
//            osw.write(params);
            byte[] postDataBytes = params.toString().getBytes("UTF-8");
            con.getOutputStream().write(postDataBytes);
//            OutputStream os = con.getOutputStream();
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
//            writer.write(params);
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;

            while((line = br.readLine()) != null) {
                output += line + "\n";
            }
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            return output;

        }

        protected void onPostExecute(String output) {
            outputText.setText(output);
            long_list = output;


        }
    }

    class ListTask extends RequestTask{
        protected void onPostExecute(String output) {
            try {
                list(output);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
