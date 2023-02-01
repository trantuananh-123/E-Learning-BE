package tran.tuananh.elearning.controller;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tran.tuananh.elearning.dto.SysUserRequestDTO;
import tran.tuananh.elearning.response.GenerateResponse;
import tran.tuananh.elearning.service.SysUserService;

@RestController
@RequestMapping(value = "")
public class UserController {

    @Autowired
    public SysUserService sysUserService;

    @PostMapping(value = "/login")
    public GenerateResponse login(@RequestBody SysUserRequestDTO dto) throws ParseException {
        return sysUserService.login(dto);
    }

    @PostMapping(value = "/signUp")
    public GenerateResponse signUp(@RequestBody SysUserRequestDTO dto) {
        return sysUserService.signUp(dto);
    }

    @PostMapping(value = "/update-profile")
    public GenerateResponse updateProfile(@RequestBody SysUserRequestDTO dto) {
        return sysUserService.updateProfile(dto);
    }

    @GetMapping(value = "/get-user-detail")
    public GenerateResponse login() {
        return sysUserService.getUserDetail();
    }
}
