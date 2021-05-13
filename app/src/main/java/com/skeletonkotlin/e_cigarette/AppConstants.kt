package com.skeletonkotlin.e_cigarette

class AppConstants {

    object App {
    }

    object Prefs {
        const val AUTH_TOKEN = "1"
        const val USER_INFO = "2"
        const val FCM_TOKEN = "3"
        const val ROOM_KEY = "4"
    }

    object Communication {

        object RequestCode {

        }

        object ResponseCode {

        }

        object Broadcast {

        }

        object BundleData {
            const val MAIN_ACT_HEADING = "1"
            const val IS_UNAUTHORISED = "2"
        }
    }

    object Api {

        val BASE_URL = "https://xyz.com/"

        object ResponseCode {
            const val UNAUTHORIZED_CODE = 401
        }

        object EndUrl {
            const val LOGIN = "login"
            const val SIGN_UP = "signup"
        }
    }
}
