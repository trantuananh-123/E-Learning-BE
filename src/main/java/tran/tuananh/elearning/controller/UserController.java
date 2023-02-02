package tran.tuananh.elearning.controller;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tran.tuananh.elearning.dto.SysUserRequestDTO;
import tran.tuananh.elearning.response.GenerateResponse;
import tran.tuananh.elearning.service.SysUserService;

@RestController
@RequestMapping(value = "user")
public class UserController {

    @Autowired
    public SysUserService sysUserService;

    @PostMapping(value = "/login")
    public GenerateResponse login(@RequestBody SysUserRequestDTO dto) throws ParseException {
        return sysUserService.login(dto);
    }

    @PostMapping(value = "/logout")
    public GenerateResponse logout(@RequestBody SysUserRequestDTO dto) {
        return sysUserService.logout(dto);
    }

    @PostMapping(value = "/signUp")
    public GenerateResponse signUp(@RequestBody SysUserRequestDTO dto) {
        return sysUserService.signUp(dto);
    }

    @PostMapping(value = "/delete")
    public GenerateResponse delete(@RequestBody SysUserRequestDTO dto) {
        return sysUserService.delete(dto);
    }

    @PostMapping(value = "/lock")
    public GenerateResponse lock(@RequestBody SysUserRequestDTO dto) {
        return sysUserService.lockAndUnlock(dto);
    }

    @PostMapping(value = "/unlock")
    public GenerateResponse unlock(@RequestBody SysUserRequestDTO dto) {
        return sysUserService.lockAndUnlock(dto);
    }

    @PostMapping(value = "/refresh-token")
    public GenerateResponse refreshToken(@RequestBody SysUserRequestDTO dto) throws ParseException {
        return sysUserService.refreshToken(dto);
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
