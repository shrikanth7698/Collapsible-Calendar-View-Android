# Collapsible-Calendar-View-Android
[![forthebadge](https://forthebadge.com/images/badges/built-with-love.svg)](https://forthebadge.com)

Collapsible CalendarView is a simple calendar view which can be collapsed and expanded to save space.

Still in Active development.

<img src="https://drive.google.com/uc?id=1tOocqnS0pI6V07-JROte0Rx7vqMqFmPg" width="300" >

[![Android Arsenal]( https://img.shields.io/badge/Android%20Arsenal-Collapsible--Calendar--View--Android-green.svg?style=flat )]( https://android-arsenal.com/details/1/6829 )

### Version
[![](https://jitpack.io/v/shrikanth7698/Collapsible-Calendar-View-Android.svg)](https://jitpack.io/#shrikanth7698/Collapsible-Calendar-View-Android)

### Installation

* **Gradle**

	Add it in your root build.gradle at the end of repositories:
	```gradle
	allprojects {
			repositories {
				...
				maven { url 'https://jitpack.io' }
			}
		}
	```

	Add the dependency in your app build.gradle
	```gradle
	dependencies {
			compile 'com.github.shrikanth7698:Collapsible-Calendar-View-Android:v0.0.1'
		}
	```

* **Maven**

	Add the JitPack repository to your build file
	```gradle
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
	```

	Add the dependency
	```gradle
	<dependency>
	    <groupId>com.github.User</groupId>
	    <artifactId>Repo</artifactId>
	    <version>Tag</version>
	</dependency>
	```



### Usage

Drop the Collapsible CalendarView in your XML layout as is shown below:
```xml
    <com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar
          android:layout_width="match_parent"
          android:layout_height="wrap_content"
          android:id="@+id/calendarView">
    </com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar>
```

And then in your Activity or fragment
```java
final CollapsibleCalendar collapsibleCalendar = findViewById(R.id.calendarView);
        collapsibleCalendar.setCalendarListener(new CollapsibleCalendar.CalendarListener() {
            @Override
            public void onDaySelect() {
                Day day = viewCalendar.getSelectedDay();
                Log.i(getClass().getName(), "Selected Day: "
                        + day.getYear() + "/" + (day.getMonth() + 1) + "/" + day.getDay());
            }

            @Override
            public void onItemClick(View view) {

            }

            @Override
            public void onDataUpdate() {

            }

            @Override
            public void onMonthChange() {

            }

            @Override
            public void onWeekChange(int i) {

            }
        });
```

### Customization


```xml
           <com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                app:primaryColor="@color/google_red"
                app:textColor="@android:color/white"
                app:selectedItem_textColor="@color/google_red"
                app:todayItem_textColor="@android:color/white"
                app:todayItem_background="@drawable/circle_white_stroke_background"
                app:selectedItem_background="@drawable/circle_white_solid_background"
                app:buttonLeft_drawableTintColor="@android:color/white"
                app:buttonRight_drawableTintColor="@android:color/white"
                app:expandIconColor="@android:color/white">
          </com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar>
```



### License
```
MIT License

Copyright (c) 2018 Shrikanth Ravi

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```


