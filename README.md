# Android网络请求数据模拟


### 功能介绍：
1、支持POST、GET、PUT、DELETE、HEAD、PATCH、OPTIONS、HTTP

2、支持多接口同时mock

3、支持动态控制mock开关

#### 使用方法：

1、在root目录的build.gradle目录中添加
```groovy
    allprojects {
        repositories {
            maven { url 'https://jitpack.io' }
        }
    }
```


2、在项目的build.gradle中添加依赖
```groovy
    dependencies {
        implementation 'com.github.wshychbydh:mock:Tag'
    }
```

**注**：如果编译的时候报重复的'META-INF/app_release.kotlin_module'时，在app的build.gradle文件的android下添加
```groovy
    packagingOptions {
        exclude 'META-INF/app_release.kotlin_module'
    }
```
报其他类似的重复错误时，添加方式同上。

3、构建Params实例
```kotlin
    MockHelper.addMockApi(MockApi) //添加单个接口模拟
    MockHelper.addMockSource()  //添加整个接口模拟类，该类中所有符合条件的接口都会被模拟 
```
 
**Demo地址：(https://github.com/wshychbydh/SampleDemo)**    

###### **欢迎fork，期待你的宝贵意见.** (*￣︶￣)

###### 联系方式 wshychbydh@gmail.com

[![](https://jitpack.io/v/wshychbydh/photo.svg)](https://jitpack.io/#wshychbydh/photo)
