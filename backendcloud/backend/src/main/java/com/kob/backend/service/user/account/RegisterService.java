package com.kob.backend.service.user.account;

import java.util.Map;

public interface RegisterService {
    Map<String,String> register(String userName, String passWord, String confirmedPassWord);
}
