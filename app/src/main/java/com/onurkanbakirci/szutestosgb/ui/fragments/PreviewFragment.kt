package com.onurkanbakirci.szutestosgb.ui.fragments

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.onurkanbakirci.szutestosgb.R
import com.onurkanbakirci.szutestosgb.data.db.AppDatabase
import com.onurkanbakirci.szutestosgb.data.db.entities.Files
import com.onurkanbakirci.szutestosgb.data.db.entities.Upload
import com.onurkanbakirci.szutestosgb.data.network.IAPI
import com.onurkanbakirci.szutestosgb.ui.MainActivity
import com.github.barteksc.pdfviewer.PDFView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class PreviewFragment :Fragment(){

    private var mFile : File? =null
    private var mPdfFile : PDFView ? =null
    private var mFinishFormBtn : FloatingActionButton?=null
    private var cancelForm : FloatingActionButton?=null
    private var mContext = this.context
    var args :String? = null
    var time :String? = null
    var percent:Float?=null
    var accessString:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        args = arguments!!.getString("fileName")
        time = arguments!!.getString("time")
        percent = arguments!!.getFloat("percentage")
        accessString = "Bearer "+ activity?.applicationContext?.getSharedPreferences("auth", Context.MODE_PRIVATE)!!.getString("token",null)
        activity?.title = "Önizleme"
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.preview_fragment, container, false)
        mPdfFile = view?.findViewById(R.id.pdfView)
        mFinishFormBtn = view?.findViewById(R.id.finishFormBtn)
        cancelForm = view?.findViewById(R.id.cancelForm)

        cancelForm?.setOnClickListener(View.OnClickListener {
            requireContext().startActivity(Intent(requireContext(),MainActivity::class.java))
        })

        mFinishFormBtn?.setOnClickListener(View.OnClickListener {

            var dialog: ProgressDialog = ProgressDialog.show(requireContext(),"İşlem Yapılıyor","Lütfen bekleyiniz...",true)
            dialog.show()

            var requestBody = RequestBody.create(MediaType.parse("ssss"),mFile!!)
            var uploadedFile : MultipartBody.Part =MultipartBody.Part.createFormData("file",mFile!!.name,requestBody)

            //var res = FileRepository().uploadFile(accessString!!,uploadedFile)

            IAPI().saveFile(accessString!!,uploadedFile,percent!!).enqueue(object : Callback<Upload> {

                override fun onFailure(call: Call<Upload>, t: Throwable) {
                }
                override fun onResponse(
                    call: Call<Upload>,
                    response: Response<Upload>
                ) {
                    val userID = requireContext().applicationContext?.getSharedPreferences(
                        "auth",
                        Context.MODE_PRIVATE
                    )!!.getInt("_uID", 0)
                    if(response.isSuccessful){
                        if(response.body()?.status == "success"){
                        AppDatabase.getAppDataBase(requireContext())
                            ?.fileDao()
                            ?.InsertFile(Files(  null
                                ,args
                                ,time
                                ,userID
                                ,accessString
                                ,percent))
                            ?.subscribeOn(Schedulers.io())
                            ?.observeOn(AndroidSchedulers.mainThread())
                            ?.subscribe(object : CompletableObserver {
                                override fun onComplete() {
                                    dialog.dismiss()
                                    val alert = AlertDialog.Builder(requireContext())
                                    alert.setTitle("Kaydedildi")
                                    alert.setMessage("Dosya kaydedildi.")
                                    alert.setCancelable(false)
                                    alert.setIcon(R.drawable.ic_check)
                                    alert.setPositiveButton("Tamam") { dialogInterface: DialogInterface, i: Int ->
                                        AppDatabase.getAppDataBase(requireContext())
                                            ?.answerDao()
                                            ?.DeleteAllAnswer()
                                            ?.subscribeOn(Schedulers.io())
                                            ?.observeOn(AndroidSchedulers.mainThread())
                                            ?.subscribe(object : CompletableObserver {
                                                override fun onComplete() {
                                                    requireContext().startActivity(Intent(requireContext(),MainActivity::class.java))
                                                }
                                                override fun onSubscribe(d: Disposable) {
                                                }
                                                override fun onError(e: Throwable) {
                                                }
                                            })
                                    }
                                    alert.show()
                                }
                                override fun onSubscribe(d: Disposable) {
                                }
                                override fun onError(e: Throwable) {
                                }
                            })
                        }
                        else
                        {
                            dialog.dismiss()
                            Toast.makeText(mContext,resources.getString(R.string.conn_err),Toast.LENGTH_LONG).show()
                        }
                    }
                    else
                    {
                        dialog.dismiss()
                        Toast.makeText(mContext,resources.getString(R.string.conn_err),Toast.LENGTH_LONG).show()
                    }
                }
           })

        })

        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFile = File(requireContext().getExternalFilesDir("/"),args!!)
        mPdfFile?.fromFile(mFile)?.load()
    }
}