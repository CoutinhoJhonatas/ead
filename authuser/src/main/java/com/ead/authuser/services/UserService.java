package com.ead.authuser.services;

import com.ead.authuser.dtos.UserRecordDto;
import com.ead.authuser.models.UserModel;

import java.util.List;
import java.util.UUID;

public interface UserService {

    List<UserModel> findAll();

    UserModel findById(UUID userId);

    void delete(UUID userId);

    UserModel registerUser(UserRecordDto userRecordDto);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    UserModel updateUser(UUID userId, UserRecordDto userRecordDto);

    UserModel updatePassword(UserModel userModel, UserRecordDto userRecordDto);

    UserModel updateImage(UUID userId, UserRecordDto userRecordDto);
}
