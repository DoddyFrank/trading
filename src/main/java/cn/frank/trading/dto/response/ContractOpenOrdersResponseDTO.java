package cn.frank.trading.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class ContractOpenOrdersResponseDTO extends BaseResponseDTO {

    private ContractOpenOrdersData data;

}
