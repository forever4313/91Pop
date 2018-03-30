package com.dante.di.component;

import com.dante.di.PerService;
import com.dante.di.module.ServiceModule;
import com.dante.service.DownloadVideoService;

import dagger.Component;

/**
 * @author flymegoc
 * @date 2018/2/4
 */
@PerService
@Component(dependencies = ApplicationComponent.class, modules = ServiceModule.class)
public interface ServiceComponent {
    void inject(DownloadVideoService downloadVideoService);
}
