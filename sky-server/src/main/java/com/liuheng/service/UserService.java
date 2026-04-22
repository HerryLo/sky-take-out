package com.liuheng.service;

import com.liuheng.dto.UserLoginDTO;
import com.liuheng.vo.UserLoginVO;

public interface UserService {

    UserLoginVO login(UserLoginDTO userLoginDTO);

    UserLoginVO refresh(String refreshToken);

    void logout(String openid);
}
