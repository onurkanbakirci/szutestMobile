package com.onurkanbakirci.szutestosgb.ui.fragments

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.onurkanbakirci.szutestosgb.R
import com.onurkanbakirci.szutestosgb.data.db.entities.Answer
import com.onurkanbakirci.szutestosgb.data.db.entities.Question
import com.onurkanbakirci.szutestosgb.data.repository.QuestionRepository
import com.onurkanbakirci.szutestosgb.ui.MainActivity
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class QuestionsViewModel(val mContext:Context,var ids : ArrayList<Int>,var companyName:String) : ViewModel() {

    var mQuestions       = MutableLiveData<List<Question>>()

    companion object{
        var lastCategory     = "bos"
        var mAnswer          = arrayListOf<Answer>()
        var space            = 0
        var rowCount         = 0
        var singlePageRow    = 0
        var lastPosition     = -1
        var trueAnswersforPecentage = 0
        var percentage = 0.0f
        var secondaryIndex = 0
        var rowOfBeforePage = -1
    }

    //for pdf image
    var bitMapLeftIcon = BitmapFactory.decodeResource(mContext.resources,R.drawable.szutest_left_corner)

    //for waiting time
    var progressOfQuestion = ObservableField(0)

    var alpha = ObservableField<Float>(1.0f)
    var radioBtnisClickable = ObservableField<Boolean>(true)

    var accessString = "Bearer "+ mContext.getSharedPreferences("auth", Context.MODE_PRIVATE).getString("token",null)

    fun getQuestions(){
        var editedIDs = "$ids"
        QuestionRepository().getQuestionsByIds(accessString,editedIDs)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<Question>> {
                override fun onSubscribe(d: Disposable) {
                }
                override fun onNext(t: List<Question>) {
                    mQuestions.postValue(t)
                }
                override fun onError(e: Throwable) {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.conn_err), Toast.LENGTH_LONG).show()
                }
                override fun onComplete() {
                }
            })
    }

    fun closeForm(view:View){
        clearCacheDatas()

        mContext.startActivity(Intent(mContext,MainActivity::class.java))
    }


    fun saveAnswer(view:View,question:Question,radioGroup: RadioGroup,editText: EditText,position:Int){

        if(radioGroup.checkedRadioButtonId == -1){
            Toast.makeText(mContext,"Boş alan kalmamalı!",Toast.LENGTH_LONG).show()
        }
        else{
            if(lastPosition+1==position) {
                lastPosition += 1
                var comment:String =editText.text.toString()
                var questionText :String = question.description!!
                var radioBtn = String()

                when(radioGroup.checkedRadioButtonId){
                    R.id.noValid->radioBtn="Kapsam Dışı"
                    R.id.no->radioBtn="Hayır"
                    R.id.yes->radioBtn="Evet"
                }

                var answerObj = Answer(null,question.category_name,questionText,radioBtn,comment)
                mAnswer.add(answerObj)

                //do it not clickable and change background
                alpha.set(0.5f)
                view.isEnabled = false
                editText.isEnabled = false
                radioBtnisClickable.set(false)

                //add for success percentage
            }
            else
            {
                Toast.makeText(mContext,"Soruları sıralı bir şekilde cevaplandırınız!",Toast.LENGTH_LONG).show()
            }
        }
    }

    fun finishForm(view: View) {
        buildPdf()
    }

    fun buildPdf() {

        lastCategory = mQuestions.value?.get(0)?.category_name.toString()

        for(i in mAnswer){
            if(i.radio_state=="Evet")
            {
                trueAnswersforPecentage+=1
            }
            if(lastCategory != i.category_name!!){
                rowCount+=2
            }
            else{
                rowCount+=1
            }
            lastCategory = i.category_name!!
        }

        var count =  rowCount/23

        calculatePercentage()

        lastCategory = "bos"

        var pdfDocument = PdfDocument()
        var painter = Paint()

        for (a in 1..count+1){
            space=0
            var pageInfo = PdfDocument.PageInfo.Builder(842, 595, a).create()
            var page = pdfDocument.startPage(pageInfo)
            var canvas: Canvas = page.canvas

            buildTitle(canvas,painter)

            if(rowCount<=22*a){
                buildLastPageFooter(canvas,painter)
            }else{
                buildFooter(canvas,painter)
            }
            buildSchema(canvas,painter)
            for((index,i)in mAnswer.withIndex()){
                if( index>rowOfBeforePage && index<=22*(a) && singlePageRow<=22*a ){
                    //title
                    if(lastCategory != i.category_name!!)
                    {
                        //kategori başlıkları
                        painter.typeface = Typeface.DEFAULT_BOLD
                        canvas.drawText(i.category_name!!,180.0f,150.0f+16*(secondaryIndex)+space,painter)
                        painter.typeface = Typeface.DEFAULT
                        space+=16
                        singlePageRow+=1
                    }
                    //if content is longer
                    if(i.question?.length!!>150){
                        painter.textSize = 7.0f
                        val firstText = i.question?.substring(0,150)
                        val lastText = i.question?.substring(150,i.question?.length!!)
                        canvas.drawText(firstText!!,32.0f,147.0f+16*(secondaryIndex)+space,painter)
                        canvas.drawText(lastText!!,32.0f,155.0f+16*(secondaryIndex)+space,painter)
                        painter.textSize = 10.0f
                        singlePageRow+=1
                    }
                    //if content is smaller
                    else
                    {
                        painter.textSize = 7.0f
                        canvas.drawText(i.question!!,32.0f,150.0f+16*(secondaryIndex)+space,painter)
                        painter.textSize = 10.0f
                        singlePageRow+=1
                    }
                    when(i.radio_state){
                        "Evet"->{canvas.drawText(i.radio_state!!,canvas.width-300.0f,150.0f+16*(secondaryIndex)+space,painter)}
                        "Hayır"->{canvas.drawText(i.radio_state!!,canvas.width-250.0f,150.0f+16*(secondaryIndex)+space,painter)}
                        "Kapsam Dışı"->{canvas.drawText(i.radio_state!!,canvas.width-180.0f,150.0f+16*(secondaryIndex)+space,painter)}
                    }
                    //if comment is valid add to pdf
                    if(!i.comment_line.isNullOrEmpty())
                    {
                        canvas.drawText(i.comment_line!!,canvas.width-100.0f,150.0f+16*(secondaryIndex)+space,painter)
                    }
                    lastCategory = i.category_name!!
                    secondaryIndex+=1
                    rowOfBeforePage = index
                }
                else
                {
                    secondaryIndex = 0
                }
            }
            pdfDocument.finishPage(page)
        }

        var dbDate = SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault()).format(Date())

        var newDate = dbDate.replace(":","-")
        var lastDate=newDate.replace("/","-")

        var nameOfFile = "$companyName$lastDate.pdf"

        var file = File(mContext.getExternalFilesDir("/"), nameOfFile)

        pdfDocument.writeTo(FileOutputStream(file))

        pdfDocument.close()

        var mBundle = Bundle()
        var mFragment = PreviewFragment()
        mBundle.putString("fileName",nameOfFile )
        mBundle.putString("time",dbDate )
        mBundle.putFloat("percentage", percentage )
        mFragment.arguments = mBundle

        clearCacheDatas()

        (mContext as AppCompatActivity).supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container,mFragment)
            .commit()
    }

    fun buildTitle(canvas:Canvas,painter:Paint){
        var dateObj = Calendar.getInstance().getTime()
        var mDate = SimpleDateFormat("dd/MM/yy")

        //left corner image
        canvas.drawBitmap(bitMapLeftIcon, 20.0f, 30.0f, painter)

        //title
        painter.textSize = 15.0f
        canvas.drawText("Covid-19", canvas.width / 2.0f - 50, 40.0f, painter)
        canvas.drawText("Saha Kontrol Formu", canvas.width / 2.0f - 90, 60.0f, painter)

        canvas.drawLine(canvas.width - 180.0f, 20.0f, canvas.width - 20.0f, 20.0f, painter)
        painter.textSize = 10.0f
        canvas.drawText("Döküman No:", canvas.width - 175.0f, 35.0f, painter)
        canvas.drawLine(canvas.width - 180.0f, 20.0f, canvas.width - 180.0f, 40.0f, painter)
        canvas.drawLine(canvas.width - 180.0f, 40.0f, canvas.width - 20.0f, 40.0f, painter)
        canvas.drawLine(canvas.width - 100.0f, 20.0f, canvas.width - 100.0f, 40.0f, painter)
        canvas.drawLine(canvas.width - 20.0f, 20.0f, canvas.width - 20.0f, 40.0f, painter)

        painter.textSize = 10.0f
        canvas.drawText("Rev. No:", canvas.width - 175.0f, 55.0f, painter)
        canvas.drawLine(canvas.width - 180.0f, 40.0f, canvas.width - 180.0f, 60.0f, painter)
        canvas.drawLine(canvas.width - 180.0f, 60.0f, canvas.width - 20.0f, 60.0f, painter)
        canvas.drawLine(canvas.width - 100.0f, 40.0f, canvas.width - 100.0f, 60.0f, painter)
        canvas.drawLine(canvas.width - 20.0f, 40.0f, canvas.width - 20.0f, 60.0f, painter)

        painter.textSize = 10.0f
        canvas.drawText("Rev. Tarihi:", canvas.width - 175.0f, 75.0f, painter)
        canvas.drawText(mDate.format(dateObj),canvas.width-80.0f,75.0f,painter)
        canvas.drawLine(canvas.width - 180.0f, 60.0f, canvas.width - 180.0f, 80.0f, painter)
        canvas.drawLine(canvas.width - 180.0f, 80.0f, canvas.width - 20.0f, 80.0f, painter)
        canvas.drawLine(canvas.width - 100.0f, 60.0f, canvas.width - 100.0f, 80.0f, painter)
        canvas.drawLine(canvas.width - 20.0f, 60.0f, canvas.width - 20.0f, 80.0f, painter)

        //inf
        painter.textSize = 10.0f
        canvas.drawText("Kontrol Listesi", 200.0f, 135.0f, painter)
        canvas.drawText("Evet", canvas.width - 300.0f, 135.0f, painter)
        canvas.drawText("Hayır", canvas.width - 250.0f, 135.0f, painter)
        canvas.drawText("Kapsam Dışı", canvas.width - 180.0f, 135.0f, painter)
        canvas.drawText("Açıklama", canvas.width - 100.0f, 135.0f, painter)
        canvas.drawLine(30.0f, 120.0f, canvas.width - 20.0f, 120.0f, painter)
        canvas.drawLine(30.0f, 120.0f, 30.0f, 140.0f, painter)
        canvas.drawLine(30.0f, 140.0f, canvas.width - 20.0f, 140.0f, painter)
        canvas.drawLine(canvas.width - 20.0f, 120.0f, canvas.width - 20.0f, 140.0f, painter)


    }
    fun buildFooter(canvas:Canvas,painter:Paint){
        //footer
        painter.textSize = 10.0f
        canvas.drawText(
            "Adres: Esenkent Mah. Ferman Sok. No:2 Ümraniye/İstanbul",
            20.0f,
            canvas.height - 50.0f,
            painter
        )
        canvas.drawText("Email: info@szutestosgb.com.tr", 20.0f, canvas.height - 40.0f, painter)
        canvas.drawText("Web: www.szutestosgb.com.tr", 20.0f, canvas.height - 30.0f, painter)
        canvas.drawText(
            "T: +90 216 644 50 08",
            canvas.width - 150.0f,
            canvas.height - 50.0f,
            painter
        )
        canvas.drawText(
            "F: +90 216 466 45 47",
            canvas.width - 150.0f,
            canvas.height - 40.0f,
            painter
        )
        canvas.drawText(
            "G: +90 543 763 46 80",
            canvas.width - 150.0f,
            canvas.height - 30.0f,
            painter
        )
    }

    fun buildLastPageFooter(canvas: Canvas,painter: Paint){

        var dateObj = Calendar.getInstance().time
        var mDate = SimpleDateFormat("dd/MM/yyyy")

        val username = mContext.applicationContext?.getSharedPreferences(
            "auth",
            Context.MODE_PRIVATE
        )!!.getString("name", null)

        //footer
        painter.textSize = 10.0f
        canvas.drawText("Kontrol Tarihi", 150.0f, canvas.height - 50.0f, painter)
        canvas.drawText("Hazırlayan İş Sağlığı ve Güvenliği Uzmanı", 320.0f, canvas.height - 50.0f, painter)
        canvas.drawText("Onaylayan İş Veren / İş Veren Vekili", 600.0f, canvas.height - 50.0f, painter)

        canvas.drawText(
            mDate.format(dateObj),
            155.0f,
            canvas.height - 35.0f,
            painter
        )
        canvas.drawText(
            username.toString(),
            370.0f,
            canvas.height - 35.0f,
            painter
        )
    }

    fun buildSchema(canvas: Canvas,painter: Paint){
        //leftCorener
        canvas.drawLine(30.0f,140.0f,30.0f,508.0f,painter)
        //bottomLine
        canvas.drawLine(30.0f,508.0f,canvas.width-20.0f,508.0f,painter)
        //rightCorner
        canvas.drawLine(canvas.width-20.0f,140.0f,canvas.width-20.0f,508.0f,painter)

        //bottom line of each item
        for(i in 1..22){
            canvas.drawLine(30.0f,140.0f+16*i,canvas.width-20.0f,140.0f+16*i,painter)
        }
    }

    fun clearCacheDatas(){
        mAnswer.clear()
        lastCategory = "bos"
        space = 0
        rowCount = 0
        singlePageRow = 0
        trueAnswersforPecentage = 0
        percentage = 0.0f
        lastPosition=-1
        secondaryIndex=0
        rowOfBeforePage = -1
    }

    fun calculatePercentage(){
        percentage = (trueAnswersforPecentage*100).toFloat()/ mAnswer.size
    }
}