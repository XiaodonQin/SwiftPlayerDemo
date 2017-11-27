package sg.com.conversant.swiftplayer.feedback;

import android.os.Build;

import com.zendesk.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/8/23.
 */

public class StringUtils {
    private static Map<Character, String> JS_ESCAPE_LOOKUP_MAP = new HashMap();
    public static final String EMPTY_STRING = "";
    public static final String LINE_SEPARATOR;
    private static final String ISO_8601_DATE_FORMAT = "yyyy-MM-dd\'T\'HH:mm:ss\'Z\'";

    private StringUtils() {
    }

    public static boolean hasLength(String s) {
        return s != null && s.trim().length() > 0;
    }

    public static boolean hasLengthMany(String... strings) {
        if(strings != null && strings.length != 0) {
            String[] arr$ = strings;
            int len$ = strings.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String currentString = arr$[i$];
                if(isEmpty(currentString)) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public static boolean isEmpty(String s) {
        return !hasLength(s);
    }

    public static String toCsvString(String... values) {
        List valuesAsArray = values == null?null: Arrays.asList(values);
        return toCsvString(valuesAsArray);
    }

    public static String toCsvString(List<String> values) {
        String csvString = null;
        if(values != null) {
            StringBuilder stringBuilder = new StringBuilder();

            for(int i = 0; i < values.size(); ++i) {
                String currentValue = (String)values.get(i);
                if(hasLength(currentValue)) {
                    stringBuilder.append((String)values.get(i));
                    if(i < values.size() - 1) {
                        stringBuilder.append(",");
                    }
                }
            }

            csvString = stringBuilder.toString();
        }

        return csvString;
    }

    public static String toCsvStringNumber(Number... values) {
        List valuesAsArray = values == null?null:Arrays.asList(values);
        return toCsvStringNumber(valuesAsArray);
    }

    public static String toCsvStringNumber(List<? extends Number> values) {
        ArrayList numbersAsStrings = null;
        if(values != null) {
            numbersAsStrings = new ArrayList();
            Iterator i$ = values.iterator();

            while(i$.hasNext()) {
                Number number = (Number)i$.next();
                if(number != null) {
                    numbersAsStrings.add(number.toString());
                }
            }
        }

        return toCsvString((List)numbersAsStrings);
    }

    public static List<String> fromCsv(String csvString) {
        if(hasLength(csvString)) {
            String[] values = csvString.split(",");
            ArrayList list = new ArrayList();
            String[] arr$ = values;
            int len$ = values.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String string = arr$[i$];
                if(hasLength(string)) {
                    list.add(string);
                }
            }

            return CollectionUtils.unmodifiableList(list);
        } else {
            return CollectionUtils.unmodifiableList(new ArrayList(0));
        }
    }

    public static String toCsvString(long... values) {
        if(values == null) {
            return null;
        } else {
            ArrayList valuesList = new ArrayList();
            long[] arr$ = values;
            int len$ = values.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                long value = arr$[i$];
                valuesList.add(Long.valueOf(value));
            }

            return toCsvStringNumber((List)valuesList);
        }
    }

    public static String toDateInIsoFormat(Date dateToFormat) {
        if(dateToFormat != null) {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss\'Z\'");
            return dateFormatter.format(dateToFormat);
        } else {
            return "";
        }
    }

    public static String escapeEcmaScript(String input) {
        if(isEmpty(input)) {
            return input;
        } else {
            StringBuilder escapedString = new StringBuilder(input.length() * 2);
            int i = 0;

            for(int len = input.length(); i < len; ++i) {
                char c = input.charAt(i);
                if(JS_ESCAPE_LOOKUP_MAP.containsKey(Character.valueOf(c))) {
                    escapedString.append((String)JS_ESCAPE_LOOKUP_MAP.get(Character.valueOf(c)));
                } else {
                    escapedString.append(c);
                }
            }

            return escapedString.toString();
        }
    }

    public static String capitalize(String input) {
        if(hasLength(input)) {
            if(Character.isUpperCase(input.charAt(0))) {
                return input;
            } else {
                StringBuilder stringBuilder = new StringBuilder(input.length());
                stringBuilder.append(Character.toTitleCase(input.charAt(0)));
                stringBuilder.append(input.substring(1));
                return stringBuilder.toString();
            }
        } else {
            return input != null?input:null;
        }
    }

    public static boolean isNumeric(String input) {
        if(isEmpty(input)) {
            return false;
        } else {
            int inputLength = input.length();

            for(int i = 0; i < inputLength; ++i) {
                if(!Character.isDigit(input.charAt(i))) {
                    return false;
                }
            }

            return true;
        }
    }

    public static boolean startsWithIdeographic(String input) {
        if(hasLength(input)) {
            int codepoint = input.codePointAt(0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                return Character.isIdeographic(codepoint);
            }
            return true;
        } else {
            return false;
        }
    }

    static {
        JS_ESCAPE_LOOKUP_MAP.put(Character.valueOf('\''), "\\\'");
        JS_ESCAPE_LOOKUP_MAP.put(Character.valueOf('\"'), "\\\"");
        JS_ESCAPE_LOOKUP_MAP.put(Character.valueOf('\\'), "\\\\");
        JS_ESCAPE_LOOKUP_MAP.put(Character.valueOf('/'), "\\/");
        JS_ESCAPE_LOOKUP_MAP.put(Character.valueOf('\b'), "\\b");
        JS_ESCAPE_LOOKUP_MAP.put(Character.valueOf('\n'), "\\n");
        JS_ESCAPE_LOOKUP_MAP.put(Character.valueOf('\t'), "\\t");
        JS_ESCAPE_LOOKUP_MAP.put(Character.valueOf('\f'), "\\f");
        JS_ESCAPE_LOOKUP_MAP.put(Character.valueOf('\r'), "\\r");
        LINE_SEPARATOR = System.getProperty("line.separator");
    }
}
