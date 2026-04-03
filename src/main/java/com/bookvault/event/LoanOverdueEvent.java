package com.bookvault.event;

import com.bookvault.entity.Loan;
import org.springframework.context.ApplicationEvent;

public class LoanOverdueEvent extends ApplicationEvent {

    private final Loan loan;

    public LoanOverdueEvent(Object source, Loan loan) {
        super(source);
        this.loan = loan;
    }

    public Loan getLoan() {
        return loan;
    }
}
