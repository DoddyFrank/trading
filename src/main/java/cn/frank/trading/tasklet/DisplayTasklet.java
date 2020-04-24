package cn.frank.trading.tasklet;

import cn.frank.trading.api.HuobiApi;
import cn.frank.trading.constants.Currency;
import cn.frank.trading.controller.TradingController;
import cn.frank.trading.dto.response.ContractPositionInfoData;
import cn.frank.trading.dto.response.ContractPositionInfoResponseDTO;
import cn.frank.trading.dto.response.FutureContractAccountData;
import cn.frank.trading.dto.response.FutureContractAccountResponseDTO;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;

@Service
@Lazy(false)
@Slf4j
@Setter
public class DisplayTasklet {

    @Autowired
    private TradingController tradingController;

    @Autowired
    private HuobiApi huobiApi;


    @Scheduled(cron = "0/5 * *  * * ? ")
    public void syncDisplay() throws IOException, HttpException {
        Currency currency = Currency.BTC;
        FutureContractAccountData accountData;
        ContractPositionInfoData positionInfoData;

        FutureContractAccountResponseDTO accountInfo = huobiApi.futureContractAccountInfo(currency.getSymbol());
        if (CollectionUtils.isEmpty(accountInfo.getData())) {
            log.info("目前没有持仓");
            return;
        } else if (accountInfo.getData().size() == 1) {
            accountData = accountInfo.getData().get(0);
        } else {
            log.info("不考虑多种仓位");
            return;
        }

        ContractPositionInfoResponseDTO positionInfo = huobiApi.futureContractPositionInfo(currency.getSymbol());
        if (CollectionUtils.isEmpty(positionInfo.getData())) {
            log.info("目前没有持仓");
        } else if (positionInfo.getData().size() == 1) {
            positionInfoData = positionInfo.getData().get(0);
            tradingController.refreshPositionDisplay(accountData, positionInfoData);
        } else {
            log.info("不考虑多种仓位");
        }
    }

}
