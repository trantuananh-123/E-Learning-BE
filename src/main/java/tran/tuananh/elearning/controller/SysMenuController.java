package tran.tuananh.elearning.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tran.tuananh.elearning.dto.response.SysMenuResponseDTO;
import tran.tuananh.elearning.response.ListResponseData;
import tran.tuananh.elearning.service.SysMenuService;

import java.util.List;

@RestController
@RequestMapping(value = "sys-menu")
public class SysMenuController {

    @Autowired
    public SysMenuService sysMenuService;

    @GetMapping(value = "/get-all")
    public ListResponseData<List<SysMenuResponseDTO>> getAll() {
        return sysMenuService.getAll();
    }


}
