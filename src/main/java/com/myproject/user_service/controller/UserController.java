package com.myproject.user_service.controller;

import com.myproject.user_service.model.PurchaseOrdersDTO;
import com.myproject.user_service.model.User;
import com.myproject.user_service.model.UserDTO;
import com.myproject.user_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.myproject.user_service.util.StringConstants.USER_ADDED_SUCCESS;

@RestController
@Validated
@RequestMapping("/user")
@Tag(name = "User Service", description = "APIs for managing Users")
public class UserController {
    @Autowired
    UserService userService;
    @PostMapping("/addUser")
    @Operation(summary = "Add New User", description = "Add a new user with Name, Email & Address and create a Unique UserID")
    public String createUser(@Valid @RequestBody UserDTO userDTO){
        User user = new User();
        user.setUserName(userDTO.getUserName());
        user.setEmail(userDTO.getEmail());
        user.setAddress(userDTO.getAddress());

        String userID = userService.saveUser(user);

        return userID + USER_ADDED_SUCCESS;
    }

    @GetMapping("getUser/{userID}")
    @Operation(summary = "Get User Details", description = "Get User details based on UserID")
    @ApiResponse(responseCode = "200", description = "User Found")
    @ApiResponse(responseCode = "404", description = "User NOT Found")
    public UserDTO fetchUserByUserID(@PathVariable String userID){

        return userService.fetchUserByUserID(userID);
    }

    @PutMapping("updateUserDetails/{userID}")
    @Operation(summary = "Update User", description = "Update specific User details based on UserID")
    public String updateEmailForUserID(@PathVariable String userID, @RequestBody UserDTO userDTO){

        return userService.updateUser(userID, userDTO);
    }

    @DeleteMapping("deleteUser/{userID}")
    @Operation(summary = "Delete User", description = "Delete a user based on UserID")
    public String deleteUser(@PathVariable String userID){
        return userService.deleteUser(userID);
    }

    @GetMapping("/{userID}")
    @Operation(summary = "Validate User", description = "Validate user based on UserID", deprecated = true)
    public Boolean validateUser(@PathVariable String userID){
        return userService.validateUser(userID);
    }

    @GetMapping("/getOrders/{userID}")
    @Operation(summary = "Get Orders For User", description = "Get all Orders or Purchased made by User based on UserID")
    public List<PurchaseOrdersDTO> getOrdersForUser(@PathVariable String userID){

        return userService.getOrdersForUser(userID);
    }
}
