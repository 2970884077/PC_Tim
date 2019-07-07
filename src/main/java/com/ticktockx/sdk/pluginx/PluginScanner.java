package com.ticktockx.sdk.pluginx;

import com.ticktockx.sdk.API;
import com.ticktockx.sdk.Plugin;

public interface PluginScanner {

    Plugin scan(API api) throws Exception;
}
