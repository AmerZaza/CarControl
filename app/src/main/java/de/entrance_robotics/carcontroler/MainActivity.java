package de.entrance_robotics.carcontroler;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import com.aldebaran.qi.Future;
import com.aldebaran.qi.sdk.QiContext;
import com.aldebaran.qi.sdk.QiSDK;
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks;
import com.aldebaran.qi.sdk.builder.ChatBuilder;
import com.aldebaran.qi.sdk.builder.QiChatbotBuilder;
import com.aldebaran.qi.sdk.builder.TopicBuilder;
import com.aldebaran.qi.sdk.design.activity.RobotActivity;
import com.aldebaran.qi.sdk.object.conversation.AutonomousReactionImportance;
import com.aldebaran.qi.sdk.object.conversation.AutonomousReactionValidity;
import com.aldebaran.qi.sdk.object.conversation.Bookmark;
import com.aldebaran.qi.sdk.object.conversation.Chat;
import com.aldebaran.qi.sdk.object.conversation.QiChatVariable;
import com.aldebaran.qi.sdk.object.conversation.QiChatbot;
import com.aldebaran.qi.sdk.object.conversation.Topic;

public class MainActivity extends RobotActivity implements RobotLifecycleCallbacks {

    Button forward ;
    Button right;
    Button left;
    Button back;
    Button stop;

    WebView main;
    public final String ip = "http://192.168.1.22";

    private Chat chat;  //public QiContext qiContext



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        QiSDK.register(this, this);

        forward = (Button)findViewById(R.id.btnForward);
        right = (Button)findViewById(R.id.btnRight);
        left = (Button)findViewById(R.id.btnLeft);
        back = (Button)findViewById(R.id.btnBack);
        stop = (Button)findViewById(R.id.btnStop) ;

        main = (WebView)findViewById(R.id.wvMain);
        main.loadUrl(ip);

        WebSettings webSettings = main.getSettings();

        webSettings.setJavaScriptEnabled(true);

    }

    @Override
    public void onRobotFocusGained(QiContext qiContext) {
        // Create a topic.
        Topic topic = TopicBuilder.with(qiContext) // Create the builder using the QiContext.
                .withResource(R.raw.topic) // Set the topic resource.
                .build(); // Build the topic.

        // Create a new QiChatbot.
        QiChatbot qiChatbot = QiChatbotBuilder.with(qiContext)
                .withTopic(topic)
                .build();


        // Create a new Chat action.
        chat = ChatBuilder.with(qiContext)
                .withChatbot(qiChatbot)
                .build();

        // Add an on started listener to the Chat action.
        chat.addOnStartedListener(() -> {
            Log.i("az", "Discussion started.");
        });

        QiChatVariable auswahl = qiChatbot.variable("auswahl");

        auswahl.addOnValueChangedListener(currentValue -> {

            // String humanSelect = auswahl.getValue();

            Log.i("az","Curent Value ->> "+currentValue);

            if(currentValue != null){

                switch (currentValue){
                    case "rechts":
                        main.post(new Runnable() {
                            @Override
                            public void run() {
                                main.loadUrl(ip+"/right");
                            }
                        });
                        break;
                    case "links":

                        main.post(new Runnable() {
                            @Override
                            public void run() {
                                main.loadUrl(ip+"/left");
                            }
                        });
                        break;
                    case "vorne":
                        main.post(new Runnable() {
                            @Override
                            public void run() {
                                main.loadUrl(ip+"/go");
                            }
                        });
                        break;
                    case "zurück":
                        main.post(new Runnable() {
                            @Override
                            public void run() {
                                main.loadUrl(ip+"/back");
                            }
                        });

                        break;
                    case "stop":
                        main.post(new Runnable() {
                            @Override
                            public void run() {
                                main.loadUrl(ip+"/stop");
                            }
                        });
                        break;
                    case "anschalten":
                        main.post(new Runnable() {
                            @Override
                            public void run() {
                                main.loadUrl(ip+"/ledon");
                            }
                        });
                        break;
                    case "ausschalten":
                        main.post(new Runnable() {
                            @Override
                            public void run() {
                                main.loadUrl(ip+"/ledoff");
                            }
                        });

                        break;
                    default:
                        main.post(new Runnable() {
                            @Override
                            public void run() {
                                main.loadUrl(ip+"/stop");
                            }
                        });
                        break;
                }

              //  qiChatbot.variable("auswahl").setValue("null");  // to get new Value s
            }  // If CurrentValue not Null
        });

        // Icebreaker :
        Bookmark icebreaker = topic.getBookmarks().get("ICEBREAKER");
        chat.addOnStartedListener(()-> qiChatbot.goToBookmark(
                icebreaker,
                AutonomousReactionImportance.HIGH,
                AutonomousReactionValidity.IMMEDIATE
        ) );

        // Run the Chat action asynchronously.   !!!!
        Future<Void> chatFuture = chat.async().run();



    }

    @Override
    public void onRobotFocusLost() {
        // Remove on started listeners from the Chat action.
        if (chat != null) {
            chat.removeAllOnStartedListeners();
        }

    }

    @Override
    public void onRobotFocusRefused(String reason) {

    }

    public void goForward(View view) {
        main.loadUrl(ip +"/go");
    }

    public void goRight(View view) {
        main.loadUrl(ip +"/right");
    }

    public void goLeft(View view) {
        main.loadUrl(ip +"/left");
    }

    public void goBack(View view) {
        main.loadUrl(ip +"/back");
    }

    public void stop(View view) {
        main.loadUrl(ip +"/stop");
    }

    public void lightOn(View view) {
        main.loadUrl(ip +"/ledon");
    }

    public void lightOff(View view) {
        main.loadUrl(ip +"/ledoff");
    }
}
