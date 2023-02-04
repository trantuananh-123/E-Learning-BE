package tran.tuananh.elearning.service;

import tran.tuananh.elearning.dto.CatsNationRequestDTO;
import tran.tuananh.elearning.entity.CatsNation;
import tran.tuananh.elearning.response.ListResponseData;

import java.util.List;

public interface CatsNationService {

    ListResponseData<List<CatsNation>> search(CatsNationRequestDTO dto);
}
