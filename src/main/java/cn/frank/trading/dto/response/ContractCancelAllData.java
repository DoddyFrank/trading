package cn.frank.trading.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ContractCancelAllData {

    private List<ContractCancelAllDataErr> errors;

    private String successes;

}
