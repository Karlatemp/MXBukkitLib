/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: Depend.java@author: karlatemp@vip.qq.com: 19-9-26 下午10:38@version: 2.0
 */

package cn.mcres.karlatemp.mxlib.annotations;

import java.lang.annotation.*;

/**
 * The depend system use.
 * If the condition is met then the class will load.
 *
 * @since 2.2
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Depend {
    /**
     * The name of depend.
     *
     * @return The name.
     */
    String value();

    String version() default "";

    /**
     * The compare type will use.
     * <table>
     * <tr><td>type</td><td>action</td></tr>
     * <tr><td>-1</td><td>Requires dependencies before this release(including this release)</td></tr>
     * <tr><td>-2</td><td>Requires dependencies before this release (not including this version)</td></tr>
     * <tr><td>0</td><td>Requires dependencies with this release (only this version)</td></tr>
     * <tr><td>1</td><td>Requires dependencies after this release(including this release)</td></tr>
     * <tr><td>2</td><td>Requires dependencies after this release(not including this release)</td></tr>
     * </table>
     *
     * @return The compare type
     */
    int compare() default 0;

}
