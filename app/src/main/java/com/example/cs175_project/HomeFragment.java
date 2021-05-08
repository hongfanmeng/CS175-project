package com.example.cs175_project;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.cs175_project.model.GetResponse;
import com.example.cs175_project.model.VideoResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private RecyclerView mRecyclerview;
    private VideoAdapter mVideoAdapter;
    IApi api;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerview = getActivity().findViewById(R.id.recycler_view);

        initNetwork();
        mVideoAdapter = new VideoAdapter(new ArrayList<>());
        List<VideoItem> videos = fetchVideoList();
        mRecyclerview.setAdapter(mVideoAdapter);
        mRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
    }

    private void initNetwork() {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(IApi.class);
    }

    List<VideoItem> fetchVideoList() {
        Call<GetResponse> resp = api.getVideo();
        resp.enqueue(new Callback<GetResponse>() {
            @Override
            public void onResponse(final Call<GetResponse> call, final Response<GetResponse> response) {
                if (!response.isSuccessful()) return;
                List<VideoResult> results = response.body().feeds;
                List<VideoItem> videos = results.stream().map((VideoResult result) -> {
                    VideoItem video = new VideoItem(Uri.parse(result.videoUrl), Uri.parse(result.imageUrl),
                            result.imageW, result.imageH,
                            result.userName, result.createdAt);
                    return video;
                }).collect(Collectors.toList());
                mVideoAdapter.notifyItems(videos);
            }

            @Override
            public void onFailure(final Call<GetResponse> call, final Throwable t) {
                t.printStackTrace();
                Log.d(TAG, t.toString());
            }
        });
        return null;
    }

}