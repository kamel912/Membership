package com.mk.membership;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    private ListView listView;
    ArrayList<HashMap<String,String>> usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.users_list);

    }
    private class GetData extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Toast.makeText(MainActivity.this,
                    "Json Data is downloading",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler httpHandler = new HttpHandler();
            String url = "http://oriflamebeauty.net/app/read.php?";
            String jsonString = httpHandler.makeServiceCall(url);

            Log.e(TAG,"Response from url : " + jsonString);

            if (jsonString != null){

                try {
                    JSONArray users = new JSONArray(jsonString);
                    for (int i=0; i < users.length();i++){

                        JSONObject object = users.getJSONObject(i);
                        String id = object.getString("id");
                        String fname = object.getString("fname");
                        String lname = object.getString("lname");
                        String age = object.getString("age");
                        String username = object.getString("username");
                        String password = object.getString("password");

                        HashMap<String,String> user = new HashMap<>();
                        user.put("id",id);
                        user.put("fname",fname);
                        user.put("lname",lname);
                        user.put("age",age);

                        usersList.add(user);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG,"JSON Parsing error : " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }
/**
 * انا مش محتاج الكود ده
        @Override
       protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
*/
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this,
                    usersList,
                    R.layout.list_item,
                    new String[] {"id","fname","lname","age"},
                    new int[]{R.id.user_id,R.id.first_name,R.id.last_name,R.id.user_age});
            listView.setAdapter(adapter);
        }
    }
}
