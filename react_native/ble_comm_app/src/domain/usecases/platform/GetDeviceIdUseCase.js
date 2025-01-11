import PlatformRepository from '../../../data/repositories/platform/PlatformRepository.js'
import Constants from '../../../utils/Constants.js'
import { outputErrorLog } from '../../../utils/logger/Logger.js'


const LOG_TAG = Constants.LOG.COMMON_USECASE_LOG

const GetDeviceIdUseCase = () => {

    const { getInstanceId, getAndroidId, getUniqueId } = PlatformRepository()

    /**
     * execute usecase of getting instance id.
     * @returns {Promise}
     */
    executeGetInstanceIdUseCase = () => {
        logDebugWithLine(LOG_TAG, "execute GetInstanceIdUseCase")

        return new Promise((fulfill, reject) => {
            getInstanceId().then((instanceId) => {
                fulfill(instanceId)

            }).catch((e) => {
                outputErrorLog(LOG_TAG, e + " occurred by getInstanceId")
                reject(e)
            })
        })
    }

    /**
     * execute usecase of getting android id.
     * @returns {Promise}
     */
    executeGetAndroidIdUseCase = () => {
        logDebugWithLine(LOG_TAG, "execute GetAndroidIdUseCase")

        return new Promise((fulfill, reject) => {
            getAndroidId().then((androidId) => {
                fulfill(androidId)

            }).catch((e) => {
                outputErrorLog(LOG_TAG, e + " occurred by getAndroidId")
                reject(e)
            })
        })
    }

    /**
     * execute usecase of getting unique id.
     * @returns {Promise}
     */
    executeGetUniqueIdUseCase = () => {
        logDebugWithLine(LOG_TAG, "execute GetUniqueIdUseCase")

        return new Promise((fulfill, reject) => {
            getUniqueId().then((instanceId) => {
                fulfill(instanceId)

            }).catch((e) => {
                outputErrorLog(LOG_TAG, e + " occurred by getUniqueId")
                reject(e)
            })
        })
    }
    return {
        executeGetInstanceIdUseCase,
        executeGetAndroidIdUseCase,
        executeGetUniqueIdUseCase
    }
}

export default GetDeviceIdUseCase