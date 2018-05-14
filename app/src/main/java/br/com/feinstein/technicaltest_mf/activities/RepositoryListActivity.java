package br.com.feinstein.technicaltest_mf.activities;

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

import br.com.feinstein.technicaltest_mf.R;
import br.com.feinstein.technicaltest_mf.adapters.EndlessRecyclerViewScrollListener;
import br.com.feinstein.technicaltest_mf.adapters.GithubRepositoriesRecyclerViewAdapter;
import br.com.feinstein.technicaltest_mf.data.repositories.GithubDataRepository;
import br.com.feinstein.technicaltest_mf.models.GithubRepository;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Activity que carrega os Repositorios mais populares de Java.
 * Funciona tanto em smartphones quanto em tablets.
 */
public class RepositoryListActivity extends AppCompatActivity {
    private GithubDataRepository dataRepository;
    private boolean mTwoPane;
    private List<GithubRepository> repositories = new ArrayList<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private SingleTransformer<List<GithubRepository>, List<GithubRepository>> configurationTransformer;

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

        dataRepository = new GithubDataRepository(); // TODO: Inject with Dagger 2

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(this::loadFirstItems);

        createConfigurationTransformer();

        setupRecyclerView();
        loadFirstItems();
    }

    // @Override
    // protected void onResume() {
    //     super.onResume();
    //     compositeDisposable = new CompositeDisposable();
    // }

    @Override
    protected void onPause() {
        compositeDisposable.clear();
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
                compositeDisposable.add(
                        dataRepository.getGithubRepositoriesFromPage(page + 1)
                                      .compose(configurationTransformer)
                                      .subscribe(newRepositories -> {
                                          repositories.addAll(newRepositories);
                                          adapter.notifyItemRangeInserted(totalItemsCount, newRepositories.size());
                                      })
                );
            }
        });
    }

    /**
     * Carrega os primeiros items da lista
     */
    private void loadFirstItems() {
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
                          compositeDisposable.clear();
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
                      .retry()
                      .doFinally(() -> {
                          if (swipeRefreshLayout.isRefreshing()) {
                              swipeRefreshLayout.setRefreshing(false);
                          }
                      });
    }
}
