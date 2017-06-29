package de.skicomp.fragments.friends;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import de.skicomp.R;
import de.skicomp.SessionManager;
import de.skicomp.activities.BaseActivity;
import de.skicomp.activities.main.FriendsActivity;
import de.skicomp.adapter.FriendAdapter;
import de.skicomp.databinding.FragmentFriendsSearchBinding;
import de.skicomp.models.Friend;
import de.skicomp.network.SkiService;
import de.skicomp.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by benjamin.schneider on 28.06.17.
 */

public class SearchFriendFragment extends Fragment implements FriendAdapter.FriendListener {

    public static final String TAG = SearchFriendFragment.class.getSimpleName();

    private FragmentFriendsSearchBinding viewBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_friends_search, container, false);
        viewBinding.setHandler(this);
        initToolbar();

        return viewBinding.getRoot();
    }

    private void initToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(viewBinding.toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @SuppressWarnings("unused")
    public void onClickSearch(View view) {
        if (viewBinding.etSearchFriend.getText().toString().isEmpty()) {
            Utils.showToast(getContext(), R.string.friends_search_no_input, Toast.LENGTH_SHORT);
            return;
        }

        SkiService.getInstance().searchUser(new Callback<List<Friend>>() {
            @Override
            public void onResponse(Call<List<Friend>> call, Response<List<Friend>> response) {
                if (response.isSuccessful()) {
                    List<Friend> friendList = response.body();

                    if (friendList.isEmpty()) {
                        viewBinding.rvFriendsSearchResult.setVisibility(View.GONE);
                        viewBinding.tvFriendSearchNoResults.setText(R.string.friends_search_no_results);
                        viewBinding.tvFriendSearchNoResults.setVisibility(View.VISIBLE);
                        return;
                    }

                    viewBinding.tvFriendSearchNoResults.setVisibility(View.GONE);
                    viewBinding.rvFriendsSearchResult.setLayoutManager(new LinearLayoutManager(getContext()));
                    viewBinding.rvFriendsSearchResult.setAdapter(new FriendAdapter(getContext(), SearchFriendFragment.this, friendList));
                } else {
                    ((BaseActivity) getActivity()).handleError(response);
                }
            }

            @Override
            public void onFailure(Call<List<Friend>> call, Throwable t) {
                Utils.showToast(getContext(), "Unbekannter Fehler", Toast.LENGTH_SHORT);
            }
        }, SessionManager.getInstance().getUsername(), viewBinding.etSearchFriend.getText().toString());
    }

    @Override
    public void onSelectedFriend(Friend friend) {
        ((FriendsActivity) getActivity()).onSelectedFriend(friend);
    }
}
