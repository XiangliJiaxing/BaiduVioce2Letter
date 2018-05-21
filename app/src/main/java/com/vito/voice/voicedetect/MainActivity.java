package com.vito.voice.voicedetect;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.google.gson.Gson;
import com.iflytek.cloud.SpeechUtility;
import com.vitovoiceprint.VocalVerifyDemo;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView regTmpResultTextView;
    private TextView finalRegResultTextView;
    private  EventManager asr; // 管理类必须为单例. SDK中 无需调用任何逻辑，但需要创建一个新的识别事件管理器的话，之前的那个请设置为null，并不再使用。
    private View startView;
    private View endView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startView = findViewById(R.id.tv_start);
        startView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });

        regTmpResultTextView = findViewById(R.id.tv_recognize_tmp_result);
        finalRegResultTextView = findViewById(R.id.tv_recognize_result);
        endView = findViewById(R.id.tv_end);
        endView.setVisibility(View.INVISIBLE);
        endView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
            }
        });
        progressBar = findViewById(R.id.progress_bar);


        // 声纹识别入口
        findViewById(R.id.tv_vioceprint).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // 初始化
                SpeechUtility.createUtility(MainActivity.this, com.iflytek.cloud.SpeechConstant.APPID +"=5b023950"
                        +"," + com.iflytek.cloud.SpeechConstant.ENGINE_MODE + "=" + com.iflytek.cloud.SpeechConstant.MODE_MSC);
                Intent intent = new Intent(MainActivity.this, VocalVerifyDemo.class);
                startActivity(intent);
            }
        });

        String[] permissions = new String []{Manifest.permission.RECORD_AUDIO, Manifest.permission.INTERNET};
        PermissonUtil.requestPermissionForActivity(this, permissions, 100, true, new PermissonUtil.IGoToSettingListener() {
            @Override
            public void gotoSetting() {
                JumpPermissionManagement.GoToSetting(MainActivity.this);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    /**
     * 初始化语言识别引擎
     */
    private void initEngine(){
//        if(asr == null){
            asr = EventManagerFactory.create(this, "asr");

            EventListener yourListener = new EventListener() {
                @Override
                public void onEvent(String name, String params, byte[] data, int offset, int length) {
                    if(name.equals(SpeechConstant.CALLBACK_EVENT_ASR_READY)){
                        // 引擎就绪，可以说话，一般在收到此事件后通过UI通知用户可以说话了
                        Toast.makeText(MainActivity.this, "引擎准备就绪", Toast.LENGTH_LONG).show();
                        startView.setVisibility(View.INVISIBLE);
                        endView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }else if(name.equals(SpeechConstant.CALLBACK_EVENT_ASR_BEGIN)){
                        Log.i(TAG, "***检测到说话开始***");
                    }else if(name.equals(SpeechConstant.CALLBACK_EVENT_ASR_END)){
                        Log.i(TAG, "***检测到说话结束***");
                        Log.i(TAG, "*** params : " + params);
                    }else if(name.equals(SpeechConstant.CALLBACK_EVENT_ASR_LONG_SPEECH)){
                        Log.i(TAG, "***长语音识别结束***");
                        Log.i(TAG, "*** params : " + params);
                    } else if(name.equals(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL)) {
                        Log.i(TAG, "*** 临时结果 params : " + params);
                        if(null == params){
                            regTmpResultTextView.setText("");
                        }else{
                            Gson gson = new Gson();
                            TempResult tempResult = gson.fromJson(params, TempResult.class);
                            regTmpResultTextView.setText(tempResult.getResults_recognition().get(0));
                        }
                    }else if(name.equals(SpeechConstant.CALLBACK_EVENT_ASR_EXIT)){
                        Log.i(TAG, "***识别结束释放资源***");
                        Log.i(TAG, "*** params : " + params);
                        Toast.makeText(MainActivity.this, "引擎已关闭", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        startView.setVisibility(View.VISIBLE);
                        endView.setVisibility(View.INVISIBLE);
                    }else if(name.equals(SpeechConstant.CALLBACK_EVENT_ASR_FINISH)){
                        // 识别结束(可能含有错误信息)
                        Log.i(TAG, "***断句识别结束***");
                        String beforeFinalResult = finalRegResultTextView.getText().toString();
                        if (TextUtils.isEmpty(beforeFinalResult)) {
                            finalRegResultTextView.setText(regTmpResultTextView.getText().toString());
                        } else {
                            Spanned spanned = Html.fromHtml(beforeFinalResult.replace("##断句##", "<font color='#65b5d2'>##断句##</font>")
                                    + "\r\n<font color='#65b5d2'>##断句##</font>\r\n" + regTmpResultTextView.getText().toString());
                            finalRegResultTextView.setText(spanned);
                        }
                    }else if(name.equals(SpeechConstant.CALLBACK_EVENT_ASR_AUDIO)){
                        // todo: 语音音频回调
                    }else if(name.equals(SpeechConstant.CALLBACK_EVENT_ASR_VOLUME)){
                        // todo: 音量回调

                    }
                    // ... 支持的输出事件和事件支持的事件参数见“输入和输出参数”一节
                }
            };
            asr.registerListener(yourListener);
//        }
    }

    private void start(){
        progressBar.setVisibility(View.VISIBLE);
        initEngine();
//        SpeechConstant.ACCEPT_AUDIO_DATA;
//        SpeechConstant.APP_ID;
//        SpeechConstant.SECRET;
        String json = "{\"accept-audio-data\":true," +
                "\"disable-punctuation\":false," +
                "\"accept-audio-volume\":true," +
                "\"pid\":1536," +
                "\"appid\":11000641," +
                "\"key\":\"4uvLCpOsMcGUUmeDLo7BNhgQ\"," +
                "\"secret\":\"72a81a14f3c6671d051c199200e1005e\"," +
                SpeechConstant.VAD_ENDPOINT_TIMEOUT + ":0," +
                "\"sample\":16000}";
        asr.send(SpeechConstant.ASR_START, json, null, 0, 0);
    }


    private void stop(){
        progressBar.setVisibility(View.VISIBLE);
        asr.send(SpeechConstant.ASR_STOP, null, null, 0, 0); // 发送停止录音事件，提前结束录音等待识别结果
    }

    private void cancel(){
        asr.send(SpeechConstant.ASR_CANCEL, null, null, 0, 0); // 取消本次识别，取消后将立即停止不会返回识别结果
    }


}
