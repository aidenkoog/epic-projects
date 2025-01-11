import { logDebug, logDebugWithLine, outputErrorLog } from "../../../utils/logger/Logger"
import { hasValidPhoneNumber } from "../../../utils/regex/RegexUtil"
import { CRYPTO_ENABLE } from "../../../configs/Configs"

const LOG_TAG = "UrlRepository"

const CryptoJS = require("crypto-js")

/**
 * Cryptojs decryption secret key and iv.
 */
const CRYPTO_SECRET_KEY = "..."
const CRYPTO_SECRET_IV = "..."
const CRYPTO_PADDING_MODE = CryptoJS.pad.Pkcs7
const CRYPTO_MODE = CryptoJS.mode.CBC

/**
 * Domain prefix list.
 */
const DOMAIN_PREFIX_LIST = ["http://", "https://"]

/**
 * Activities query parameter keys.
 */
const WATCH_MOBILE_NUMBER = ["loadKey"]
const TYPES = "types"


export default function UrlRepository() {

    /**
     * Parse url query parameters with delimeters and return array.
     * @param {String} urlQueryString
     * @returns {Array}
     */
    function getQueryParams(urlQueryString) {
        let hasQuestion = urlQueryString.includes("?")
        let hasAmpersand = urlQueryString.includes("&")
        let result = []

        if (hasQuestion) {
            let queryStrings = urlQueryString.replace("?", "")

            if (hasAmpersand) {
                let splitStrings = queryStrings.split("&")
                for (const item of splitStrings) {
                    result.push(item)
                }
            } else {
                result.push(queryStrings)
            }

        } else {
            result = []
        }
        return result
    }

    /**
     * Parse mobile phone number information from query parameter array.
     * @param {String} urlQueryString
     * @returns {String}
     */
    function getDeviceMobileNumber(urlQueryString) {

        let urlQueryParams = getQueryParams(urlQueryString)
        if (urlQueryParams == null || urlQueryParams.length === 0) {
            return null
        }

        for (const item of urlQueryParams) {
            if (!item.includes("=")) {
                return null
            }
            let splitStrings = item.split("=")
            let paramKeyName = splitStrings[0]

            if (hasMatchedPhoneNumber(paramKeyName)) {
                if (hasValidPhoneNumber(splitStrings[1])) {
                    logDebug(LOG_TAG, "[TEST] ENCRYPTED Result: " + encryptDeviceMobileNumber(splitStrings[1]))
                }
                return splitStrings[1]
            }
        }
        return null
    }

    /**
     * Check if there's matched parameter key corresponding to mobile number key array.
     * @param {String} paramKeyName 
     * @returns {Boolean}
     */
    function hasMatchedPhoneNumber(paramKeyName) {
        for (const phoneNumberItem of WATCH_MOBILE_NUMBER) {
            if (phoneNumberItem === paramKeyName) {
                return true
            }
        }
        return false
    }

    /**
     * parse event types information from query parameter array.
     * @param {String} urlQueryString
     * @returns {String}
     */
    function getTypes(urlQueryString) {
        let urlQueryParams = getQueryParams(urlQueryString)
        if (urlQueryParams == null || urlQueryParams.length === 0) {
            return null
        }

        for (const item of urlQueryParams) {
            if (!item.includes("=")) {
                return null
            }

            let splitStrings = item.split("=")
            let paramKeyName = splitStrings[0]

            if (TYPES === paramKeyName) {
                let eventTypes = splitStrings[1]
                if (eventTypes.includes(",")) {
                    let splitEventTypes = eventTypes.split(",")
                    return splitEventTypes

                } else {
                    return eventTypes
                }
            }
        }
        return null
    }

    /**
     * Parse domain url from web url.
     * @param {String} urlLocationString 
     * @returns {String}
     */
    function getDomain(urlLocationString) {
        if (urlLocationString == null || urlLocationString === "") {
            outputErrorLog(LOG_TAG, "INVALID urlLocationString (" + urlLocationString + ")")
            return null
        }

        let hasDomainPrefix = false
        let locationWithoutPrefix = ""
        let domainPrefix = ""

        for (const item of DOMAIN_PREFIX_LIST) {
            if (urlLocationString.includes(item)) {
                hasDomainPrefix = true
                domainPrefix = item
                locationWithoutPrefix = urlLocationString.replace(item, "")
                break
            }
        }

        if (!hasDomainPrefix) {
            outputErrorLog(LOG_TAG, "there's NO any domain prefix such as http:// or https://")
            return null
        }

        if (locationWithoutPrefix.includes("/")) {
            let urlItemListBySlash = locationWithoutPrefix.split("/")
            const domainUrl = urlItemListBySlash[0]
            if (domainUrl == null || domainUrl === "") {
                outputErrorLog(LOG_TAG, "domainUrl is NULL or EMPTY")
                return null

            } else {
                const result = domainPrefix + domainUrl
                return result
            }

        } else {
            if (urlLocationString == null || urlLocationString === "") {
                return null

            } else {
                const result = domainPrefix + urlLocationString
                return result
            }
        }
    }

    /**
     * Decrypt mobile phone number.
     * @param {String} encryptedDevicePhoneNumber
     * @returns {String}
     */
    function decryptDeviceMobileNumber(encryptedDevicePhoneNumber) {
        const decodedDevicePhoneNumber = decodeURIComponent(encryptedDevicePhoneNumber)
        logDebugWithLine(LOG_TAG, "decryption START: ENCRYPTED PHONE NUMBER: " + encryptedDevicePhoneNumber)
        logDebugWithLine(LOG_TAG, "decryption START: URI Decoded PHONE NUMBER: " + decodedDevicePhoneNumber)

        const secretKeyWordArray = CryptoJS.enc.Utf8.parse(CRYPTO_SECRET_KEY)
        const ivKeyWordArray = CryptoJS.enc.Utf8.parse(CRYPTO_SECRET_IV)

        if (CRYPTO_ENABLE) {
            try {
                const cipher = CryptoJS.AES.decrypt(decodedDevicePhoneNumber, secretKeyWordArray, {
                    iv: ivKeyWordArray,
                    padding: CRYPTO_PADDING_MODE,
                    keySize: 128,
                    mode: CRYPTO_MODE
                })
                logDebug(LOG_TAG, ">>> CIPHERED Decrypted Result: " + cipher)
                return cipher.toString(CryptoJS.enc.Utf8)

            } catch (e) {
                outputErrorLog(LOG_TAG, e + " occurred by Decrypting / Decoding")
                return null
            }

        } else {
            return encryptedDevicePhoneNumber
        }
    }

    /**
     * Encrypt mobile phone number.
     * @param {String} deviceMobileNumber 
     * @returns {String}
     */
    function encryptDeviceMobileNumber(deviceMobileNumber) {
        logDebugWithLine(LOG_TAG, "encryption START: PHONE NUMBER: " + deviceMobileNumber)

        const secretKeyWordArray = CryptoJS.enc.Utf8.parse(CRYPTO_SECRET_KEY)
        const ivKeyWordArray = CryptoJS.enc.Utf8.parse(CRYPTO_SECRET_IV)

        if (CRYPTO_ENABLE) {
            try {
                const cipher = CryptoJS.AES.encrypt(deviceMobileNumber, secretKeyWordArray, {
                    iv: ivKeyWordArray,
                    padding: CRYPTO_PADDING_MODE,
                    keySize: 128,
                    mode: CRYPTO_MODE
                })
                return cipher

            } catch (e) {
                outputErrorLog(LOG_TAG, e + " occurred by Encrypting / Encoding")
                return null
            }

        } else {
            return deviceMobileNumber
        }
    }

    return {
        getDeviceMobileNumber,
        decryptDeviceMobileNumber,
        getTypes,
        getDomain
    }
}