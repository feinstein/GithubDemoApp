package br.com.feinstein.githubapp.di;

import javax.inject.Singleton;

import br.com.feinstein.githubapp.data.db.GithubDbCache;
import dagger.Module;
import dagger.Provides;

@Module
class GithubDbCacheModule {
    @Provides @Singleton static GithubDbCache provideGithubDbCache() {
        return new GithubDbCache();
    }
}
