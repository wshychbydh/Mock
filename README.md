# Android网络请求数据模拟

### 功能介绍：
1、支持POST、GET、PUT、DELETE、HEAD、PATCH、OPTIONS、HTTP

2、支持多个接口同时mock

3、支持模拟延迟返回数据

4、支持动态控制mock开关

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

3、使用实例
```kotlin
    //模拟数据源包装类
    interface DataSourceProvider {
      fun <T> createMockWrapper(data: T): String {
        val wrapper = NetResult(data = data)
        return gson.toJson(wrapper)
      }
    
      fun <T> toJson(data: T): String = gson.toJson(data)
    
      private class NetResult<T>(
          val code: Int = 0,
          val message: String = "success",
          val data: T?,
      )
    
      companion object {
        private val gson = Gson()
      }
    }
    
    //模拟数据接口实现类
    object MockImpl : DataSourceProvider {
    
      @GET("/weather")
      fun fetchWeather(): String {
        SystemClock.sleep(1000)
        val weather = MockWeather("北京", "小雨")
        return toJson(weather)
      }
    
      @MOCK(
            method = "GET",
            url = "/traffic",
            delay = 2000
      )
      fun fetchTraffic(): String {
        SystemClock.sleep(1000)
        val traffic = MockTraffic("北京", "拥堵")
        return toJson(traffic)
      }
    }
    
    //数据展示页面
    class MockActivity : AppCompatActivity() {
      override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mock)
        lifecycleScope.launch(Dispatchers.IO) {

          //添加模拟数据源
          MockHelper.addMockDataSource(MockImpl)
          //开启数据模拟
          MockHelper.enableMock(true)

          val retrofit = createRetrofit()
          val api = retrofit.create(MockApiService::class.java)
  
          //请求数据，若enableMock设置为true，将从模拟数据中返回
          val weather = api.fetchWeather()
          val traffic = api.fetchTraffic()
    
          withContext(Dispatchers.Main) {
            trafficTv.text = traffic.toString()
            weatherTv.text = weather.toString()
          }
        }
      }
    
      //创建retrofit
      private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://mock/api/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient().newBuilder()
                    .addInterceptor(MockInterceptor())
                    .build()
            )
            .build()
      }
    }
```

**主要参数设置**：
```kotlin
    //添加模拟数据源，该类中所有符合条件的接口都会被模拟
    //注：模拟数据源中的设置的url和method必须同接口定义一致才能匹配成功。
    MockHelper.addMockDataSource(MockImpl)
    //或添加单个接口模拟
    MockHelper.addMockApi(MockApi) 
    //开启数据模拟
    MockHelper.enableMock(true)
```
 
**Demo地址：(https://github.com/wshychbydh/SampleDemo)**    

###### **欢迎fork，期待你的宝贵意见.** (*￣︶￣)

###### 联系方式 wshychbydh@gmail.com

[![](https://jitpack.io/v/wshychbydh/mock.svg)](https://jitpack.io/#wshychbydh/mock)
