package com.skeletonkotlin.e_cigarette.main.base

import com.skeletonkotlin.e_cigarette.api.ErrorUtil
import com.skeletonkotlin.e_cigarette.helper.util.NetworkUtil
import com.skeletonkotlin.e_cigarette.main.base.BaseRepo.ApiResultType.CANCELLED
import com.skeletonkotlin.e_cigarette.main.base.BaseRepo.ApiResultType.HTTP_ERROR
import com.skeletonkotlin.e_cigarette.main.base.BaseRepo.ApiResultType.MISCELLANEOUS
import com.skeletonkotlin.e_cigarette.main.base.BaseRepo.ApiResultType.NO_INTERNET
import com.skeletonkotlin.e_cigarette.main.base.BaseRepo.ApiResultType.SUCCESS
import com.skeletonkotlin.e_cigarette.main.base.BaseRepo.ApiResultType.TIME_OUT
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.util.concurrent.CancellationException

open class BaseRepo : KoinComponent {
    private val nwUtil by inject<NetworkUtil>()

    object ApiResultType {
        const val SUCCESS = 1
        const val NO_INTERNET = 2
        const val HTTP_ERROR = 3
        const val TIME_OUT = 4
        const val MISCELLANEOUS = 5
        const val CANCELLED = 6
    }

    internal suspend fun <T : Any> apiCall(executable: suspend () -> T): ApiResult<T> {
        return try {
            if (nwUtil.isNetworkAvailable())
                ApiResult(executable.invoke())
            else
                ApiResult(null, NO_INTERNET, "Internet not connected")
        } catch (e: HttpException) {
           /* val error = e.response()?.let { ErrorUtil.parseError(it) }
            ApiResult(null, HTTP_ERROR, error?.message, resCode = e.code())*/
            ApiResult(null, HTTP_ERROR, e.message ?: "Something went wrong", resCode = e.code())
        } catch (e: SocketTimeoutException) {
            ApiResult(null, TIME_OUT, "Time out")
        } catch (e: CancellationException) {
            ApiResult(null, CANCELLED, "")
        } catch (e: Throwable) {
            ApiResult(null, MISCELLANEOUS, e.message ?: "Something went wrong")
        }
    }
}

data class ApiResult<T>(
    val data: T?,
    val resultType: Int = SUCCESS,
    val error: String? = null,
    val reqCode: Int = -1,
    val resCode: Int = -1
)