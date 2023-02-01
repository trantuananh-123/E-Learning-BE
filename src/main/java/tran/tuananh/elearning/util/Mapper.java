package tran.tuananh.elearning.util;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class Mapper {

    public <F, T> T fromTo(F input, Class<T> destinationClass) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        return modelMapper.map(input, destinationClass);
    }

    public <F, T> List<T> fromToList(List<F> input, Class<T> destinationClass) {
        return input.stream().map(item -> fromTo(item, destinationClass)).collect(Collectors.toList());
    }
}
