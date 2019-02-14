-injars dist\MXBukkitLib.jar
-outjars dist\MXBukkitLib-out.jar

-libraryjars 'C:\Program Files\Java\jdk1.8.0_181\jre\lib\rt.jar'
-libraryjars 'C:\Users\32798\Documents\NetBeansProjects\MXBukkitLib-Pay\static'
-libraryjars 'G:\Java\spigot-1.13.2.jar'
-libraryjars 'G:\Java\spigot-1.12.2.jar'
-libraryjars 'G:\Java\bukkit 1.8.jar'

-skipnonpubliclibraryclasses
-dontshrink
-dontoptimize
-optimizations !class/*,method/marking/synchronized,!method/marking/*
-keepattributes *Annotations*,*Annotation*,Exceptions,Innerclasses,Signature,Deprecated
-keepparameternames
-dontnote
-dontwarn



-keep,includedescriptorclasses public class ** {
    public <fields>;
    public <methods>;
    protected <methods>;
}

-keep class * extends org.bukkit.plugin.java.JavaPlugin {
    <fields>;
    <methods>;
}

-keep class **.package-info {
    <fields>;
    <methods>;
}

-keep,allowshrinking class javassist.** {
    <fields>;
    <methods>;
}

# Keep - Native method names. Keep all native class/method names.
-keepclasseswithmembers,includedescriptorclasses,allowshrinking class * {
    native <methods>;
}

# Keep - _class method names. Keep all .class method names. This may be
# useful for libraries that will be obfuscated again with different obfuscators.
-keepclassmembers,allowshrinking class * {
    java.lang.Class class$(java.lang.String);
    java.lang.Class class$(java.lang.String,boolean);
}
