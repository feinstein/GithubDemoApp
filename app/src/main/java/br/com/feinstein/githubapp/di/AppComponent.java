package br.com.feinstein.githubapp.di;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        GithubRepositoryModule.class,
        MainActivityModule.class
})
public interface AppComponent {
}
