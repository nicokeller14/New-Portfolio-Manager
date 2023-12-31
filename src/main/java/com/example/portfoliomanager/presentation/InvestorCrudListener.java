package com.example.portfoliomanager.presentation;

import com.example.portfoliomanager.model.Investor;
import com.example.portfoliomanager.service.InvestorService;
import com.example.portfoliomanager.viewmodel.InvestorViewModel;
import com.vaadin.flow.component.notification.Notification;
import org.vaadin.crudui.crud.CrudListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.vaadin.flow.spring.annotation.SpringComponent;

@SpringComponent
public class InvestorCrudListener implements CrudListener<InvestorViewModel> {

    private final InvestorService investorService;

    public InvestorCrudListener(InvestorService investorService) {
        this.investorService = investorService;
    }

    @Override
    public Collection<InvestorViewModel> findAll() {
        List<Investor> investorList = investorService.findAllInvestors();
        List <InvestorViewModel> investorViewModelList = new ArrayList<>();
        for (Investor investor : investorList) {
            InvestorViewModel i = new InvestorViewModel(investor.getInvestorId(), investor.getFirstName(), investor.getLastName(), investor.getEmail());
            investorViewModelList.add(i);
        }
        return investorViewModelList;
    }

    @Override
    public InvestorViewModel add(InvestorViewModel investor) {
        List<Investor> investorList = investorService.findAllInvestors();
        boolean exists = false;
        for (Investor i : investorList) {
            if (i.getInvestorId() == investor.getInvestorId()) {
                Notification.show("Investor with this ID already exists!", 3000, Notification.Position.MIDDLE);
                exists = true;
            }
        }
        if (!exists) {
            Investor i = new Investor(investor.getInvestorId(), investor.getFirstName(), investor.getLastName(), investor.getEmail());
            investorService.addInvestor(i);
        }

        return investor;
    }

    @Override
    public InvestorViewModel update(InvestorViewModel investor) {
        List<Investor> investorList = investorService.findAllInvestors();
        for (Investor i : investorList) {
            if (i.getInvestorId() == investor.getInvestorId()) {
                i.setFirstName(investor.getFirstName());
                i.setLastName(investor.getLastName());
                i.setEmail(investor.getEmail());
                investorService.updateInvestor(i);
            }
        }
        return investor;
    }

    @Override
    public void delete(InvestorViewModel investor) {
        List<Investor> investorList = investorService.findAllInvestors();
        for (Investor i : investorList) {
            if (i.getInvestorId() == investor.getInvestorId()) {
                investorService.deleteInvestor(i);
            }
        }
    }
}
