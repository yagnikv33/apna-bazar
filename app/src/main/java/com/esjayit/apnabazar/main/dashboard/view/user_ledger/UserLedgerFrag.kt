package com.esjayit.apnabazar.main.dashboard.view.user_ledger

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import com.esjayit.R
import com.esjayit.apnabazar.AppConstants
import com.esjayit.apnabazar.Layouts
import com.esjayit.apnabazar.data.model.response.LedgerData
import com.esjayit.apnabazar.data.model.response.PartyLedgerResponse
import com.esjayit.apnabazar.helper.custom.CustomProgress
import com.esjayit.apnabazar.helper.util.logE
import com.esjayit.apnabazar.main.base.BaseFrag
import com.esjayit.apnabazar.main.common.ApiRenderState
import com.esjayit.apnabazar.main.dashboard.view.user_ledger.model.UserLedgerVM
import com.esjayit.apnabazar.main.notificationmodule.view.NotificationAct
import com.esjayit.databinding.FragmentUserLedgerBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


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

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            binding.btnDownload -> {
                checkForPermission()
            }
            binding.btnNotification -> {
                startActivity(NotificationAct::class.java)
            }
        }
    }

    fun checkForPermission() {
        if (Build.VERSION.SDK_INT < 29) {
            requestPermissions(
                arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), 1
            )
        } else {
            Toast.makeText(requireContext(),"Downloading...",Toast.LENGTH_LONG).show()
            createPDF(data)
        }
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
                            data = apiRenderState.result.data
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

        myPaint.color = Color.BLACK
        canvas.drawLine(100F, 100F, (pageWidth - 100).toFloat(), 100F, myPaint)
        canvas.drawLine(100F, 140F, (pageWidth - 100).toFloat(), 140F, myPaint)
        canvas.drawLine(100F, 180F, (pageWidth - 100).toFloat(), 180F, myPaint)
        canvas.drawLine(100F, 220F, (pageWidth - 100).toFloat(), 220F, myPaint)
        canvas.drawLine(100F, 260F, (pageWidth - 100).toFloat(), 260F, myPaint)

        canvas.drawLine(100F, 100F, 100F, 140F, myPaint)
        canvas.drawLine(100F, 140F, 100F, 180F, myPaint)
        canvas.drawLine(100F, 180F, 100F, 220F, myPaint)
        canvas.drawLine(100F, 220F, 100F, 260F, myPaint)

        canvas.drawLine(
            (pageWidth - 100).toFloat(),
            100F,
            (pageWidth - 100).toFloat(),
            140F,
            myPaint
        )
        canvas.drawLine(
            (pageWidth - 100).toFloat(),
            140F,
            (pageWidth - 100).toFloat(),
            180F,
            myPaint
        )
        canvas.drawLine(
            (pageWidth - 100).toFloat(),
            180F,
            (pageWidth - 100).toFloat(),
            220F,
            myPaint
        )
        canvas.drawLine(
            (pageWidth - 100).toFloat(),
            220F,
            (pageWidth - 100).toFloat(),
            260F,
            myPaint
        )

        canvas.drawLine(350F, 100F, 350F, 140F, myPaint)
        canvas.drawLine(350F, 140F, 350F, 180F, myPaint)
        canvas.drawLine(350F, 180F, 350F, 220F, myPaint)
        canvas.drawLine(350F, 220F, 350F, 260F, myPaint)

        myPaint.textAlign = Paint.Align.LEFT
        myPaint.textSize = 24F
        myPaint.style = Paint.Style.FILL
        myPaint.color = Color.BLACK
        canvas.drawText("Name :", 130F, 130F, myPaint)
        canvas.drawText("Mobile Number :", 130F, 170F, myPaint)
        canvas.drawText("Pan No :", 130F, 210F, myPaint)
        canvas.drawText("GST No :", 130F, 250F, myPaint)

        canvas.drawText(
            prefs.userProfileDetail.userData?.detail?.username.toString(),
            380F,
            130F,
            myPaint
        )
        canvas.drawText(
            prefs.userProfileDetail.userData?.detail?.uphone1.toString(),
            380F,
            170F,
            myPaint
        )
        canvas.drawText(
            prefs.userProfileDetail.userData?.detail?.panno.toString(),
            380F,
            210F,
            myPaint
        )
        canvas.drawText(
            prefs.userProfileDetail.userData?.detail?.gstno.toString(),
            380F,
            250F,
            myPaint
        )

        canvas.drawLine(100F, 300F, (pageWidth - 100).toFloat(), 300F, myPaint)

        canvas.drawLine(100F, 300F, 100F, 340F, myPaint)
        canvas.drawLine(200F, 300F, 200F, 340F, myPaint)
        canvas.drawLine(400F, 300F, 400F, 340F, myPaint)
        canvas.drawLine(700F, 300F, 700F, 340F, myPaint)
        canvas.drawLine(834F, 300F, 834F, 340F, myPaint)
        canvas.drawLine(968F, 300F, 968F, 340F, myPaint)
        canvas.drawLine(
            (pageWidth - 100).toFloat(),
            300F,
            (pageWidth - 100).toFloat(),
            340F,
            myPaint
        )

        canvas.drawLine(100F, 340F, (pageWidth - 100).toFloat(), 340F, myPaint)

        myPaint.textAlign = Paint.Align.LEFT
        myPaint.textSize = 24F
        myPaint.style = Paint.Style.FILL
        myPaint.color = requireContext().getColor(R.color.light_blue)
//        canvas.drawText("No", 140F, 330F, myPaint)
//        canvas.drawText("Date", 280F, 330F, myPaint)
//        canvas.drawText("Narration", 505F, 330F, myPaint)
//        canvas.drawText("Credit", 738F, 330F, myPaint)
//        canvas.drawText("Debit", 875F, 330F, myPaint)
//        canvas.drawText("Balance", 1000F, 330F, myPaint)

        canvas.drawText("No", 110F, 330F, myPaint)
        canvas.drawText("Date", 210F, 330F, myPaint)
        canvas.drawText("Narration", 410F, 330F, myPaint)
        canvas.drawText("Credit", 710F, 330F, myPaint)
        canvas.drawText("Debit", 844F, 330F, myPaint)
        canvas.drawText("Balance", 978F, 330F, myPaint)

        myPaint.color = Color.BLACK
//        canvas.drawLine(100F, 340F, 100F, 380F, myPaint)
//        canvas.drawLine(200F, 340F, 200F, 380F, myPaint)
//        canvas.drawLine(400F, 340F, 400F, 380F, myPaint)
//        canvas.drawLine(700F, 340F, 700F, 380F, myPaint)
//        canvas.drawLine(834F, 340F, 834F, 380F, myPaint)
//        canvas.drawLine(968F, 340F, 968F, 380F, myPaint)
//        canvas.drawLine(
//            (pageWidth - 100).toFloat(),
//            340F,
//            (pageWidth - 100).toFloat(),
//            380F,
//            myPaint
//        )
//
//        canvas.drawText("1", 145F, 370F, myPaint)
//        canvas.drawText("", 280F, 370F, myPaint)
//        canvas.drawText("OPENING BALANCE", 470F, 370F, myPaint)
//        canvas.drawText("0", 760F, 370F, myPaint)
//        canvas.drawText("0", 895F, 370F, myPaint)
//        canvas.drawText("0", 1025F, 370F, myPaint)

        if (data?.ledger?.size!! > 0) {
            Log.e("createPDF: ", "Ledger Size : ${data.ledger.size}")
            for (i in data.ledger.indices) {
                if (i == data.ledger.size - 1) {
                    canvas.drawLine(
                        100F,
                        (340 + ((i + 1) * 40)).toFloat(),
                        (pageWidth - 100).toFloat(),
                        (340 + ((i + 1) * 40)).toFloat(),
                        myPaint
                    )
                }
                canvas.drawLine(
                    100F,
                    (340 + (i * 40)).toFloat(),
                    (pageWidth - 100).toFloat(),
                    (340 + (i * 40)).toFloat(),
                    myPaint
                )

                canvas.drawLine(
                    100F,
                    (340 + (i * 40)).toFloat(),
                    100F,
                    (340 + ((i + 1) * 40)).toFloat(),
                    myPaint
                )
                canvas.drawLine(
                    200F,
                    (340 + (i * 40)).toFloat(),
                    200F,
                    (340 + ((i + 1) * 40)).toFloat(),
                    myPaint
                )
                canvas.drawLine(
                    400F,
                    (340 + (i * 40)).toFloat(),
                    400F,
                    (340 + ((i + 1) * 40)).toFloat(),
                    myPaint
                )
                canvas.drawLine(
                    700F,
                    (340 + (i * 40)).toFloat(),
                    700F,
                    (340 + ((i + 1) * 40)).toFloat(),
                    myPaint
                )
                canvas.drawLine(
                    834F,
                    (340 + (i * 40)).toFloat(),
                    834F,
                    (340 + ((i + 1) * 40)).toFloat(),
                    myPaint
                )
                canvas.drawLine(
                    968F,
                    (340 + (i * 40)).toFloat(),
                    968F,
                    (340 + ((i + 1) * 40)).toFloat(),
                    myPaint
                )
                canvas.drawLine(
                    (pageWidth - 100).toFloat(),
                    (340 + (i * 40)).toFloat(),
                    (pageWidth - 100).toFloat(),
                    (340 + ((i + 1) * 40)).toFloat(),
                    myPaint
                )

//                canvas.drawText((i+1).toString(), 145F, ((340+((i+1)*40))-10).toFloat(), myPaint)
//                canvas.drawText(data.ledger[i]?.trandate.toString(), 220F, ((340+((i+1)*40))-10).toFloat(), myPaint)
//                canvas.drawText(data.ledger[i]?.narration.toString(), 470F, ((340+((i+1)*40))-10).toFloat(), myPaint)
//                canvas.drawText(data.ledger[i]?.credit.toString(), 760F, ((340+((i+1)*40))-10).toFloat(), myPaint)
//                canvas.drawText(data.ledger[i]?.debit.toString(), 895F, ((340+((i+1)*40))-10).toFloat(), myPaint)
//                canvas.drawText(data.ledger[i]?.balance.toString(), 1025F, ((340+((i+1)*40))-10).toFloat(), myPaint)

                canvas.drawText(
                    (i + 1).toString(),
                    110F,
                    ((340 + ((i + 1) * 40)) - 10).toFloat(),
                    myPaint
                )
                canvas.drawText(
                    data.ledger[i]?.trandate.toString(),
                    210F,
                    ((340 + ((i + 1) * 40)) - 10).toFloat(),
                    myPaint
                )
                canvas.drawText(
                    data.ledger[i]?.narration.toString(),
                    410F,
                    ((340 + ((i + 1) * 40)) - 10).toFloat(),
                    myPaint
                )
                canvas.drawText(
                    data.ledger[i]?.credit.toString(),
                    710F,
                    ((340 + ((i + 1) * 40)) - 10).toFloat(),
                    myPaint
                )
                canvas.drawText(
                    data.ledger[i]?.debit.toString(),
                    844F,
                    ((340 + ((i + 1) * 40)) - 10).toFloat(),
                    myPaint
                )
                canvas.drawText(
                    data.ledger[i]?.balance.toString(),
                    978F,
                    ((340 + ((i + 1) * 40)) - 10).toFloat(),
                    myPaint
                )
            }
        } else {
            canvas.drawLine(100F, 340F, (pageWidth - 100).toFloat(), 340F, myPaint)
        }
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

        val filePath: String? = Objects.requireNonNull(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        ).absolutePath
        val folder = File(filePath, requireContext().resources.getString(R.string.app_name))

        try {
            folder.mkdirs()
            val file =
                File(folder, Date().time.toString()  + ".pdf")
            myPdfDocument.writeTo(FileOutputStream(file));
            Toast.makeText(requireContext(),"Pdf downloaded!",Toast.LENGTH_LONG).show()
//            Toast.makeText(requireContext(), "File path : ${file.path}", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        myPdfDocument.close()

    }

}
