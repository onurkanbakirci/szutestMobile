package com.onurkanbakirci.szutestosgb.ui.fragments

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.text.Editable
import android.view.View
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.onurkanbakirci.szutestosgb.R
import com.onurkanbakirci.szutestosgb.data.db.AppDatabase
import com.onurkanbakirci.szutestosgb.data.db.entities.PCR
import com.onurkanbakirci.szutestosgb.data.db.entities.Upload
import com.onurkanbakirci.szutestosgb.data.network.IAPI
import com.onurkanbakirci.szutestosgb.ui.MainActivity
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class PCRFormFragmentViewModel(val mContext:Context) :ViewModel() {

    companion object{
        var indexOfText = 0
    }

    fun savePCR(
        view: View,
        name: EditText,
        surname: EditText,
        firmName : EditText,
        identity_number: EditText,
        bornDate: EditText,
        address: EditText,
        phoneNumber: EditText,
        anticor: RadioGroup,
        foreign: EditText,
        nationality: EditText,
        flightInf: EditText
    ) {

        val mName = name.text.toString()
        val mSurname = surname.text.toString()
        val mFirmName = firmName.text.toString()
        val mIdentityNumber = identity_number.text.toString()
        val mBornDate = bornDate.text.toString()
        val mAddress = address.text.toString()
        val mPhoneNumber = phoneNumber.text.toString()
        var mAnticor = ""

        when(anticor.checkedRadioButtonId){
            R.id.pcr->{mAnticor="PCR" }
            R.id.anti->{mAnticor="Antikor"}
            R.id.pcr_and_anticor->{mAnticor="PCR ve Antikor"}
        }


        val mForeign = foreign.text.toString()
        val mNationality = nationality.text.toString()
        val mFlightInf = flightInf.text.toString()
        
        val accessString = "Bearer " + mContext.applicationContext?.getSharedPreferences(
            "auth",
            Context.MODE_PRIVATE
        )!!.getString("token", null)

        val uID = mContext.applicationContext?.getSharedPreferences(
            "auth",
            Context.MODE_PRIVATE
        )!!.getInt("_uID", 0)

        val username = mContext.applicationContext?.getSharedPreferences(
            "auth",
            Context.MODE_PRIVATE
        )!!.getString("name", null)

        if(mName.isEmpty() || mSurname.isEmpty() || mFirmName.isEmpty() ||
             mIdentityNumber.isEmpty()
            || mBornDate.isEmpty() || mAddress.isEmpty() || mPhoneNumber.isEmpty()
        ){
            Toast.makeText(mContext,mContext.resources.getString(R.string.checkCredentials),Toast.LENGTH_LONG).show()
        }
        else if(mAnticor==""){
            Toast.makeText(mContext,mContext.resources.getString(R.string.pcr_err),Toast.LENGTH_LONG).show()
        }
        else if(!(mIdentityNumber.length==11 || mIdentityNumber.length==9)){
            Toast.makeText(mContext,mContext.resources.getString(R.string.identity_number_err),Toast.LENGTH_LONG).show()
        }
        else if(mPhoneNumber.length!=11){
            Toast.makeText(mContext,mContext.resources.getString(R.string.phone_number_err),Toast.LENGTH_LONG).show()
        }
        else
        {
            var dialog: ProgressDialog =
                ProgressDialog.show(mContext, "İşlem Yapılıyor", "Lütfen bekleyiniz...", true)
            dialog.show()

            val date = SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault()).format(
                Date()
            )
            var newDate = date.replace(":","-")
            var lastDate=newDate.replace("/","-")

            IAPI().savePCR(accessString,uID,mName,mSurname,mFirmName,mIdentityNumber,mBornDate,mAddress,mPhoneNumber,mAnticor,mForeign,mNationality,mFlightInf)
                .enqueue(object : Callback<Upload> {

                    override fun onFailure(call: Call<Upload>, t: Throwable) {
                    }
                    override fun onResponse(
                        call: Call<Upload>,
                        response: Response<Upload>
                    ) {
                        if(response.isSuccessful){
                            if(response.body()?.status == "success"){
                                var title = "$mName $mSurname"
                                AppDatabase.getAppDataBase(mContext)
                                    ?.pcrDao()
                                    ?.InsertPCR(PCR(null
                                        ,title
                                        ,lastDate
                                        ,uID
                                        ,username
                                    ))
                                    ?.subscribeOn(Schedulers.io())
                                    ?.observeOn(AndroidSchedulers.mainThread())
                                    ?.subscribe(object : CompletableObserver {
                                        override fun onComplete() {
                                            dialog.dismiss()
                                            val alert = AlertDialog.Builder(mContext)
                                            alert.setTitle("Kaydedildi")
                                            alert.setMessage("Form kaydedildi.")
                                            alert.setCancelable(false)
                                            alert.setIcon(R.drawable.ic_check)
                                            alert.setPositiveButton("Tamam") { dialogInterface: DialogInterface, i: Int ->
                                                mContext.startActivity(Intent(mContext,MainActivity::class.java))
                                            }
                                            alert.show()
                                            indexOfText=0
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
                                Toast.makeText(mContext,mContext.resources.getString(R.string.conn_err),Toast.LENGTH_LONG).show()
                            }
                        }
                        else
                        {
                            dialog.dismiss()
                            Toast.makeText(mContext,mContext.resources.getString(R.string.conn_err),Toast.LENGTH_LONG).show()
                        }
                    }
                })
        }
    }
     fun textChanges(text : Editable ) {
         if( indexOfText<text.length && (text.length == 2 || text.length == 5))
         {
             text.append("/")
         }
         indexOfText=text.length
     }
}