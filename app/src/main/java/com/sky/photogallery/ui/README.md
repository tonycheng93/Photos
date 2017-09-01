### [Android RxJava 2 JUnit test - getMainLooper in android.os.Looper not mocked RuntimeException](https://stackoverflow.com/questions/43356314/android-rxjava-2-junit-test-getmainlooper-in-android-os-looper-not-mocked-runt)

```java
java.lang.ExceptionInInitializerError
    at io.reactivex.android.schedulers.AndroidSchedulers$1.call(AndroidSchedulers.java:35)
    at io.reactivex.android.schedulers.AndroidSchedulers$1.call(AndroidSchedulers.java:33)
    at io.reactivex.android.plugins.RxAndroidPlugins.callRequireNonNull(RxAndroidPlugins.java:70)
    at io.reactivex.android.plugins.RxAndroidPlugins.initMainThreadScheduler(RxAndroidPlugins.java:40)
    at io.reactivex.android.schedulers.AndroidSchedulers.<clinit>(AndroidSchedulers.java:32)
    …
Caused by: java.lang.RuntimeException: Method getMainLooper in android.os.Looper not mocked. See http://g.co/androidstudio/not-mocked for details.
    at android.os.Looper.getMainLooper(Looper.java)
    at io.reactivex.android.schedulers.AndroidSchedulers$MainHolder.<clinit>(AndroidSchedulers.java:29)
    ...


java.lang.NoClassDefFoundError: Could not initialize class io.reactivex.android.schedulers.AndroidSchedulers
    at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
    at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
    at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
    at java.lang.reflect.Method.invoke(Method.java:498)
    …
```

