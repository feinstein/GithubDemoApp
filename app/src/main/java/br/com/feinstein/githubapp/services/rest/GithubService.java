package br.com.feinstein.githubapp.services.rest;

import java.util.List;

import br.com.feinstein.githubapp.models.GithubPullRequest;
import br.com.feinstein.githubapp.models.GithubRepositoryResponse;
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
    Single<List<GithubPullRequest>> getPullRequests(@Path("username") String ownerName,
                                                    @Path("repository_name") String repositoryName);
}
