/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2016/5/4.
 */
package sg.com.conversant.swiftplayer.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import sg.com.conversant.swiftplayer.R;
import sg.com.conversant.swiftplayer.adapter.SearchPreviewGridAdapter;

/**
 * TODO
 *
 * @author Xiaodong
 */
public class SearchActivity extends AppCompatActivity {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @InjectView(R.id.search_edit)
    EditText searchEditView;

    @InjectView(R.id.search_delete)
    ImageView searchDeleteView;

    @InjectView(R.id.search_btn)
    TextView searchBtn;

    @InjectView(R.id.search_preview)
    LinearLayout searchPreview;

    @InjectView(R.id.search_history)
    LinearLayout searchHistory;

    @InjectView(R.id.search_history_empty)
    TextView searchHistoryEmpty;

    @InjectView(R.id.search_history_grid)
    GridView searchHistoryGridView;

    @InjectView(R.id.search_hot_grid)
    GridView searchHotGridView;

    @InjectView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private List<String> searchHistoryList;
    private List<String> searchHotList;

    private SearchPreviewGridAdapter searchHistoryAdapter;
    private SearchPreviewGridAdapter searchHotAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.inject(this);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        searchEditView.addTextChangedListener(mTextWatcher);
        searchEditView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    attemptSearch();
                    return true;
                }
                return false;
            }
        });

        searchDeleteView.setOnClickListener(clickListener);
        searchBtn.setOnClickListener(clickListener);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        searchHistoryList = new ArrayList<>();
        searchHotList = new ArrayList<>();

        initSearchHistory();

        initSearchHot();

        searchHistoryAdapter = new SearchPreviewGridAdapter(this);
        searchHistoryGridView.setAdapter(searchHistoryAdapter);
        searchHistoryAdapter.setItems(searchHistoryList);

        searchHotAdapter = new SearchPreviewGridAdapter(this);
        searchHotGridView.setAdapter(searchHotAdapter);
        searchHotAdapter.setItems(searchHotList);

    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.search_delete:

                    searchEditView.setText("");
                    searchDeleteView.setVisibility(View.GONE);

                    break;

                case R.id.search_btn:

                    attemptSearch();

                    break;
            }
        }
    };

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (count > 0) {
                searchDeleteView.setVisibility(View.VISIBLE);
            } else {
                searchDeleteView.setVisibility(View.GONE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void initSearchHistory() {
        searchHistoryList.add("欢乐颂");
        searchHistoryList.add("美国队长3：冬日之战");
        searchHistoryList.add("权利的游戏");
    }

    private void initSearchHot() {
        searchHotList.add("欢乐颂");
        searchHotList.add("熊出没");
        searchHotList.add("太阳的后裔");
        searchHotList.add("最好的我们");
        searchHotList.add("武神赵子龙");
        searchHotList.add("我是杜拉拉");
        searchHotList.add("美人鱼");
        searchHotList.add("山海经之赤影传说");
        searchHotList.add("极限挑战");
        searchHotList.add("火锅英雄");
    }

    private void attemptSearch() {

        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(searchEditView.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }
}
