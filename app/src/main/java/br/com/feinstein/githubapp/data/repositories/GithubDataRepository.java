package br.com.feinstein.githubapp.data.repositories;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.feinstein.githubapp.data.db.GithubDbCache;
import br.com.feinstein.githubapp.models.GithubPullRequest;
import br.com.feinstein.githubapp.models.GithubRepository;
import br.com.feinstein.githubapp.models.GithubRepositoryResponse;
import br.com.feinstein.githubapp.services.rest.GithubService;
import io.reactivex.Single;

/**
 * Repositorio que serve como Single Source of Truth, recolhendo dados da web e armazenando em cache.
 */
@Singleton
public class GithubDataRepository {
    private final GithubService githubService;
    private final GithubDbCache dbCache;

    @Inject
    public GithubDataRepository(GithubService service, GithubDbCache db) {
        this.githubService = service;
        this.dbCache = db;
    }

    public Single<List<GithubRepository>> getGithubRepositoriesFromPage(int page) {
        return githubService.getRepositories(page).map(GithubRepositoryResponse::getItems);
    }

    public Single<List<GithubPullRequest>> getGithubPullRequests(String ownername, String repositoryName) {
        return githubService.getPullRequests(ownername, repositoryName);
    }
}
