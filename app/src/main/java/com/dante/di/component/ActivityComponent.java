package com.dante.di.component;

import com.dante.di.PerActivity;
import com.dante.di.module.ActivityModule;
import com.dante.ui.about.AboutActivity;
import com.dante.ui.basemain.BaseMainFragment;
import com.dante.ui.download.DownloadActivity;
import com.dante.ui.download.DownloadingFragment;
import com.dante.ui.download.FinishedFragment;
import com.dante.ui.favorite.FavoriteActivity;
import com.dante.ui.history.HistoryActivity;
import com.dante.ui.images.meizitu.MeiZiTuFragment;
import com.dante.ui.images.mm99.Mm99Fragment;
import com.dante.ui.images.viewimage.PictureViewerActivity;
import com.dante.ui.main.MainActivity;
import com.dante.ui.mine.MineFragment;
import com.dante.ui.pigav.PigAvFragment;
import com.dante.ui.pigav.playpigav.PlayPigAvActivity;
import com.dante.ui.porn91forum.Forum91IndexFragment;
import com.dante.ui.porn91forum.ForumFragment;
import com.dante.ui.porn91forum.browse91porn.Browse91PornActivity;
import com.dante.ui.porn91video.author.AuthorActivity;
import com.dante.ui.porn91video.index.IndexFragment;
import com.dante.ui.porn91video.play.BasePlayVideo;
import com.dante.ui.porn91video.search.SearchActivity;
import com.dante.ui.porn91video.videolist.VideoListFragment;
import com.dante.ui.proxy.ProxySettingActivity;
import com.dante.ui.setting.SettingActivity;
import com.dante.ui.splash.SplashActivity;
import com.dante.ui.user.UserLoginActivity;
import com.dante.ui.user.UserRegisterActivity;

import dagger.Component;

/**
 * @author flymegoc
 * @date 2018/2/4
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(SplashActivity splashActivity);

    void inject(MainActivity mainActivity);

    void inject(DownloadActivity downloadActivity);

    void inject(SettingActivity settingActivity);

    void inject(AboutActivity aboutActivity);

    void inject(FavoriteActivity favoriteActivity);

    void inject(SearchActivity searchActivity);

    void inject(BasePlayVideo basePlayVideo);

    void inject(UserLoginActivity userLoginActivity);

    void inject(UserRegisterActivity userRegisterActivity);

    void inject(AuthorActivity authorActivity);

    void inject(ProxySettingActivity proxySettingActivity);

    void inject(PlayPigAvActivity playPigAvActivity);

    void inject(PictureViewerActivity pictureViewerActivity);

    void inject(Browse91PornActivity browse91PornActivity);

    void inject(HistoryActivity historyActivity);

    void inject(VideoListFragment videoListFragment);

    void inject(PigAvFragment pigAvFragment);

    void inject(IndexFragment indexFragment);

    void inject(MeiZiTuFragment meiZiTuFragment);

    void inject(Mm99Fragment mm99Fragment);

    void inject(DownloadingFragment downloadingFragment);

    void inject(FinishedFragment finishedFragment);

    void inject(BaseMainFragment baseMainFragment);

    void inject(MineFragment mineFragment);

    void inject(ForumFragment forumFragment);

    void inject(Forum91IndexFragment forum91IndexFragment);
}
