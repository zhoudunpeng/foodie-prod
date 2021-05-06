package com.zdp.mapper;

import com.zdp.pojo.vo.ItemCommentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author sesshomaru
 * @date 2021/5/4 0:01
 */
public interface ItemsMapperCustom {

    public List<ItemCommentVO> queryItemComments(@Param("paramsMap") Map<String, Object> map);
}
