package com.esjayit.apnabazar

class AppConstants {

    object App {
        object Buttons {
            const val PERSONALISE_VAPE = "XoOcELl3"
            const val OUR_BRANDS = "SsWZFFHf"
            const val WHAT_IS_VAPING = "CyvHhRuT"
            const val SAVING_CALCULATOR = "9jexWf0h"
            const val TESTIMONIALS = "OYf9cupC"
            const val KNOW_THE_FACTS = "A4gcZ9UR"
            const val PG_VS_VG = "o6RUyzWz"

            const val YEARS_SMOKING = "years_smoking"
            const val CIGARETTES_PER_DAY = "cigarettes_per_day"
            const val HABITS = "habits"
        }

    }

    object Prefs{
        const val AUTH_TOKEN = "1"
    }

    object Communication {

        object BundleData {
            const val BRAND_ITEM_ID = "1"
        }
    }

    object Api {

        //For First 4 API
        const val BASE_URL = "http://developerapis.esjayit.com/"
        //Testing Server
        const val TEST_URL = "http://testapis.apnabazarrajkot.com/"
        object EndUrl {
            const val  HOME = "home"
            //BASE_URL
            const val ADD_DEVICE_INFO = "adddeviceinfo"
            const val CHECK_UPDATE = "checkupdate"
            const val LOG_ERROR = "logerror"
            const val APP_LAUNCH_STATUS = "appfirstlaunch"
            //TEST_URL
            const val CHECK_USER_ACTIVE = "checkactiveuser"


        }

        object Value {
            const val COMPANY_ID = "rR6lWnPC"
            const val TYPE_VIDEO = "video/webm"
            const val TYPE_IMAGE = "image/jpeg"
            const val TYPE_GIF = "image/gif"
        }
    }
}
