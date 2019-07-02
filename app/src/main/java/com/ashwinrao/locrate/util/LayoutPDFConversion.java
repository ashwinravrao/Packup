package com.ashwinrao.locrate.util;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.hendrix.pdfmyxml.PdfDocument;
import com.hendrix.pdfmyxml.viewRenderer.AbstractViewRenderer;

import java.io.File;

public class LayoutPDFConversion {

    private String filename;
    private int layout;
    private Context context;

    public LayoutPDFConversion(Context context, int layoutResId, String filename) {
        this.context = context;
        this.layout = layoutResId;
        this.filename = filename;
    }

    public void convert() {
        final AbstractViewRenderer page = renderViewAbstractly(context, layout);
        page.setReuseBitmap(true);
        buildPDF(context, filename, page);
    }

    private AbstractViewRenderer renderViewAbstractly(Context context, int layoutResId) {
        return new AbstractViewRenderer(context, layoutResId) {
            @Override
            protected void initView(View view) { }
        };
    }

    private void buildPDF(Context context, String filename, AbstractViewRenderer page) {
        new PdfDocument.Builder(context).addPage(page).orientation(PdfDocument.A4_MODE.LANDSCAPE)
                .renderWidth(2115).renderHeight(1500)
                .saveDirectory(context.getExternalMediaDirs()[0])
                         .filename(filename)
                .listener(new PdfDocument.Callback() {
                    @Override
                    public void onComplete(File file) {
                        Log.i(PdfDocument.TAG_PDF_MY_XML, "Complete");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.i(PdfDocument.TAG_PDF_MY_XML, "Error");
                    }
                }).create().createPdf(context);
    }
}
