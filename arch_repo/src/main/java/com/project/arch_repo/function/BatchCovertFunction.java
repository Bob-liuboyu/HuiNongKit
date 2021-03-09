package com.project.arch_repo.function;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Function;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 批量转换
 */
public class BatchCovertFunction<T, R> implements Function<List<T>, List<R>> {
    private Function<T, R> singleConvertFunction;

    public BatchCovertFunction(Function<T, R> singleConvertFunction) {
        this.singleConvertFunction = singleConvertFunction;
    }

    @Override
    public List<R> apply(List<T> ts) throws Exception {
        if (singleConvertFunction == null) {
            throw new NullPointerException("singleConvertFunction params is null");
        }
        if (ts != null) {
            List<R> result = new ArrayList<>();
            for (T t : ts) {
                R apply = singleConvertFunction.apply(t);
                result.add(apply);
            }
            return result;
        }
        return null;
    }
}
