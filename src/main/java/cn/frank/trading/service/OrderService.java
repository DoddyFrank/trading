package cn.frank.trading.service;

import cn.frank.trading.dto.request.Order;
import cn.frank.trading.dto.response.BatchOrderResponseDTO;
import cn.frank.trading.dto.response.OrderResponseDTO;
import org.apache.http.HttpException;

import java.io.IOException;
import java.util.List;

public interface OrderService {

    OrderResponseDTO order(Order orderRequestDTO) throws IOException;

    //批量下单
    BatchOrderResponseDTO batchOrder(List<Order> orders) throws IOException, HttpException;


}
