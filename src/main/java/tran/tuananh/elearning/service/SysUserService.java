package tran.tuananh.elearning.service;

import org.json.simple.parser.ParseException;
import tran.tuananh.elearning.dto.SysUserRequestDTO;
import tran.tuananh.elearning.entity.SysUser;
import tran.tuananh.elearning.response.GenerateResponse;

import javax.servlet.http.HttpServletRequest;

public interface SysUserService {

    GenerateResponse<SysUser> login(SysUserRequestDTO dto, HttpServletRequest httpServletRequest) throws ParseException;
}
