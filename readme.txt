//virgolib 2022/03/09 书写规范，集大成者
1. 项目常用配置：
    a: gradle库源：该用阿里云;
    b: gradle 软件版本信息;
    c: release 签名包 -> signKey;
    d: 多渠道打包：适配机型、android 不同版本;

2. 项目文件层级：
    a: bean: 自定义对象类（区分数据库的bean）;
    b: base: application、一些父类;
    c: config: 常量（Constants）;
    d: data: 数据库专用;
    e: event: eventbus 注册类;
    f: helper: 静态、非静态工具类;
    g: interfaces: 接口类;
    h: mainbody: 四大组件（activity[fragment]，services，broadcast receiver,content provider）;
    i: widget: 自定义控件;
    

3. 添加本地资源、库：
    a: libs -> .jar, .aar   引用 implementation fileTree(include: ['*.jar', "*.aar"], dir: 'libs'); aar需手动添加，jar可as libary
    b. main 下创建 assets (app -> new -> Folder -> Assets Folder) -> 访问的时候需要AssetManager类;
    c. res 下创建 raw (res -> new -> Android Resource Directory -> Resource Type 选择 raw) -> 访问的时候直接使用资源ID;

    c. main 下创建 jniLibs -> ndk so库集 [arm64-v8a, armeabi, armeabi-v7a...];
    d. android 下显示的jniLibs 目录和 project 下的libs 其实是一个?;

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
    a: 自启广播：android.intent.action.BOOT_COMPLETED
    b: usb 插入拔出广播：该广播先于自启广播，且自启时自带外部存储会会触发
        android.intent.action.MEDIA_MOUNTED
        android.intent.action.MEDIA_UNMOUNTED
    c: 电源、电量变化广播：
        Intent.ACTION_BATTERY_CHANGE
        Intent.ACTION_BATTERY_LOW
        Intent.ACTION_BATTERY_OKAY
        Intent.ACTION_POWER_CONNECTED
        Intent.ACTION_POWER_DISCONNECTED


////////////////////////////////////////////////////////////////////////////////////////////////////
本地依赖库：.jar/.aar/导入模块
1. virgocom: aar 控件模块；（独立打包：Terminal ->$ gradlew makeAar + (ctrl+enter))
2. virgosdk: jar 工具库模块；（独立打包：Terminal ->$ gradlew makeJar + (ctrl+enter))
3. 添加引用：settings.gradle
    include ':virgosdk'
    include ':virgocom'

4. .jar/.aar:
    implementation fileTree(include: ['*.jar', "*.aar"], dir: 'libs')
    implementation files('libs/VirgoSDK_1.1.2.jar')
    implementation files('libs/VirgoComponent_1.1.2.jar')