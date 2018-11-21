package com.ntnikka.common.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Liuq
 * Date: 2018/9/18
 * Time: 09:15
 * Description:
 */
public class EmptyUtil {
    /**
     * 判断对象为空
     *
     * @param obj 对象名
     * @return 是否为空
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if ((obj instanceof List)) {
            return ((List) obj).size() == 0;
        }
        if ((obj instanceof String)) {
            return ((String) obj).trim().equals("");
        }
        return false;
    }

    /**
     * 判断对象不为空
     *
     * @param obj 对象名
     * @return 是否不为空
     */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    public static void main(String[] args) {
        Map map = new HashMap();
        map.put("order", 55555555L);
        System.out.println("args = [" + isNotEmpty(map.get("orderid")) + "]");
    }

}

