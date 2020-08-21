## 项目介绍
这是一套封装好的自定义View

## 使用文档

#### 最新版本

[![](https://jitpack.io/v/yinshuai0324/ViewLibrary.svg)](https://jitpack.io/#yinshuai0324/ViewLibrary)

#### 引入项目
```
//项目根目录下的build.gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}

//app下的build.gradle
dependencies {
 //引入依赖库
 implementation 'com.github.yinshuai0324:ViewLibrary:v1.0.1'
}
```

#### 目前已有的控件
- NetImageView 加载网络图片的Image
- RoundNetworkImageView 带圆角的网络图片
- SelectBtn 选中的Button 继承自ImageView
