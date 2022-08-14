package com.github.peacetrue.io;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;

/**
 * 资源。
 *
 * @author peace
 */
public interface Resource {

    /**
     * 获取资源路径。
     *
     * @return 资源路径，相对于 {@link ResourcesLoader#getResources(String) 指定位置}
     */
    String getPath();

    /**
     * 是否目录。
     *
     * @return {@code true} 如果是目录，否则 {@code false}
     */
    boolean isDirectory();

    /**
     * 获取输入流，目录时为 {@code null}。
     *
     * @return 输入流
     * @throws IOException 获取输入流时发生异常
     */
    @Nullable
    InputStream getInputStream() throws IOException;

}
