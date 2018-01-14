package com.vizlore.phasmafood.dagger;

import com.vizlore.phasmafood.dagger.modules.AppModule;
import com.vizlore.phasmafood.dagger.modules.NetworkModule;
import com.vizlore.phasmafood.dagger.modules.UserModule;
import com.vizlore.phasmafood.viewmodel.UserViewModel;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by smedic on 1/4/18.
 */

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class, UserModule.class})
public interface AppComponent {

	void inject(UserViewModel userViewModel);

	//void inject(SearchActivity activity);

	//void inject(MainActivityViewModel activity);

	//void inject(RetrofitInterceptor interceptor);
}