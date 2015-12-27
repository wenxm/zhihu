package krelve.app.kuaihu.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import krelve.app.kuaihu.R;
import krelve.app.kuaihu.util.Constant;
import krelve.app.kuaihu.util.HttpUtils;

/**
 * Created by wwjun.wang on 2015/8/11.
 * revise by wenxm on 2015/12/27
 * 启动界面，有一个图片逐渐放大的效果
 */

public class SplashActivity extends Activity {
    private ImageView iv_start; //定义一个启动界面图片控件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //强制全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
       //绑定xml
        setContentView(R.layout.splash);
        iv_start = (ImageView) findViewById(R.id.iv_start);
        initImage();
    }

    //初始化图片的方法
    private void initImage() {
        File dir = getFilesDir();
        final File imgFile = new File(dir, "start.jpg");
        if (imgFile.exists()) {
            iv_start.setImageBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
        } else {
            iv_start.setImageResource(R.mipmap.start);
        }

        final ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        scaleAnim.setFillAfter(true);
        scaleAnim.setDuration(3000);
        scaleAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (HttpUtils.isNetworkConnected(SplashActivity.this)) {
                    HttpUtils.get(Constant.START, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int i, Header[] headers, byte[] bytes) {
                            try {
                                JSONObject jsonObject = new JSONObject(new String(bytes));
                                String url = jsonObject.getString("img");
                                HttpUtils.getImage(url, new BinaryHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                                        saveImage(imgFile, bytes);
                                        startActivity();
                                    }

                                    @Override
                                    public void onFailure(int i, Header[] headers, byte[] bytes,
                                                          Throwable throwable) {
                                        startActivity();
                                    }
                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int i, Header[] headers, byte[] bytes,
                                              Throwable throwable) {
                            startActivity();
                        }
                    });
                } else {
                    Toast.makeText(SplashActivity.this, "没有网络连接!", Toast.LENGTH_LONG).show();
                    startActivity();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        iv_start.startAnimation(scaleAnim);
    }

    private void startActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
        finish();
    }

    public void saveImage(File file, byte[] bytes) {
        try {
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
