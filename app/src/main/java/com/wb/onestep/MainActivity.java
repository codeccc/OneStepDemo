package com.wb.onestep;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{

    private TextView txtInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtInfo = findViewById(R.id.txtInfo);


    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Intent intent = getIntent();
        if (intent == null || intent.getExtras() == null)
        {
            return;
        }
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null)
        {
            if ("text/plain".equals(type))
            {//文字
                String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                if (sharedText != null)
                {
                    // todo: 处理文字
                    txtInfo.setText("分享的文字:" + sharedText);
                }
            } else if (type.startsWith("image/"))
            {//单张图片和文字
                String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                if (sharedText != null)
                {
                    // todo:处理文字
                    txtInfo.setText("分享的文字:" + sharedText);
                }
                Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
                // todo:处理图片路径
                txtInfo.append("\n图片uri" + imageUri.toString() + "\n路径: " + imageUri.getPath());
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null)
        {//多张图片
            if (type.startsWith("image/"))
            {
                txtInfo.setText("");
                ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
                for (Uri uri : imageUris)
                {
                    // todo: 处理图片路径
                    txtInfo.append("\n图片路径: " + uri.toString());
                }
            }
        }
    }
}
