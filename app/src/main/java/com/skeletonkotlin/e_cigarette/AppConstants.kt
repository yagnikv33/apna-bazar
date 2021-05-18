package com.skeletonkotlin.e_cigarette

class AppConstants {

    object App {
        object Buttons {
            const val PERSONALISE_VAPE = "XoOcELl3"
            const val OUR_BRANDS = "SsWZFFHf"
            const val WHAT_IS_VAPING = "CyvHhRuT"
            const val SAVING_CALCULATOR = "9jexWf0h"
            const val TESTIMONIALS = "OYf9cupC"
        }

        object Brands {
            const val VAMPIRE_VAPE = "1VomcQMR"
            const val LOGIC = "ytuXNxrF"
            const val BLU = "beBoAx6P"
            const val VYPE = "dNJYw6ge"
            const val TOTALLY_WICKED ="4ZHSfMi5"
            const val JUUL ="qzavFobQ"
            const val AQUA_VAPE ="wRZd2pNf"
            const val VAPOURIZE ="SXRSX8Oh"
        }
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

        const val BASE_URL = "https://webdevprojects.cloud/php/laravel/ecigarette-admin/api/v1/"

        object ResponseCode {
            const val UNAUTHORIZED_CODE = 401
        }

        object EndUrl {
            const val LOGIN = "login"
            const val SIGN_UP = "signup"
            const val HOME = "home"
            const val PORTAL = "portal"
        }

        object Value {
            const val COMPANY_ID = "rR6lWnPC"
        }
    }
}
