package com.esjayit.apnabazar.main.dashboard.view.user_ledger

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.widget.Toast
import com.esjayit.apnabazar.AppConstants
import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.data.model.response.LedgerData
import com.esjayit.apnabazar.data.model.response.PartyLedgerResponse
import com.esjayit.apnabazar.helper.custom.CustomProgress
import com.esjayit.apnabazar.helper.util.logE
import com.esjayit.apnabazar.main.base.BaseFrag
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.view.user_ledger.model.UserLedgerVM
import com.esjayit.databinding.FragmentUserLedgerBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class UserLedgerFrag :
    BaseFrag<FragmentUserLedgerBinding, UserLedgerVM>(Layouts.fragment_user_ledger) {

    override val hasProgress: Boolean = false
    override val vm: UserLedgerVM by viewModel()
    val progressDialog: CustomProgress by lazy { CustomProgress(requireActivity()) }
    var data: LedgerData? = null
    var pageWidth = 1200

    override fun init() {
        vm?.getPartyLedgerData(userId = prefs.user.userId, installedId = prefs.installId!!)

    }

    override fun renderState(apiRenderState: ApiRenderState) {
        when (apiRenderState) {
            is ApiRenderState.ApiSuccess<*> -> {
                when (apiRenderState.result) {
                    is PartyLedgerResponse -> {
                        progressDialog?.hideProgress()
                        val statusCode = apiRenderState.result.statuscode
                        if (statusCode == AppConstants.Status_Code.Success) {
                            "USER LEDGER API ${apiRenderState.result.data}".logE()
                            successToast(apiRenderState.result.data.toString())
                            data = apiRenderState.result.data
                            if (Build.VERSION.SDK_INT > 22) {
                                requestPermissions(
                                    arrayOf(
                                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                                    ), 1
                                )
                            } else {
                                createPDF(apiRenderState.result.data)
                            }
                        } else {
                            errorToast(apiRenderState.result.message.toString())
                        }
                    }
                }
            }
            ApiRenderState.Idle -> {
                hideProgress()
            }
            ApiRenderState.Loading -> {
                showProgress()
            }
            is ApiRenderState.ValidationError -> {
                "Error API CALLING".logE()
            }
            is ApiRenderState.ApiError<*> -> {
                "Error API CALLING API ERROR".logE()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            1 -> {
                if (!(grantResults.size > 0 && grantResults[0] === PackageManager.PERMISSION_GRANTED && grantResults[1] === PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(
                        requireContext(),
                        "Permission denied",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    createPDF(data)
                }
            }
        }
    }

    private fun createPDF(data: LedgerData?) {
        val myPdfDocument = PdfDocument()
        val myPaint = Paint()
        val titlePaint = Paint()

        val myPageInfo1 = PageInfo.Builder(1200, 2010, 1).create()
        val myPage1 = myPdfDocument.startPage(myPageInfo1)
        val canvas: Canvas = myPage1.canvas

//        myPaint.setColor(Color.rgb(139, 0, 139))

//        myPaint.setColor(Color.rgb(139, 0, 139))
        myPaint.setColor(Color.BLACK)
        canvas.drawLine(100F, 100F, (pageWidth - 100).toFloat(), 100F, myPaint)
        canvas.drawLine(100F, 180F, (pageWidth - 100).toFloat(), 180F, myPaint)
        canvas.drawLine(100F, 260F, (pageWidth - 100).toFloat(), 260F, myPaint)
        canvas.drawLine(100F, 340F, (pageWidth - 100).toFloat(), 340F, myPaint)
        canvas.drawLine(100F, 420F, (pageWidth - 100).toFloat(), 420F, myPaint)

        canvas.drawLine(100F, 100F, 100F, 180F, myPaint)
        canvas.drawLine(100F, 180F, 100F, 260F, myPaint)
        canvas.drawLine(100F, 260F, 100F, 340F, myPaint)
        canvas.drawLine(100F, 340F, 100F, 420F, myPaint)

        canvas.drawLine(
            (pageWidth - 100).toFloat(),
            100F,
            (pageWidth - 100).toFloat(),
            180F,
            myPaint
        )
        canvas.drawLine(
            (pageWidth - 100).toFloat(),
            180F,
            (pageWidth - 100).toFloat(),
            260F,
            myPaint
        )
        canvas.drawLine(
            (pageWidth - 100).toFloat(),
            260F,
            (pageWidth - 100).toFloat(),
            340F,
            myPaint
        )
        canvas.drawLine(
            (pageWidth - 100).toFloat(),
            340F,
            (pageWidth - 100).toFloat(),
            420F,
            myPaint
        )

        canvas.drawLine(500F, 100F, 500F, 180F, myPaint)
        canvas.drawLine(500F, 180F, 500F, 260F, myPaint)
        canvas.drawLine(500F, 260F, 500F, 340F, myPaint)
        canvas.drawLine(500F, 340F, 500F, 420F, myPaint)

        myPaint.textAlign = Paint.Align.LEFT
        myPaint.textSize = 40F
        myPaint.style = Paint.Style.FILL
        myPaint.color = Color.BLACK
        canvas.drawText("Name :", 150F, 150F, myPaint)
        canvas.drawText("Mobile Number :", 150F, 230F, myPaint)
        canvas.drawText("Pan No :", 150F, 310F, myPaint)
        canvas.drawText("GST No :", 150F, 390F, myPaint)

        canvas.drawText(prefs.userProfileDetail.userData?.detail?.username.toString(), 550F, 150F, myPaint)
        canvas.drawText(prefs.userProfileDetail.userData?.detail?.uphone1.toString(), 550F, 230F, myPaint)
        canvas.drawText(prefs.userProfileDetail.userData?.detail?.panno.toString(), 550F, 310F, myPaint)
        canvas.drawText(prefs.userProfileDetail.userData?.detail?.gstno.toString(), 550F, 390F, myPaint)
//
//        canvas.drawLine(180F, 790F, 180F, 840F, myPaint)
//        canvas.drawLine(680F, 790F, 680F, 840F, myPaint)
//        canvas.drawLine(880F, 790F, 880F, 840F, myPaint)
//        canvas.drawLine(1030F, 790F, 1030F, 840F, myPaint)
//
//        myPaint.setColor(Color.BLACK)
//        canvas.drawText("1.", 65F, 950F, myPaint)
//        canvas.drawText("1", 200F, 950F, myPaint)
//        canvas.drawText("2", 700F, 950F, myPaint)
//        canvas.drawText("3", 900F, 950F, myPaint)
//        myPaint.setTextAlign(Paint.Align.RIGHT)
//        canvas.drawText("4", (pageWidth - 70).toFloat(), 950F, myPaint)
//        myPaint.setTextAlign(Paint.Align.LEFT)
//
//        canvas.drawLine(680F, 1200F, (pageWidth - 20).toFloat(), 1200F, myPaint)
//
//        canvas.drawText("Sub total", 700F, 1250F, myPaint)
//        canvas.drawText(":", 900F, 1250F, myPaint)
//        myPaint.setTextAlign(Paint.Align.RIGHT)
//        canvas.drawText("1000", (pageWidth - 40).toFloat(), 1250F, myPaint)
//        myPaint.setTextAlign(Paint.Align.LEFT)
//
//        canvas.drawText("Discount Rate", 700F, 1300F, myPaint)
//        canvas.drawText(":", 900F, 1300F, myPaint)
//        myPaint.setTextAlign(Paint.Align.RIGHT)
//        canvas.drawText("Rate", (pageWidth - 90).toFloat(), 1300F, myPaint)
//        canvas.drawText("%", (pageWidth - 40).toFloat(), 1300F, myPaint)
//        myPaint.setTextAlign(Paint.Align.LEFT)
//
//        canvas.drawText("Discount Price", 700F, 1350F, myPaint)
//        canvas.drawText(":", 900F, 1350F, myPaint)
//        myPaint.setTextAlign(Paint.Align.RIGHT)
//        canvas.drawText("Price", (pageWidth - 40).toFloat(), 1350F, myPaint)
//        myPaint.setTextAlign(Paint.Align.LEFT)
//
//        myPaint.setColor(Color.rgb(139, 0, 139))
//        canvas.drawRect(680F, 1400F, (pageWidth - 20).toFloat(), 1500F, myPaint)
//
//        myPaint.setColor(Color.WHITE)
//        myPaint.setTextSize(50f)
//        myPaint.setTextAlign(Paint.Align.LEFT)
//        canvas.drawText("Total", 700F, 1465F, myPaint)
//        myPaint.setTextAlign(Paint.Align.RIGHT)
//        canvas.drawText("Amount", (pageWidth - 40).toFloat(), 1465F, myPaint)

        myPdfDocument.finishPage(myPage1)

        var filePath = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/" + System.currentTimeMillis() + ".pdf";
        var file = File(filePath);

        try {
            myPdfDocument.writeTo(FileOutputStream(file));
            var sendIntent = Intent();
            sendIntent.setAction(Intent.ACTION_VIEW);
            var url = "https://api.whatsapp.com/send?phone= 91" + 9104545759 + "&text=" + "";
            sendIntent.setData(Uri.parse(url));
            startActivity(sendIntent);
        } catch (e: IOException) {
            e.printStackTrace();
        }
        myPdfDocument.close();

    }

}
