package tran.tuananh.elearning.service;

import org.json.simple.parser.ParseException;
import tran.tuananh.elearning.dto.SysUserRequestDTO;
import tran.tuananh.elearning.dto.TokenResponseDTO;
import tran.tuananh.elearning.entity.SysUser;
import tran.tuananh.elearning.response.GenerateResponse;

public interface SysUserService {

    GenerateResponse<TokenResponseDTO> login(SysUserRequestDTO dto) throws ParseException;

    GenerateResponse<?> logout(SysUserRequestDTO dto);

    GenerateResponse<SysUser> signUp(SysUserRequestDTO dto);

    GenerateResponse<SysUser> updateProfile(SysUserRequestDTO dto);

    GenerateResponse<SysUser> getUserDetail();

    GenerateResponse<TokenResponseDTO> refreshToken(SysUserRequestDTO dto) throws ParseException;
}
