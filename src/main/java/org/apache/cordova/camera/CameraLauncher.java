//start - license
/*
 * Copyright (c) 2025 Ashera Cordova
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */
//end - license
package org.apache.cordova.camera;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

public class CameraLauncher extends CordovaPlugin {
	private CallbackContext callbackContext;
    private static final String TAKE_PICTURE_ACTION = "takePicture";

	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		this.callbackContext = callbackContext;

		if (action.equals(TAKE_PICTURE_ACTION)) {

		}
		
		com.ashera.core.FragmentManager fragmentManager = ((com.ashera.core.BaseCordovaActivity) cordova.getActivity()).getFragmentManager();
		
		WebCameraPanel panel = new WebCameraPanel(this, fragmentManager.getRootComposite());
		panel.start();
		fragmentManager.pushNativeWidget(panel.getPanelComposite());
        PluginResult r = new PluginResult(PluginResult.Status.NO_RESULT);
        r.setKeepCallback(true);
        callbackContext.sendPluginResult(r);
        return true;
	}

	public void resultCallBack(String imgToBase64String) {	
		com.ashera.widget.PluginInvoker.runOnMainThread(() -> {
			com.ashera.core.FragmentManager fragmentManager = ((com.ashera.core.BaseCordovaActivity) cordova.getActivity()).getFragmentManager();
			fragmentManager.popBackStack();
			this.callbackContext.success(imgToBase64String);
		});
	}
}
