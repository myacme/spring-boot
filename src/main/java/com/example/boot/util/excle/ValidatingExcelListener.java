package com.example.boot.util.excle;


import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.event.AnalysisEventListener;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.Set;

/**
 * 创建带校验功能的监听器
 *
 * Validator 需要依赖：
 *         <dependency>
 *             <groupId>jakarta.platform</groupId>
 *             <artifactId>jakarta.jakartaee-api</artifactId>
 *             <version>9.1.0</version>
 *             <scope>provided</scope>
 *         </dependency>
 *
 *
 * @author ljx
 * @version 1.0.0
 * @create 2025/9/1 15:40
 */
public class ValidatingExcelListener<T> extends AnalysisEventListener<T> {

    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();


    /**
     * 创建带校验功能的监听器实例
     *
     * @param <T> 数据类型
     * @return ValidatingExcelListener实例
     */
    public static <T> ValidatingExcelListener<T> create() {
        return new ValidatingExcelListener<>();
    }

    @Override
    public void invoke(T data, AnalysisContext context) {
        // 执行校验
        Set<ConstraintViolation<T>> violations = VALIDATOR.validate(data);
        if (!violations.isEmpty()) {
            StringBuilder errorMsg = new StringBuilder();
            for (ConstraintViolation<T> violation : violations) {
                errorMsg.append(violation.getMessage()).append("；");
            }
            System.out.println( "数据校验失败：" + errorMsg);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 所有数据解析完成
    }
}
