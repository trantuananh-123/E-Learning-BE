package tran.tuananh.elearning.service;

import tran.tuananh.elearning.dto.response.SysMenuResponseDTO;
import tran.tuananh.elearning.response.ListResponseData;

import java.util.List;

public interface SysMenuService {

    ListResponseData<List<SysMenuResponseDTO>> getAll();
}
