package com.zdp.mapper;

import com.zdp.pojo.vo.ItemCommentVO;
import com.zdp.pojo.vo.SearchItemsVO;
import com.zdp.pojo.vo.ShopcartVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author sesshomaru
 * @date 2021/5/4 0:01
 */
@Repository
public interface ItemsMapperCustom {

    // 查询商品评论
    public List<ItemCommentVO> queryItemComments(@Param("paramsMap") Map<String, Object> map);

    // 通过商品名模糊查询商品列表信息
    public List<SearchItemsVO> searchItems(@Param("paramsMap") Map<String, Object> map);

    // 首页中根据商品的三级分类id查询商品列表信息
    public List<SearchItemsVO> searchItemsByThirdCat(@Param("paramsMap") Map<String, Object> map);

    public List<ShopcartVO> queryItemsBySpecIds(@Param("paramsList") List specIdsList);

    public int decreaseItemSpecStock(@Param("specId") String specId,
                                     @Param("pendingCounts") int pendingCounts);
}
