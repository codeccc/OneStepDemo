package com.wb.onestep;

import android.content.res.AssetManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.wb.onestep.databinding.ActivityOnestepDragBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import smartisanos.api.OneStepHelper;

/**
 * @author : wangbo
 * @date : 2018/9/14
 * Function : TODO 请在这里输入文件用途
 * @desc : TODO 请在这里输入文件描述
 * @targetVersion : TODO 请在这里输入是为哪一版开始添加的
 */
public class OnestepDragActivity extends AppCompatActivity implements View.OnLongClickListener
{

    private OneStepHelper mOneStepHelper;
    private ActivityOnestepDragBinding mBinding;
    private String[] imageNames
            = new String[]{"ic_smartisan_pro2s_1.jpg", "ic_smartisan_pro2s_2.jpg"
            , "ic_smartisan_pro2s_3.jpg", "ic_smartisan_pro2s_4.jpg"
            , "ic_smartisan_pro2s_5.jpg", "ic_smartisan_pro2s_6.jpg"};
    private File singleImage;
    private File multiImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mBinding
                = DataBindingUtil.setContentView(
                OnestepDragActivity.this, R.layout.activity_onestep_drag);
        mOneStepHelper = OneStepHelper.getInstance(OnestepDragActivity.this);
        setLongClick(mBinding.imgMultiStyle, mBinding.imgSingleStyle
                , mBinding.txtLinkStyle, mBinding.txtTextStyle);
        //TODO NOTICE:确保保存目录已存在
        new File(SAMPLE_FILE_DIR).mkdir();

        //初始化单图拖拽预览图
        mBinding.imgSingleStyle
                .setImageBitmap(getImageFromAssetsFile("ic_smartisan_pro2s_single.jpg"));

        //初始化多图拖拽预览图
        mBinding.imgMultiStyle
                .setImageBitmap(getImageFromAssetsFile(imageNames[0]));
    }

    private void setLongClick(View... views)
    {
        for (int i = 0; i < views.length; i++)
        {
            views[i].setOnLongClickListener(this);
        }
    }

    @Override
    public boolean onLongClick(View view)
    {
        switch (view.getId())
        {
            case R.id.txt_link_style:
                //链接类型
                if (mOneStepHelper.isOneStepShowing())
                {
                    mOneStepHelper.dragLink(view, mBinding.txtLinkStyle.getText().toString().trim());
                }
                break;
            case R.id.txt_text_style:
                //文本类型
                if (mOneStepHelper.isOneStepShowing())
                {
                    mOneStepHelper.dragText(view, mBinding.txtTextStyle.getText().toString().trim());
                }
                break;
            case R.id.img_multi_style:
                //多图类型
                if (mOneStepHelper.isOneStepShowing())
                {
                    File[] files = new File[imageNames.length];
                    String[] mimeTypes = new String[imageNames.length];
                    for (int i = 0; i < files.length; i++)
                    {
                        files[i] = new File(SAMPLE_FILE_DIR, imageNames[i]);
                        if (!files[i].exists())
                        {
                            copyAssetFile2Sdcard(imageNames[i]);
                        }
                        mimeTypes[i] = "image/jpeg";
                    }
                    mOneStepHelper.dragMultipleImages(view, files, mimeTypes);
                    return true;
                }
                break;
            case R.id.img_single_style:
                //单图类型
                if (mOneStepHelper.isOneStepShowing())
                {
                    File image = new File(SAMPLE_FILE_DIR,"ic_smartisan_pro2s_single.jpg");
                    if (!image.exists())
                    {
                        copyAssetFile2Sdcard("ic_smartisan_pro2s_single.jpg");
                    }
                    mOneStepHelper.dragImage(view, image, "image/jpeg");
                }
                break;
        }
        return true;
    }

    /**
     * 保存目录
     */
    private static final String SAMPLE_FILE_DIR
            = Environment.getExternalStorageDirectory() + "/OneStepDemo/";

    /**
     * 创建文件
     * @param filename 文件名称
     * @return
     */
    private File createTestFileIfNotExists(String filename)
    {
        File testFile = new File(SAMPLE_FILE_DIR, filename);
        if (!testFile.exists())
        {
            try
            {
                testFile.createNewFile();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return testFile;
    }

    /**
     * 将图片从assets中拷贝到sd卡中
     * @param assetFile 文件
     * @return
     */
    private void copyAssetFile2Sdcard(String assetFile)
    {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try
        {
            inputStream = getAssets().open(assetFile);
            String destFilePath = createTestFileIfNotExists(assetFile).getAbsolutePath();
            File f = new File(destFilePath);
            outputStream = new FileOutputStream(f);
            byte[] buf = new byte[1024 * 4];
            int len = 0;
            while ((len = inputStream.read(buf)) > 0)
            {
                outputStream.write(buf, 0, len);
            }
            outputStream.flush();
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (outputStream != null)
                {
                    outputStream.close();
                }
                if (inputStream != null)
                {
                    inputStream.close();
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从assets中获取图片bitmap
     * @param fileName 文件名
     * @return
     */
    private Bitmap getImageFromAssetsFile(String fileName)
    {
        Bitmap image = null;
        AssetManager am = getResources().getAssets();
        InputStream is = null;
        try
        {
            is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                is.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return image;
    }

}
