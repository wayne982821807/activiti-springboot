package com.lizhen.service;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

public class SignListener implements ExecutionListener {


    @Override
    public void notify(DelegateExecution delegateExecution) {
        System.out.println("基金合同签署完成，特此通知！");
    }
}
