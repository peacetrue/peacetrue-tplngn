package com.github.peacetrue.io;

import lombok.*;

/**
 * 抽象资源。
 *
 * @author peace
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public abstract class AbstractResource implements Resource {

    /** 资源路径 */
    private String path;
    /** 是否目录 */
    private boolean directory;

}
