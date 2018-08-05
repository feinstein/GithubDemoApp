package br.com.feinstein.githubapp.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import br.com.feinstein.githubapp.R;
import br.com.feinstein.githubapp.adapters.EndlessRecyclerViewScrollListener;
import br.com.feinstein.githubapp.adapters.GithubRepositoriesRecyclerViewAdapter;
import br.com.feinstein.githubapp.data.repositories.GithubDataRepository;
import br.com.feinstein.githubapp.models.GithubRepository;
import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import io.reactivex.Flowable;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Activity que carrega os Repositorios mais populares de Java.
 * Funciona tanto em smartphones quanto em tablets.
 */
public class RepositoryListActivity extends AppCompatActivity {
    @Inject
    GithubDataRepository dataRepository;
    private boolean mTwoPane;
    private List<GithubRepository> repositories = new ArrayList<>();
    private CompositeDisposable compositeDisposable;
    private SingleTransformer<List<GithubRepository>, List<GithubRepository>> configurationTransformer;
    private EndlessRecyclerViewScrollListener endlessScrollListener;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.repository_list) RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.root) CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository_list);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (findViewById(R.id.repository_detail_container) != null) {
            // indica se esta num tablet ou nao
            mTwoPane = true;
        }

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(this::loadFirstItems);

        compositeDisposable = new CompositeDisposable();
        createConfigurationTransformer();

        setupRecyclerView();
        loadFirstItems();
    }

    @Override
    protected void onPause() {
        compositeDisposable.clear();
        endlessScrollListener.resetLoadingState();
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
        endlessScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                compositeDisposable.clear();
                compositeDisposable.add(
                        dataRepository.getGithubRepositoriesFromPage(page + 1)
                                      .compose(configurationTransformer)
                                      .subscribe(newRepositories -> {
                                          repositories.addAll(newRepositories);
                                          adapter.notifyItemRangeInserted(totalItemsCount, newRepositories.size());
                                      })
                );
            }
        };
        recyclerView.addOnScrollListener(endlessScrollListener);
    }

    /**
     * Carrega os primeiros items da lista
     */
    private void loadFirstItems() {
        compositeDisposable.clear();
        compositeDisposable.add(
                dataRepository.getGithubRepositoriesFromPage(1)
                              .compose(configurationTransformer)
                              .subscribe((newRepositories) -> {
                                  repositories.clear();
                                  repositories.addAll(newRepositories);
                                  recyclerView.getAdapter().notifyDataSetChanged();
                              }));
    }

    /**
     * Cria um {@link SingleTransformer} para funcionar com a UI, recebendo tratamentos de erro,
     * indicadores de loading etc e armazena numa instancia dessa classe para ser reutilizado.
     */
    private void createConfigurationTransformer() {
        configurationTransformer = (single) ->
                single.subscribeOn(Schedulers.io())
                      .timeout(1, TimeUnit.MINUTES)
                      .observeOn(AndroidSchedulers.mainThread())
                      .doOnSubscribe((d) -> {
                          if (!swipeRefreshLayout.isRefreshing()) {
                              swipeRefreshLayout.setRefreshing(true);
                          }})
                      .doOnError((throwable) -> {
                          Toast.makeText(RepositoryListActivity.this,
                                  getString(R.string.network_error_message),
                                  Toast.LENGTH_LONG).show();
                          // Snackbar.make(coordinatorLayout,
                          //         getString(R.string.network_error_message),
                          //         Snackbar.LENGTH_LONG).show();
                      })
                      .retryWhen(errors -> errors.flatMap(error -> Flowable.timer(30, TimeUnit.SECONDS)))
                      .doFinally(() -> {
                          if (swipeRefreshLayout.isRefreshing()) {
                              swipeRefreshLayout.setRefreshing(false);
                          }
                      });
    }
}
