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
    a: libs -> .jar, .aar   引用 implementation fileTree(include: ['*.jar', "*.aar"], dir: 'libs');
    b. main 下创建 assets (app -> new -> Folder -> Assets Folder) -> 访问的时候需要AssetManager类;
    c. res 下创建 raw (res -> new -> Android Resource Directory -> Resource Type 选择 raw) -> 访问的时候直接使用资源ID;

    c. main 下创建 jniLibs -> ndk so库集 [arm64-v8a, armeabi, armeabi-v7a...];
    d. android 下显示的jniLibs 目录和 project 下的libs 其实是一个?;

4. 同步git:（安装git）
    a; ternimal -> git init -> 创建本地git库 .git;
    b: commit;