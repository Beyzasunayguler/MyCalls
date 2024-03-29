package com.example.mycalls;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailFragment extends Fragment {
    private ProgressBar loadingBar;
    private RecyclerView mRecyclerView;
    private DetailAdapter detailAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    GridLayout mGridLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View detailView = inflater.inflate(R.layout.fragment_detail, container, false);
        mGridLayout=(GridLayout) detailView.findViewById(R.id.fragment_detail_grid_layout);
        mRecyclerView = (RecyclerView) detailView.findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) detailView.findViewById(R.id.swipe_refresh_layout);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        loadingBar = (ProgressBar) detailView.findViewById(R.id.loadingBar);
        detailAdapter = new DetailAdapter();//MainActivity.this,null
        mRecyclerView.setAdapter(detailAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                        MInterface mInterface = ApiClient.getClient().create(MInterface.class);
                        Call<CallResult> call = mInterface.getCalls();
                        call.enqueue(new Callback<CallResult>() {
                            @Override
                            public void onResponse(Call<CallResult> call, Response<CallResult> response) {
                                detailAdapter.setData(response.body().getCalls());
                                mRecyclerView.setAdapter(detailAdapter);
                                mSwipeRefreshLayout.setRefreshing(false);
                                loadingBar.setVisibility(View.GONE);
                                mRecyclerView.setVisibility(View.VISIBLE);
                                mGridLayout.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onFailure(Call<CallResult> call, Throwable t) {
                                loadingBar.setVisibility(View.GONE);
                                mSwipeRefreshLayout.setRefreshing(false);
                                Toast.makeText(getContext(), "Something went wrong \n" + t.getLocalizedMessage() + " url: " + call.request().url(), Toast.LENGTH_LONG).show();
                                t.printStackTrace();
                            }
                        });
            }
        });
        MInterface mInterface = ApiClient.getClient().create(MInterface.class);
        Call<CallResult> call = mInterface.getCalls();
        call.enqueue(new Callback<CallResult>() {
            @Override
            public void onResponse(Call<CallResult> call, Response<CallResult> response) {
                detailAdapter.setData(response.body().getCalls());
                mRecyclerView.setAdapter(detailAdapter);
                loadingBar.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                mGridLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<CallResult> call, Throwable t) {
                loadingBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Something went wrong \n" + t.getLocalizedMessage() + " url: " + call.request().url(), Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
        return detailView;
    }

}
