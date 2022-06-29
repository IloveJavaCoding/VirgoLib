package com.nepalese.virgocomponent.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nepalese.virgocomponent.R;
import com.nepalese.virgocomponent.adapter.ListView_FileSelector_Adapter;
import com.nepalese.virgocomponent.common.CommonUtil;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author nepalese on 2020/10/30 15:20
 * @usage 弹框式文件选择器，可筛选文件类型或仅文件夹，返回选中的文件或文件夹
 * 2022/06/28: 修改 -> 文件夹模式文件可见，不可选
 */
public class VirgoFileSelectorDialog extends Dialog implements ListView_FileSelector_Adapter.FileInterListener {
    private static final String TAG = "FileSelectorDialog";

    //选择文件或文件夹
    public static final int FLAG_DIR = 0;
    public static final int FLAG_FILE = 1;

    //仅显示某种特定文件
    public static final String TYPE_ALL = "all";
    public static final String TYPE_IMAGE = "image";
    public static final String TYPE_TEXT = "txt";
    public static final String TYPE_VIDEO = "video";
    public static final String TYPE_AUDIO = "audio";
    public static final String TYPE_DOC = "docs";
    public static final String TYPE_ONLY_ONE = "only";//仅筛选一种文件类型

    //选择特定文件类型后将显示的文件拓展名
    public static final String[] IMAGE_EXTENSION = {"jpg", "jpeg", "png", "svg", "bmp", "tiff"};
    public static final String[] AUDIO_EXTENSION = {"mp3", "wav", "wma", "aac", "flac"};
    public static final String[] VIDEO_EXTENSION = {"mp4", "flv", "avi", "wmv", "mpeg", "mov", "rm", "swf"};
    public static final String[] TEXT_EXTENSION = {"txt", "java", "html", "xml", "php"};
    public static final String[] DOC_EXTENSION = {"doc", "pdf", "xls", "xlsx"};

    private static final String DEFAULT_ROOT_PATH = "/storage/emulated/0";//默认初始位置

    private final Context context;
    private SelectFileCallback callback;

    private TextView tvCurPath, tvConfirm;
    private LinearLayout layoutRoot, layoutLast;
    private ListView listView;
    private ListView_FileSelector_Adapter adapter;

    private List<File> outFiles;//返回值
    private List<Integer> choseIndexs;//选中文件、夹索引
    private String curPath;//当前路径
    private String rootPath;//根目录
    private String fileType;//文件类型
    private String uniqueSuffix;//仅筛选一种文件类型时可用

    private int flag;//选择类型
    private int dialogWidth;//弹框宽度
    private int dialogHeight;//弹框高度
    private float dialogAlpha;//弹框透明度
    private boolean needReLayout;//需变更布局？
    private boolean needReData;//需变更起始数据？

    public VirgoFileSelectorDialog(Context context) {
        //默认自定义弹框样式，使用下面的构造函数可另设样式
        this(context, R.style.VirgoFile_Dialog);
    }

    public VirgoFileSelectorDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        setCancelable(true);

        init();
    }

    private void init() {
        initUI();
        initData();
        setListener();
    }

    private void initUI() {
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        View view = mLayoutInflater.inflate(R.layout.layout_file_selector, null);

        tvCurPath = view.findViewById(R.id.tvCurPath);
        tvConfirm = view.findViewById(R.id.tvConfirmChoose);

        layoutRoot = view.findViewById(R.id.layoutToRoot);
        layoutLast = view.findViewById(R.id.layoutToLast);

        listView = view.findViewById(R.id.listViewFile);
        setContentView(view);
    }

    private void initData() {
        choseIndexs = new ArrayList<>();
        outFiles = new ArrayList<>();

        //设置默认值
        flag = FLAG_FILE;//默认选择文件
        rootPath = DEFAULT_ROOT_PATH;
        fileType = TYPE_ALL;
        dialogWidth = 500;//默认弹框宽度
        dialogHeight = 500;//默认弹框高度
        dialogAlpha = 0.85f;//默认弹框透明度
        needReLayout = true;
        needReData = true;
    }

    //==============================================================================================
    private void setData() {
        setLayout();
        initFiles();
    }

    //设置弹框布局
    private void setLayout() {
        if (!needReLayout) {
            return;
        }
        needReLayout = false;

        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);

        /*
         * lp.x与lp.y表示相对于原始位置的偏移.
         * 当参数值包含Gravity.LEFT时,对话框出现在左边,所以lp.x就表示相对左边的偏移,负值忽略.
         * 当参数值包含Gravity.RIGHT时,对话框出现在右边,所以lp.x就表示相对右边的偏移,负值忽略.
         * 当参数值包含Gravity.TOP时,对话框出现在上边,所以lp.y就表示相对上边的偏移,负值忽略.
         * 当参数值包含Gravity.BOTTOM时,对话框出现在下边,所以lp.y就表示相对下边的偏移,负值忽略.
         * 当参数值包含Gravity.CENTER_HORIZONTAL时
         * ,对话框水平居中,所以lp.x就表示在水平居中的位置移动lp.x像素,正值向右移动,负值向左移动.
         * 当参数值包含Gravity.CENTER_VERTICAL时
         * ,对话框垂直居中,所以lp.y就表示在垂直居中的位置移动lp.y像素,正值向右移动,负值向左移动.
         * gravity的默认值为Gravity.CENTER,即Gravity.CENTER_HORIZONTAL |
         * Gravity.CENTER_VERTICAL.
         */

        lp.width = dialogWidth; // 宽度
        lp.height = dialogHeight; // 高度
        lp.alpha = dialogAlpha; // 透明度
        Log.d(TAG, "setLayout: " + lp);

        dialogWindow.setAttributes(lp);
    }

    private void initFiles() {
        if (!needReData) {
            return;
        }
        needReData = false;

        curPath = rootPath;
        tvCurPath.setText(curPath);

        List<File> temp = getFiles(curPath);
        if (temp == null) {
            CommonUtil.showToast(context, "空文件夹！");
            return;
        }

        outFiles.addAll(temp);
        adapter = new ListView_FileSelector_Adapter(context, outFiles, this, flag);//指向的是最开始的list
//        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);
    }

    //进入新的路径或返回上一层，刷新数据
    private void resetData(String path) {
        //若为空文件夹，则不进入
        File file = new File(path);
        File[] listFiles = file.listFiles();
        if (listFiles == null || listFiles.length < 1) {
            CommonUtil.showToast(context, "空文件夹！");
            return;
        }

        curPath = path;
        tvCurPath.setText(curPath);

        outFiles.clear();
        choseIndexs.clear();//重置选中

        List<File> temp = getFiles(curPath);
        if (temp == null || temp.isEmpty()) {
            return;
        }

        outFiles.addAll(temp);
        adapter.notifyDataSetChanged();
    }

    //根据条件筛选显示文件
    private List<File> getFiles(String path) {
        if (flag == FLAG_DIR) {
            //文件夹修改为全显示
//            return getOnlyDir(path);
            return getAllFiles(path);
        } else if (flag == FLAG_FILE) {//显示所有文件夹及选择的类型的文件
            switch (fileType) {
                case TYPE_ALL:
                    return getAllFiles(path);
                case TYPE_AUDIO:
                    return getCertainFile(path, AUDIO_EXTENSION);
                case TYPE_IMAGE:
                    return getCertainFile(path, IMAGE_EXTENSION);
                case TYPE_TEXT:
                    return getCertainFile(path, TEXT_EXTENSION);
                case TYPE_VIDEO:
                    return getCertainFile(path, VIDEO_EXTENSION);
                case TYPE_DOC:
                    return getCertainFile(path, DOC_EXTENSION);
                case TYPE_ONLY_ONE:
                    if (TextUtils.isEmpty(uniqueSuffix)) {
                        CommonUtil.showToast(context, "未设置筛选后缀名！");
                        return null;
                    }
                    return getOnlyFile(path, uniqueSuffix);
            }
        }
        return null;
    }

    private List<File> getOnlyFile(String path, String uniqueSuffix) {
        File[] files = new File(path).listFiles();
        if (files == null) {
            return null;
        }

        List<File> list = new ArrayList<>();
        for (File file : files) {
            if (file.isDirectory()) {
                list.add(file);
                continue;
            }
            if (file.getName().endsWith(uniqueSuffix)) {
                list.add(file);
            }
        }

        //排序
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            list.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
        }

        return list;
    }

    //仅返回某路径下文件夹
    private List<File> getOnlyDir(String path) {
        FileFilter filter = File::isDirectory;//File::isDirectory
        return Arrays.asList(Objects.requireNonNull(new File(path).listFiles(filter)));
    }

    //返回某路径下所有文件|夹
    private List<File> getAllFiles(String path) {
        File[] files = new File(path).listFiles();
        if (files == null) {
            return null;
        }
        Arrays.sort(files, (o1, o2) -> o1.getName().compareTo(o2.getName()));
        return Arrays.asList(files);
    }

    //仅返回某路径下指定文件|夹
    private List<File> getCertainFile(String path, String[] extension) {
        File[] files = new File(path).listFiles();
        if (files == null) {
            return null;
        }

        List<File> list = new ArrayList<>();
        //排序
//        Arrays.sort(files, (o1, o2) -> o1.getName().compareTo(o2.getName()));
        List<String> extList = Arrays.asList(extension);
        for (File file : files) {
            if (file.isDirectory()) {
                list.add(file);
                continue;
            }
            if (extList.contains(getExtensionName(file.getName()))) {
                list.add(file);
            }
        }

        //排序
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            list.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
        }

        return list;
    }

    private String getExtensionName(String filename) {
        if (TextUtils.isEmpty(filename)) {
            return "";
        }
        int dot = filename.lastIndexOf('.');
        if ((dot > -1) && (dot < (filename.length() - 1))) {
            return filename.substring(dot + 1).toLowerCase();
        }
        return "";
    }

    //清空数据
    private void release() {
        outFiles.clear();
        choseIndexs.clear();
        adapter = null;
        callback = null;
    }

    //===============================================外部调用api=====================================
    @Override
    public void show() {
        super.show();
        setData();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    public void destory() {
        dismiss();
        release();
    }

    /**
     * 设置根目录
     * @param rootPath
     */
    public void setRootPath(String rootPath) {
        if (rootPath.equals(this.rootPath)) {
            return;
        }
        File file = new File(rootPath);
        if (file.exists() && file.isDirectory()) {
            this.rootPath = rootPath;
            needReData = true;
        }
    }

    /**
     * 设置选择文件或文件夹
     * @param flag FLAG_DIR | FLAG_FILE
     */
    public void setFlag(int flag) {
        if (flag == this.flag) {
            return;
        }
        this.flag = flag;
        needReData = true;
    }

    /**
     * 设置要筛选文件类型
     * FLAG_FILE 下生效
     * @param fileType TYPE_
     */
    public void setFileType(String fileType) {
        if (fileType.equals(this.fileType)) {
            return;
        }

        this.fileType = fileType;
        needReData = true;
    }

    /**
     * 设置要唯一筛选文件后缀
     * FLAG_FILE，TYPE_ONLY_ONE 下生效
     * @param uniqueSuffix 文件后缀名
     */
    public void setUniqueSuffix(String uniqueSuffix) {
        this.uniqueSuffix = uniqueSuffix;
        needReData = true;
    }

    public void setCallback(SelectFileCallback callback) {
        this.callback = callback;
    }

    public void setDialogWidth(int dialogWidth) {
        this.dialogWidth = dialogWidth;
        needReLayout = true;
    }

    public void setDialogHeight(int dialogHeight) {
        this.dialogHeight = dialogHeight;
        needReLayout = true;
    }

    public void setDialogAlpha(float dialogAlpha) {
        this.dialogAlpha = dialogAlpha;
        needReLayout = true;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.cancel();
        }
        return super.onKeyDown(keyCode, event);
    }

    //添加或移除选择对象
    @Override
    public void itemClick(View v, boolean isChecked) {
        Integer position = (Integer) v.getTag();
        if (v.getId() == R.id.cbChoose) {
            if (isChecked) {
                choseIndexs.add(position);
                Log.i(TAG, "add: " + position);
            } else {
                choseIndexs.remove(position);
                Log.i(TAG, "remove: " + position);
            }
        }
    }

    private void setListener() {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Log.d("click", String.valueOf(position + 1));
            //judge file/dir
            if (outFiles.get(position).isFile()) {
                //do nothing
                //可增加本地打开查看
            } else {
                //点击，进入文件夹
                resetData(outFiles.get(position).getPath());
            }
        });

        tvConfirm.setOnClickListener(v -> {
            if (choseIndexs.size() < 1) {
                //未选择, 退出
                dismiss();
                return;
            }

            List<File> result = new ArrayList<>();
            switch (flag) {
                case FLAG_DIR://return dirs
                    for (int i = 0; i < choseIndexs.size(); i++) {
                        //双重保险
                        if (outFiles.get(choseIndexs.get(i)).isDirectory()) {
                            result.add(outFiles.get(choseIndexs.get(i)));
                        }
                    }
                    break;
                case FLAG_FILE://return files
                    for (int i = 0; i < choseIndexs.size(); i++) {
                        if (outFiles.get(choseIndexs.get(i)).isFile()) {
                            result.add(outFiles.get(choseIndexs.get(i)));
                        }
                    }
                    break;
            }
            //通过回调函数返回结果
            if (callback != null) {
                callback.onResult(result);
            }
            //退出
            dismiss();
        });

        //返回上一级
        layoutLast.setOnClickListener(v -> {
            //judge curPath is root or not
            if (curPath.equals(rootPath)) {
                //do nothing
                CommonUtil.showToast(context, "已是根目录");
            } else {
                //back to last layer
                resetData(curPath.substring(0, curPath.lastIndexOf("/")));
            }
        });

        //返回根目录
        layoutRoot.setOnClickListener(v -> {
            if (curPath.equals(rootPath)) {
                //do nothing
                CommonUtil.showToast(context, "已是根目录");
            } else {
                //back to root
                resetData(rootPath);
            }
        });
    }

    //自定义回调接口：
    public interface SelectFileCallback {
        void onResult(List<File> list);
    }
}
