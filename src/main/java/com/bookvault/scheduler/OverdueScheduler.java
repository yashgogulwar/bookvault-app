package com.bookvault.scheduler;


import com.bookvault.service.LoanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OverdueScheduler {

    private final LoanService loanService;

    @Scheduled(cron = "0 0 0 * * *") // Every midnight
    public void checkOverdueLoans() {
        log.info("[SCHEDULER] Running daily overdue loan check...");
        loanService.updateOverdueStatuses();
        log.info("[SCHEDULER] Overdue loan check complete.");
    }
}
