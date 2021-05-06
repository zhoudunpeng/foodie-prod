package com.zdp.mapper;


import com.zdp.my.mapper.MyMapper;
import com.zdp.pojo.ItemsComments;
import com.zdp.pojo.vo.MyCommentVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ItemsCommentsMapperCustom extends MyMapper<ItemsComments> {

    public void saveComments(Map<String, Object> map);

    public List<MyCommentVO> queryMyComments(@Param("paramsMap") Map<String, Object> map);

}