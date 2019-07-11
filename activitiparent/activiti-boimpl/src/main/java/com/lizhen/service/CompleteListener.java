package com.lizhen.service;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

/**
 * @author xiaodell
 */
public class CompleteListener implements ExecutionListener {
    @Override
    public void notify(DelegateExecution execution) {
        System.out.println("投资者签约结束！");
    }
}
