#CutoLoadingView
[![](https://jitpack.io/v/andyxialm/CutoLoadingView.svg)](https://jitpack.io/#andyxialm/CutoLoadingView)

A custom loading view, just like CutoWallpaper.

![](https://github.com/andyxialm/CutoLoadingView/blob/master/art/screenshot.gif?raw=true)
### Usage

##### Gradle
###### Step 1. Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:
~~~ xml
allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
~~~
###### Step 2. Add the dependency
~~~ xml
dependencies {
        compile 'com.github.andyxialm:CutoLoadingView:1.0.1'
}
~~~
	
##### Edit your layout XML:

~~~ xml
<cn.refactor.cutoloadingview.CutoLoadingView
    android:id="@+id/cuto_loading_view"
    xmlns:cuto="http://schemas.android.com/apk/res-auto"
    android:layout_width="42dp"
    android:layout_height="42dp"
    cuto:animDuration="1400"
    cuto:circleRadius="3dp"
    cuto:strokeColor="@android:color/white"
    cuto:strokeWidth="2dp"/>
~~~

### License

    Copyright 2016 andy

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.