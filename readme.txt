//virgolib 2022/03/09 书写规范，集大成者
1. 项目常用配置：
    a: gradle库源：该用阿里云;
    b: gradle 软件版本信息;
    c: release 签名包 -> signKey;
    d: 多渠道打包：适配机型、android 不同版本;

2. 项目文件层级：
    b: bean: 自定义对象类（区分数据库的bean）;
    c: config: 常量(Constants), application, shareDao;
    d: data: 数据库专用;
    e: event: eventbus 注册类;
    h: helper: 静态、非静态工具类;
    i: interfaces: 接口类;
    m: mainbody: 四大组件（activity[fragment]，services，broadcast receiver,content provider）;
    w: widget: 自定义控件;
    

3. 添加本地资源、库：
    a: libs -> .jar, .aar   引用 implementation fileTree(include: ['*.jar', "*.aar"], dir: 'libs'); aar需手动添加，jar可as libary
    b. main 下创建 assets (app -> new -> Folder -> Assets Folder) -> 访问的时候需要AssetManager类;
    c. res 下创建 raw (res -> new -> Android Resource Directory -> Resource Type 选择 raw) -> 访问的时候直接使用资源ID;
            drawable: .xml 样式文件（自定义背景，边框）；
            mipmap: 图标文件（.jpg, .png）；

    d. main 下创建 jniLibs -> ndk so库集 [arm64-v8a, armeabi, armeabi-v7a...];
        android{
            sourceSets {
                main {
                    jniLibs.srcDirs = ['libs']
                }
            }

            //或
            sourceSets.main {
                 ni.srcDirs = []
                 jniLibs.srcDir "src/main/libs"
            }

            //或
            sourceSets.main.jniLibs.srcDirs = ['libs']

            //或
            sourceSets {
                main {
                    jniLibs.srcDir 'src/main/libs'
                }
            }
        }

    e. android 下显示的jniLibs 目录和 project 下的libs 其实是一个?;
    f. values:
        dimen: 大小单位引用；
        arrays: 引用变量数组；
        styles: 自定义样式；

4. 同步git:（安装git）
    a; ternimal -> git init -> 创建本地git库 .git;
    b: commit;
    c: VCS -> Git -> Remotes 添加git项目地址；
    d: ctrl+shift+k 上传;

////////////////////////////////////////////////////////////////////////////////////////////////////
mainactivity:
1. 权限申请：
    Android 6.0以前：只需在AndroidManifest注册使用的权限即可使用；
    Android 6.0+（M 23）：正常权限，注册后，系统会自动授予该权限；
                        危险权限，部分需动态申请权限（弹框，有些需跳转到设置页手动授权)；
                        权限组以一盖全；
    Android 10+ (Q 29)：权限按单个分配，不再按组分配；
                        app 对自身内/外部文件访问不需要权限申请；
                        访问别的应用保存在公有目录下的文件需读取权限；
                        对后台应用可启动 Activity进行限制, 当App的Activity不在前台时，其启动Activity会被系统拦截，导致无法启动，
                        自启 需申请 SYSTEM_ALERT_WINDOW 权限；
    Android 11+ (R 30)：对自身文件不需要申请权限；
                        访问外部其他文件可申请 MANAGE_EXTERNAL_STORAGE；（WRITE_EXTERNAL_STORAGE 已无效）


    部分功能可通过intent调用第三方app完成，可避免不必要的权限申请；

2. 自定义 Application（仅一个）：（没需求可忽略，系统会自动创建）
    a: 继承父类 class MyApp extends Application；
    b: 注册 AndroidManifest -> Application -> android:name=".base.MyApp"；
    c: 程序启动时，先启动Application，且每一个进程模块启动前会启动一次；
    d: Application 内数据（静态）可全局访问；

3. app 图标：
    a: app -> new -> image asset -> path 选择图片（jpg, png, jpeg）

////////////////////////////////////////////////////////////////////////////////////////////////////
后台
1. broadcast receiver:
    a: 自启广播：
        android.intent.action.BOOT_COMPLETED
    b: usb 插入拔出广播：该广播先于自启广播，且自启时自带外部存储会会触发
        android.intent.action.MEDIA_MOUNTED
        android.intent.action.MEDIA_UNMOUNTED
        android.intent.action.MEDIA_REMOVED
        android.intent.action.MEDIA_EJECT
    c: 电源、电量变化广播：
        Intent.ACTION_BATTERY_CHANGE
        Intent.ACTION_BATTERY_LOW
        Intent.ACTION_BATTERY_OKAY
        Intent.ACTION_POWER_CONNECTED
        Intent.ACTION_POWER_DISCONNECTED
    d: 网络状态变化广播：
        ConnectivityManager.CONNECTIVITY_ACTION
        android.net.ethernet.STATE_CHANGE //有线网络
        android.net.ethernet.ETHERNET_STATE_CHANGED
        WifiManager.WIFI_STATE_CHANGED_ACTION
        WifiManager.NETWORK_STATE_CHANGED_ACTION
    e: 屏幕状态变化广播：
        Intent.ACTION_SCREEN_ON
        Intent.ACTION_SCREEN_OFF
        Intent.ACTION_USER_PRESENT



////////////////////////////////////////////////////////////////////////////////////////////////////
本地依赖库：.jar/.aar/导入模块
1. virgocom: aar 控件模块；（独立打包：Terminal ->$ gradlew makeAar + (ctrl+enter))
2. virgosdk: jar 工具库模块；（独立打包：Terminal ->$ gradlew makeJar + (ctrl+enter))
3. 添加引用：settings.gradle
    include ':virgosdk'
    include ':virgocom'
4. dependencies:
    implementation project(path: ':virgosdk')
    implementation project(path: ':virgocom')

5. .jar/.aar:
    implementation fileTree(include: ['*.jar', "*.aar"], dir: 'libs')
    implementation files('libs/VirgoSDK_1.1.2.jar')
    implementation files('libs/VirgoComponent_1.1.2.jar')

开启分包：dex max is 65536(单个包最多纪录方法数)
    Dex主要组成：
    - Android FrameWork 方法数
    - Lib 方法数
    - 你自己写的代码方法数
    a: android5.0之前，dalvik规定每一个apk只能包含一个dex文件;
        defaultConfig {
            multiDexEnabled true //开启分包
        }
        dependencies {
          implementation 'com.android.support:multidex:1.0.3'
        }
    b: android5.0+(21)，开始使用了art环境取代Dalvik，而art架构本身支持多dex文件的加载;
        defaultConfig {
            multiDexEnabled true //开启分包
        }
    c: 配置Application:
        <application
                android:name="android.support.multidex.MultiDexApplication" >
                ...
        </application>
        //或者 重写Application
        @Override
        protected void attachBaseContext(Context base) {
             super.attachBaseContext(context);
             Multidex.install(this);
        }

////////////////////////////////////////////////////////////////////////////////////////////////////
原配控件使用:
 * 1. textview, editview, spinner
 * 2. button, imagebutton, checkbox, radiobutton, togglebutton, switch
 * 3. fragments
 * 4. listview, gridview, recycleview
 * 5. seekbar, processbar, ratingbar
 * 6. calendar, textclock, datepicker, timepicker
 * 7. dialog
 * 8. webview

 第三方库：
 1. greendao: 数据库
    a: project dependencies: classpath 'org.greenrobot:greendao-gradle-plugin:3.3.0'
    b: app module:
        apply plugin: 'org.greenrobot.greendao'
        android:
                //greendao 配置
                greendao {
                    schemaVersion 1 //数据库版本号
                    daoPackage 'com.nepalese.virgolib.data.db'// 设置DaoMaster、DaoSession、Dao 包名
                    targetGenDir 'src/main/java/'//设置DaoMaster、DaoSession、Dao目录
                }
        dependencies :
            implementation 'org.greenrobot:greendao:3.3.0'
            implementation 'org.greenrobot:greendao-generator:3.3.0'
    c: 创建bean类： @Entity
    d: build -> make project;

 占位符：%1$s%2$d%3$.2f
    %n：序号，第n个参数；
    $s：任意字符串；
    $d：任意数字（整型）；
    $f：任意数字（浮点型， .2f: 保留两位小数）；

 Log:有五个方法，分别是（级别由低到高）：
    在onCreate()方法的外面输入logt，然后按下Tab键，这时就会以当前的类名作为值产生一个tag常量；
    1.Log.v()[verbose]：打印一些最为繁琐、意义不大的日志信息；
    2.Log.d()[debug]：打印一些调试信息(logd+tab)；
    3.Log.i()[info]：打印一些比较重要的数据，可帮助你分析用户行为数据（logi+tab）；
    4.Log.w()[warn]：打印一些警告信息，提示程序该处可能存在的风险(logw+tab)；
    5.Log.e()[error]：打印程序中的错误信息(loge+tab)；

 创建中英文string （中文：zh; 英文：en)
    1. 视角调为Project视图
    2. 找到res资源文件夹
    3. 右键res, New -> Android Resource Directory)
    4. 创建values-zh/values-en文件夹 (type: values)
    5. 右键values-zh/values-en文件夹, New -> Values Resource file
    6. 创建strings.xml文件 (输入：strings)
    7. 点击OK
    8. 返回Android视图查看

////////////////////////////////////////////////////////////////////////////////////////////////////
第三方控件:
 * 1. 定位，地图；
 * 2. pdf 阅读器；
 * 3. 二维码生成及扫描；
 * 4. 和风天气；
 * 5. 视频播放器；
 * 6. 日历；
 * 7. Glide 图片加载；
 * 8. 统计图表