package br.com.feinstein.technicaltest_mf.services.rest;

import java.util.List;

import br.com.feinstein.technicaltest_mf.models.GithubPullRequest;
import br.com.feinstein.technicaltest_mf.models.GithubRepositoryResponse;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface a ser usada com o Retrofit para acesso aos Repositorios do Github.
 */

public interface GithubService {
    @GET("search/repositories?q=language:Java&sort=stars")
    Single<GithubRepositoryResponse> getRepositories(@Query("page") int page);

    @GET("repos/{username}/{repository_name}/pulls")
    Single<List<GithubPullRequest>> getPullRequests(@Path("username") String username,
                                                    @Path("repository_name") String repositoryName);
}
