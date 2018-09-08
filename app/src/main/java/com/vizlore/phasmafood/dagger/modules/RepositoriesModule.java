package com.vizlore.phasmafood.dagger.modules;

import android.support.annotation.NonNull;

import com.vizlore.phasmafood.api.MeasurementApi;
import com.vizlore.phasmafood.repositories.MeasurementRepository;
import com.vizlore.phasmafood.repositories.MeasurementRepositoryImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoriesModule {

	@Provides
	@Singleton
	MeasurementRepository provideMeasurementModule(@NonNull final MeasurementApi measurementApi) {
		return new MeasurementRepositoryImpl(measurementApi);
	}

}
