package com.pj.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.pj.project.bin_card.BinCard;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.*;

/**
 * code is far away from bug with the animal protecting
 * ┏┓　　　┏┓
 * ┏┛┻━━━┛┻┓
 * ┃　　　　　　　┃
 * ┃　　　━　　　┃
 * ┃　┳┛　┗┳　┃
 * ┃　　　　　　　┃
 * ┃　　　┻　　　┃
 * ┃　　　　　　　┃
 * ┗━┓　　　┏━┛
 * 　　┃　　　┃神兽保佑
 * 　　┃　　　┃代码无BUG！
 * 　　┃　　　┗━━━┓
 * 　　┃　　　　　　　┣┓
 * 　　┃　　　　　　　┏┛
 * 　　┗┓┓┏━┳┓┏┛
 * 　　　┃┫┫　┃┫┫
 * 　　　┗┻┛　┗┻┛
 *
 * @author lishiquan@color-sz.com
 * @description
 * @date Create in 2018/6/21 10:39
 */
public class BeanExUtils extends BeanUtils {

    private BeanExUtils() {
    }

    private static String[] getNullPropertyNames(Object source, boolean flag) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
            if (flag && srcValue instanceof String && isEmptyString(srcValue)) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    private static boolean isEmptyString(Object obj) {
        return obj == null || StrUtil.isEmpty(obj.toString());
    }

    /**
     * 复制的时候忽略null而不忽略String的""
     *
     * @param src    源对象
     * @param target 目标对象
     */
    public static void copyPropertiesIgnoreNull(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src, false));
    }

    /**
     * 复制的时候忽略null和String的"",空串
     * 参考apache的StringUtils.isBlank()
     *
     * @param src    源对象
     * @param target 目标对象
     */
    public static void copyPropertiesIgnoreBlank(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src, true));
    }

    public static void copy(BinCard src, BinCard target) {
        List<String>list=new ArrayList<>();
        Arrays.stream(ReflectUtil.getFields(BinCard.class)).forEach(field -> {
            String name = field.getName();
            Object value = ReflectUtil.getFieldValue(src, name);
            if (value == null ||StrUtil.isEmpty(value.toString())) {
                list.add(name);
            }
        });
        BeanUtil.copyProperties(src,target);
    }
}
