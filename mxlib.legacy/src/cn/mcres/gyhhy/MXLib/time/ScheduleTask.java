/*
 * Copyright (c) 2018-2019 Karlatemp. All rights reserved.
 * Reserved.FileName: ScheduleTask.java@author: karlatemp@vip.qq.com: 19-9-18 下午5:54@version: 2.0
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.mcres.gyhhy.MXLib.time;

import cn.mcres.gyhhy.MXLib.StringHelper;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.function.Predicate;

/**
 *
 * @author Administrator
 */
public class ScheduleTask {
    private Runnable r;

    public void setRunnable(Runnable run) {
        this.r = run;
    }

    public Runnable getRunnable() {
        return r;
    }

    public void run() {
        if (this.r != null) {
            this.r.run();
        }
    }

    public String toString() {
        return StringHelper.variable(
                "{0}[r={1}, {2}/{3}/{4} {5}:{6}:{7} /={8}]",
                new Object[]{
                    getClass().getSimpleName(),
                    r,
                    year,
                    month,
                    day,
                    hour,
                    min,
                    s,
                    date
                });
    }

    public static class Data {
        public static Runnable shutdown = () -> {
            System.exit(0);
        };

        public static String getString(Map map, String key, String def) {
            if (map == null) {
                return def;
            }
            Object o = map.get(key);
            return o instanceof String ? (String) o : def;
        }

        public static String getString(Map map, String key) {
            if (map == null) {
                return "";
            }
            Object o = map.get(key);
            return o == null ? "" : String.valueOf(o);
        }

        public static int getInt(Map map, String key) {
            if (map == null) {
                return -1;
            }
            Object o = map.get(key);
            if (o instanceof Integer) {
                return (Integer) o;
            }
            if (o instanceof Double) {
                return (int) (double) (Double) o;
            }
            if (o instanceof Short) {
                return (Short) o;
            }
            if (o instanceof Long) {
                return (int) (long) (Long) o;
            }
            if (o instanceof String) {
                try {
                    return Integer.parseInt(((String) o).trim());
                } catch (NumberFormatException e) {
                }
            }
            return -1;
        }

        public static <T> T getCustom(Map map, String custom, Class<T> c) {
            Object data = map.get(custom);
            if (data != null) {
                Class x = data.getClass();
                if (x == c) {
                    return c.cast(data);
                } else if (c.isAssignableFrom(x)) {
                    return c.cast(data);
                }
            }
            return null;
        }
    }
    private int hour = -1, min = -1, s = -1, date = -1, day = -1, month = -1, year = -1;
    private Predicate<ScheduleTask> custom = null;
    private static final Date $date = new Date();
    private ZoneId zone = TimeZone.getDefault().toZoneId();

    public boolean test() {
        if (custom != null) {
            if (!custom.test(this)) {
                return false;
            }
        }
        return true;
    }

    private ScheduleTask() {
    }

    public static ScheduleTask create(HashMap map) {
        return create(map, Testable.t);
    }

    public Predicate<ScheduleTask> getCustom() {
        return custom;
    }

    private static class Testable implements Predicate<ScheduleTask> {

        private static final Testable t = new Testable();

        @Override
        public boolean test(ScheduleTask t) {
            $date.setTime(System.currentTimeMillis());
            ZonedDateTime ti = $date.toInstant().atZone(t.zone);
            if (t.hour != -1) {
                if (ti.getHour() != t.hour) {
                    return false;
                }
            }
            if (t.min != -1) {
                if (ti.getMinute() != t.min) {
                    return false;
                }
            }
            if (t.s != -1) {
                if (ti.getSecond() != t.s) {
                    return false;
                }
            }
            if (t.date != -1) {
                if (ti.getDayOfMonth() != t.date) {
                    return false;
                }
            }
            if (t.day != -1) {
                if (ti.getDayOfWeek().getValue() != t.day) {
                    return false;
                }
            }
            if (t.month != -1) {
                if (ti.getMonthValue() != t.month) {
                    return false;
                }
            }
            if (t.year != -1) {
                if (ti.getYear() != t.year) {
                    return false;
                }
            }
            return true;
        }
    }

    public ScheduleTask setCustom(Predicate<ScheduleTask> c) {
        custom = c;
        return this;
    }

    ;
    public static ScheduleTask create(HashMap map, Predicate<ScheduleTask> run) {
        ScheduleTask task = new ScheduleTask();
        String call = Data.getCustom(map, "code", String.class);
        Runnable r = null;
        if (call != null) {
            switch (call.toLowerCase()) {
                case "shutdown": {
                    r = Data.shutdown;
                    break;
                }
            }
        }
        task.r = r;
        task.date = Data.getInt(map, "date");
        task.day = Data.getInt(map, "day");
        task.hour = Data.getInt(map, "hour");
        task.min = Data.getInt(map, "min");
        task.month = Data.getInt(map, "month");
        task.s = Data.getInt(map, "s");
        task.year = Data.getInt(map, "year");
        if (run == null) {
            run = Testable.t;
        }
        task.custom = Data.getCustom(map, "custom", Predicate.class);
        if (task.custom == null) {
            task.custom = run;
        }
        String zone = Data.getCustom(map, "zone", String.class);
        if (zone != null) {
            task.zone = TimeZone.getTimeZone(zone).toZoneId();
        }
        return task;
    }
}
