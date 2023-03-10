package tran.tuananh.elearning.service;

import org.json.simple.parser.ParseException;
import tran.tuananh.elearning.dto.request.SysUserRequestDTO;
import tran.tuananh.elearning.dto.response.TokenResponseDTO;
import tran.tuananh.elearning.entity.SysUser;
import tran.tuananh.elearning.response.DetailResponseData;

public interface SysUserService {

    DetailResponseData<TokenResponseDTO> login(SysUserRequestDTO dto) throws ParseException;

    DetailResponseData<?> logout(SysUserRequestDTO dto);

    DetailResponseData<SysUser> signUp(SysUserRequestDTO dto);

    DetailResponseData<TokenResponseDTO> refreshToken(SysUserRequestDTO dto) throws ParseException;

    DetailResponseData<SysUser> delete(SysUserRequestDTO dto);

    DetailResponseData<SysUser> lockAndUnlock(SysUserRequestDTO dto);

    DetailResponseData<SysUser> getUserDetail();

    DetailResponseData<SysUser> updateProfile(SysUserRequestDTO dto);

    DetailResponseData<SysUser> updatePassword(SysUserRequestDTO dto);

    DetailResponseData<SysUser> updateEmail(SysUserRequestDTO dto);

}
