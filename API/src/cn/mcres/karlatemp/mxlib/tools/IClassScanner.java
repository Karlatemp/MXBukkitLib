package cn.mcres.karlatemp.mxlib.tools;

import cn.mcres.karlatemp.mxlib.exceptions.ScanException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URL;
import java.util.List;

public interface IClassScanner {
    @NotNull
    List<String> scan(@NotNull File file, @NotNull List<String> list) throws ScanException;

    @NotNull
    List<String> scan(@NotNull Class c, @NotNull List<String> list) throws ScanException;

    @NotNull
    List<String> scan(@NotNull URL url, @NotNull List<String> list) throws ScanException;
}
