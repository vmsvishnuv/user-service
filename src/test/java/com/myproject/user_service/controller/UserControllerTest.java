package com.myproject.user_service.controller;

import com.myproject.user_service.model.PurchaseOrdersDTO;
import com.myproject.user_service.model.PurchaseOrdersProductDTO;
import com.myproject.user_service.model.User;
import com.myproject.user_service.model.UserDTO;
import com.myproject.user_service.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.myproject.user_service.util.StringConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build(); // Initialize MockMvc
    }

    @Test
    void testCreateUser() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName("JohnDoe");
        userDTO.setEmail("john.doe@example.com");
        userDTO.setAddress("123 Main St");

        String expectedUserID = "123";

        when(userService.saveUser(any(User.class))).thenReturn(expectedUserID);

        mockMvc.perform(post("/user/addUser")
                .contentType("application/json")
                .content("{\"userName\":\"JohnDoe\",\"email\":\"john.doe@example.com\",\"address\":\"123 Main St\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedUserID+USER_ADDED_SUCCESS));

        verify(userService, times(1)).saveUser(any(User.class));
    }

    @Test
    void  testFetchUserByUserID() throws Exception {
        String userID = "123";

        UserDTO expectedUserDTO = new UserDTO();
        expectedUserDTO.setUserId("123");
        expectedUserDTO.setUserName("JohnDoe");
        expectedUserDTO.setEmail("john.doe@example.com");
        expectedUserDTO.setAddress("123 Main St");

        when(userService.fetchUserByUserID(userID)).thenReturn(expectedUserDTO);

        mockMvc.perform(get("/user/getUser/123"))
                .andExpect(status().isOk()) // Expect HTTP 200
                .andExpect(jsonPath("$.userName").value("JohnDoe")) // Validate JSON response
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.address").value("123 Main St"));

        verify(userService, times(1)).fetchUserByUserID(userID);
    }

    @Test
    void testUpdateEmailForUserID() throws Exception {
        // Mock data
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName("JohnDoe");
        userDTO.setEmail("john.doe@example.com");
        userDTO.setAddress("123 Main St");

        // Mock service response
        when(userService.updateUser("123", userDTO)).thenReturn(USER_DETAILS_UPDATE_SUCCESS + "123");

        // Perform PUT request and verify response
        mockMvc.perform(put("/user/updateUserDetails/123")
                        .contentType("application/json")
                        .content("{\"userName\":\"JohnDoe\",\"email\":\"john.doe@example.com\",\"address\":\"123 Main St\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(USER_DETAILS_UPDATE_SUCCESS + "123"));

        // Verify service method was called
        verify(userService, times(1)).updateUser("123", userDTO);
    }


    @Test
    void testDeleteUser() throws Exception{
        String userID = "123";

        String expectedResult = userID+USER_DELETE_SUCCESS;

        when(userService.deleteUser(userID)).thenReturn(expectedResult);

        mockMvc.perform(delete("/user/deleteUser/123"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResult));

        verify(userService, times(1)).deleteUser(userID);
    }


    @Test
    void testValidateUser() throws Exception{
        String userID = "123";
        Boolean expectedResult = true;

        when(userService.validateUser(userID)).thenReturn(expectedResult);

        mockMvc.perform(get("/user/123"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(userService, times(1)).validateUser(userID);
    }


    @Test
    void testGetOrdersForUser() throws Exception {
        String userID = "123";

        PurchaseOrdersProductDTO purchaseOrdersProductDTO = new PurchaseOrdersProductDTO();
        purchaseOrdersProductDTO.setProductName("Apple");
        purchaseOrdersProductDTO.setQuantity(1);

        List<PurchaseOrdersProductDTO> purchaseOrdersProductDTOList = new ArrayList<>();
        purchaseOrdersProductDTOList.add(purchaseOrdersProductDTO);

        PurchaseOrdersDTO purchaseOrdersDTO = new PurchaseOrdersDTO();
        purchaseOrdersDTO.setOrderID(1L);
        purchaseOrdersDTO.setProductsOrdered(purchaseOrdersProductDTOList);
        purchaseOrdersDTO.setStatus("CONFIRMED");
        purchaseOrdersDTO.setTotalAmount(25000.00);

        List<PurchaseOrdersDTO> expectedResult = new ArrayList<>();
        expectedResult.add(purchaseOrdersDTO);

        when(userService.getOrdersForUser(userID)).thenReturn(expectedResult);

        mockMvc.perform(get("/user/getOrders/123"))
                .andDo(print()) // Print request and response for debugging
                .andExpect(status().isOk()) // Expect HTTP 200
                .andExpect(jsonPath("$[0].orderID").value(1L))
                .andExpect(jsonPath("$[0].status").value("CONFIRMED"))
                .andExpect(jsonPath("$[0].productsOrdered[0].productName").value("Apple"))
                .andExpect(jsonPath("$[0].productsOrdered[0].quantity").value(1));

        verify(userService, times(1)).getOrdersForUser(userID);
    }

}