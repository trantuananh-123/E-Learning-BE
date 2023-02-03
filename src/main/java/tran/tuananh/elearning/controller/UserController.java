package tran.tuananh.elearning.controller;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tran.tuananh.elearning.dto.SysUserRequestDTO;
import tran.tuananh.elearning.dto.TokenResponseDTO;
import tran.tuananh.elearning.entity.SysUser;
import tran.tuananh.elearning.response.DetailResponseData;
import tran.tuananh.elearning.service.SysUserService;

@RestController
@RequestMapping(value = "user")
public class UserController {

    @Autowired
    public SysUserService sysUserService;

    @PostMapping(value = "/login")
    public DetailResponseData<TokenResponseDTO> login(@RequestBody SysUserRequestDTO dto) throws ParseException {
        return sysUserService.login(dto);
    }

    @PostMapping(value = "/logout")
    public DetailResponseData<?> logout(@RequestBody SysUserRequestDTO dto) {
        return sysUserService.logout(dto);
    }

    @PostMapping(value = "/signUp")
    public DetailResponseData<SysUser> signUp(@RequestBody SysUserRequestDTO dto) {
        return sysUserService.signUp(dto);
    }

    @PostMapping(value = "/delete")
    public DetailResponseData<SysUser> delete(@RequestBody SysUserRequestDTO dto) {
        return sysUserService.delete(dto);
    }

    @PostMapping(value = "/lock")
    public DetailResponseData<SysUser> lock(@RequestBody SysUserRequestDTO dto) {
        return sysUserService.lockAndUnlock(dto);
    }

    @PostMapping(value = "/unlock")
    public DetailResponseData<SysUser> unlock(@RequestBody SysUserRequestDTO dto) {
        return sysUserService.lockAndUnlock(dto);
    }

    @PostMapping(value = "/refresh-token")
    public DetailResponseData<TokenResponseDTO> refreshToken(@RequestBody SysUserRequestDTO dto) throws ParseException {
        return sysUserService.refreshToken(dto);
    }

    @PostMapping(value = "/update-profile")
    public DetailResponseData<SysUser> updateProfile(@RequestBody SysUserRequestDTO dto) {
        return sysUserService.updateProfile(dto);
    }

    @GetMapping(value = "/get-user-detail")
    public DetailResponseData<SysUser> getUserDetail() {
        return sysUserService.getUserDetail();
    }
}
