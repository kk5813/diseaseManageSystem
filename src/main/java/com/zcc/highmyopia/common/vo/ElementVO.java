package com.zcc.highmyopia.common.vo;

import com.zcc.highmyopia.po.Element;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.connection.ReactiveListCommands;

import java.util.List;

/**
 * @Author zcc
 * @Date 2024/12/17
 * @Description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ElementVO {

    private Long total;

    private List<Element> elements;

}
