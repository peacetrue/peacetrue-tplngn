package com.github.peacetrue.io;

import lombok.Getter;
import lombok.ToString;

import javax.annotation.Nullable;
import java.io.InputStream;

/**
 * 资源实现。
 *
 * @author peace
 */
@Getter
@ToString(callSuper = true)
public class ResourceImpl extends AbstractResource {

    @Nullable
    private InputStream inputStream;

    /**
     * 构造一个目录资源。
     *
     * @param path 资源路径
     */
    public ResourceImpl(String path) {
        super(path, true);
    }

    /**
     * 构造一个文件资源。
     *
     * @param path        资源路径
     * @param inputStream 资源内容输入流
     */
    public ResourceImpl(String path, @Nullable InputStream inputStream) {
        super(path, inputStream == null);
        this.inputStream = inputStream;
    }
}
