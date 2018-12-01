package com.example.samsung.recyclerviewparagraphsample;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.text.PrecomputedTextCompat;
import androidx.core.widget.TextViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArticleBodyFragment extends Fragment {

    private static final String ARG_ARTICLE_BODY = "article_body";

    private String articleBody;

    public ArticleBodyFragment() {
        // Required empty public constructor
    }

    static ArticleBodyFragment newInstance(String articleBody) {
        Bundle arguments = new Bundle();
        arguments.putString(ARG_ARTICLE_BODY, articleBody);
        ArticleBodyFragment fragment = new ArticleBodyFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        articleBody = getArguments().getString(ARG_ARTICLE_BODY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_article_body, container, false);

        RecyclerView mRecyclerView = view.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        RecyclerView.Adapter mAdapter = new MyAdapter(convertSingleArticleInToParagraphArray(articleBody));
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    private String[] convertSingleArticleInToParagraphArray(String textBody) {
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
            return new MyAdapter.ViewHolder(itemView);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {
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
