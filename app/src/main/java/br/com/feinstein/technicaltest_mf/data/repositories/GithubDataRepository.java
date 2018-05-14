package br.com.feinstein.technicaltest_mf.data.repositories;

import java.util.List;

import br.com.feinstein.technicaltest_mf.models.GithubPullRequest;
import br.com.feinstein.technicaltest_mf.models.GithubRepository;
import br.com.feinstein.technicaltest_mf.models.GithubRepositoryResponse;
import br.com.feinstein.technicaltest_mf.services.rest.GithubService;
import br.com.feinstein.technicaltest_mf.services.rest.GithubServiceFactory;
import io.reactivex.Single;

public class GithubDataRepository {
    private final GithubService githubService;

    public GithubDataRepository() {
        GithubServiceFactory factory = new GithubServiceFactory();
        githubService = factory.createGithubService();
    }

    public Single<List<GithubRepository>> getGithubRepositoriesFromPage(int page) {
        return githubService.getRepositories(page).map(GithubRepositoryResponse::getItems);
    }

    public Single<List<GithubPullRequest>> getGithubPullRequests(String ownername, String repositoryName) {
        return githubService.getPullRequests(ownername, repositoryName);
    }
}
