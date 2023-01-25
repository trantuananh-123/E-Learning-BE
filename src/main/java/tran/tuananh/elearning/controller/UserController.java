package tran.tuananh.elearning.controller;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tran.tuananh.elearning.dto.SysUserRequestDTO;
import tran.tuananh.elearning.response.GenerateResponse;
import tran.tuananh.elearning.service.SysUserService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "")
public class UserController {

    @Autowired
    public SysUserService sysUserService;

    @PostMapping(value = "/login")
    public GenerateResponse login(@RequestBody SysUserRequestDTO dto, HttpServletRequest httpServletRequest) throws ParseException {
        return sysUserService.login(dto, httpServletRequest);
    }
}
