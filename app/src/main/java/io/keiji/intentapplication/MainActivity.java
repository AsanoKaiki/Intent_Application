package io.keiji.intentapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    // <対象> のファイルは, git status で表示させて、
    // ダブルクリックで選択してコピペすればいい。

    // コマンドライン上でもとに戻せなくなったときの対応
    // [q] ... q を押すだけ。
    // [ctr + D], [ctr + C] ... 実行中のプログラムを強制終了する。
    // -------

    // $ git statu
    //　これは変更のテスト

    // 変更を見るには
    // $ git diff <対象>
    // 例: $ git diff app/src/main/java/io/keiji/intentapplication/MainActivity.java
    // ------ ここまでが確認する方法

    // 変更を git に教える
    // $ git add <対象>
    // ... git status で赤く表示された部分を緑色にする。
    // ... 緑色にしたファイルは、 「git がすでにわかっているファイル」

    // (もし, 間違えて add してしまった場合)
    // ($ git reset <対象>)

    //「git がすでにわかっているファイル」を git の log に登録する。
    // $ git commit -m "  --commit コメント-- "


    static final int REQUEST_CODE_MAP = 1;
    static final int REQUEST_CODE_MAIL = 2;
    static final int REQUEST_CODE_CAMERA = 3;
    static final int REQUEST_CODE_GALLERY = 4;


    ImageView imageView;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView)findViewById(R.id.imageView);
        editText = (EditText)findViewById(R.id.editText);
        editText.setText("東京都港区南麻布2-12-3");
    }
    public void map(View v){
        String location = "go:0,0?q=" + editText.getText().toString();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(location));
        startActivityForResult(intent,REQUEST_CODE_MAP);
    }
    public void gallery(View v){
        Intent intent = new Intent(Intent.ACTION_PICK,
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
        startActivityForResult(intent, REQUEST_CODE_GALLERY);

    }

    public void mail(View v){
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:life.is.tech@gmail.com"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "題名");
        intent.putExtra(Intent.EXTRA_TEXT, "本文");
        startActivityForResult(intent,REQUEST_CODE_MAIL);

    }
    public void wed(View v) {
        Intent intent;
        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://life-is-tech.com/"));
        startActivity(intent);
    }


    public void app(View v){
        PackageManager pm = getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage("com.android.vending");
        startActivity(intent);
    }
    public void camera(View v){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_GALLERY){

                try {
                    InputStream inputStream = getContentResolver().openInputStream(intent.getData());
                    Bitmap bmp = BitmapFactory.decodeStream(inputStream);
                    imageView.setImageBitmap(bmp);
                }catch (Exception e){
                    Toast.makeText(MainActivity.this, "エラー", Toast.LENGTH_LONG).show();

                }

            }else if (requestCode == REQUEST_CODE_CAMERA){

                Bitmap bpm = (Bitmap) intent.getExtras().get("data");
                imageView.setImageBitmap(bpm);

            }
        }else if (resultCode == RESULT_CANCELED){
            Toast.makeText(MainActivity.this, "CANCEL" , Toast.LENGTH_LONG).show();

        }
    }



}
