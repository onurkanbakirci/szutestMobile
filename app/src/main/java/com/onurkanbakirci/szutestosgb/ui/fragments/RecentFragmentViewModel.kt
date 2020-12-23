package com.onurkanbakirci.szutestosgb.ui.fragments

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.onurkanbakirci.szutestosgb.R
import com.onurkanbakirci.szutestosgb.data.db.entities.Files
import java.io.File
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.onurkanbakirci.szutestosgb.BuildConfig
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import android.graphics.drawable.ColorDrawable
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.onurkanbakirci.szutestosgb.data.db.AppDatabase
import com.github.barteksc.pdfviewer.PDFView
import io.reactivex.CompletableObserver
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class RecentFragmentViewModel ( var mContext:Context) : ViewModel() {

    var mFile = MutableLiveData<List<Files>>()
    var notFoundVisibility = ObservableField(8)

    init {
        getFiles()
    }

    fun getFiles(){
        val uID = mContext.applicationContext?.getSharedPreferences(
            "auth",
            Context.MODE_PRIVATE
        )!!.getInt("_uID", 0)
        AppDatabase.getAppDataBase(mContext)
            ?.fileDao()
            ?.GetAllFile(uID)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : Observer<List<Files>> {
                override fun onComplete() {
                }
                override fun onSubscribe(d: Disposable) {
                }
                override fun onNext(t: List<Files>) {
                    mFile.postValue(t)
                }
                override fun onError(e: Throwable) {
                }
            })
    }
    fun newForm(view: View){
        (mContext as AppCompatActivity).supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container,CompanyFragment())
            .commit()
    }

    fun openPDF(file:String){
        val dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        assert(dialog.getWindow() != null)
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.alert_preview)
        val pdfView = dialog.findViewById<PDFView>(R.id.pdfViewAlert)
        pdfView.fromFile(File(mContext.getExternalFilesDir("/"), file)).defaultPage(0)
            .enableAnnotationRendering(true)
            .scrollHandle(DefaultScrollHandle(mContext))
            .load()
        dialog.show()
    }

    fun sharePDF(file:String){
        val outputFile = File(mContext.getExternalFilesDir("/"), file)
        var pdfUri : Uri ? =null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                pdfUri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID+".provider", outputFile)
        } else {
            pdfUri = Uri.fromFile(outputFile)
        }
        var share = Intent()
        share.setAction(Intent.ACTION_SEND)
        share.setType("application/pdf")
        share.putExtra(Intent.EXTRA_STREAM, pdfUri)
        mContext.startActivity(Intent.createChooser(share, "Dosyayı Paylaş"))
    }
    fun deletePDF(file:String){
        val alert = AlertDialog.Builder(mContext)
        alert.setTitle("Silme")
        alert.setMessage("Bu dosyayı silmek istediğinizden emin misiniz?")
        alert.setCancelable(false)
        alert.setIcon(R.drawable.ic_pdf)
        alert.setPositiveButton("Evet") { dialogInterface: DialogInterface, i: Int ->
            AppDatabase.getAppDataBase(mContext)
                ?.fileDao()
                ?.DeleteFileByName(file)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object : CompletableObserver {
                    override fun onComplete() {
                        val outputFile = File(mContext.getExternalFilesDir("/"), file)
                        outputFile.delete()
                        Toast.makeText(mContext,"Dosya Silindi",Toast.LENGTH_LONG).show()
                    }
                    override fun onSubscribe(d: Disposable) {
                    }
                    override fun onError(e: Throwable) {
                    }
                })
        }
        alert.setNegativeButton("Hayır") {dialogInterface: DialogInterface, i: Int -> }
        alert.show()
    }
}