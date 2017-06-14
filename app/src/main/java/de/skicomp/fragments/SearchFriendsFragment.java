package de.skicomp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.skicomp.R;
import de.skicomp.SessionManager;
import de.skicomp.activities.BaseActivity;
import de.skicomp.adapter.FriendAdapter;
import de.skicomp.models.Friend;
import de.skicomp.network.SkiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by benjamin.schneider on 12.05.17.
 */

public class SearchFriendsFragment extends Fragment {

    public static final String TAG = SearchFriendsFragment.class.getSimpleName();

    @BindView(R.id.et_username) EditText etUsername;
    @BindView(R.id.rv_search_friends) RecyclerView rvSearchFriends;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_search_friends, container, false);
        unbinder = ButterKnife.bind(this, inflatedView);

        return inflatedView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.ib_search_friends)
    public void onClickSearchFriends() {
        if (allFieldsValid()) {
            final String username = etUsername.getText().toString();

           SkiService.getInstance().searchUser(new Callback<List<Friend>>() {
               @Override
               public void onResponse(Call<List<Friend>> call, Response<List<Friend>> response) {
                   if (response.isSuccessful()) {
                       showSearchResult(response.body());
                   } else {
                       ((BaseActivity) getActivity()).handleError(response);
                   }
               }

               @Override
               public void onFailure(Call<List<Friend>> call, Throwable t) {
                   Toast.makeText(getContext(), "failure", Toast.LENGTH_SHORT).show();
               }
           }, SessionManager.getInstance().getUsername(), username);
        }
    }

    private boolean allFieldsValid() {
        if (etUsername.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Username fehlt", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void showSearchResult(List<Friend> searchResultList) {
        rvSearchFriends.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSearchFriends.setAdapter(new FriendAdapter(getContext(), (BaseActivity) getActivity(), searchResultList));
    }
}
