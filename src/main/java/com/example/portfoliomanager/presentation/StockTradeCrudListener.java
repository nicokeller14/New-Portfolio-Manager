package com.example.portfoliomanager.presentation;

import com.example.portfoliomanager.model.Portfolio;
import com.example.portfoliomanager.model.Stock;
import com.example.portfoliomanager.model.StockTrade;
import com.example.portfoliomanager.service.PortfolioService;
import com.example.portfoliomanager.service.StockService;
import com.example.portfoliomanager.service.StockTradeService;
import com.example.portfoliomanager.viewmodel.StockTradeViewModel;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.vaadin.crudui.crud.CrudListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@SpringComponent
public class StockTradeCrudListener implements CrudListener<StockTradeViewModel> {

    private final StockTradeService stockTradeService;

    private final PortfolioService portfolioService;

    private final StockService stockService;

    public StockTradeCrudListener(StockTradeService stockTradeService, PortfolioService portfolioService, StockService stockService) {
        this.stockTradeService = stockTradeService;
        this.portfolioService = portfolioService;
        this.stockService = stockService;
    }

    @Override
    public Collection<StockTradeViewModel> findAll() {
        List<StockTrade> stockTradeList = stockTradeService.findAllStockTrades();
        List<StockTradeViewModel> stockTradeViewModelList = new ArrayList<>();
        for (StockTrade stockTrade : stockTradeList) {
            stockTradeViewModelList.add(new StockTradeViewModel(stockTrade.getTransactionId(), stockTrade.getStock().getTickerSymbol(), stockTrade.getStockPrice(), stockTrade.getTransactionAmount(), stockTrade.getDate(), stockTrade.getPortfolio().getPortfolioId()));
        }
        return stockTradeViewModelList;
    }

    @Override
    public StockTradeViewModel add(StockTradeViewModel stockTrade) {
        List<StockTrade> stockTradeList = stockTradeService.findAllStockTrades();
        boolean exists = false;
        for (StockTrade s : stockTradeList) {
            if (s.getTransactionId() == stockTrade.getTransactionId()) {
                Notification.show("StockTrade with this ID already exists!", 3000, Notification.Position.MIDDLE);
                exists = true;
            }
        }
        if (!exists) {
            List<Portfolio> portfolioList = portfolioService.findAllPortfolios();
            Portfolio portfolio = null;
            for (Portfolio p : portfolioList) {
                if (p.getPortfolioId() == stockTrade.getPortfolioId()) {
                    portfolio = p;
                }
            }
            List<Stock> stockList = stockService.findAllStocks();
            Stock stock = null;
            for (Stock s : stockList) {
                if (s.getTickerSymbol().equals(stockTrade.getTickerSymbol().toUpperCase())) {
                    stock = s;
                }
            }
            if (portfolio == null) {
                Notification.show("Portfolio with this ID does not exist!", 3000, Notification.Position.MIDDLE);
            } else if (stock == null) {
                Notification.show("Stock with this ticker symbol does not exist!", 3000, Notification.Position.MIDDLE);
            } else {
                StockTrade s = new StockTrade(stockTrade.getTransactionId(), stockTrade.getStockPrice(), stockTrade.getTransactionAmount(), stockTrade.getDate(), portfolio, stock);
                stockTradeService.addStockTrade(s);
                portfolio.getStockTransactions().add(s);
                stock.getStockTransactions().add(s);
            }
            stockTradeService.addStockTrade(new StockTrade(stockTrade.getTransactionId(), stockTrade.getStockPrice(), stockTrade.getTransactionAmount(), stockTrade.getDate(), portfolioService.findPortfolioById(stockTrade.getPortfolioId()), stockService.findStockByTickerSymbol(stockTrade.getTickerSymbol())));
        }
        return stockTrade;
    }

    @Override
    public StockTradeViewModel update(StockTradeViewModel stockTrade) {
        List<StockTrade> stockTradeList = stockTradeService.findAllStockTrades();
        for (StockTrade s : stockTradeList) {
            if (s.getTransactionId() == stockTrade.getTransactionId()) {
                s.setStockPrice(stockTrade.getStockPrice());
                s.setTransactionAmount(stockTrade.getTransactionAmount());
                s.setDate(stockTrade.getDate());
                stockTradeService.updateStockTrade(s);
            }
        }
        return stockTrade;
    }

    @Override
    public void delete(StockTradeViewModel stockTrade) {
        List<StockTrade> stockTradeList = stockTradeService.findAllStockTrades();
        for (StockTrade s : stockTradeList) {
            if (s.getTransactionId() == stockTrade.getTransactionId()) {
                stockTradeService.deleteStockTradeById(s.getTransactionId());
            }
        }
    }
}
