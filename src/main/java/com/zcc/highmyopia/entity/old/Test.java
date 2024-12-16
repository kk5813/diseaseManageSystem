package com.zcc.highmyopia.entity.old;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author liangyue
 * @since 2021-05-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Test implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String examId;

    private String path;

    private String type;

    private Integer downfile;

    private String localpath;


}
