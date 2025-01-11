import React, { useState } from 'react'
import { Strings } from '../../../utils/theme'
import { BackHandler } from 'react-native'
import { launchImageLibrary, launchCamera } from 'react-native-image-picker'
import { emptyField, getOnlyNumber, imageOptions } from "../../../utils/common/CommonUtil"
import { formatDateToString } from '../../../utils/time/TimeUtil'
import Constants from '../../../utils/Constants'
import SetProfileInfoUseCase from '../../../domain/usecases/common/SetProfileInfoUseCase'
import { logDebug, outputErrorLog } from '../../../utils/logger/Logger'
import { showAlert } from '../../../utils/alert/AlertUtil'
import ProfileComponent from './ProfileComponent'
import { navigateToNextScreen } from '../../../utils/navigation/NavigationUtil'
import { UserProfile } from '../../../domain/entities/user/UserProfile'
import CheckPermissionUseCase from '../../../domain/usecases/platform/CheckPermissionUseCase'

const NAVIGATION_NO_DELAY_TIME = Constants.NAVIGATION.NO_DELAY_TIME
const REGISTER_USER_STRINGS = Strings.registerSenior

const NAVIGATION_PURPOSE = Constants.NAVIGATION.PURPOSE.NORMAL
const LOG_TAG = Constants.LOG.PROFILE_UI_LOG

/**
 * use state keys.
 * the following key values are used in both the profile container and the component.
 */
export const KEY_MODAL_PHOTO_VISIBLE = "modalPhotoVisible"
export const KEY_MODAL_DATE_PICKER_VISIBLE = "modalDatePickerVisible"
export const KEY_NAME_STATE = "name"
export const KEY_HEIGHT_STATE = "height"
export const KEY_WEIGHT_STATE = "weight"
export const KEY_GENDER_STATE = "gender"

/**
 * cached formatted birthday date information.
 */
let cachedFormattedBirthdayDate = null

/**
 * next screen information.
 */
const NEXT_SCREEN = Constants.SCREEN.QR_SCAN

/**
 * profile registration screen.
 * @param {Any} props 
 * @returns {JSX.Element}
 */
export default function ProfileContainer(props) {

    /**
     * useState code for ui interaction.
     * Refs. the use-state variable 'date' means birthday.
     */
    const [modalPhotoVisible, setModalPhotoVisible] = useState(false)
    const [modalDatePickerVisible, setModalDatePickerVisible] = useState(false)
    const [photoUrl, setPhotoUrl] = useState("")
    const [name, setName] = useState("")
    const [gender, setGender] = useState(0)
    const [date, setDate] = useState(REGISTER_USER_STRINGS.placeHolderBirthday)
    const [height, setHeight] = useState("")
    const [weight, setWeight] = useState("")

    /**
     * usecases.
     */
    const { executeSetProfileInfoUseCase } = SetProfileInfoUseCase()
    const { executeCheckCameraPermission } = CheckPermissionUseCase()

    /**
     * handle back button click event. (android)
     * @returns {boolean}
     */
    const handleBackButtonClick = () => {
        showAlert("Warning", "Do you want to terminate app?", "Cancel", "OK", false)
        return true
    }

    /**
     * save formatted birthday date information.
     */
    const onHandleFormatBirthdayDate = (date) => {
        cachedFormattedBirthdayDate = date
        logDebug(LOG_TAG, "<<< cachedFormattedBirthdayDate: " + cachedFormattedBirthdayDate)
    }

    /**
     * handle the event that occurs when 'Done' button is pressed.
     */
    onClickDoneButton = () => {
        executeSetProfileInfoUseCase(getUserProfileInfo(), (succeeded) => {
            if (succeeded) {
                navigateToNextScreen(props.navigation, NEXT_SCREEN, NAVIGATION_NO_DELAY_TIME, NAVIGATION_PURPOSE)

            } else {
                outputErrorLog(LOG_TAG, "failed to set profile")
            }
        })
    }

    /**
     * get user profile information.
     */
    getUserProfileInfo = () => {
        UserProfile.imageUrl = photoUrl
        UserProfile.name = name
        UserProfile.gender = gender
        UserProfile.birthday = date
        UserProfile.birthdayTimestamp = cachedFormattedBirthdayDate
        UserProfile.height = height
        UserProfile.weight = weight

        logDebug(LOG_TAG, ">>> userProfile: " + UserProfile + ", element name: " + UserProfile.name)
        return UserProfile
    }

    /**
     * set whether to show the modal corresponding to the key parameter.
     * @param {string} key
     * @param {boolean} visible
     */
    setModalVisibilty = (key, visible) => {
        switch (key) {
            case KEY_MODAL_PHOTO_VISIBLE:
                setModalPhotoVisible(visible)
                break
            case KEY_MODAL_DATE_PICKER_VISIBLE:
                setModalDatePickerVisible(visible)
                break
        }
        BackHandler.removeEventListener('hardwareBackPress', handleBackButtonClick)
    }

    /**
     * handle state' change of name, height and weight.
     */
    onChangeState = (key, value) => {
        switch (key) {
            case KEY_NAME_STATE:
                setName(value)
                break
            case KEY_HEIGHT_STATE:
                setHeight(value)
                break
            case KEY_WEIGHT_STATE:
                setWeight(value)
                break
            case KEY_GENDER_STATE:
                setGender(value)
                break
        }
    }

    /**
     * handle the event that occurs when 'Done' button is pressed in birthday modal dialog.
     */
    onSelectDateOfBirthday = (date, timestamp) => {
        logDebug(LOG_TAG, "<<< selected birthday date value: " + date + ", timestamp: " + timestamp)
        setDate(formatDateToString(date))
    }

    /**
     * request camera permission and execute camera if permission is granted.
     */
    takePhotoAction = async () => {
        setModalVisibilty(KEY_MODAL_PHOTO_VISIBLE, false)
        executeCheckCameraPermission().then((accepted) => {
            if (accepted) {
                logDebug(LOG_TAG, "<<< camera permission is granted")
                launchCamera(imageOptions, (response) => {
                    logDebug(LOG_TAG, "<<< photoFileName: " + response.fileName
                        + ", photoUri: " + response.uri
                        + ", photoType: " + response.type)

                    if (response.uri != null) {
                        setPhotoUrl(response.uri)
                        setModalVisibilty(KEY_MODAL_PHOTO_VISIBLE, false)

                    } else {
                        outputErrorLog(LOG_TAG, "response uri is null")
                    }
                })

            } else {
                outputErrorLog(LOG_TAG, "camera permission is NOT granted")
            }

        }).catch((e) => {
            outputErrorLog(LOG_TAG, e + "occurred by executeCheckCameraPermission")
        })
    }

    /**
     * launch image libary.
     */
    selectGallery = () => {
        setModalVisibilty(KEY_MODAL_PHOTO_VISIBLE, false)
        launchImageLibrary(imageOptions, (response) => {
            logDebug(LOG_TAG, "<<< photoFileName: " + response.fileName
                + ", photoUri: " + response.uri
                + ", photoType: " + response.type)

            if (response.uri != null) {
                setPhotoUrl(response.uri)

            } else {
                outputErrorLog(LOG_TAG, "<<< response uri is null")
            }
        })
    }

    /**
     * determine if 'Done' button can be enabled or not.
     */
    disableDoneButton = () => {
        return emptyField(name)
            || emptyField(getOnlyNumber(height))
            || emptyField(getOnlyNumber(weight))
            || emptyField(date)
    }

    return (
        <ProfileComponent
            modalPhotoVisible={modalPhotoVisible}
            modalDatePickerVisible={modalDatePickerVisible}
            setModalVisibilty={setModalVisibilty}
            gender={gender}
            photoUrl={photoUrl}
            date={date}
            disableDoneButton={disableDoneButton}
            onClickDoneButton={onClickDoneButton}
            onSelectDateOfBirthday={onSelectDateOfBirthday}
            selectGallery={selectGallery}
            takePhotoAction={takePhotoAction}
            onChangeState={onChangeState}
            onHandleFormatBirthdayDate={onHandleFormatBirthdayDate}
        />
    )
}