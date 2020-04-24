package cn.frank.trading.dto.request;

import com.alibaba.fastjson.annotation.JSONField;
import cn.frank.trading.constants.Currency;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Generated
public class Order {

    public String symbol;
    @JSONField(name = "contract_type")
    public String contractType;
    @JSONField(name = "client_order_id")
    public String clientOrderId;
    @JSONField(name = "contract_code")
    public String contractCode;
    public String price;
    public String volume;
    public String direction;
    public String offset;
    @JSONField(name = "lever_rate")
    public String leverRate;
    @JSONField(name = "order_price_type")
    public String orderPriceType;

    public static Order buyOpen(Currency currency, String contractCode, long volume) {
        return Order.builder()
                .symbol(currency.getSymbol())
                .contractType("quarter")
                .contractCode(contractCode)
                .clientOrderId("")
                .direction("buy")
                .offset("open")
                .leverRate("5")
                .volume(String.valueOf(volume))
                .orderPriceType("opponent").build();
    }

    public static Order buyClose(Currency currency, String contractCode, long volume) {
        return Order.builder()
                .symbol(currency.getSymbol())
                .contractType("quarter")
                .contractCode(contractCode)
                .clientOrderId("")
                .direction("buy")
                .offset("close")
                .leverRate("5")
                .volume(String.valueOf(volume))
                .orderPriceType("opponent").build();
    }

    public static Order sellOpen(Currency currency, String contractCode, long volume) {
        return Order.builder()
                .symbol(currency.getSymbol())
                .contractType("quarter")
                .contractCode(contractCode)
                .clientOrderId("")
                .direction("sell")
                .offset("open")
                .leverRate("5")
                .volume(String.valueOf(volume))
                .orderPriceType("opponent").build();
    }

    public static Order sellClose(Currency currency, String contractCode, long volume) {
        return Order.builder()
                .symbol(currency.getSymbol())
                .contractType("quarter")
                .contractCode(contractCode)
                .clientOrderId("")
                .direction("sell")
                .offset("close")
                .leverRate("5")
                .volume(String.valueOf(volume))
                .orderPriceType("opponent").build();
    }

}
