package tran.tuananh.elearning.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tran.tuananh.elearning.dto.request.CatsNationRequestDTO;
import tran.tuananh.elearning.entity.CatsNation;
import tran.tuananh.elearning.response.ListResponseData;
import tran.tuananh.elearning.service.CatsNationService;

import java.util.List;

@RestController
@RequestMapping(value = "cats-nation")
public class CatsNationController {

    @Autowired
    public CatsNationService catsNationService;

    @PostMapping(value = "/search")
    public ListResponseData<List<CatsNation>> search(@RequestBody CatsNationRequestDTO dto) {
        return catsNationService.search(dto);
    }


}
