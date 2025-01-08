# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
# Keep the main activity and its methods from being obfuscated
-keep class com.example.antoanbmtt.MainActivity { *; }

# Keep the classes used for reflection (such as data binding, Gson, etc.)
-keep class * extends android.os.Parcelable { *; }
-keep class * implements java.io.Serializable { *; }

# Keep the classes annotated with @Keep annotation (useful for libraries like Room, Retrofit, etc.)
-keep @interface androidx.annotation.Keep

# Keep Gson-related classes if you're using Gson for serialization
-keep class com.google.gson.** { *; }

# If you're using Retrofit, keep the Retrofit service classes and any models used in the API calls
-keep class com.example.antoanbmtt.network.** { *; }
-keep class com.example.antoanbmtt.model.** { *; }

# Keep all classes inside a specific package (e.g., if you have certain models or helper classes you want to preserve)
-keep class com.example.antoanbmtt.helper.** { *; }

# If you're using Firebase or other libraries that use reflection, you may need to keep those classes
-keep class com.google.firebase.** { *; }
-keep class com.google.firebase.auth.** { *; }

# Keep all enums, since they are usually used in the code and might be stripped if obfuscated
-keep class * extends java.lang.Enum { *; }

# Keep the classes used for the Android data binding feature (if using data binding)
-keep class androidx.databinding.** { *; }

# Keep classes annotated with @Entity and their fields (if you're using Room for database storage)
-keep class androidx.room.** { *; }
-keep class com.example.antoanbmtt.database.** { *; }

# Keep the custom classes for Android's view binding (if you're using view binding)
-keep class com.example.antoanbmtt.databinding.** { *; }

# Keep the Retrofit interface (if you use it for API calls)
-keep interface com.example.antoanbmtt.network.** { *; }

# Add any other specific libraries you are using (e.g., Crashlytics, third-party libraries, etc.)
-keep class com.google.firebase.crashlytics.** { *; }

# Obfuscate the rest of the code
# These rules will obfuscate the rest of the code, but leave your important parts intact
-dontwarn com.example.antoanbmtt.**
