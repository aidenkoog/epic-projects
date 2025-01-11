export default {
    SCREEN: {
        SPLASH: "Splash",
        PROFILE: "Profile",
        QR_SCAN: "QrScan",
        BLUETOOTH: "Bluetooth",
        HOME: "Home",
        HOME_BOTTOM_TAB_SCREEN: "HomeBottomTabScreen",
        DEVICE_STATUS: "DeviceStatus",
        SETTINGS: "Settings",
        SW_UPDATE: "SwUpdate",
        EDIT_PROFILE: "EditProfile",
        DEVICE_INFO: "DeviceInfo",
        DISCONNECTION: "Disconnection",
        HIDDEN: {
            SPLASH: "HiddenSplash",
            HOME: "HiddenHome",
            BLUETOOTH: "HiddenBluetooth",
            COMMON: "HiddenCommon",
            PLATFORM: "HiddenPlatform",
            SERVER: "HiddenServer"
        }
    },
    ROOT: {
        APP_EVENT_TYPE: 'change',
        APP_ACTIVE: 'active',
        APP_BACKGROUND: 'background',
    },
    COMMON: {
        DEFAULT_DATA: '',
        DEFAULT_STATE: false,
        DEFAULT_INT_VALUE: 0,
        DEFAULT_LIST_STATE: [],
        SAVE_PROFILE_SUCCESS: true,
        SAVE_PROFILE_FAILURE: false,
    },
    BT: {
        SCAN_DURATION: 3,
        SCAN_DELAY_TIME: 1500,
        BT_PERMISSION_ACCEPTED: true,
        BT_PERMISSION_REJECTED: false,
        NEXT_SCREEN_DELAY_TIME: 1000
    },
    NAVIGATION: {
        NO_DELAY_TIME: 0,
        PURPOSE: {
            ADD_DEVICE: "ADD_DEVICE",
            EDIT_PROFILE: "EDIT_PROFILE",
            NORMAL: "NORMAL"
        }
    },
    SPLASH: {
        SPLASH_LOADING_TIME: 2000,
    },
    LOG: {
        BT_BLE_MANAGER: 'BT_BLE_MANAGER',

        BT_REPO_LOG: 'BT_REPO_LOG',
        COMMON_REPO_LOG: 'COMMON_REPO_LOG',
        PLATFORM_REPO_LOG: 'PLATFORM_REPO_LOG',
        SERVER_REPO_LOG: 'SERVER_REPO_LOG',

        BT_COMMAND_GENERATOR: 'BT_COMMAND_GENERATOR',
        BT_AUTHENTICATE_MESSAGE: 'BT_AUTHENTICATE_MESSAGE',
        BT_PAYLOAD_MESSAGE: 'BT_PAYLOAD_MESSAGE',

        BT_CRYPTO: 'BT_CRYPTO',
        BT_SECURITY_PACKET: 'BT_SECURITY_PACKET',
        BT_KEY_CRYPTO: 'BT_KEY_CRYPTO',
        BT_HASH_MAC: 'BT_HASH_MAC',
        BT_MESSAGE: 'BT_MESSAGE',

        BT_USECASE_LOG: 'BT_USECASE_LOG',
        COMMON_USECASE_LOG: 'COMMON_USECASE_LOG',
        PLATFORM_USECASE_LOG: 'PLATFORM_USECASE_LOG',
        SERVER_USECASE_LOG: 'SERVER_USECASE_LOG',

        BT_UI_LOG: 'BT_UI_LOG',
        HOME_UI_LOG: 'HOME_UI_LOG',
        DEVICE_STATUS_UI_LOG: 'DEVICE_STATUS_UI_LOG',
        PROFILE_UI_LOG: 'PROFILE_UI_LOG',
        QR_SCAN: 'QR_SCAN',
        ROOT_UI_LOG: 'ROOT_UI_LOG',
        SETTINGS_UI_LOG: 'SETTINGS_UI_LOG',
        EDIT_PROFILE_UI_LOG: 'EDIT_PROFILE_UI_LOG',
        SPLASH_UI_LOG: 'SPLASH_UI_LOG',
        DISCONNECTION_UI_LOG: 'DISCONNECTION_UI_LOG',

        HIDDEN_BT_UI_LOG: 'HIDDEN_BT_UI_LOG',

        REPO_TEST_LOG: 'REPO_TEST_LOG',
        UI_TEST_LOG: 'UI_TEST_LOG',
        SOURCE_TEST_LOG: 'SOURCE_TEST_LOG',
        USECASE_TEST_LOG: 'USECASE_TEST_LOG',

        STORAGE_LOG_TAG: 'STORAGE_LOG',

        BLE_UTIL_LOG_TAG: 'BLE_UTIL_LOG',
        BLE_FEATURE_UTIL_LOG_TAG: 'BLE_FEATURE_UTIL_LOG',
        BLE_ENCRYPTION_UTIL_LOG_TAG: 'BLE_ENCRYPTION_UTIL_LOG',

        PERMISSION_LOG_TAG: "PERMISSION_LOG_TAG",
        TIME_UTIL_LOG_TAG: "TIME_UTIL_LOG"
    },
    STORAGE: {
        KEY_USER_PROFILE: "KEY_USER_PROFILE",

        KEY_PROFILE_NAME: "KEY_PROFILE_NAME",
        KEY_PROFILE_IMAGE_URL: "KEY_PROFILE_IMAGE_URL",
        KEY_PROFILE_GENDER: "KEY_PROFILE_GENDER",
        KEY_PROFILE_BIRTHDAY: "'KEY_PROFILE_BIRTHDAY",
        KEY_PROFILE_HEIGHT: "KEY_PROFILE_HEIGHT",
        KEY_PROFILE_WEIGHT: "KEY_PROFILE_WEIGHT",

        KEY_BLE_DEVICE_MAC_ADDRESS: 'KEY_BLE_DEVICE_MAC_ADDRESS',
        KEY_BLE_DEVICE_NAME: 'KEY_BLE_DEVICE_NAME',
        KEY_DEVICE_REGISTRATION_FLAG: 'KEY_DEVICE_REGISTRATION_FLAG',
    }
};

export const NO_DATA_VALUE = "-"

export const HEALTH_TYPE = {
    STEP: 'STEP',
    HEART_RATE: 'HEART_RATE',
    SLEEP: 'SLEEP'
}

export const HEALTH_GRAPH_TYPE = {
    DAY: 'Day',
    WEEK: 'Week',
    MONTH: 'Month',
    YEAR: 'Year'
}
