import { RecoilRoot } from 'recoil'
import { LogBox } from "react-native"
import { NavigationContainer } from '@react-navigation/native'
import { createNativeStackNavigator } from '@react-navigation/native-stack'

import SplashContainer from './presentation/containers/splash/SplashContainer'
import ProfileContainer from './presentation/containers/profile/ProfileContainer'
import QrScanContainer from './presentation/containers/device/QrScanContainer'
import BluetoothContainer from './presentation/containers/bluetooth/BluetoothContainer'
import HomeContainer from './presentation/containers/home/HomeContainer'
import SettingsContainer from './presentation/containers/settings/SettingsContainer'
import SoftwareUpdateContainer from './presentation/containers/settings/device/software_update/SoftwareUpdateContainer'
import EditProfileContainer from './presentation/containers/settings/edit_profile/EditProfileContainer'
import HrMonitoringSettingContainer from './presentation/containers/settings/hr_monitoring/HrMonitoringSettingContainer'
import DeviceInfoContainer from './presentation/containers/settings/device/DeviceInfoContainer'
import DeviveStatusContainer from './presentation/containers/home/device_status/DeviceStatusContainer'

import HiddenHomeContainer from './test/hidden_menu/home/HiddenHomeContainer'
import HiddenSplashContainer from './test/hidden_menu/splash/HiddenSplashContainer'
import HiddenBluetoothContainer from './test/hidden_menu/home/bluetooth/HiddenBluetoothContainer'
import HiddenCommonContainer from './test/hidden_menu/home/common/HiddenCommonContainer'
import HiddenPlatformContainer from './test/hidden_menu/home/platform/HiddePlatformContainer'
import HiddenServerContainer from './test/hidden_menu/home/server/HiddenServerContainer'

import Constants from './utils/Constants'
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs'
import { Image } from 'react-native'
import { Colors, Strings, Images } from './utils/theme'
import styles from './presentation/stylesheets/StyleSet'
import DisconnectionContainer from './presentation/containers/settings/device/disconnection/DisconnectionContainer'
import { createDrawerNavigator } from '@react-navigation/drawer'

LogBox.ignoreAllLogs()


/**
 * bottom tab navigation's screen display name.
 */
const strings = Strings.home.bottomTabBar

/**
 * feature screens.
 */
const SPLASH_SCREEN = Constants.SCREEN.SPLASH
const PROFILE_SCREEN = Constants.SCREEN.PROFILE
const QR_SCAN_SCREEN = Constants.SCREEN.QR_SCAN
const BLUETOOTH_SCREEN = Constants.SCREEN.BLUETOOTH
const HOME_SCREEN = Constants.SCREEN.HOME
const HOME_BOTTOM_TAB_SCREEN = Constants.SCREEN.HOME_BOTTOM_TAB_SCREEN
const DEVICE_STATUS_SCREEN = Constants.SCREEN.DEVICE_STATUS
const SETTINGS_SCREEN = Constants.SCREEN.SETTINGS
const SW_UPDATE_SCREEN = Constants.SCREEN.SW_UPDATE
const EDIT_PROFILE_SCREEN = Constants.SCREEN.EDIT_PROFILE
const HR_MONITORING_SCREEN = Constants.SCREEN.HR_MONITORING
const DEVICE_INFO_SCREEN = Constants.SCREEN.DEVICE_INFO
const DISCONNECTION_SCREEN = Constants.SCREEN.DISCONNECTION

/**
 * hidden feature screens.
 */
const HIDDEN_SPLASH_SCREEN = Constants.SCREEN.HIDDEN.SPLASH
const HIDDEN_HOME_SCREEN = Constants.SCREEN.HIDDEN.HOME
const HIDDEN_BLUETOOTH_SCREEN = Constants.SCREEN.HIDDEN.BLUETOOTH
const HIDDEN_PLATFORM_SCREEN = Constants.SCREEN.HIDDEN.PLATFORM
const HIDDEN_COMMON_SCREEN = Constants.SCREEN.HIDDEN.COMMON
const HIDDEN_SERVER_SCREEN = Constants.SCREEN.HIDDEN.SERVER


const Stack = createNativeStackNavigator()
const BottomTab = createBottomTabNavigator()
const DrawerStack = createDrawerNavigator()

/**
 * configure drawer navigator.
 * @returns {JSX.Element}
 */
function DrawerNavigator() {
    return (
        <Drawer.Navigator>
            <Drawer.Screen>

            </Drawer.Screen>
        </Drawer.Navigator>
    )
}

/**
 * configure bottom tab navigator.
 * @returns {JSX.Element}
 */
function BottomTabNavigator() {
    return (
        <BottomTab.Navigator
            // initial tab screen: home container.
            initialRouteName={HOME_SCREEN}
            screenOptions={{
                tabBarActiveTintColor: Colors.black,
                tabBarInactiveTintColor: Colors.slateGrey,
            }}>
            {/* home screen. */}
            <BottomTab.Screen
                name={HOME_SCREEN}
                component={HomeContainer}
                options={{
                    tabBarLabel: strings.home,
                    tabBarIcon: ({ focused }) => (
                        focused ? <Image source={Images.icHomeOn} style={styles.homeBottomTabIconMenu} />
                            : <Image source={Images.icHomeOff} style={styles.homeBottomTabIconMenu} />
                    ),
                    headerShown: false,
                    tabBarStyle: styles.homeBottomTabBar
                }}
            />

            {/* settings screen. */}
            <BottomTab.Screen
                name={SETTINGS_SCREEN}
                component={SettingsContainer}
                options={{
                    tabBarLabel: strings.setting,
                    tabBarIcon: ({ focused }) => (
                        focused ? <Image source={Images.icSettingOn} style={styles.homeBottomTabIconMenu} />
                            : <Image source={Images.icSettingOff} style={styles.homeBottomTabIconMenu} />
                    ),
                    headerShown: false,
                    tabBarStyle: styles.homeBottomTabBar
                }}
            />
        </BottomTab.Navigator>
    )
}

export default function App() {
    return (
        <>
            {/* state management tool. */}
            <RecoilRoot>
                <NavigationContainer>
                    {/* initial screen: splash container */}
                    <Stack.Navigator initialRouteName={SPLASH_SCREEN}>
                        {/* feature screen stack. */}
                        <Stack.Screen
                            name={SPLASH_SCREEN}
                            component={SplashContainer}
                            options={{ headerShown: false }} />
                        <Stack.Screen
                            name={PROFILE_SCREEN}
                            component={ProfileContainer}
                            options={{ headerShown: false }} />
                        <Stack.Screen
                            name={QR_SCAN_SCREEN}
                            component={QrScanContainer}
                            options={{ headerShown: false }} />
                        <Stack.Screen
                            name={BLUETOOTH_SCREEN}
                            component={BluetoothContainer}
                            options={{ headerShown: false }} />
                        <Stack.Screen
                            name={HOME_BOTTOM_TAB_SCREEN}
                            component={BottomTabNavigator}
                            options={{ headerShown: false }} />
                        <Stack.Screen
                            name={DEVICE_STATUS_SCREEN}
                            component={DeviveStatusContainer}
                            options={{ headerShown: false }} />
                        <Stack.Screen
                            name={SW_UPDATE_SCREEN}
                            component={SoftwareUpdateContainer}
                            options={{ headerShown: false }} />
                        <Stack.Screen
                            name={EDIT_PROFILE_SCREEN}
                            component={EditProfileContainer}
                            options={{ headerShown: false }} />
                        <Stack.Screen
                            name={HR_MONITORING_SCREEN}
                            component={HrMonitoringSettingContainer}
                            options={{ headerShown: false }} />
                        <Stack.Screen
                            name={DEVICE_INFO_SCREEN}
                            component={DeviceInfoContainer}
                            options={{ headerShown: false }} />
                        <Stack.Screen
                            name={DISCONNECTION_SCREEN}
                            component={DisconnectionContainer}
                            options={{ headerShown: false }} />

                        {/* hidden feature screen stack. */}
                        <Stack.Screen
                            name={HIDDEN_SPLASH_SCREEN}
                            component={HiddenSplashContainer}
                            options={{ headerShown: false }} />

                        <Stack.Screen name={HIDDEN_HOME_SCREEN}
                            component={HiddenHomeContainer}
                            options={{ headerShown: true }} />

                        <Stack.Screen name={HIDDEN_BLUETOOTH_SCREEN}
                            component={HiddenBluetoothContainer}
                            options={{ headerShown: true }} />

                        <Stack.Screen name={HIDDEN_COMMON_SCREEN}
                            component={HiddenCommonContainer}
                            options={{ headerShown: false }} />

                        <Stack.Screen name={HIDDEN_PLATFORM_SCREEN}
                            component={HiddenPlatformContainer}
                            options={{ headerShown: false }} />

                        <Stack.Screen name={HIDDEN_SERVER_SCREEN}
                            component={HiddenServerContainer}
                            options={{ headerShown: false }} />

                    </Stack.Navigator>
                </NavigationContainer>
            </RecoilRoot>
        </>
    )
}