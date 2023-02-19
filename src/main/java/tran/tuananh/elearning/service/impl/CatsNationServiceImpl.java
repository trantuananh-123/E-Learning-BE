package tran.tuananh.elearning.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import tran.tuananh.elearning.dto.request.CatsNationRequestDTO;
import tran.tuananh.elearning.entity.CatsNation;
import tran.tuananh.elearning.repository.CatsNationRepository;
import tran.tuananh.elearning.response.GenerateResponse;
import tran.tuananh.elearning.response.ListResponseData;
import tran.tuananh.elearning.service.CatsNationService;
import tran.tuananh.elearning.util.Mapper;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CatsNationServiceImpl implements CatsNationService {

    @Autowired
    private CatsNationRepository catsNationRepository;

    @Autowired
    private Mapper mapper;

    @Override
    public ListResponseData<List<CatsNation>> search(CatsNationRequestDTO dto) {
        int page = dto.getPage() != null ? dto.getPage() : null;
        int size = dto.getSize() != null ? dto.getSize() : null;
        Pageable pageable = PageRequest.of(page, size);

        List<CatsNation> catsNationList = new ArrayList<>();
        Long totalItem = 0L;

        Page<CatsNation> catsNationPage = catsNationRepository.search(dto.getId(), pageable);
        catsNationList = catsNationPage.getContent();
        totalItem = catsNationPage.getTotalElements();
        return GenerateResponse.generateListResponseData("Get category list successfully", catsNationList, totalItem, HttpStatus.OK.value(), page, size);
    }
}
