package br.com.feinstein.technicaltest_mf.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import br.com.feinstein.technicaltest_mf.R;
import br.com.feinstein.technicaltest_mf.adapters.EndlessRecyclerViewScrollListener;
import br.com.feinstein.technicaltest_mf.adapters.GithubRepositoriesRecyclerViewAdapter;
import br.com.feinstein.technicaltest_mf.models.GithubRepository;
import br.com.feinstein.technicaltest_mf.services.rest.GithubService;
import br.com.feinstein.technicaltest_mf.services.rest.GithubServiceFactory;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Activity que carrega os Repositorios mais populares de Java.
 * Funciona tanto em smartphones quanto em tablets.
 */
public class RepositoryListActivity extends AppCompatActivity {
    private GithubService githubService;
    private boolean mTwoPane;
    private List<GithubRepository> repositories = new ArrayList<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.repository_list) RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.root) CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository_list);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (findViewById(R.id.repository_detail_container) != null) {
            // indica se esta num tablet ou nao
            mTwoPane = true;
        }

        GithubServiceFactory githubServiceFactory = new GithubServiceFactory();
        githubService = githubServiceFactory.createGithubService();

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(this::loadFirstItems);

        setupRecyclerView();
    }

    @Override
    protected void onPause() {
        compositeDisposable.dispose();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        GithubRepositoriesRecyclerViewAdapter adapter = new GithubRepositoriesRecyclerViewAdapter(this,
                repositories, mTwoPane);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadItemsPage(page, (size) -> adapter.notifyItemRangeInserted(totalItemsCount, size));
            }
        });

        loadFirstItems();
    }

    /**
     * Carrega os primeiros items da lista
     */
    private void loadFirstItems() {
            loadItemsPage(1, (size) -> recyclerView.getAdapter().notifyDataSetChanged());
    }

    /**
     * Carrega a pagina de repositorios do Github.
     *
     * @param page numero da pagina
     * @param notification notificacao a ser executada no final, para que o {@link RecyclerView}
     *                     possa ser atualizado na UI.
     */
    private void loadItemsPage(int page, Consumer<Integer> notification) {
        if (!swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(true);
        }

        compositeDisposable.add(
                githubService.getRepositories(page)
                        .subscribeOn(Schedulers.io())
                        .timeout(1, TimeUnit.MINUTES)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnError((throwable) -> {
                            Toast.makeText(RepositoryListActivity.this,
                                    getString(R.string.network_error_message),
                                    Toast.LENGTH_LONG).show();
                            // Snackbar.make(coordinatorLayout,
                            //         getString(R.string.network_error_message),
                            //         Snackbar.LENGTH_LONG).show();
                        })
                        .retry()
                        .doFinally(() -> {
                            if (swipeRefreshLayout.isRefreshing()) {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        })
                        .subscribe(repositoriesResponse -> {
                                    List<GithubRepository> newRepositories = repositoriesResponse.getItems();

                            Log.d("ListActivity", "\nReceived Repos: " + newRepositories.size());

                            Log.d("ListActivity", "\n==============START============");

                            for (GithubRepository r : newRepositories) {
                                Log.d("ListActivity", r.getName() + "\t" + r.getOwner().getUsername());
                            }

                                    repositories.addAll(newRepositories);

                            Log.d("ListActivity", "\n==========================");
                            Log.d("ListActivity", "\nAll Repos: " + repositories.size());
                            for (GithubRepository r : repositories) {
                                Log.d("ListActivity", r.getName() + "\t" + r.getOwner().getUsername());
                            }
                            Log.d("ListActivity", "\n============END==============");


                            notification.accept(newRepositories.size());
                                })
        );
    }
}
