/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: LineReader.java@author: karlatemp@vip.qq.com: 2020/1/13 下午11:30@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.common.class_info.mapping;

import java.util.Scanner;

public class LineReader {
    private Scanner scan;
    private String current;

    public String next() {
        if (current != null) {
            var c = current;
            current = null;
            return c;
        }
        while (scan.hasNextLine()) {
            var l = scan.nextLine();
            var trim = l.trim();
            if (trim.isEmpty()) continue;
            if (trim.charAt(0) == '#') continue;
            return l;
        }
        return null;
    }

    public Scanner scan() {
        return scan;
    }

    public void scan(Scanner scan) {
        this.scan = scan;
    }

    public void current(String c) {
        current = c;
    }

    public LineReader(Scanner scan) {
        this.scan = scan;
    }
}
