import React, { useState, useLayoutEffect } from 'react'
import { Strings } from '../../../../utils/theme'
import { BackHandler } from 'react-native'
import { launchImageLibrary, launchCamera } from 'react-native-image-picker'
import { emptyField, getOnlyNumber, imageOptions } from "../../../../utils/common/CommonUtil"
import { requestCameraPermission } from '../../../../utils/permission/PermissionUtil'
import Constants from '../../../../utils/Constants'
import SetProfileInfoUseCase from '../../../../domain/usecases/common/SetProfileInfoUseCase'
import GetProfileInfoUseCase from '../../../../domain/usecases/common/GetProfileInfoUseCase'
import { logDebug, logDebugWithLine, outputErrorLog } from '../../../../utils/logger/Logger'
import EditProfileComponent from './EditProfileComponent'
import { UserProfile } from '../../../../domain/entities/user/UserProfile'
import { formatDateToString } from '../../../../utils/time/TimeUtil'

/**
 * flag to import profile information only once after entering the profile editing screen.
 */
let isProfileInfoImported = false

const REGISTER_USER_STRINGS = Strings.registerSenior
const LOG_TAG = Constants.LOG.EDIT_PROFILE_UI_LOG

/**
 * modal related useState keys.
 * the following key values are used in both the profile container and the component.
 */
export const KEY_MODAL_PHOTO_VISIBLE = "modalPhotoVisible"
export const KEY_MODAL_DATE_PICKER_VISIBLE = "modalDatePickerVisible"

/**
 * profile registration screen.
 * @param {Any} props 
 * @returns {JSX.Element}
 */
export default function EditProfileContainer({ navigation }) {

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
    const { executeGetProfileInfoUseCase } = GetProfileInfoUseCase()

    /**
     * handle back button click event. (android)
     * @returns {boolean}
     */
    const handleBackButtonClick = () => {
        handleNavigationPop()
        return true
    }

    /** 
     * handle the event that occurs when 'Done' button is pressed.
     */
    onClickDoneButton = () => {
        executeSetProfileInfoUseCase(getUserProfileInfo(), (succeeded) => {
            if (succeeded) {
                this.handleNavigationPop()

            } else {
                outputErrorLog(LOG_TAG, "failed to edit profile")
            }
        })
    }

    /**
     * handle header's back icon press event.
     */
    const onPressHeaderBackIcon = () => {
        logDebug(LOG_TAG, "<<< header back icon is pressed")
        this.handleNavigationPop()
    }

    /**
     * handle navigation's pop operation.
     */
    handleNavigationPop = () => {
        this.enableBackHandler(false)
        isProfileInfoImported = false
        navigation.pop()
    }

    /**
     * get user profile information.
     */
    getUserProfileInfo = () => {
        UserProfile.imageUrl = photoUrl
        UserProfile.name = name
        UserProfile.gender = gender
        UserProfile.birthday = date
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
        this.enableBackHandler(false)
    }

    /**
     * handle state' change of name, height and weight.
     */
    onChangeState = (key, value) => {
        switch (key) {
            case "name":
                setName(value)
                break
            case "height":
                setHeight(value)
                break
            case "weight":
                setWeight(value)
                break
            case "gender":
                setGender(value)
                break
        }
    }

    /**
     * handle the event that occurs when 'Done' button is pressed in birthday modal.
     */
    onSelectDateOfBirthday = (date) => {
        setDate(formatDateToString(date))
    }

    /**
     * request camera permission and execute camera if permission is granted.
     */
    takePhotoAction = async () => {
        setModalVisibilty(KEY_MODAL_PHOTO_VISIBLE, false)
        try {
            if (requestCameraPermission()) {
                logDebug(LOG_TAG, "<<< camera permission given")
                launchCamera(imageOptions, (response) => {
                    logDebug(LOG_TAG, "<<< photoFileName: " + response.fileName
                        + ", photoUri: " + response.uri
                        + ", photoType: " + response.type)

                    if (response.uri != null) {
                        setPhotoUrl(response.uri)
                        setModalVisibilty(KEY_MODAL_PHOTO_VISIBLE, false)

                    } else {
                        outputErrorLog(LOG_TAG, "<<< response uri is null")
                    }
                })
            }
        } catch (e) {
            outputErrorLog(LOG_TAG, e)
        }
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

    /**
     * get user profile information and update them for rendering ui.
     */
    loadUserProfile = () => {
        executeGetProfileInfoUseCase(userProfileInfo => {
            const userProfile = JSON.parse(userProfileInfo)
            const userImageUrl = userProfile.imageUrl

            logDebugWithLine(LOG_TAG, "userName: " + userProfile.name
                + ", userImageUrl: " + userImageUrl
                + ", userGender: " + userProfile.gender
                + ", userBirthDay: " + userProfile.birthday
                + ", userBirthDayTimestamp: " + userProfile.birthdayTimestamp
                + ", userHeight: " + userProfile.height
                + ", userWeight: " + userProfile.weight)

            setName(userProfile.name)
            setPhotoUrl(userImageUrl)
            setGender(userProfile.gender)
            setDate(userImageUrl.birthdayTimestamp)
            setHeight(userProfile.height)
            setWeight(userProfile.weight)

            /*--------------------------------------------------------------------
             * Refs. delivered data information.
             * userName: koooooooo, 
             * userImageUrl: file:///data/user/0/com.react_native/cache/...jpg, 
             * userGender: 0, 
             * userBirthDay: 2022.11.14, 
             * userBirthDayTimestamp: 2022-11-14T04:52:34.130Z, 
             * userHeight: 586, 
             * userWeight: 36
             *--------------------------------------------------------------------*/
            isProfileInfoImported = true
        })
    }

    /**
     * enable or disable back handler.
     */
    enableBackHandler = (enabled) => {
        if (enabled) {
            BackHandler.addEventListener('hardwareBackPress', handleBackButtonClick)
        } else {
            BackHandler.removeEventListener('hardwareBackPress', handleBackButtonClick)
        }
    }

    /**
     * executed logic before ui rendering and painting.
     */
    useLayoutEffect(() => {
        if (isProfileInfoImported) {
            return
        }
        this.loadUserProfile()
        this.enableBackHandler(true)
    }, [isProfileInfoImported])

    return (
        <EditProfileComponent
            modalPhotoVisible={modalPhotoVisible}
            modalDatePickerVisible={modalDatePickerVisible}
            setModalVisibilty={setModalVisibilty}
            photoUrl={photoUrl}
            name={name}
            gender={gender}
            date={date}
            height={height}
            weight={weight}
            disableDoneButton={disableDoneButton}
            onClickDoneButton={onClickDoneButton}
            onSelectDateOfBirthday={onSelectDateOfBirthday}
            selectGallery={selectGallery}
            takePhotoAction={takePhotoAction}
            onChangeState={onChangeState}
            onPressHeaderBackIcon={onPressHeaderBackIcon}
        />
    )
}