package tran.tuananh.elearning.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import tran.tuananh.elearning.dto.response.SysMenuResponseDTO;
import tran.tuananh.elearning.entity.SysMenu;
import tran.tuananh.elearning.repository.SysMenuRepository;
import tran.tuananh.elearning.response.GenerateResponse;
import tran.tuananh.elearning.response.ListResponseData;
import tran.tuananh.elearning.service.SysMenuService;
import tran.tuananh.elearning.util.Mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    private SysMenuRepository sysMenuRepository;

    @Autowired
    private Mapper mapper;

    @Override
    public ListResponseData<List<SysMenuResponseDTO>> getAll() {
        List<SysMenu> sysMenuList = sysMenuRepository.findAllByIsActiveIsTrueAndIsDeleteIsFalse();
        List<SysMenuResponseDTO> sysMenuResponseDTOList = convertToMenuTree(sysMenuList);
        return GenerateResponse.generateListResponseData("Get menu tree successfully", sysMenuResponseDTOList, (long) sysMenuResponseDTOList.size(), HttpStatus.OK.value(), null, null);
    }

    private List<SysMenuResponseDTO> convertToMenuTree(List<SysMenu> sysMenuList) {
        Map<Integer, List<SysMenuResponseDTO>> childrenMap = new HashMap<>();
        List<SysMenuResponseDTO> root = new ArrayList<>();

        List<SysMenuResponseDTO> sysMenuResponseDTOList = mapper.fromToList(sysMenuList, SysMenuResponseDTO.class);

        for (SysMenuResponseDTO sysMenuResponseDTO : sysMenuResponseDTOList) {
            Integer parentId = sysMenuResponseDTO.getParentId();
            List<SysMenuResponseDTO> children = childrenMap.get(parentId);
            if (children == null) {
                children = new ArrayList<>();
                childrenMap.put(parentId, children);
            }
            children.add(sysMenuResponseDTO);
        }

        // Create root nodes and add their children
        for (SysMenuResponseDTO sysMenuResponseDTO : sysMenuResponseDTOList) {
            Integer parentId = sysMenuResponseDTO.getParentId();
            if (parentId == 0) {
                // Root node
                root.add(sysMenuResponseDTO);
            } else {
                // Non-root node
                List<SysMenuResponseDTO> children = childrenMap.get(parentId);
                if (children != null) {
                    SysMenuResponseDTO parent = sysMenuResponseDTOList.stream().filter(e -> e.getId() == parentId).findFirst().orElse(null);
                    if (parent != null) {
                        parent.setChildren(children);
                    }
                } else {
                    sysMenuResponseDTO.setChildren(new ArrayList<>());
                }
            }
        }
        return root;
    }
}
