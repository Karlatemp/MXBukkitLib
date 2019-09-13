package cn.mcres.gyhhy.MXLib.fcs;

import java.io.IOException;

public interface TrIv<T, R> {

    R run(T tt) throws IOException;
}
