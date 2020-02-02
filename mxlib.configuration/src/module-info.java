/**
 * The MXLib Configuration module.
 *
 * <pre>{@code
 *      Configuration config = new YamlConfiguration();
 *      config.load(new File("plugin/MyPlugin/config.yml"));
 *      System.out.println(config.value("key"));
 *      System.out.println(config.value("split.key"));
 * }</pre>
 *
 * @since 2.12
 */
module mxlib.configuration {
    requires mxlib.api;
    requires org.yaml.snakeyaml;
    requires org.jetbrains.annotations;
    requires com.google.common;
    requires com.google.gson;
    exports cn.mcres.karlatemp.mxlib.config;
}