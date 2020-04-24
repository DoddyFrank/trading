package cn.frank.trading.api.impl;

import com.alibaba.fastjson.JSON;
import cn.frank.trading.api.HuobiApi;
import cn.frank.trading.dto.request.Order;
import cn.frank.trading.dto.response.*;
import cn.frank.trading.util.HttpUtil;
import cn.frank.trading.util.HuobiHttpClient;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Data
public class HuobiApiImpl implements HuobiApi {

    private static final String HUOBI_FUTURE_TICKER = "/market/detail/merged?symbol=%s";
    private static final String HUOBI_FUTURE_DEPTH = "/market/depth";
    private static final String HUOBI_FUTURE_KLINE = "/market/history/kline?symbol=%s&period=%s&size=%s";
    private static final String HUOBI_FUTURE_TRADE = "/market/history/trade?symbol=%s&size=%s";
    // 获取合约信息 /v1/contract_contract_info
    private static final String HUOBI_FUTURE_CONTRACT_INFO = "/api/v1/contract_contract_info?symbol=%s&contract_type=%s&contract_code=%s";
    // 获取合约指数v1/contract_index
    private static final String HUOBI_FUTURE_CONTRACT_INDEX = "/api/v1/contract_index?symbol=%s";
    // 获取合约最低最高限价/v1/contract_price_limit
    private static final String HUOBI_FUTURE_CONTRACT_PRICE_LIMIT = "/api/v1/contract_price_limit?symbol=%s&contract_type=%s&contract_code=%s";
    // 获取合约总持仓量
    private static final String HUOBI_FUTURE_CONTRACT_OPEN_INTEREST = "/api/v1/contract_open_interest?symbol=%s&contract_type=%s&contract_code=%s";
    // 订单明细
    private static final String HUOBI_FUTURE_CONTRACT_ORDER_DETAIL = "/api/v1/contract_order_detail";
    private static final String HUOBI_FUTURE_CONTRACT_HISORDERS = "/api/v1/contract_hisorders";
    // 批量下单contract_batchorder
    private static final String HUOBI_FUTURE_CONTRACT_BATCHORDER = "/api/v1/contract_batchorder";
    // account相关
    private static final String HUOBI_FUTURE_ACCOUNT_INFO = "/api/v1/contract_account_info";
    private static final String HUOBI_FUTURE_POSITION_INFO = "/api/v1/contract_position_info";
    private static final String HUOBI_FUTURE_ORDER = "/api/v1/contract_order";
    private static final String HUOBI_FUTURE_ORDER_CANCEL = "/api/v1/contract_cancel";
    private static final String HUOBI_FUTURE_ORDER_INFO = "/api/v1/contract_order_info";
    private static final String HUOBI_FUTURE_ORDER_CANCEL_ALL = "/api/v1/contract_cancelall";
    private static final String HUOBI_CONTRACE_CODE = "/api/v1/contract_open_interest";
    private static final String HUOBI_CONTRACE_OPENORDERS = "/api/v1/contract_openorders";
    @Value("${huobi.secret.key}")
    private String secretKey;
    @Value("${huobi.api.key}")
    private String apiKey;
    @Value("${huobi.url.prefix}")
    private String urlPrefix;

    private final HuobiHttpClient huobiHttpClient;
    private final HttpUtil httpUtil;

    @Autowired
    public HuobiApiImpl(HuobiHttpClient huobiHttpClient, HttpUtil httpUtil) {
        this.huobiHttpClient = huobiHttpClient;
        this.httpUtil = httpUtil;
    }

    @Override
    public ContractInfoResponseDTO futureContractInfo(String symbol, String contractType, String contractCode) {

        String url = String.format(urlPrefix + HUOBI_FUTURE_CONTRACT_INFO, symbol, contractType, contractCode);

        String response = httpUtil.getForObject(url, String.class);

        return JSON.parseObject(response, ContractInfoResponseDTO.class);
    }

    @Override
    public ContractIndexResponseDTO futureContractIndex(String symbol) {
        String url = String.format(urlPrefix + HUOBI_FUTURE_CONTRACT_INDEX, symbol);

        String response = httpUtil.getForObject(url, String.class);

        return JSON.parseObject(response, ContractIndexResponseDTO.class);
    }

    @Override
    public PriceLimitResponseDTO futurePriceLimit(String symbol, String contractType, String contractCode) {


        String url = String.format(urlPrefix + HUOBI_FUTURE_CONTRACT_PRICE_LIMIT, symbol, contractType, contractCode);

        String response = httpUtil.getForObject(url, String.class);

        return JSON.parseObject(response, PriceLimitResponseDTO.class);
    }

    @Override
    public OpenInterestResponseDTO futureOpenInterest(String symbol, String contractType, String contractCode) {

        String url = String.format(urlPrefix + HUOBI_FUTURE_CONTRACT_OPEN_INTEREST, symbol, contractType, contractCode);

        String response = httpUtil.getForObject(url, String.class);

        return JSON.parseObject(response, OpenInterestResponseDTO.class);
    }

    public String futureMarketDepth(String symbol, String type) {
        Map<String, String> params = new HashMap<>();
        if (!StringUtils.isEmpty(symbol)) {
            params.put("symbol", symbol);
        }
        if (!StringUtils.isEmpty(type)) {
            params.put("type", type);
        }
        return huobiHttpClient.doGet(urlPrefix + HUOBI_FUTURE_DEPTH, params);
    }

    public MarketHistoryKlineResponseDTO futureMarketHistoryKline(String symbol, String period, String size) {


        String url = String.format(urlPrefix + HUOBI_FUTURE_KLINE, symbol, period, size);

        String response = httpUtil.getForObject(url, String.class);

        return JSON.parseObject(response, MarketHistoryKlineResponseDTO.class);

    }

    public MarketDetailMergedResponseDTO futureMarketDetailMerged(String symbol) {
        String url = String.format(urlPrefix + HUOBI_FUTURE_TICKER, symbol);

        String response = httpUtil.getForObject(url, String.class);

        return JSON.parseObject(response, MarketDetailMergedResponseDTO.class);
    }

    public MarketDetailTradeResponseDTO futureMarketDetailTrade(String symbol, String size) {
        String url = String.format(urlPrefix + HUOBI_FUTURE_TRADE, symbol, size);

        String response = httpUtil.getForObject(url, String.class);

        return JSON.parseObject(response, MarketDetailTradeResponseDTO.class);
    }

    public MarketHistoryTradeResponseDTO futureMarketHistoryTrade(String symbol, String size) {

        String url = String.format(urlPrefix + HUOBI_FUTURE_TRADE, symbol, size);

        String response = httpUtil.getForObject(url, String.class);

        return JSON.parseObject(response, MarketHistoryTradeResponseDTO.class);
    }

    public FutureContractAccountResponseDTO futureContractAccountInfo(String symbol) throws IOException {
        Map<String, String> params = new HashMap<>();
        if (!StringUtils.isEmpty(symbol)) {
            params.put("symbol", symbol);
        }

        String response = huobiHttpClient.call(apiKey, secretKey, "POST",
                urlPrefix + HUOBI_FUTURE_ACCOUNT_INFO, params, new HashMap<>());

        return JSON.parseObject(response, FutureContractAccountResponseDTO.class);
    }

    public ContractPositionInfoResponseDTO futureContractPositionInfo(String symbol) throws IOException {
        Map<String, String> params = new HashMap<>();
        if (!StringUtils.isEmpty(symbol)) {
            params.put("symbol", symbol);
        }
        String response = huobiHttpClient.call(apiKey, secretKey, "POST",
                urlPrefix + HUOBI_FUTURE_POSITION_INFO, params, new HashMap<>());

        return JSON.parseObject(response, ContractPositionInfoResponseDTO.class);
    }

    public String futureContractOrder(String symbol, String contractType, String contractCode, String clientOrderId,
                                      String price, String volume, String direction, String offset, String leverRate, String orderPriceType) throws IOException {
        Map<String, String> params = new HashMap<>();
        if (!StringUtils.isEmpty(symbol)) {
            params.put("symbol", symbol);
        }
        if (!StringUtils.isEmpty(contractType)) {
            params.put("contract_type", contractType);
        }

        if (!StringUtils.isEmpty(contractCode)) {
            params.put("contract_code", contractCode);
        }
        if (!StringUtils.isEmpty(clientOrderId)) {
            params.put("client_order_id", clientOrderId);
        }
        if (!StringUtils.isEmpty(price)) {
            params.put("price", price);
        }
        if (!StringUtils.isEmpty(volume)) {
            params.put("volume", volume);
        }
        if (!StringUtils.isEmpty(direction)) {
            params.put("direction", direction);
        }
        if (!StringUtils.isEmpty(offset)) {
            params.put("offset", offset);
        }
        if (!StringUtils.isEmpty(leverRate)) {
            params.put("lever_rate", leverRate);
        }
        if (!StringUtils.isEmpty(orderPriceType)) {
            params.put("order_price_type", orderPriceType);
        }
        return huobiHttpClient.call(apiKey, secretKey, "POST", urlPrefix + HUOBI_FUTURE_ORDER,
                params, new HashMap<>());
    }

    public String futureContractBatchOrder(List<Order> orders) throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("orders_data", orders);
        return huobiHttpClient.call(apiKey, secretKey, "POST", urlPrefix + HUOBI_FUTURE_CONTRACT_BATCHORDER,
                params, new HashMap<>());
    }

    public String futureContractCancel(String orderId, String clientOrderId, String symbol) throws IOException {
        Map<String, String> params = new HashMap<>();
        if (!StringUtils.isEmpty(orderId)) {
            params.put("order_id", orderId);
        }
        if (!StringUtils.isEmpty(clientOrderId)) {
            params.put("client_order_id", clientOrderId);
        }
        if (!StringUtils.isEmpty(symbol)) {
            params.put("symbol", symbol);
        }
        return huobiHttpClient.call(apiKey, secretKey, "POST",
                urlPrefix + HUOBI_FUTURE_ORDER_CANCEL, params, new HashMap<>());
    }

    public ContractCancelAllResponseDTO futureContractCancelAll(String symbol) throws IOException {
        Map<String, String> params = new HashMap<>();
        if (!StringUtils.isEmpty(symbol)) {
            params.put("symbol", symbol);
        }

        String response = huobiHttpClient.call(apiKey, secretKey, "POST",
                urlPrefix + HUOBI_FUTURE_ORDER_CANCEL_ALL, params, new HashMap<>());

        return JSON.parseObject(response, ContractCancelAllResponseDTO.class);
    }

    public String futureContractOrderInfo(String orderId, String clientOrderId, String symbol, String orderType) throws IOException {
        Map<String, String> params = new HashMap<>();
        if (!StringUtils.isEmpty(orderId)) {
            params.put("order_id", orderId);
        }
        if (!StringUtils.isEmpty(clientOrderId)) {
            params.put("client_order_id", clientOrderId);
        }
        if (!StringUtils.isEmpty(symbol)) {
            params.put("symbol", symbol);
        }
        if (!StringUtils.isEmpty(symbol)) {
            params.put("order_type", orderType);
        }
        return huobiHttpClient.call(apiKey, secretKey, "POST",
                urlPrefix + HUOBI_FUTURE_ORDER_INFO, params, new HashMap<>());
    }

    public String futureContractOrderDetail(String symbol, String orderId, String pageIndex, String pageSize, String createdAt, String orderType) throws IOException {
        Map<String, String> params = new HashMap<>();
        if (!StringUtils.isEmpty(symbol)) {
            params.put("symbol", symbol);
        }
        if (!StringUtils.isEmpty(orderId)) {
            params.put("order_id", orderId);
        }
        if (!StringUtils.isEmpty(pageIndex)) {
            params.put("page_index", pageIndex);
        }
        if (!StringUtils.isEmpty(pageSize)) {
            params.put("page_size", pageSize);
        }
        if (!StringUtils.isEmpty(createdAt)) {
            params.put("created_at", createdAt);
        }
        if (!StringUtils.isEmpty(orderType)) {
            params.put("order_type", orderType);
        }
        return huobiHttpClient.call(apiKey, secretKey, "POST",
                urlPrefix + HUOBI_FUTURE_CONTRACT_ORDER_DETAIL, params, new HashMap<>());
    }

    public ContractOpenOrdersResponseDTO futureContractOpenorders(String symbol, String pageIndex, String pageSize) throws IOException {
        Map<String, String> params = new HashMap<>();
        if (!StringUtils.isEmpty(symbol)) {
            params.put("symbol", symbol);
        }
        if (!StringUtils.isEmpty(pageIndex)) {
            params.put("page_index", pageIndex);
        }
        if (!StringUtils.isEmpty(pageSize)) {
            params.put("page_size", pageSize);
        }
        String response = huobiHttpClient.call(apiKey, secretKey, "POST",
                urlPrefix + HUOBI_CONTRACE_OPENORDERS, params, new HashMap<>());

        return JSON.parseObject(response, ContractOpenOrdersResponseDTO.class);
    }

    public String futureContractHisorders(String symbol, String tradeType, String type, String status,
                                          String createDate, String pageIndex, String pageSize) throws IOException {
        Map<String, String> params = new HashMap<>();
        if (!StringUtils.isEmpty(symbol)) {
            params.put("symbol", symbol);
        }
        if (!StringUtils.isEmpty(tradeType)) {
            params.put("trade_type", tradeType);
        }
        if (!StringUtils.isEmpty(type)) {
            params.put("type", type);
        }
        if (!StringUtils.isEmpty(createDate)) {
            params.put("create_date", createDate);
        }
        if (!StringUtils.isEmpty(status)) {
            params.put("status", status);
        }
        if (!StringUtils.isEmpty(pageIndex)) {
            params.put("page_index", pageIndex);
        }
        if (!StringUtils.isEmpty(pageSize)) {
            params.put("page_size", pageSize);
        }
        return huobiHttpClient.call(apiKey, secretKey, "POST",
                urlPrefix + HUOBI_FUTURE_CONTRACT_HISORDERS, params, new HashMap<>());
    }
}
