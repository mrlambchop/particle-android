package io.particle.android.sdk.devicesetup.setupsteps;

import io.particle.android.sdk.devicesetup.SetupProcessException;
import io.particle.android.sdk.devicesetup.ui.DeviceSetupState;
import io.particle.android.sdk.utils.EZ;
import io.particle.android.sdk.utils.Preconditions;
import io.particle.android.sdk.utils.SSID;
import io.particle.android.sdk.utils.WifiFacade;


public class WaitForDisconnectionFromDeviceStep extends SetupStep {

    private final SSID softApName;
    private final WifiFacade wifiFacade;

    private boolean wasDisconnected = false;

    WaitForDisconnectionFromDeviceStep(StepConfig stepConfig, SSID softApSSID, WifiFacade wifiFacade) {
        super(stepConfig);
        Preconditions.checkNotNull(softApSSID, "softApSSID cannot be null.");
        this.softApName = softApSSID;
        this.wifiFacade = wifiFacade;
    }

    @Override
    public boolean isStepFulfilled() {
        return wasDisconnected;
    }

    @Override
    protected void onRunStep() throws SetupStepException, SetupProcessException {
        for (int i = 0; i <= 5; i++) {
            if (isConnectedToSoftAP()) {
                // wait and try again
                EZ.threadSleep(200);
            } else {
                //sleep after loosing the softAP network to give time to connect to the main wifi again
                EZ.threadSleep(1500);

                // success, no longer connected.
                wasDisconnected = true;

                //if we are not connected to our previous wifi network now, prompt it now
                if( !isConnectedToMainWiFi() ) {
                    wifiFacade.reenablePreviousWifi(DeviceSetupState.previouslyConnectedWifiNetwork);
                }
                return;
            }
        }

        // Still connected after the above completed: fail
        throw new SetupStepException("Not disconnected from soft AP");
    }

    private boolean isConnectedToSoftAP() {
        SSID currentlyConnectedSSID = wifiFacade.getCurrentlyConnectedSSID(false);
        SSID prevSSID = DeviceSetupState.previouslyConnectedWifiNetwork;
        log.d("Currently connected SSID: " + currentlyConnectedSSID);
        return prevSSID.equals(currentlyConnectedSSID);
    }

    private boolean isConnectedToMainWiFi() {
        SSID currentlyConnectedSSID = wifiFacade.getCurrentlyConnectedSSID(false);
        SSID prevSSID = DeviceSetupState.previouslyConnectedWifiNetwork;
        log.d("Currently connected SSID: " + currentlyConnectedSSID);
        return prevSSID.equals(currentlyConnectedSSID);
    }
}
