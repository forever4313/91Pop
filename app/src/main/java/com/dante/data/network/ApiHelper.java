package com.dante.data.network;

import android.graphics.Bitmap;

import com.dante.data.model.BaseResult;
import com.dante.data.model.Forum91PronItem;
import com.dante.data.model.MeiZiTu;
import com.dante.data.model.MmRosi;
import com.dante.data.model.Notice;
import com.dante.data.model.PigAv;
import com.dante.data.model.PigAvVideo;
import com.dante.data.model.PinnedHeaderEntity;
import com.dante.data.model.Porn91ForumContent;
import com.dante.data.model.ProxyModel;
import com.dante.data.model.UnLimit91PornItem;
import com.dante.data.model.UpdateVersion;
import com.dante.data.model.User;
import com.dante.data.model.VideoComment;
import com.dante.data.model.VideoResult;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * @author flymegoc
 * @date 2018/3/4
 */

public interface ApiHelper {
    Observable<List<UnLimit91PornItem>> loadPorn91VideoIndex(boolean cleanCache);

    Observable<BaseResult<List<UnLimit91PornItem>>> loadPorn91VideoByCategory(String category, String viewType, int page, String m, boolean cleanCache, boolean isLoadMoreCleanCache);

    Observable<BaseResult<List<UnLimit91PornItem>>> loadPorn91authorVideos(String uid, String type, int page, boolean cleanCache);

    Observable<BaseResult<List<UnLimit91PornItem>>> loadPorn91VideoRecentUpdates(String next, int page, boolean cleanCache, boolean isLoadMoreCleanCache);

    Observable<VideoResult> loadPorn91VideoUrl(String viewKey);

    Observable<List<VideoComment>> loadPorn91VideoComments(String videoId, int page, String viewKey);

    Observable<String> commentPorn91Video(String cpaintFunction, String comment, String uid, String vid, String viewKey, String responseType);

    Observable<String> replyPorn91VideoComment(String comment, String username, String vid, String commentId, String viewKey);

    Observable<BaseResult<List<UnLimit91PornItem>>> searchPorn91Videos(String viewType, int page, String searchType, String searchId, String sort);

    Observable<String> favoritePorn91Video(String uId, String videoId, String ownnerId);

    Observable<BaseResult<List<UnLimit91PornItem>>> loadPorn91MyFavoriteVideos(String userName, int page, boolean cleanCache);

    Observable<List<UnLimit91PornItem>> deletePorn91MyFavoriteVideo(String rvid);

    Observable<Bitmap> porn9VideoLoginCaptcha();
    Observable<User> userLoginPorn91Video(String username, String password, String captcha);

    Observable<User> userRegisterPorn91Video(String username, String password1, String password2, String email, String captchaInput);

    Observable<List<PinnedHeaderEntity<Forum91PronItem>>> loadPorn91ForumIndex();

    Observable<BaseResult<List<Forum91PronItem>>> loadPorn91ForumListData(String fid, int page,String filter);

    Observable<Porn91ForumContent> loadPorn91ForumContent(Long tid, final boolean isNightModel);

    Observable<UpdateVersion> checkUpdate(String url);

    Observable<Notice> checkNewNotice(String url);

    Observable<BaseResult<List<MeiZiTu>>> listMeiZiTu(String tag, int page, boolean pullToRefresh);

    Observable<List<String>> meiZiTuImageList(int id, boolean pullToRefresh);

    Observable<BaseResult<List<MmRosi>>> listMmRosi(String category, int page, boolean cleanCache);

    Observable<List<String>> mmRosiImageList(int id, String imageUrl, boolean pullToRefresh);

    Observable<List<PigAv>> loadPigAvListByCategory(String category, boolean pullToRefresh);

    Observable<List<PigAv>> loadMorePigAvListByCategory(String category, int page, boolean pullToRefresh);

    Observable<PigAvVideo> loadPigAvVideoUrl(String url, String pId, boolean pullToRefresh);

    Observable<BaseResult<List<ProxyModel>>> loadXiCiDaiLiProxyData(int page);

    Observable<String> testPorn91VideoAddress();

    Observable<String> testPorn91ForumAddress();

    Observable<String> testPigAvAddress(String url);

    Observable<Response<ResponseBody>> testV9Porn(String url);

    Observable<Response<ResponseBody>> verifyGoogleRecaptcha(String action, String r, String id, String recaptcha);

}
