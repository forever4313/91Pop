package com.dante.ui.images.viewimage;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dante.R;
import com.dante.adapter.PictureAdapter;
import com.dante.custom.TastyToast;
import com.dante.data.model.Mm99;
import com.dante.ui.MvpActivity;
import com.dante.utils.DialogUtils;
import com.dante.utils.GlideApp;
import com.dante.utils.SDCardUtils;
import com.dante.utils.constants.Keys;
import com.dante.widget.ProblematicViewPager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.common.io.FileUtils;

/**
 * @author flymegoc
 */
public class PictureViewerActivity extends MvpActivity<PictureViewerView, PictureViewerPresenter> implements PictureViewerView {
    private static final int SYSTEM_UI_SHOW = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
    private static final int SYSTEM_UI_HIDE = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN;
    @Inject
    protected PictureViewerPresenter pictureViewerPresenter;
    @BindView(R.id.viewPager)
    ProblematicViewPager viewPager;
    @BindView(R.id.tv_num)
    TextView tvNum;
    private List<String> imageList;
    private boolean isFullScreen = false;

    private PictureAdapter pictureAdapter;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        ButterKnife.bind(this);
        alertDialog = DialogUtils.initLodingDialog(this, "解析图片列表中...");
        init();
        initListener();
    }

    @NonNull
    @Override
    public PictureViewerPresenter createPresenter() {
        getActivityComponent().inject(this);
        return pictureViewerPresenter;
    }

    private void init() {
//        fixSwipeBack();
        imageList = getIntent().getStringArrayListExtra(Keys.KEY_INTENT_PICTURE_VIEWER_IMAGE_ARRAY_LIST);
        int currentPosition = getIntent().getIntExtra(Keys.KEY_INTENT_PICTURE_VIEWER_CURRENT_IMAGE_POSITION, 0);
        updateNumberText(currentPosition);
        if (imageList == null) {
            imageList = new ArrayList<>();
        }
        pictureAdapter = new PictureAdapter(imageList);
        viewPager.setAdapter(pictureAdapter);
        viewPager.setCurrentItem(currentPosition);
        int id = getIntent().getIntExtra(Keys.KEY_INTENT_MEI_ZI_TU_CONTENT_ID, 0);
        if (id > 0) {
            presenter.listMeZiPicture(id, false);
        }
        Mm99 mm99 = (Mm99) getIntent().getSerializableExtra(Keys.KEY_INTENT_99_MM_ITEM);
        if (mm99 != null) {
            presenter.list99MmPicture(mm99.getId(), mm99.getImgUrl(), false);
        }
    }

    private void initListener() {
        pictureAdapter.setOnImageClickListener(new PictureAdapter.onImageClickListener() {
            @Override
            public void onImageClick(View view, int position) {
                onBackPressed();
            }

            @Override
            public void onImageLongClick(View view, int position) {
                showSavePictureDialog(imageList.get(position));
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateNumberText(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void updateNumberText(int position) {
        if (imageList == null) {
            return;
        }
        if (position < 0) {
            position = 0;
        }
        tvNum.setText((position + 1) + "/" + imageList.size());
    }

    private void showSavePictureDialog(final String imageUrl) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle("操作")
                .setPositiveButton("保存图片", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GlideApp.with(PictureViewerActivity.this).downloadOnly().load(Uri.parse(imageUrl)).into(new SimpleTarget<File>() {
                            @Override
                            public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                                File filePath = new File(SDCardUtils.DOWNLOAD_IMAGE_PATH);
                                if (!filePath.exists()) {
                                    if (!filePath.mkdirs()) {
                                        showMessage("创建文件夹失败", TastyToast.ERROR);
                                        return;
                                    }
                                }
                                File file = new File(filePath, UUID.randomUUID().toString() + ".jpg");
                                try {
                                    FileUtils.copyFile(resource, file);
                                    showMessage("保存成功", TastyToast.SUCCESS);
                                    notifySystemGallery(file);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    showMessage("图片保存失败", TastyToast.ERROR);
                                }
                            }
                        });
                        dialog.dismiss();
                    }
                })
                .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    private void fixSwipeBack() {
        ViewGroup viewGroup = getWindow().getDecorView().findViewById(android.R.id.content);
        //就是这里了,有些statusbar库为了模拟状态栏，可能设置了padding,会在视频上方出现一条横幅，看上去好像状态栏没隐藏，其实已经隐藏了，这个是假的，错觉，所以重新设置padding为0即可
        viewGroup.setPadding(0, 0, 0, 0);
    }

    private void notifySystemGallery(File file) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context.getApplicationContext(), "com.u91porn.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(uri);
        sendBroadcast(intent);
    }

    @Override
    public void setStatusBarColor(int color, int statusBarAlpha) {

    }

    @Override
    public void setData(List<String> imageList) {
        this.imageList.clear();
        this.imageList.addAll(imageList);
        pictureAdapter.notifyDataSetChanged();

    }

    @Override
    public void showLoading(boolean pullToRefresh) {
        alertDialog.show();
    }

    @Override
    public void showContent() {
        dismissDialog();
    }

    @Override
    public void showMessage(String msg, int type) {
        super.showMessage(msg, type);
    }

    @Override
    public void showError(String message) {
        showMessage(message, TastyToast.ERROR);
        dismissDialog();
    }

    private void dismissDialog() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        viewPager = null;
        imageList = null;
        pictureAdapter = null;
        super.onDestroy();

    }
}
