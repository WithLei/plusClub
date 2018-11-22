package com.android.renly.plusclub.Activity;

import android.os.Bundle;
import android.widget.TextView;

import com.android.renly.plusclub.Module.base.BaseActivity;
import com.android.renly.plusclub.R;
import com.zzhoujay.richtext.RichText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class OpenSourceActivity extends BaseActivity {
    @BindView(R.id.text)
    TextView text;
    private Unbinder unbinder;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_opensource;
    }

    @Override
    protected void initData() {
        String content = "**# 一切伟大的行动和思想**  \n**都有一个微不足道的开始**\n" +
                "\n" +
                "## 项目介绍\n" +
                "[PlusClub](http://118.24.0.78/#/home)校园论坛\n" +
                "支持主题切换、发帖、回帖、课程表、校园卡收支\n" +
                "\n" +
                "Web端项目：[Robinson28years/lsuplusclub](https://github.com/Robinson28years/lsuplusclub)  \n" +
                "前移动端项目：[xiaoshidefeng/PLUS-for-Android](https://github.com/xiaoshidefeng/PLUS-for-Android)\n" +
                "\n" +
                "## 意见和反馈\n" +
                "联系我们：\n" +
                " - heyrenly@163.com\n" +
                " - bb.chen@outlook.com\n" +
                "\n" +
                "## 开源框架\n" +
                "### RxJava\n" +
                "> RxJava is a Java VM implementation of Reactive Extensions: a library for composing asynchronous and event-based programs by using observable sequences.  \n" +
                "> [ReactiveX](https://github.com/ReactiveX/RxJava)\n" +
                "### FastJson\n" +
                "> Fastjson is a Java library that can be used to convert Java Objects into their JSON representation. It can also be used to convert a JSON string to an equivalent Java object. Fastjson can work with arbitrary Java objects including pre-existing objects that you do not have source-code of.  \n" +
                "> [Alibaba](https://github.com/alibaba/fastjson)\n" +
                "### OkHttpUtils\n" +
                "> An HTTP & HTTP/2 client for Android and Java applications.  \n" +
                "> [hongyangAndroid](https://github.com/hongyangAndroid/okhttputils)\n" +
                "### EventBus\n" +
                "> Event bus for Android and Java that simplifies communication between Activities, Fragments, Threads, Services, etc. Less code, better quality.  \n" +
                "> [greenrobot](https://github.com/greenrobot/EventBus)\n" +
                "### Butter Knife\n" +
                "> Field and method binding for Android views which uses annotation processing to generate boilerplate code for you.  \n" +
                "> [JakeWharton](https://github.com/JakeWharton/butterknife)\n" +
                "### jsoup\n" +
                "> jsoup is a Java library for working with real-world HTML. It provides a very convenient API for extracting and manipulating data, using the best of DOM, CSS, and jquery-like methods.  \n" +
                "> [jhy](https://github.com/jhy/jsoup)\n" +
                "### Picasso\n" +
                "> A powerful image downloading and caching library for Android.Images add much-needed context and visual flair to Android applications. Picasso allows for hassle-free image loading in your application—often in one line of code!  \n" +
                "> [square](https://github.com/square/picasso)\n" +
                "### RichText\n" +
                "> Android平台下的富文本解析器，支持Html和Markdown  \n" +
                "> [zzhoujay](https://github.com/zzhoujay/RichText)\n" +
                "### Slidr\n" +
                "> Easily add slide-to-dismiss functionality to your Activity by calling Slidr.attach(this) in your onCreate(..) method.  \n" +
                "> [r0adkll](https://github.com/r0adkll/Slidr)\n" +
                "### Android Sliding Up Panel\n" +
                "> This library provides a simple way to add a draggable sliding up panel (popularized by Google Music and Google Maps) to your Android application.  \n" +
                "> [umano](https://github.com/umano/AndroidSlidingUpPanel)\n" +
                "### nice-spinner\n" +
                "> NiceSpinner is a re-implementation of the default Android's spinner, with a nice arrow animation and a different way to display its content.  \n" +
                "> [arcadefire](https://github.com/arcadefire/nice-spinner)\n" +
                "### SmartRefreshLayout\n" +
                "> SmartRefreshLayout is a \"smart\" or \"intelligent\" pull-down refresh layout，because of its \"smart\", it does not just support all the Views , but also support multi-layered nested view structures.  \n" +
                "> [scwang90](https://github.com/scwang90/SmartRefreshLayout)\n" +
                "## License\n" +
                "Copyright 2016-2018 PlusClub  \n" +
                "\n" +
                "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                "you may not use this file except in compliance with the License.\n" +
                "You may obtain a copy of the License at  \n" +
                "\n" +
                "http://www.apache.org/licenses/LICENSE-2.0  \n" +
                "\n" +
                "Unless required by applicable law or agreed to in writing, software\n" +
                "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                "See the License for the specific language governing permissions and\n" +
                "limitations under the License.";
        RichText.fromMarkdown(content).into(text);
    }

    @Override
    protected void initView() {
        initToolBar(true,"热爱开源，感谢分享");
        initSlidr();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
