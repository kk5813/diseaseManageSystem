package com.zcc.highmyopia.common.vo;

import com.zcc.highmyopia.po.Element;
import lombok.Data;
import org.springframework.data.redis.connection.ReactiveListCommands;

import java.util.List;

/**
 * @Author zcc
 * @Date 2024/12/17
 * @Description
 */
@Data
public class ElementVO {

    private Long total;

    private List<Element> elements;

}
