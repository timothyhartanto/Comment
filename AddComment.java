package com.example.proto.comment;

/**
 * Created by Proto on 12/16/2015.
 */

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddComment extends Activity implements OnClickListener{

    private EditText title, message;
    private Button  mSubmit;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    //php login script

    private static final String POST_COMMENT_URL = "http://xx.xx.xx.xx/webservice/addcomment.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_comment);

        title = (EditText)findViewById(R.id.title);
        message = (EditText)findViewById(R.id.message);

        mSubmit = (Button)findViewById(R.id.submit);
        mSubmit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String post_title = title.getText().toString();
        String post_message = message.getText().toString();
        new PostComment().execute(post_title, post_message);
    }


    class PostComment extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddComment.this);
            pDialog.setMessage("Posting Comment...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;
            String post_title = args[0];
            String post_message = args[1];

            //We need to change this:
            String post_username = "temp";

            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", post_username));
                params.add(new BasicNameValuePair("title", post_title));
                params.add(new BasicNameValuePair("message", post_message));

                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(
                        POST_COMMENT_URL, "POST", params);

                // full json response
                Log.d("Post Comment attempt", json.toString());

                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Comment Added!", json.toString());
                    finish();
                    return json.getString(TAG_MESSAGE);
                }else{
                    Log.d("Comment Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null){
                Toast.makeText(AddComment.this, file_url, Toast.LENGTH_LONG).show();
            }

        }

    }


}
