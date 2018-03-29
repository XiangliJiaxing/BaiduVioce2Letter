package com.vito.voice.voicedetect;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView regTmpResultTextView;
    private  EventManager asr; // 管理类必须为单例. SDK中 无需调用任何逻辑，但需要创建一个新的识别事件管理器的话，之前的那个请设置为null，并不再使用。

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });

        regTmpResultTextView = findViewById(R.id.tv_recognize_tmp_result);

        findViewById(R.id.tv_end).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
            }
        });
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
                    }else if(name.equals(SpeechConstant.CALLBACK_EVENT_ASR_FINISH)){
                        // 识别结束(可能含有错误信息)
                        Log.i(TAG, "***识别结束***");
                        Log.i(TAG, "*** params : " + params);
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
                "\"sample\":16000}";
        asr.send(SpeechConstant.ASR_START, json, null, 0, 0);
    }


    private void stop(){
        asr.send(SpeechConstant.ASR_STOP, null, null, 0, 0); // 发送停止录音事件，提前结束录音等待识别结果
    }

    private void cancel(){
        asr.send(SpeechConstant.ASR_CANCEL, null, null, 0, 0); // 取消本次识别，取消后将立即停止不会返回识别结果
    }


}
