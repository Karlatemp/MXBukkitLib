open module mxlib.core {
    exports cn.mcres.karlatemp.mxlib.shared;
    requires mxlib.api;
    requires JetBrains.Java.Annotations;
    requires org.javassist;
    requires java.instrument;
    requires java.logging;
    requires com.google.gson;
    provides cn.mcres.karlatemp.mxlib.MXLibBootProvider with
            cn.mcres.karlatemp.mxlib.shared.SharedMXLibBootProvider,
            cn.mcres.karlatemp.mxlib.shared.SharedMXLibBootProvider.AutoConfigurationProcessor;
}