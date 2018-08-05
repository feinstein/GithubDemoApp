package br.com.feinstein.githubapp.di;

import javax.inject.Singleton;

import br.com.feinstein.githubapp.services.rest.GithubService;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

@Module
class GithubServiceModule {
    private static final String BASE_URL = "https://api.github.com/";

    @Provides @Singleton static GithubService providesGithubService() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(GithubService.class);
    }
}
