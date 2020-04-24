package cn.frank.trading.controller;

import cn.frank.trading.dto.response.ContractPositionInfoData;
import cn.frank.trading.dto.response.FutureContractAccountData;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@FXMLController
@Setter
@Slf4j
public class TradingController {

    @FXML
    private Label accountTotalBalance;
    @FXML
    private Label marginCloseoutPrice;
    @FXML
    private Label marginRate;
    @FXML
    private Label contractType;
    @FXML
    private Label holdingPosition;
    @FXML
    private Label averageOpenPrice;
    @FXML
    private Label profit;
    @FXML
    private Label profitRate;
    @FXML
    private Label availableClosePosition;
    @FXML
    private Label averageHoldingPrice;
    @FXML
    private Label margin;


    public void refreshPositionDisplay(FutureContractAccountData accountData, ContractPositionInfoData positionInfoData) {
        log.info("positionInfoData: {}", positionInfoData);
        Platform.runLater(() -> {
            accountTotalBalance.setText(accountData.getMarginBalance().toString().substring(0, 6));
            marginCloseoutPrice.setText(accountData.getLiquidationPrice() != null ? accountData.getLiquidationPrice().setScale(2, 0).toString() : "");
            marginRate.setText(accountData.getRiskRate().multiply(new BigDecimal("100")).setScale(2, 0).toString() + "%");

            contractType.setText(positionInfoData.getContractCode());
            holdingPosition.setText(positionInfoData.getVolume().toString());
            averageOpenPrice.setText(positionInfoData.getCostOpen().toString().substring(0, 6));
            profit.setText(positionInfoData.getProfit().toString().substring(0, 6));
            profitRate.setText(positionInfoData.getProfitRate().divide(new BigDecimal("0.01"), 2, 0).toString() + "%");
            availableClosePosition.setText(positionInfoData.getAvailable().toString());
            averageHoldingPrice.setText(positionInfoData.getCostHold().toString().substring(0, 6));
            margin.setText(positionInfoData.getPositionMargin().toString().substring(0, 6));
        });

        log.info("positionInfoData: {}", positionInfoData);
    }

}

