package net.cybercake.cyberapi.common.basic;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * The {@link Time} class manages anything to do with time
 * @since 15
 */
public class Time {

    /**
     * Gets the current unix time with a specified {@link TimeUnit}
     * <br>
     * What is Unix time? How does it work? <a href="https://en.wikipedia.org/wiki/Unix_time">View more info by clicking here!</a>
     * @param unit what {@link TimeUnit} to convert the unix time into
     * @return the current unix time in the specified {@code unit}
     * @since 15
     */
    public static long getUnix(TimeUnit unit) {
        return (unit.convert(Duration.ofMillis(System.currentTimeMillis())));
    }

    /**
     * Gets the current unix time.
     * <br>
     * What is Unix time? How does it work? <a href="https://en.wikipedia.org/wiki/Unix_time">View more info by clicking here!</a>
     * @return the current unix time, in seconds, not milliseconds
     * @since 15
     */
    public static long getUnix() {
        return getUnix(TimeUnit.SECONDS);
    }

    /**
     * Represents one second in milliseconds
     */
    public final static long ONE_SECOND = 1000;
    /**
     * Represents one minute in milliseconds
     */
    public final static long ONE_MINUTE = ONE_SECOND * 60;
    /**
     * Represents one hour in milliseconds
     */
    public final static long ONE_HOUR = ONE_SECOND * 3600;
    /**
     * Represents on day in milliseconds
     */
    public final static long ONE_DAY = ONE_SECOND * 86400;
    /**
     * Represents one week in milliseconds
     */
    public final static long ONE_WEEK = ONE_SECOND * 604800;
    /**
     * Represents one month in milliseconds
     */
    public final static long ONE_MONTH = ONE_SECOND * 2628000;
    /**
     * Represents one year in milliseconds
     */
    public final static long ONE_YEAR = ONE_SECOND * 31560000;

    /**
     * Gets a better duration display, assumes "showAll" is true
     * @param duration the duration to display, must be a positive number
     * @return the better time display, looks like {@code "1 week, 2 days, 3 hours, 4 minutes, 5 seconds"}
     * @since 15
     */
    public static String getBetterTimeDisplay(long duration) {
        return getBetterTimeDisplay(duration, true);
    }

    /**
     * Gets a better duration display, assumes "showAll" is true
     * @param biggerDuration the duration to display, must be bigger than {@code smallerNumber}
     * @param smallerDuration the duration to display, must be smaller than {@code biggerNumber}
     * @return the better time display, looks like {@code "1 week, 2 days, 3 hours, 4 minutes, 5 seconds"}
     * @since 15
     */
    public static String getBetterTimeDisplay(long biggerDuration, long smallerDuration) {
        return getBetterTimeDisplay(biggerDuration, smallerDuration, true);
    }

    /**
     * Gets a better duration display
     * @param biggerDuration the duration to display, must be bigger than {@code smallerNumber}
     * @param smallerDuration the duration to display, must be smaller than {@code biggerNumber}
     * @param showAll whether to show all values or just the ones that aren't zero
     * @return the better time display, looks like {@code "1 week, 2 days, 3 hours, 4 minutes, 5 seconds"} <br> or if '<b>showAll</b>' is false {@code "1 week"} (just shows the biggest time value)
     * @since 15
     */
    public static String getBetterTimeDisplay(long biggerDuration, long smallerDuration, boolean showAll) {
        return getBetterTimeDisplay(biggerDuration-smallerDuration, showAll);
    }

    /**
     * Gets a better duration display
     * @param duration the duration to display, must be a positive number
     * @param showAll whether to show all values or just the ones that aren't zero
     * @return the better time display, looks like {@code "1 week, 2 days, 3 hours, 4 minutes, 5 seconds"} <br> or if '<b>showAll</b>' is false {@code "1 week"} (just shows the biggest time value)
     * @since 15
     */
    public static String getBetterTimeDisplay(long duration, boolean showAll) {
        StringBuilder durationBuilder = new StringBuilder();
        duration = duration*1000L;
        long temp;
        if (duration >= ONE_SECOND) {
            temp = duration / ONE_YEAR;
            if (temp > 0) {
                duration -= temp * ONE_YEAR;
                if(!showAll) return temp + " year" + (temp != 1 ? "s" : "");
                durationBuilder.append(temp).append(" year").append(temp != 1 ? "s" : "")
                        .append(duration >= ONE_MINUTE ? ", " : "");
            }

            temp = duration / ONE_MONTH;
            if (temp > 0) {
                duration -= temp * ONE_MONTH;
                if(!showAll) return temp + " month" + (temp != 1 ? "s" : "");
                durationBuilder.append(temp).append(" month").append(temp != 1 ? "s" : "")
                        .append(duration >= ONE_MINUTE ? ", " : "");
            }

            temp = duration / ONE_WEEK;
            if (temp > 0) {
                duration -= temp * ONE_WEEK;
                if(!showAll) return temp + " week" + (temp != 1 ? "s" : "");
                durationBuilder.append(temp).append(" week").append(temp != 1 ? "s" : "")
                        .append(duration >= ONE_MINUTE ? ", " : "");
            }

            temp = duration / ONE_DAY;
            if (temp > 0) {
                duration -= temp * ONE_DAY;
                if(!showAll) return temp + " day" + (temp != 1 ? "s" : "");
                durationBuilder.append(temp).append(" day").append(temp != 1 ? "s" : "")
                        .append(duration >= ONE_MINUTE ? ", " : "");
            }

            temp = duration / ONE_HOUR;
            if (temp > 0) {
                duration -= temp * ONE_HOUR;
                if(!showAll) return temp + " hour" + (temp != 1 ? "s" : "");
                durationBuilder.append(temp).append(" hour").append(temp != 1 ? "s" : "")
                        .append(duration >= ONE_MINUTE ? ", " : "");
            }

            temp = duration / ONE_MINUTE;
            if (temp > 0) {
                duration -= temp * ONE_MINUTE;
                if(!showAll) return temp + " minute" + (temp != 1 ? "s" : "");
                durationBuilder.append(temp).append(" minute").append(temp != 1 ? "s" : "");
            }

            if (!durationBuilder.toString().equals("") && duration >= ONE_SECOND) {
                durationBuilder.append(" and ");
            }

            temp = duration / ONE_SECOND;
            if (temp > 0) {
                durationBuilder.append(temp).append(" second").append(temp != 1 ? "s" : "");
                if(!showAll) return temp + " second" + (temp != 1 ? "s" : "");
            }
            return durationBuilder.toString();
        } else {
            return "0 seconds";
        }
    }

    /**
     * Using a {@link Time#getBetterTimeDisplay(long)}, this method will condense the display into something like "5d, 12h, 38m, 13s" ago instead of the longer form
     * @param timeDisplay the time display output from {@link Time#getBetterTimeDisplay(long)}
     * @return the condensed time display
     * @since 103
     */
    public static String condenseTimeDisplay(String timeDisplay) {
        return timeDisplay
                .replace(" and ", ", ")
                .replace(" seconds", "s")
                .replace(" second", "s")
                .replace(" minutes", "m")
                .replace(" minute", "m")
                .replace(" hours", "h")
                .replace(" hour", "h")
                .replace(" days", "d")
                .replace(" day", "d")
                .replace(" weeks", "w")
                .replace(" week", "w")
                .replace(" months", "mo")
                .replace(" month", "mo")
                .replace(" years", "yr")
                .replace(" year", "yr");
    }

    /**
     * Gets the difference between two {@link Date}s, returns a {@link Map} of {@link TimeUnit}s and {@link Long}s
     * @param biggerDate the bigger date, closer to the most recent time
     * @param smallerDate the smaller date, the furthest away from the most recent time
     * @return the {@link Map} of {@link TimeUnit}'s and their distance from the {@link Date}
     * @since 15
     */
    public static Map<TimeUnit,Long> getDateDifference(Date biggerDate, Date smallerDate) {
        long diffInSeconds = biggerDate.getTime()-smallerDate.getTime();

        //create the list
        List<TimeUnit> units = new ArrayList<>(EnumSet.allOf(TimeUnit.class));
        Collections.reverse(units);

        //create the result map of TimeUnit and difference
        Map<TimeUnit,Long> result = new LinkedHashMap<>();
        long secondsRest = diffInSeconds;

        for ( TimeUnit unit : units ) {

            //calculate difference in millisecond
            long diff = unit.convert(secondsRest,TimeUnit.SECONDS);
            long diffInSecondsForUnit = unit.toSeconds(diff);
            secondsRest = secondsRest - diffInSecondsForUnit;

            //put the result in the map
            result.put(unit,diff);
        }

        return result;
    }

    /**
     * Gets the current time and formats it using the {@link SimpleDateFormat}
     * @param pattern the pattern used by <a href="https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html">SimpleDateFormat</a>
     * @return the formatted date
     * @apiNote you can view {@link SimpleDateFormat}'s formats <a href="https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html">by clicking here</a>
     * @see SimpleDateFormat
     * @since 15
     */
    public static String getFormattedDate(String pattern) {
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat(pattern);
        return ft.format(dNow);
    }

    /**
     * Gets the current time and formats it using the {@link SimpleDateFormat} after using {@link ZoneOffset} and {@link OffsetDateTime} to offset the current time by a set amount
     * @param pattern the pattern used by <a href="https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html">SimpleDateFormat</a>
     * @param zoneOffset the time offset, see {@link ZoneOffset}
     * @return the formatted date
     * @apiNote you can view {@link SimpleDateFormat}'s formats <a href="https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html">by clicking here</a>
     * @see SimpleDateFormat
     * @see ZoneOffset#of(String)
     * @since 15
     */
    public static String getFormattedDate(String pattern, ZoneOffset zoneOffset) {
        OffsetDateTime offset = OffsetDateTime.now(zoneOffset);
        Date date = new Date(offset.toInstant().toEpochMilli());
        SimpleDateFormat ft = new SimpleDateFormat(pattern);
        return ft.format(date);
    }

    /**
     * Gets the current time and formats it using the {@link SimpleDateFormat} after using {@link ZoneOffset} and {@link OffsetDateTime} to offset the current time by a set amount of hours
     * @param pattern the pattern used by <a href="https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html">SimpleDateFormat</a>
     * @param timeOffset the time offset in GMT standards (example: -5 for EST, -4 for EDT)
     * @return the formatted date
     * @apiNote you can view {@link SimpleDateFormat}'s formats <a href="https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html">by clicking here</a>
     * @see SimpleDateFormat
     * @since 15
     */
    public static String getFormattedDate(String pattern, int timeOffset) {
        String date;
        Calendar time = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        time.add(Calendar.HOUR_OF_DAY,timeOffset);
        DateFormat formatter = new SimpleDateFormat(pattern);
        formatter.setCalendar(time);
        date = formatter.format(time.getTime());
        return date;
    }

    /**
     * Gets the current time and formats it using the {@link SimpleDateFormat} after converting the time to a specified {@link TimeZone}
     * @param pattern the pattern used by <a href="https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html">SimpleDateFormat</a>
     * @param timeZone the timezone to use (example: "America/New_York" or "Europe/Paris")
     * @return the formatted date
     * @apiNote you can view {@link SimpleDateFormat}'s formats <a href="https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html">by clicking here</a>
     * @see SimpleDateFormat
     * @see TimeZone
     * @since 78
     */
    public static String getFormattedDate(String pattern, String timeZone) {
        return getFormattedDate(pattern, TimeZone.getTimeZone(timeZone));
    }

    /**
     * Gets the current time and formats it using the {@link SimpleDateFormat} after converting the time to a specified {@link TimeZone}
     * @param pattern the pattern used by <a href="https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html">SimpleDateFormat</a>
     * @param timeZone the {@link TimeZone} to use
     * @return the formatted date
     * @apiNote you can view {@link SimpleDateFormat}'s formats <a href="https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html">by clicking here</a>
     * @see SimpleDateFormat
     * @see TimeZone
     * @since 78
     */
    public static String getFormattedDate(String pattern, TimeZone timeZone) {
        return LocalDateTime.ofInstant(new Date().toInstant(), timeZone.toZoneId())
                .format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * Gets a formatted time from a unix timestamp (in seconds, not milliseconds) and formatted using {@link SimpleDateFormat} formatting patterns
     * @param unix the unix time to get from the date
     * @param pattern the pattern used by <a href="https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html">SimpleDateFormat</a>
     * @return the formatted date with a set unix time
     * @apiNote you can view {@link SimpleDateFormat}'s formats <a href="https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html">by clicking here</a>
     * @see SimpleDateFormat
     * @since 15
     */
    public static String getFormattedDateUnix(long unix, String pattern) {
        return getFormattedDateUnix(unix, pattern, 0);
    }

    /**
     * Gets a formatted time from a unix timestamp (in seconds, not milliseconds) and formatted using {@link SimpleDateFormat} and offset by a {@link TimeZone}
     * @param unix the unix time to get from the date
     * @param pattern the pattern used by <a href="https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html">SimpleDateFormat</a>
     * @param timeZone the timezone to use (example: "America/New_York" or "Europe/Paris")
     * @return the formatted date with a set unix time and offset by a {@link TimeZone}
     * @apiNote you can view {@link SimpleDateFormat}'s formats <a href="https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html">by clicking here</a>
     * @see SimpleDateFormat
     * @since 78
     */
    public static String getFormattedDateUnix(long unix, String pattern, String timeZone) {
        return getFormattedDateUnix(unix, pattern, TimeZone.getTimeZone(timeZone));
    }

    /**
     * Gets a formatted time from a unix timestamp (in seconds, not milliseconds) and formatted using {@link SimpleDateFormat} and offset by a {@link TimeZone}
     * @param unix the unix time to get from the date
     * @param pattern the pattern used by <a href="https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html">SimpleDateFormat</a>
     * @param timeZone the {@link TimeZone} to use
     * @return the formatted date with a set unix time and offset by a {@link TimeZone}
     * @apiNote you can view {@link SimpleDateFormat}'s formats <a href="https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html">by clicking here</a>
     * @see SimpleDateFormat
     * @since 78
     */
    public static String getFormattedDateUnix(long unix, String pattern, TimeZone timeZone) {
        LocalDateTime local = LocalDateTime.ofInstant(new Date(unix).toInstant(), Calendar.getInstance().getTimeZone().toZoneId());
        int offset = (int)(ChronoUnit.HOURS.between(
                local.atZone(Calendar.getInstance().getTimeZone().toZoneId()),
                local.atZone(timeZone.toZoneId())
        ));
        return getFormattedDateUnix(unix, pattern, offset < 0 ? Math.abs(offset) : -offset);
    }

    /**
     * Gets a formatted time from a unix timestamp (in seconds, not milliseconds), formatted using {@link SimpleDateFormat} formatting patterns, and with a certain time offset
     * @param unix the unix time to get from the date
     * @param pattern the pattern used by <a href="https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html">SimpleDateFormat</a>
     * @param timeOffset the time offset in GMT standards (example: -5 for EST, -4 for EDT)
     * @return the formatted date with a set unix time
     * @apiNote you can view {@link SimpleDateFormat}'s formats <a href="https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html">by clicking here</a>
     * @see SimpleDateFormat
     * @since 15
     */
    public static String getFormattedDateUnix(long unix, String pattern, int timeOffset) {
        Date time = new Date((unix*1000L)+(timeOffset*3600L*1000L));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(time);
    }

    /**
     * [OLDER METHOD] <br>
     * Formats the basic time
     * @param number the amount of seconds to format
     * @return the basic time
     * @since 15
     */
    public static String formatBasicSeconds(long number) {

        long hours   = (number / ONE_HOUR);
        long minutes = ((number % ONE_HOUR) / ONE_MINUTE);
        long seconds = ((number % ONE_MINUTE) / ONE_SECOND);

        return hours + "h, " + minutes + "m, " + seconds + "s";
    }

    /**
     * Formats the basic time but with colons instead
     * @param number the amount of seconds to format
     * @return the basic time with colons
     * @since 15
     */
    public static String formatBasicSecondsColons(long number) {

        long hours   = (number / ONE_SECOND);
        long minutes = ((number % ONE_HOUR) / ONE_MINUTE);
        long seconds = ((number % ONE_MINUTE) / ONE_SECOND);

        return (hours != 0 ? hours + ":" : "") + (NumberUtils.isBetweenEquals(minutes, 0, 9) ? "0" : "") + minutes + ":" + (NumberUtils.isBetweenEquals(seconds, 0, 9) ? "0" : "") + seconds + "";
    }

    /**
     * Formats the basic millisecond time with colons. Assumes '{@code minuteLeadingZero}' is true
     * @param number the amount of milliseconds to format
     * @return the basic time with milliseconds
     * @since 15
     */
    public static String formatBasicMs(long number) {
        return formatBasicMs(number, true);
    }

    /**
     * Formats the basic millisecond time with colons
     * @param number the amount of milliseconds to format
     * @param minuteLeadingZero whether there should be "0" before "09", {@code 'true'} if you want "09" and {@code 'false'} if you want "9"
     * @return the basic time with milliseconds
     * @since 15
     */
    public static String formatBasicMs(long number, boolean minuteLeadingZero) {

        int SECOND = 1000;        // no. of ms in a second
        int MINUTE = SECOND * 60; // no. of ms in a minuts

        long minutes = (number / MINUTE);
        long seconds = ((number % MINUTE) / SECOND);

        return (minuteLeadingZero ? (NumberUtils.isBetweenEquals((int)minutes, 0, 9) ? "0" : "") : "") + minutes + ":" + (seconds <= 9 ? "0" + seconds : seconds) + "." + ((number % 1000) <= 9 ? "00" + (number % 1000) : ((number % 1000) <= 99 ? "0" + (number % 1000) : (number % 1000)));
    }

}
