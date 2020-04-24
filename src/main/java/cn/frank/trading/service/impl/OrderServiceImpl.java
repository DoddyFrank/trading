package cn.frank.trading.service.impl;

import com.alibaba.fastjson.JSON;
import cn.frank.trading.api.HuobiApi;
import cn.frank.trading.dto.request.Order;
import cn.frank.trading.dto.response.BatchOrderResponseDTO;
import cn.frank.trading.dto.response.OrderResponseDTO;
import cn.frank.trading.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final HuobiApi huobiApi;

    @Autowired
    public OrderServiceImpl(HuobiApi huobiApi) {
        this.huobiApi = huobiApi;
    }

    @Override
    public OrderResponseDTO order(Order order) throws IOException {

        log.info("下单数据: {}", order);

        String response = huobiApi.futureContractOrder(order.getSymbol(),
                order.getContractType(),
                order.getContractCode(),
                order.getClientOrderId(),
                order.getPrice(),
                order.getVolume(),
                order.getDirection(),
                order.getOffset(),
                order.getLeverRate(),
                order.getOrderPriceType());
        return JSON.parseObject(response, OrderResponseDTO.class);
    }

    @Override
    public BatchOrderResponseDTO batchOrder(List<Order> orders) throws IOException, HttpException {

        log.info("下单数据: {}", orders);

        String response = huobiApi.futureContractBatchOrder(orders);

        return JSON.parseObject(response, BatchOrderResponseDTO.class);
    }

}
