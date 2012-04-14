/*
 * Software License Agreement (BSD License)
 *
 * Copyright (c) 2011, Willow Garage, Inc.
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  * Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above
 *    copyright notice, this list of conditions and the following
 *    disclaimer in the documentation and/or other materials provided
 *    with the distribution.
 *  * Neither the name of Willow Garage, Inc. nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *    
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.ros.android.app_chooser;

import android.content.Intent;

import java.util.HashMap;
import java.util.List;
import java.lang.String;
import android.content.Context;
import android.util.Log;

import org.ros.message.app_manager.ClientApp;
import org.ros.message.app_manager.KeyValue;

/**
 * Convenience class which populates HashMaps with manager_data and app_data
 * from the corresponding KeyValue arrays in the ClientApp message.
 */
public class ClientAppData {
  public HashMap<String, String> managerData;
  public List<KeyValue> appData;

  public ClientAppData(ClientApp clientApp) {
    managerData = keyValueListToMap(clientApp.manager_data);
    appData = clientApp.app_data;
  }

  public Intent createIntent(Context context, boolean getPackageIntent) {
    Intent intent = new Intent();

    // Set up standard intent fields.
    if( managerData.get("intent-action" ) != null ) {
     /* if (!start) {
        intent.setAction(managerData.get("intent-action"));
        intent.putExtra("stop", "stop");
        return intent;
      }
     */

      //intent.setAction(managerData.get("intent-action"));
      if (getPackageIntent) {
      String s = managerData.get("intent-action");
      String delimiter = "\\.";
      String str = "";
      String package_array[] = s.split(delimiter);
      Log.i("ClientAppData", "intent-action is: " + s);
      Log.i("ClientAppData", "package_array is: " + package_array.length);
      for (int i = 0; i < 2; i++) {
        str += (package_array[i] + ".");
      }
      str += package_array[2];
      Log.i("ClientAppData", "str is: " + str);

      
        intent = context.getPackageManager().getLaunchIntentForPackage(str);
      } else { 
        Log.i("ClientAppData", "Second try!");        
        intent = new Intent();
        intent.setAction(managerData.get("intent-action"));
     }
     if (intent == null) {
               Log.i("ClientAppData", "Null intent");
        intent = new Intent();
        intent.setAction(managerData.get("intent-action"));
     }
        //intent.addCategory("android.intent.category.LAUNCHER");
        //intent.addCategory("android.intent.category.DEFAULT");
      //if (!start) {
      //intent.putExtra("stop", "stop");
      //return intent;
      //}     
      //intent.setAction("android.intent.action.MAIN");
    }
    if( managerData.get("intent-category") != null ) {
      intent.addCategory(managerData.get("intent-category"));
    } else {
      //intent.addCategory("android.intent.category.LAUNCHER");
      //intent.addCategory("android.intent.category.DEFAULT");
    }
    if( managerData.get("intent-type") != null ) {
      intent.setType(managerData.get("intent-type"));
    }

    // Can we handle classname and package name?

    // Copy all app data to "extra" data in the intent.
    for (int i = 0; i < appData.size(); i++) {
      KeyValue kv = appData.get(i);
      intent.putExtra(kv.key, kv.value);
    }

    return intent;
  }

  private HashMap<String, String> keyValueListToMap(List<KeyValue> kvl) {
    HashMap<String, String> map = new HashMap<String, String>();
    for (int i = 0; i < kvl.size(); i++) {
      KeyValue kv = kvl.get(i);
      map.put(kv.key, kv.value);
    }
    return map;
  }
}
