package com.edu.lx.onedayworkfinal.seeker;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.seeker.recycler_view.SeekerManageProjectRecyclerViewAdapter;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.ProjectVO;
import com.edu.lx.onedayworkfinal.vo.SeekerVO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.edu.lx.onedayworkfinal.seeker.FindJobFrontFragment.items;

//신청 일감 관리 RecyclerViewFragment 윤정민 (진행중)
public class ManageJobListFragment extends Fragment {

    SeekerMainActivity activity;
    String seekerId;
    RecyclerView ManageJobRecylerView;
    SeekerManageProjectRecyclerViewAdapter adapter;

    @Override
    public void onAttach (Context context) {
        super.onAttach(context);
        activity = (SeekerMainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_manage_job_list,container,false);
        ManageJobRecylerView = rootView.findViewById(R.id.ManageJobRecylerView);

        return rootView;
    }

    @Override
    public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //RecyclerView 의 layoutManager 세팅
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity.getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        ManageJobRecylerView.setLayoutManager(layoutManager);
        seekerId = Base.sessionManager.getUserDetails().get("id");
        //신청 일감 요청

        requestManageList(seekerId);

    }

    //신청 일감 요청
    private void requestManageList (final String seekerId) {
        //TODO 거리기반으로 요청이 가도록 구현
        String url = getResources().getString(R.string.url) + "manageJobList.do";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse (String response) {
                        processProjectResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse (VolleyError error) {

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams () throws AuthFailureError {
                //TODO ID로 결과값 가져오기
                Map<String, String> params = new HashMap<>();
                params.put("seekerId", String.valueOf(seekerId));
                return params;
            }
        };

        request.setShouldCache(false);
        Base.requestQueue.add(request);
    }

    //서버로부터 받아온 projectList 를 RecyclerView 에 뿌려줌
    private void processProjectResponse (String response) {
        ProjectVO[] projectArray = Base.gson.fromJson(response,ProjectVO[].class);

        items = new ArrayList<>(Arrays.asList(projectArray));

        //Adapter 할당
        adapter = new SeekerManageProjectRecyclerViewAdapter(activity);
        adapter.setItems(items);
        ManageJobRecylerView.setAdapter(adapter);
    }


}
