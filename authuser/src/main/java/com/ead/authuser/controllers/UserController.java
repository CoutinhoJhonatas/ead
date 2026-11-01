package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserRecordDto;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.ead.authuser.specification.SpecificationTemplate;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
public class UserController {

    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<UserModel>> getAllUsers(SpecificationTemplate.UserSpec userSpec, Pageable pageable) {
        Page<UserModel> userModelPage = userService.findAll(userSpec, pageable);
        if (!userModelPage.isEmpty()) {
            for (UserModel user : userModelPage.toList()) {
                user.add(linkTo(methodOn(UserController.class).getOneUser(user.getUserId())).withSelfRel());
            }
        }
        return ResponseEntity.ok().body(userModelPage);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserModel> getOneUser(@PathVariable(value = "userId") UUID userId) {
        return ResponseEntity.ok().body(userService.findById(userId));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable(value = "userId") UUID userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserModel> updateUser(@PathVariable(value = "userId") UUID userId,
                                                @RequestBody @Validated(UserRecordDto.UserView.UserPut.class)
                                                @JsonView(UserRecordDto.UserView.UserPut.class)
                                                UserRecordDto userRecordDto) {
        return ResponseEntity.ok().body(userService.updateUser(userId, userRecordDto));
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Object> updatePassword(@PathVariable(value = "userId") UUID userId,
                                                @RequestBody @Validated(UserRecordDto.UserView.PasswordPut.class)
                                                @JsonView(UserRecordDto.UserView.PasswordPut.class)
                                                UserRecordDto userRecordDto) {
        var userModel = userService.findById(userId);
        if (!userModel.getPassword().equals(userRecordDto.oldPassword())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Mismatched old password!");
        }
        userService.updatePassword(userModel, userRecordDto);
        return ResponseEntity.ok().body("Password updated successfully.");
    }

    @PutMapping("/{userId}/image")
    public ResponseEntity<UserModel> updateImage(@PathVariable(value = "userId") UUID userId,
                                                 @RequestBody @Validated(UserRecordDto.UserView.ImagePut.class)
                                                 @JsonView(UserRecordDto.UserView.ImagePut.class)
                                                 UserRecordDto userRecordDto) {
        return ResponseEntity.ok().body(userService.updateImage(userId, userRecordDto));
    }
}
