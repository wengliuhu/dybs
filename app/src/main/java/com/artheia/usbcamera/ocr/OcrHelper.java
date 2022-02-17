package com.artheia.usbcamera.ocr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;
import android.widget.Toast;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * @author : wengliuhu
 * @version : 0.1
 * @since : 2022/2/15 11:00
 * Describe：
 */
public class OcrHelper {
    private final String TAG = getClass().toString();
    private boolean hasGotToken;
    private static class SingletonHolder{
        private final static OcrHelper INSTANCE = new OcrHelper();
        private SingletonHolder() {
        }
    }

    public static OcrHelper getInstance(){
        return SingletonHolder.INSTANCE;
    }

    /**
     * 用明文ak，sk初始化
     */
    public void initAccessTokenWithAkSk(Context context) {
        OCR.getInstance(context.getApplicationContext()).initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                String token = result.getAccessToken();
                hasGotToken = true;
//                Toast.makeText(context.getApplicationContext(), "AK，SK方式获取token成功", Toast.LENGTH_LONG).show();
                Log.d(TAG, "AK，SK方式获取token成功:" + token);


            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                hasGotToken = false;
                Observable.just(1)
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Integer>() {
                            @Override
                            public void onSubscribe( Disposable d) {

                            }

                            @Override
                            public void onNext(Integer integer) {
                                Toast.makeText(context.getApplicationContext(), "AK，SK方式获取token失败:" + error.getMessage(), Toast.LENGTH_LONG).show();
                                Log.d(TAG, "AK，SK方式获取token失败:" + error.getMessage());
                            }

                            @Override
                            public void onError( Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
            //}, getApplicationContext(),  "请填入您的AK", "请填入您的SK");
        }, context.getApplicationContext(),  "jiyHEi6HuxGY0xl3HetNHNYa", "4ES8uLqBxBTLxh2S0blf9z3CktIblGtA");
    }

    /**
     * 翻转图片
     * @param picpath
     */
    public boolean recognizeGeneralBasic(String picpath){
        //获取图片
        Bitmap origlnalBmp = null;
        Bitmap resizeBitmap = null;

        boolean result = false; //默认结果
        OutputStream outputStream = null; //文件输出流
        try {
            origlnalBmp = BitmapFactory.decodeFile(picpath);
            Matrix matrix = new Matrix();
            // 缩放 当sy为-1时向上翻转 当sx为-1时向左翻转 sx、sy都为-1时相当于旋转180°
            matrix.postScale(-1, 1);
//            matrix.setRotate(180);
            // 因为向上翻转了所以y要向下平移一个bitmap的高度
//            matrix.postTranslate(0, origlnalBmp.getHeight());
            matrix.postTranslate(origlnalBmp.getWidth(), 0);
            resizeBitmap = Bitmap.createBitmap(origlnalBmp, 0, 0, origlnalBmp.getWidth(), origlnalBmp.getHeight(), matrix, true);
            if (resizeBitmap == null || picpath == null) {
                return false;
            }
            File file = new File(picpath);
            outputStream = new FileOutputStream(file);
            result = resizeBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream); //将图片压缩为JPEG格式写到文件输出流，100是最大的质量程度
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close(); //关闭输出流
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (origlnalBmp != null) origlnalBmp.recycle();
            if (resizeBitmap != null) resizeBitmap.recycle();
        }
        return result;
    }
}
