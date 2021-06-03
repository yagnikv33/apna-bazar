package com.yudiz.e_cigarette

import com.yudiz.e_cigarette.data.model.response.PersonaliseVape

class AppConstants {

    object App {
        object Buttons {
            const val PERSONALISE_VAPE = 0
            const val OUR_BRANDS = 1
            const val WHAT_IS_VAPING = 2
            const val SAVING_CALCULATOR = 3
            const val TESTIMONIALS = 4
            const val KNOW_THE_FACTS = "A4gcZ9UR"
            const val PG_VS_VG = "o6RUyzWz"
        }

    }

    object Communication {

        object BundleData {
            const val BRAND_ITEM_ID = "1"
            const val BUTTON_ITEM_ID = "2"
        }
    }

    object Api {

        const val BASE_URL = "https://webdevprojects.cloud/php/laravel/ecigarette-admin/api/v1/"

        object ResponseCode {
            const val UNAUTHORIZED_CODE = 401
        }

        object EndUrl {
            const val HOME = "home"
            const val PORTAL = "portal"
            const val BRAND_DETAIL = "brand-detail"
            const val BRAND_LIST = "brand-list"
            const val VAPING_LIST = "vaping-button-list"
            const val KNOW_THE_FACTS = "know-the-facts"
            const val PG_VG = "PG-vs-VG"
            const val TESTIMONIALS = "testimonials"
            const val PERSONALISE_VAPE = "personalise_your_vape"
        }

        object Value {
            const val COMPANY_ID = "rR6lWnPC"
            const val TYPE_VIDEO = "video/webm"
            const val TYPE_IMAGE = "image/jpeg"
        }
    }
}
