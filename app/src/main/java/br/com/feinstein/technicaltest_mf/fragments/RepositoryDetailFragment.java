package br.com.feinstein.technicaltest_mf.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import br.com.feinstein.technicaltest_mf.R;
import br.com.feinstein.technicaltest_mf.activities.RepositoryDetailActivity;
import br.com.feinstein.technicaltest_mf.activities.RepositoryListActivity;
import br.com.feinstein.technicaltest_mf.adapters.GithubPullRequestsRecyclerViewAdapter;
import br.com.feinstein.technicaltest_mf.data.repositories.GithubDataRepository;
import br.com.feinstein.technicaltest_mf.models.GithubPullRequest;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A fragment representing a single GithubRepository detail screen.
 * This fragment is either contained in a {@link RepositoryListActivity}
 * in two-pane mode (on tablets) or a {@link RepositoryDetailActivity}
 * on handsets.
 */
public class RepositoryDetailFragment extends Fragment {
    public static final String ARG_REPOSITORY_NAME = "repository_name";
    public static final String ARG_REPOSITORY_OWNER_NAME = "repository_owner_name";

    private GithubDataRepository dataRepository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private List<GithubPullRequest> pullRequests = new ArrayList<>();
    private String repositoryName;
    private String repositoryOwnerName;

    @BindView(R.id.pull_requests_list) RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh) SwipeRefreshLayout swipeRefreshLayout;
    private Unbinder unbinder;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RepositoryDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_REPOSITORY_NAME) &&
            getArguments().containsKey(ARG_REPOSITORY_OWNER_NAME)) {
            repositoryName = getArguments().getString(ARG_REPOSITORY_NAME);
            repositoryOwnerName = getArguments().getString(ARG_REPOSITORY_OWNER_NAME);
        }

        dataRepository = new GithubDataRepository(); // TODO: Inject with Dagger 2
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.repository_detail, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(this::loadPullRequests);
        setupRecyclerView();
        loadPullRequests();
        return rootView;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        compositeDisposable.dispose();
        unbinder.unbind();
    }

    private void setupRecyclerView() {
        recyclerView.setAdapter(new GithubPullRequestsRecyclerViewAdapter(pullRequests, getContext()));
        loadPullRequests();
    }

    private void loadPullRequests() {
        if (repositoryOwnerName == null || repositoryName == null) {
            return;
        }

        compositeDisposable.add(
                dataRepository.getGithubPullRequests(repositoryOwnerName, repositoryName)
                        .subscribeOn(Schedulers.io())
                        .timeout(1, TimeUnit.MINUTES)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe((d) -> {
                            if (!swipeRefreshLayout.isRefreshing()) {
                                swipeRefreshLayout.setRefreshing(true);
                            }})
                        .doOnError((throwable) -> {
                            Toast.makeText(getContext(),
                                    getString(R.string.network_error_message),
                                    Toast.LENGTH_LONG).show();
                            // Snackbar.make(getActivity().findViewById(R.id.root),
                            //         getString(R.string.network_error_message),
                            //         Snackbar.LENGTH_LONG).show();
                        })
                        .retryWhen(errors -> errors.flatMap(error -> Flowable.timer(30, TimeUnit.SECONDS)))
                        .doFinally(() -> {
                            if (swipeRefreshLayout.isRefreshing()) {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        })
                        .subscribe(newPullRequests -> {
                            pullRequests.clear();
                            pullRequests.addAll(newPullRequests);

                            recyclerView.getAdapter().notifyDataSetChanged();
                        })
        );
    }
}
