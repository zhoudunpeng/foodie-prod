package com.zdp.service;

import com.zdp.pojo.Items;
import com.zdp.pojo.ItemsImg;
import com.zdp.pojo.ItemsParam;
import com.zdp.pojo.ItemsSpec;
import com.zdp.pojo.vo.CommentLevelCountsVO;
import com.zdp.pojo.vo.ItemCommentVO;
import com.zdp.utils.PagedGridResult;

import java.util.List;

/**
 * @author sesshomaru
 * @date 2021/5/3 21:51
 */
public interface ItemService {

    /**
     * 根据商品id查询详情
     * @param itemId
     * @return
     */
    public Items queryItemById(String itemId);

    /**
     * 根据商品id查询商品图片列表
     * @param itemId
     * @return
     */
    public List<ItemsImg> queryItemImgList(String itemId);

    /**
     * 根据商品id查询这个商品下的所有商品规格信息
     * @param itemId
     * @return
     */
    public List<ItemsSpec> queryItemSpecList(String itemId);

    /**
     * 根据商品id查询商品参数 产地等信息
     * @param itemId
     * @return
     */
    public ItemsParam queryItemParam(String itemId);

    /**
     * 查询商品评价数量
     * @param itemId
     * @return
     */
    public CommentLevelCountsVO queryCommentCounts(String itemId);


    /**
     * 分页查询评论内容
     * @param itemId
     * @param level
     * @param page
     * @param pageSize
     * @return
     */
    public PagedGridResult queryPagedComments(String itemId,
                                              Integer level,
                                              Integer page,
                                              Integer pageSize);
}
