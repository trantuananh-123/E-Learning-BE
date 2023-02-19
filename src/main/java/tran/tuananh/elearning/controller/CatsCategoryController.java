package tran.tuananh.elearning.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tran.tuananh.elearning.dto.request.CatsCategoryRequestDTO;
import tran.tuananh.elearning.entity.CatsCategory;
import tran.tuananh.elearning.response.DetailResponseData;
import tran.tuananh.elearning.response.ListResponseData;
import tran.tuananh.elearning.service.CatsCategoryService;

import java.util.List;

@RestController
@RequestMapping(value = "cats-category")
public class CatsCategoryController {

    @Autowired
    public CatsCategoryService catsCategoryService;

    @PostMapping(value = "/search")
    public ListResponseData<List<CatsCategory>> search(@RequestBody CatsCategoryRequestDTO dto) {
        return catsCategoryService.search(dto);
    }

    @PostMapping(value = "/save")
    public DetailResponseData<CatsCategory> saveOrUpdate(@RequestBody CatsCategoryRequestDTO dto) {
        return catsCategoryService.saveOrUpdate(dto);
    }

    @PostMapping(value = "/delete")
    public DetailResponseData<CatsCategory> delete(@RequestBody CatsCategoryRequestDTO dto) {
        return catsCategoryService.delete(dto);
    }

    @PostMapping(value = "/get-detail")
    public DetailResponseData<CatsCategory> getDetail(@RequestBody CatsCategoryRequestDTO dto) {
        return catsCategoryService.getDetail(dto);
    }

}
