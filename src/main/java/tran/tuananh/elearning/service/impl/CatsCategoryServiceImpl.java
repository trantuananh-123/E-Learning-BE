package tran.tuananh.elearning.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import tran.tuananh.elearning.dto.CatsCategoryRequestDTO;
import tran.tuananh.elearning.entity.CatsCategory;
import tran.tuananh.elearning.repository.CatsCategoryRepository;
import tran.tuananh.elearning.response.DetailResponseData;
import tran.tuananh.elearning.response.GenerateResponse;
import tran.tuananh.elearning.response.ListResponseData;
import tran.tuananh.elearning.service.CatsCategoryService;
import tran.tuananh.elearning.util.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CatsCategoryServiceImpl implements CatsCategoryService {

    @Autowired
    private CatsCategoryRepository catsCategoryRepository;

    @Autowired
    private Mapper mapper;

    @Override
    public ListResponseData<List<CatsCategory>> search(CatsCategoryRequestDTO dto) {
        int page = dto.getPage() != null ? dto.getPage() : null;
        int size = dto.getSize() != null ? dto.getSize() : null;
        Pageable pageable = PageRequest.of(page, size);

        List<CatsCategory> catsCategoryList = new ArrayList<>();
        Long totalItem = 0L;

        if (!dto.getIsGetAll()) {
            Page<CatsCategory> catsCategoryPage = catsCategoryRepository.search(dto.getId(), pageable);
            catsCategoryList = catsCategoryPage.getContent();
            totalItem = catsCategoryPage.getTotalElements();
        } else {
            catsCategoryList = catsCategoryRepository.searchWithoutPage(dto.getId());
            totalItem = (long) catsCategoryList.size();
        }
        return GenerateResponse.generateListResponseData("Get category list successfully", totalItem, catsCategoryList, HttpStatus.OK.value());
    }

    @Override
    public DetailResponseData<CatsCategory> saveOrUpdate(CatsCategoryRequestDTO dto) {
        CatsCategory catsCategory = mapper.fromTo(dto, CatsCategory.class);
        if (dto.getId() != null) {
            catsCategoryRepository.save(catsCategory);
            return GenerateResponse.generateDetailResponseData("Save category successfully", catsCategory, HttpStatus.OK.value());
        } else {
            return GenerateResponse.generateDetailResponseData("Category id cannot null", null, HttpStatus.BAD_REQUEST.value());
        }
    }

    @Override
    public DetailResponseData<CatsCategory> delete(CatsCategoryRequestDTO dto) {
        CatsCategory catsCategory = mapper.fromTo(dto, CatsCategory.class);
        if (dto.getId() != null) {
            catsCategoryRepository.deleteById(dto.getId());
            return GenerateResponse.generateDetailResponseData("Save category successfully", catsCategory, HttpStatus.OK.value());
        } else {
            return GenerateResponse.generateDetailResponseData("Category id cannot null", null, HttpStatus.BAD_REQUEST.value());
        }
    }

    @Override
    public DetailResponseData<CatsCategory> getDetail(CatsCategoryRequestDTO dto) {
        CatsCategory catsCategory = null;
        if (dto.getId() != null) {
            Optional<CatsCategory> optionalCatsCategory = catsCategoryRepository.findById(dto.getId());
            if (optionalCatsCategory.isPresent()) {
                catsCategory = optionalCatsCategory.get();
            }
            return GenerateResponse.generateDetailResponseData("Save category successfully", catsCategory, HttpStatus.OK.value());
        } else {
            return GenerateResponse.generateDetailResponseData("Category id cannot null", null, HttpStatus.BAD_REQUEST.value());
        }
    }
}
