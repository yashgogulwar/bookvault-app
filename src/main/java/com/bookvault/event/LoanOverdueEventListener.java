package com.bookvault.event;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LoanOverdueEventListener {

    @Async
    @EventListener
    public void handleLoanOverdue(LoanOverdueEvent event) {
        var loan = event.getLoan();
        log.info("[NOTIFICATION SENT] Overdue notification for member '{}' - Book: '{}' was due on {}",
                loan.getMember().getName(),
                loan.getBook().getTitle(),
                loan.getDueDate());
    }
}
