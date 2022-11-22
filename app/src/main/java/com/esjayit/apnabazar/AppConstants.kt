package com.esjayit.apnabazar

class AppConstants {


    object App {
        const val ONESIGNAL_APP_ID = "3fd18645-8abc-44a7-842d-61d8b9a34c99"

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

    object Prefs {
        const val AUTH_TOKEN = "1"
    }

    object Status_Code {
        const val Success = "1"
        const val Failed = "0"
        const val Code2 = "2"
        const val Code3 = "3"
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
            const val HOME = "home"

            //BASE_URL
            const val APP_FIRST_LAUNCH_STATUS = BASE_URL + "appfirstlaunch"
            const val ADD_DEVICE_INFO = BASE_URL + "adddeviceinfo"
            const val CHECK_UPDATE = BASE_URL + "checkupdate"
            const val LOG_ERROR = BASE_URL + "logerror"

            //TEST_URL
            const val CHECK_USER_VERIFICATION = "checkuserverification"
            const val SEND_OTP = "sendotp"
            const val VERIFY_OTP = "verifyotp"
            const val NEW_PASSWORD = "newpassword"
            const val LOGIN = "login"

            //Home Screen
            const val CHECK_USER_ACTIVE = "checkactiveuser"
            const val GET_HOME_DATA = "gethomescreendata"

            //Demand
            const val GET_MEDIUM = "getmediumlist"
            const val GET_STANDARD = "getstandardlist"
            const val GET_SUBJECT_LIST = "getsubjectlist"
            const val GET_ITEM_DETAIL = "getitemdetail"
            const val DEMAND_LIST = "getdemandlist"
            const val ADD_DEMAND = "demanddate"

            //Profile Related
            const val GET_USER_PROFILE = "getuserdetails"
            const val EDIT_USER_PROFILE = "edituserprofile"
            const val VIEW_DEMAND_LIST = "viewsalesdemand"
        }

        object Value {
            const val COMPANY_ID = "rR6lWnPC"
            const val TYPE_VIDEO = "video/webm"
            const val TYPE_IMAGE = "image/jpeg"
            const val TYPE_GIF = "image/gif"
        }
    }
}
