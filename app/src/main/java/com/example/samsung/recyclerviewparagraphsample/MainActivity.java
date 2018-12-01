package com.example.samsung.recyclerviewparagraphsample;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.renderscript.ScriptGroup;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.text.PrecomputedTextCompat;
import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView mRecyclerView = findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        RecyclerView.Adapter mAdapter = new MyAdapter(convertStringToStringArray(parseJSON(getJSONString(this))));
        mRecyclerView.setAdapter(mAdapter);
    }

    private String getJSONString(Context context) {
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

    public String parseJSON(String jsonString) {
        JSONObject object;
        String bodyString = null;
        try {
            JSONTokener tokener = new JSONTokener(jsonString);
            Object val = tokener.nextValue();
            if (!(val instanceof JSONArray)) {
                throw new JSONException("Expected JSONArray");
            }
            JSONArray array = (JSONArray) val;
            object = array.getJSONObject(0);// in this sample app, we only parse the first article
            bodyString = object.getString("body");// get the article body
        } catch (JSONException e) {
        }
        Log.i("Main", "body string: " + bodyString);
        return bodyString;
    }

    private String[] convertStringToStringArray(String textBody) {
        // split text in paragraphs
        return textBody.split("\\r\\n\\r\\n");
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private String[] mDataset;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            AppCompatTextView mTextView;

            ViewHolder(View v) {
                super(v);
                mTextView = v.findViewById(R.id.item_text_body);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        MyAdapter(String[] myDataset) {
            mDataset = myDataset;
        }

        // Create new views (invoked by the layout manager)
        @NonNull
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                       int viewType) {
            // create a new view
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.my_text_view, parent, false);
            return new ViewHolder(itemView);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
//            holder.mTextView.setText(mDataset[position]);
//            holder.mTextView.setText(Html.fromHtml(mDataset[position]));

            // use PrecomputedText to boost performance:
            // https://medium.com/androiddevelopers/prefetch-text-layout-in-recyclerview-4acf9103f438
            holder.mTextView.setTextFuture(PrecomputedTextCompat.getTextFuture(Html.fromHtml(mDataset[position]), TextViewCompat.getTextMetricsParams(holder.mTextView), null));

            Log.i("Main", "paragraph: " + mDataset[position]);
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            Log.i("adapter", "paragraph count: " + mDataset.length);
            return mDataset.length;
        }
    }
}
