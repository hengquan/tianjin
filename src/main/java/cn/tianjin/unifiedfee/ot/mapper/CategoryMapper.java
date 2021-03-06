package cn.tianjin.unifiedfee.ot.mapper;

import java.util.List;
import java.util.Map;

import cn.tianjin.unifiedfee.ot.entity.Category;

public interface CategoryMapper {
    public List<Category> getAllList();

    public List<Map<String,Object>> getAllListWithMap();

    public List<Map<String,Object>> getListWithMap(Map<String,Object> cate);

    public int insert(Category record);

    public int insertSelective(Category record);

    public int update(Category record);

    public int changeValid(Category record);
}