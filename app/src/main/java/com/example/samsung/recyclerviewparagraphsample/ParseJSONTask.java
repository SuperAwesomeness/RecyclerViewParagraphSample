package com.example.samsung.recyclerviewparagraphsample;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ParseJSONTask extends AsyncTask<Void, Void, ArrayList<String>> {

    private AsyncResponse delegate;

    private Context context;

    public interface AsyncResponse {
        void processFinish(ArrayList<String> output);
    }

    ParseJSONTask(AsyncResponse delegate, Context context) {
        this.delegate = delegate;
        this.context = context;
    }

    @Override
    protected ArrayList<String> doInBackground(Void... params) {
        //Do some task
        String JSONString = getEntireJSONString(context);
        return parseEntireJSON(JSONString);
    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {
        //Show the result obtained from doInBackground
        delegate.processFinish(result);
    }

    private String getEntireJSONString(Context context) {
        String str = "";
        try {
            AssetManager assetManager = context.getAssets();
            InputStream in = assetManager.open("xyz-reader.json");
            InputStreamReader isr = new InputStreamReader(in);
            char[] inputBuffer = new char[100];

            int charRead;
            while ((charRead = isr.read(inputBuffer)) > 0) {
                String readString = String.copyValueOf(inputBuffer, 0, charRead);
                str += readString;
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        Log.i("Main", "json string: " + str);

        return str;
    }

    private ArrayList<String> parseEntireJSON(String jsonString) {
        JSONObject object;
        String bodyString = null;
        ArrayList<String> articleBodyArray = null;
        try {
            JSONTokener tokener = new JSONTokener(jsonString);
            Object val = tokener.nextValue();
            if (!(val instanceof JSONArray)) {
                throw new JSONException("Expected JSONArray");
            }
            JSONArray array = (JSONArray) val;
            int arrayLength = array.length();
            articleBodyArray = new ArrayList<>(arrayLength);
            for (int i = 0; i < arrayLength; i++) {
                object = array.getJSONObject(i);// in this sample app, we only parse the first article
                bodyString = object.getString("body");// get the article body
                articleBodyArray.add(bodyString);
            }

        } catch (JSONException e) {
        }
        Log.i("Main", "body string: " + bodyString);
        return articleBodyArray;
    }


}
