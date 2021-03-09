package com.project.arch_repo.utils;

import com.google.gson.JsonSyntaxException;
import com.xxf.arch.json.JsonUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class ObjectUtils {

    /**
     * 将父亲转换为儿子,或者拷贝为自己
     *
     * @param f
     * @param classS
     * @param <F>
     * @param <S>
     */
    public static <F, S extends F> S convert(F f, Class<S> classS) throws JsonSyntaxException {
        return JsonUtils.toBean(JsonUtils.toJsonString(f), classS);
    }

    /**
     * 深拷贝 需要 implements Serializable
     *
     * @param src
     * @param <T>
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T cloneTo(T src) throws Exception {
        ByteArrayOutputStream memoryBuffer = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        T dist = null;
        try {
            out = new ObjectOutputStream(memoryBuffer);
            out.writeObject(src);
            out.flush();
            in = new ObjectInputStream(new ByteArrayInputStream(memoryBuffer.toByteArray()));
            dist = (T) in.readObject();
        } finally {
            if (out != null) {
                try {
                    out.close();
                    out = null;
                } catch (IOException e) {
                }
            }
            if (in != null) {
                try {
                    in.close();
                    in = null;
                } catch (IOException e) {
                }
            }
        }
        return dist;
    }
}
