package com.it.example.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    /**
     * 插入字段自动填充
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
       log.info("公共字段自动填充[insert]...");
       log.info(metaObject.toString());
       metaObject.setValue("createTime", LocalDateTime.now());
       metaObject.setValue("updateTime",LocalDateTime.now());
       metaObject.setValue("createUser",BaseContext.getCurrentId());
       metaObject.setValue("updateUser",BaseContext.getCurrentId());

    }

    /**
     * 修改字段时自动填充
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充[update]...");
        log.info(metaObject.toString());

        Long id = Thread.currentThread().getId();
        log.info("线程id为{}",id);

        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }
}
