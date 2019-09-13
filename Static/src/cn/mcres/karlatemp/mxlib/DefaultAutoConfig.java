package cn.mcres.karlatemp.mxlib;

import cn.mcres.karlatemp.mxlib.annotations.Bean;
import cn.mcres.karlatemp.mxlib.annotations.Configuration;
import cn.mcres.karlatemp.mxlib.bean.IEnvironmentFactory;
import cn.mcres.karlatemp.mxlib.cmd.ICommandProcessor;
import cn.mcres.karlatemp.mxlib.configuration.IConfigurationProcessor;
import cn.mcres.karlatemp.mxlib.logging.IMessageFactory;
import cn.mcres.karlatemp.mxlib.logging.MessageFactoryAnsi;
import cn.mcres.karlatemp.mxlib.tools.*;

import java.lang.invoke.MethodHandles;

@Configuration
public class DefaultAutoConfig {

    @Bean
    IConfigurationProcessor processor() {
        return new SharedConfigurationCommandProcessor();
    }

    @Bean
    ICommandProcessor commandProcessor() {
        return new SharedCommandProcessorImpl();
    }

    @Bean
    MethodHandles.Lookup lookup() {
        return Toolkit.Reflection.getRoot();
    }

    @Bean
    IParamSorter sorter() {
        return new SharedParamSorter();
    }

    @Bean
    IClassScanner cs() {
        return new SharedClassScanner();
    }

    @Bean
    IEnvironmentFactory environmentFactory() {
        return new SharedEnvironmentFactory();
    }

    @Bean
    IMessageFactory factory() {
        return new MessageFactoryAnsi();
    }
}
