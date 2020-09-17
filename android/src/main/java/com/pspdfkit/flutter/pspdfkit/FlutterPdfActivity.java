package com.pspdfkit.flutter.pspdfkit;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.pspdfkit.document.PdfDocument;
import com.pspdfkit.ui.PdfActivity;

import java.util.concurrent.atomic.AtomicReference;

import io.flutter.plugin.common.MethodChannel.Result;

/**
 * For communication with the PSPDFKit plugin, we keep a static reference to the current
 * activity.
 */
public class FlutterPdfActivity extends PdfActivity {

    private static FlutterPdfActivity currentActivity;
    private static AtomicReference<Result> loadedDocumentResult = new AtomicReference<>();

    public static void setLoadedDocumentResult(Result result) {
        loadedDocumentResult.set(result);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        bindActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseActivity();
    }

    @Override
    public void onDocumentLoaded(@NonNull PdfDocument pdfDocument) {
        super.onDocumentLoaded(pdfDocument);
        Result result = loadedDocumentResult.getAndSet(null);
        if (result != null) {
            result.success(true);
        }
    }

    @Override
    public void onDocumentLoadFailed(@NonNull Throwable throwable) {
        super.onDocumentLoadFailed(throwable);
        Result result = loadedDocumentResult.getAndSet(null);
        if (result != null) {
            result.success(false);
        }
    }

    private void bindActivity() {
        currentActivity = this;
    }

    private void releaseActivity() {
        Result result = loadedDocumentResult.getAndSet(null);
        if (result != null) {
            result.success(false);
        }
        currentActivity = null;
    }

    public static FlutterPdfActivity getCurrentActivity() {
        return currentActivity;
    }

    // public class CustomActivity extends PdfActivity {
    //     @Override
    //     public List<Integer> onGenerateMenuItemIds(@NonNull List<Integer> menuItems) {
    //         // Take the default menu item IDs and remove the outlined items.
    //         menuItems.remove(PdfActivity.disableAnnotationEditing());
    
    //         // Return the new order for the menu items.
    //         return menuItems;
    //     }
    // }
}
