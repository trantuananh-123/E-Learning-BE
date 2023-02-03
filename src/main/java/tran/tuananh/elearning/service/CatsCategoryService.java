package tran.tuananh.elearning.service;

import tran.tuananh.elearning.dto.CatsCategoryRequestDTO;
import tran.tuananh.elearning.entity.CatsCategory;
import tran.tuananh.elearning.response.DetailResponseData;
import tran.tuananh.elearning.response.ListResponseData;

import java.util.List;

public interface CatsCategoryService {

    ListResponseData<List<CatsCategory>> search(CatsCategoryRequestDTO dto);

    DetailResponseData<CatsCategory> saveOrUpdate(CatsCategoryRequestDTO dto);

    DetailResponseData<CatsCategory> delete(CatsCategoryRequestDTO dto);

    DetailResponseData<CatsCategory> getDetail(CatsCategoryRequestDTO dto);
}
