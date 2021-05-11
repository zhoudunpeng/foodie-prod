package com.zdp.service;



import com.zdp.pojo.UserAddress;
import com.zdp.pojo.bo.AddressBO;

import java.util.List;

public interface AddressService {

    /**
     * 根据用户id查询用户的收货地址列表
     * @param userId
     * @return
     */
    public List<UserAddress> queryAll(String userId);

    /**
     * 添加收货地址
     * @param addressBO
     */
    public void addNewUserAddress(AddressBO addressBO);

    /**
     * 通过用户id和地址id获取地址信息
     * @param userId
     * @param addressId
     * @return
     */
    public UserAddress queryUserAddres(String userId, String addressId);

}
