package com.nepalese.virgolib.mainbody.activity.thirdlib;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.nepalese.virgolib.R;
import com.nepalese.virgosdk.Base.BaseActivity;

import java.io.File;

/**
 * pdf 阅读器
 * implementation 'com.github.barteksc:android-pdf-viewer:2.8.0'
 */
public class PDFViewActivity extends BaseActivity implements OnDrawListener, OnPageChangeListener, OnErrorListener, OnLoadCompleteListener {
    private static final String TAG = "PDFViewActivity";

    private Context context;
    private PDFView pdfView;
    private TextView curPage, allPage;
    private EditText inputPage;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_d_f_view);
        init();
    }

    @Override
    protected void initUI() {
        pdfView = findViewById(R.id.pdf_view);
        curPage = findViewById(R.id.tv_cur_page);
        allPage = findViewById(R.id.tv_all_page);
        inputPage = findViewById(R.id.input_page);
    }

    @Override
    protected void initData() {
        context = getApplicationContext();
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void release() {

    }

    @Override
    protected void onBack() {
        finish();
    }

    public void onImportFile(View view) {

    }

    public void onJumpPage(View view) {
        String page = inputPage.getText().toString().trim();
        if (TextUtils.isEmpty(page)) {
            return;
        }
        int aimPage = Integer.parseInt(page) - 1;
        if (aimPage < pdfView.getPageCount()) {
            pdfView.jumpTo(aimPage);
        } else {
            showToast("跳转页超出总页数！");
        }
    }

    private void loadPdf() {
        try {
            //打开pef
            pdfView.fromFile(new File(path))   //设置pdf文件地址 fromAsset(assetFileName)
                    .defaultPage(0)         //设置默认显示第1页
                    .onPageChange(this)     //设置翻页监听
                    .onDraw(this)            //绘图监听
                    .onError(this)
                    .onLoad(this)
                    .enableAnnotationRendering(true)
                    .swipeHorizontal(false) // pdf文档翻页是否是水平翻页，默认是左右滑动翻页
                    .enableSwipe(true)   //是否允许翻页，默认是允许翻
                    .load();
        } catch (Throwable e) {
            showToast("打开文件失败1!");
        }
    }

    @Override
    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

    }

    @Override
    public void onError(Throwable t) {
        showToast("打开文件失败2!");
    }

    @Override
    public void loadComplete(int nbPages) {
        allPage.setText(String.format(getString(R.string.all_page), nbPages));
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        curPage.setText(String.format(getString(R.string.cur_page), page + 1));
    }

    public void onLastPage(View view) {
        //上一页
        int curpage = pdfView.getCurrentPage();
        if (curpage > 0) {
            curpage--;
            pdfView.jumpTo(curpage);
        }
    }

    public void onNextPage(View view) {
        //下一页
        int curpage = pdfView.getCurrentPage();//index
        if (curpage < pdfView.getPageCount() - 1) {
            curpage++;
            pdfView.jumpTo(curpage);
        }
    }
}