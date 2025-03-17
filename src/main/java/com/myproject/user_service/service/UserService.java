package com.myproject.user_service.service;

import com.myproject.user_service.model.PurchaseOrdersDTO;
import com.myproject.user_service.model.User;
import com.myproject.user_service.model.UserDTO;

import java.util.List;

public interface UserService {
    public String saveUser(User user);
    public UserDTO fetchUserByUserID(String userID);
    public String updateUser(String userID, UserDTO userDto);
    public String deleteUser(String userID);
    public Boolean validateUser(String userID);
    public List<PurchaseOrdersDTO> getOrdersForUser(String userID);
}
