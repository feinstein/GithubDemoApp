package br.com.feinstein.technicaltest_mf.services.rest;


import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * Factory para a criacao de servicos de acesso a API do Github.
 */
public class GithubServiceFactory {
    private static final String BASE_URL = "https://api.github.com/";

    private final Retrofit retrofit;

    public GithubServiceFactory() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public GithubService createGithubService() {
        return retrofit.create(GithubService.class);
    }
}
