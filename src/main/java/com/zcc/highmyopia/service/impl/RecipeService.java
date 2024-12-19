package com.zcc.highmyopia.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcc.highmyopia.mapper.IDeptMapper;
import com.zcc.highmyopia.mapper.IRecipeMapper;
import com.zcc.highmyopia.po.Dept;
import com.zcc.highmyopia.po.Recipe;
import com.zcc.highmyopia.service.IDeptService;
import com.zcc.highmyopia.service.IRecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Author zcc
 * @Date 2024/12/18
 * @Description
 */
@Service
@RequiredArgsConstructor
public class RecipeService extends ServiceImpl<IRecipeMapper, Recipe> implements IRecipeService {

}
