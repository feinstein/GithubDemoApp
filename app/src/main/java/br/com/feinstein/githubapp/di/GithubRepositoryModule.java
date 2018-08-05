package br.com.feinstein.githubapp.di;

import javax.inject.Singleton;

import br.com.feinstein.githubapp.data.db.GithubDbCache;
import br.com.feinstein.githubapp.data.repositories.GithubDataRepository;
import br.com.feinstein.githubapp.services.rest.GithubService;
import dagger.Module;
import dagger.Provides;

@Module(includes = {GithubDbCacheModule.class, GithubServiceModule.class})
class GithubRepositoryModule {
    @Provides @Singleton static GithubDataRepository provideGithubRepository(GithubService service, GithubDbCache db){
        return new GithubDataRepository(service, db);
    }
}
