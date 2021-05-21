package com.yudiz.e_cigarette

class AppConstants {

    object App {
        object Buttons {
            const val PERSONALISE_VAPE = 0
            const val OUR_BRANDS = 1
            const val WHAT_IS_VAPING = 2
            const val SAVING_CALCULATOR = 3
            const val TESTIMONIALS = 4
        }

    }

    object Communication {

        object RequestCode {

        }

        object ResponseCode {

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
            const val BRAND_DETAIL = "brand-detail"
        }

        object Value {
            const val COMPANY_ID = "rR6lWnPC"
        }
    }
}
