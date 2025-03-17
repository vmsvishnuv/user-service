package com.myproject.user_service.serviceImpl;

import com.myproject.user_service.exception.UserNotFoundException;
import com.myproject.user_service.model.PurchaseOrdersDTO;
import com.myproject.user_service.model.User;
import com.myproject.user_service.model.UserDTO;
import com.myproject.user_service.repository.UserRepository;
import com.myproject.user_service.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.myproject.user_service.util.StringConstants.*;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String saveUser(User user) {
        User userResponse = userRepository.save(user);
        return userResponse.getUserID();
    }

    @Override
    public UserDTO fetchUserByUserID(String userID) {
        User user = checkUserExist(userID);

        UserDTO userDTO = new UserDTO();
        userDTO.setUserName(user.getUserName());
        userDTO.setEmail(user.getEmail());
        userDTO.setAddress(user.getAddress());
        userDTO.setUserId(user.getUserID());

        return userDTO;
    }

    @Override
    public String updateUser(String userID, UserDTO userDto) {
        User user = checkUserExist(userID);

        if(userDto.getUserName() != null){
            user.setUserName(userDto.getUserName());
        }
        if(userDto.getEmail() != null){
            user.setEmail(userDto.getEmail());
        }
        if(userDto.getAddress() != null){
            user.setAddress(userDto.getAddress());
        }

        userRepository.save(user);

        return USER_DETAILS_UPDATE_SUCCESS+userID;
    }

    @Override
    @Transactional
    public String deleteUser(String userID) {
        String status;
        if(userRepository.deleteByUserID(userID) > 0){
            status = userID+USER_DELETE_SUCCESS;
        }
        else{
            status = USER_DELETE_FAIL+USER_NOT_FOUND_EXCEPTION+userID;
        }

        return status;
    }

    @Override
    public Boolean validateUser(String userID) {

        return checkUserExist(userID) != null;
    }

    @Override
    public List<PurchaseOrdersDTO> getOrdersForUser(String userID) {
        User user = checkUserExist(userID);
        if(user == null){
            throw new UserNotFoundException(USER_NOT_FOUND_EXCEPTION + userID);
        }
        List<PurchaseOrdersDTO> result = new ArrayList<>();

        try {
            ResponseEntity<List<PurchaseOrdersDTO>> response =  restTemplate.exchange(
                    REST_CALL_ORDERSERVICE+userID,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<PurchaseOrdersDTO>>() {
                    });
            result = response.getBody();

        }catch (HttpClientErrorException | HttpServerErrorException e){
            throw new UserNotFoundException(ISSUE_CALLING_ORDERSERVICE);
        }

        return result;
    }


    public User checkUserExist(String userID){
        User user = userRepository.findUserByUserID(userID);
        if(user == null){
            throw new UserNotFoundException(USER_NOT_FOUND_EXCEPTION + userID);
        }
        return user;
    }


}
