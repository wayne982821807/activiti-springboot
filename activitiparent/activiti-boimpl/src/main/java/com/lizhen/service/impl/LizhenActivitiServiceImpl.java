package com.lizhen.service.impl;


import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.google.common.collect.Lists;
import com.lizhen.service.LizhenActivitiService;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.ProcessEngineImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service(version = "1.0.0",timeout = 6000,interfaceClass = LizhenActivitiService.class)
public class LizhenActivitiServiceImpl implements LizhenActivitiService {
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ProcessEngine processEngine;

    @Override
    public String deployByKey(String path) {

        Deployment d = repositoryService.createDeployment()
                .addClasspathResource(path)
                .deploy();
        return d.toString();
    }

    @Override
    public ProcessInstance startProcessByKey(String key, Map<String, Object> vars) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(key, vars);
        return processInstance;


    }

    @Override
    public void claimTask(String taskId, String userId) {
        taskService.claim(taskId, userId);
    }

    @Override
    public String startProcessByKey(String key, String businessKey, Map<String, Object> vars) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(key, businessKey, vars);
        if (processInstance != null) {
            return processInstance.getId();
        }
        return null;
    }

    @Override
    public List<Task> queryTaskListByUser(String userid) {
        List<Task> tasks = taskService
                .createTaskQuery()
                .taskAssignee(userid)
                .orderByTaskCreateTime()
                .desc()
                .list();
        // 根据当前人未签收的任务
        List<Task> unsignedTasks = taskService
                .createTaskQuery()
                .taskCandidateUser(userid)
                .orderByTaskCreateTime()
                .desc()
                .list();
        tasks.addAll(unsignedTasks);

        return tasks;


    }

    @Override
    public String complete(String taskId, Map<String, Object> vars) {
        taskService.complete(taskId, vars);
        return "task is completed";
    }

    @Override
    public void completeByPiId(String processInstanceId) {
        List<Task> piList = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        if (piList != null && piList.size() > 0) {
            taskService.complete(piList.get(0).getId());
        }


    }

    @Override
    public List<HistoricProcessInstance> queryHistoricInstance() {
        List<HistoricProcessInstance> hpi = historyService.createHistoricProcessInstanceQuery().list();

        return hpi;
    }

    @Override
    public List<HistoricActivityInstance> queryHistoricActivityInstance() {
        return historyService.createHistoricActivityInstanceQuery().list();
    }

    @Override
    public List<HistoricTaskInstance> queryHistoricTaskInstance() {
        return historyService.createHistoricTaskInstanceQuery().list();
    }

    @Override
    public Task queryTaskByBusinessId(String businessId, String processDefKey, String userId) {
        ProcessInstance pi = queryProInstanceByBusinessId(businessId, processDefKey);
        Task task = null;
        if (pi != null) {
            task = taskService.createTaskQuery()
                    .processInstanceId(pi.getId())
                    //.taskAssignee(userId)
                    // .taskCandidateUser(userId)
                    .taskCandidateOrAssigned(userId)
                    .singleResult();


        }
        return task;
    }

    @Override
    public Task queryTaskByBusinessId(String businessId, String processDefKey) {
        ProcessInstance pi = queryProInstanceByBusinessId(businessId, processDefKey);
        Task task = null;
        if (pi != null) {
            task = taskService.createTaskQuery()
                    .processInstanceId(pi.getId())
                    //.taskAssignee(userId)
                    // .taskCandidateUser(userId)
                    //.taskCandidateOrAssigned(userId)
                    .singleResult();


        }
        return task;
    }

    @Override
    public Task queryTaskByProInsId(String proInsId) {
        return taskService.createTaskQuery()
                .processInstanceId(proInsId)
                .singleResult();
    }

    @Override
    public Task queryTaskByProInsId(String proInsId, String userId) {
        return taskService.createTaskQuery()
                .processInstanceId(proInsId)
                .taskCandidateOrAssigned(userId)
                .singleResult();
    }

    @Override
    public InputStream tracePhoto(String processDefinitionId, String executionId) {
//		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(executionId).singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);

        List<String> activeActivityIds = Lists.newArrayList();
        if (runtimeService.createExecutionQuery().executionId(executionId).count() > 0) {
            activeActivityIds = runtimeService.getActiveActivityIds(executionId);
        }

        // 不使用spring请使用下面的两行代码
        ProcessEngineImpl defaultProcessEngine = (ProcessEngineImpl) ProcessEngines.getDefaultProcessEngine();
        Context.setProcessEngineConfiguration(defaultProcessEngine.getProcessEngineConfiguration());

        // 使用spring注入引擎请使用下面的这行代码
        //       Context.setProcessEngineConfiguration(processEngineFactory.getProcessEngineConfiguration());
//		return ProcessDiagramGenerator.generateDiagram(bpmnModel, "png", activeActivityIds);
        return processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator()
                //.generateDiagram(bpmnModel, "png","宋体", activeActivityIds);
                .generateDiagram(bpmnModel, "png", activeActivityIds);
    }


    public void queryProcessStatus() {
        taskService.createTaskQuery().singleResult();

    }

    /**
     * 根据taskId查找businessKey
     */
    public String queryBusinessKeyByTaskId(String taskId) {
        Task task = taskService
                .createTaskQuery()
                .taskId(taskId)
                .singleResult();
        ProcessInstance pi = runtimeService
                .createProcessInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .singleResult();
        return pi.getBusinessKey();
    }

    public ProcessInstance queryProInstanceByTaskId(String taskId) {
        Task task = taskService
                .createTaskQuery()
                .taskId(taskId)
                .singleResult();
        ProcessInstance pi = runtimeService
                .createProcessInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .singleResult();
        return pi;

    }

    /**
     * 根据 processDefinitionKey查找ProcessInstance
     *
     * @param businessId
     * @return
     */
    @Override
    public ProcessInstance queryProInstanceByBusinessId(String businessId, String processDefinitionKey) {
//        Task task = taskService
//                .createTaskQuery()
//                .processInstanceBusinessKey(businessId)
//                .singleResult();
        ProcessInstance pi = runtimeService
                .createProcessInstanceQuery()
                .processInstanceBusinessKey(businessId, processDefinitionKey)
                .singleResult();

        return pi;

    }

    @Override
    public HistoricProcessInstance queryHisProInstanceByBusinessId(String businessId, String processDefinitionKey) {
        HistoricProcessInstance hpi= historyService.createHistoricProcessInstanceQuery()
                .processDefinitionKey(processDefinitionKey)
                .processInstanceBusinessKey(businessId)
                .singleResult();
        return  hpi;


    }

    @Override
    public HistoricProcessInstance queryHisProInstance(String procInsId) {
        return historyService.createHistoricProcessInstanceQuery().processInstanceId(procInsId)
                .singleResult();
    }

    @Override
    public ProcessInstance queryProcInstance(String procInsId) {
        return runtimeService.createProcessInstanceQuery().processInstanceId(procInsId).singleResult();
    }

    /**
     * 根据任务获取当前流程节点key值
     *
     * @param task
     * @return
     */
    public String queryProActiveKey(Task task) {
        String excId = task.getExecutionId();
        ExecutionEntity execution = (ExecutionEntity) runtimeService.createExecutionQuery().executionId(excId).singleResult();
        String activitiId = execution.getActivityId();
        String proDefKey = execution.getProcessDefinitionKey();
        return proDefKey;
        //
    }


    @Override
    public void revoke(String procInsId) throws Exception {

        Task task = taskService.createTaskQuery().processInstanceId(procInsId).singleResult();
        if(task==null) {
            throw new Exception("流程未启动或已执行完成，无法撤回");
        }

        //LoginUser loginUser = SessionContext.getLoginUser();
        String userName="userName";
        List<HistoricTaskInstance> htiList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(procInsId)
                .orderByTaskCreateTime()
                .asc()
                .list();
        String myTaskId = null;
        HistoricTaskInstance myTask = null;
        for(HistoricTaskInstance hti : htiList) {
            //if(userName.equals(hti.getAssignee())) {
            if(true) {
                myTaskId = hti.getId();
                myTask = hti;
                break;
            }
        }
        if(null==myTaskId) {
            throw new Exception("该任务非当前用户提交，无法撤回");
        }

        String processDefinitionId = myTask.getProcessDefinitionId();
        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);

        //变量
//		Map<String, VariableInstance> variables = runtimeService.getVariableInstances(currentTask.getExecutionId());
        String myActivityId = null;
        List<HistoricActivityInstance> haiList = historyService.createHistoricActivityInstanceQuery()
                .executionId(myTask.getExecutionId()).finished().list();
        for(HistoricActivityInstance hai : haiList) {
            if(myTaskId.equals(hai.getTaskId())) {
                myActivityId = hai.getActivityId();
                break;
            }
        }
        FlowNode myFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(myActivityId);
        Execution execution = runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
        String activityId = execution.getActivityId();
        FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(activityId);
        //记录原活动方向
        List<SequenceFlow> oriSequenceFlows = new ArrayList<SequenceFlow>();
        oriSequenceFlows.addAll(flowNode.getOutgoingFlows());
        //清理活动方向
        flowNode.getOutgoingFlows().clear();
        //建立新方向
        List<SequenceFlow> newSequenceFlowList = new ArrayList<SequenceFlow>();
        SequenceFlow newSequenceFlow = new SequenceFlow();
        newSequenceFlow.setId("newSequenceFlowId");
        newSequenceFlow.setSourceFlowElement(flowNode);
        newSequenceFlow.setTargetFlowElement(myFlowNode);
        newSequenceFlowList.add(newSequenceFlow);
        flowNode.setOutgoingFlows(newSequenceFlowList);
        Authentication.setAuthenticatedUserId(userName);
        taskService.addComment(task.getId(), task.getProcessInstanceId(), "撤回");
        Map<String,Object> currentVariables = new HashMap<String,Object>();
        currentVariables.put("applier", userName);
        //完成任务
        taskService.complete(task.getId(),currentVariables);
        //恢复原方向
        flowNode.setOutgoingFlows(oriSequenceFlows);
    }


    @Override
    public String getHisVar(String proInsId,String varName){
        if(StringUtils.isBlank(proInsId)|| StringUtils.isBlank(varName)){
            return null;
        }

        HistoricVariableInstance hvi=historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(proInsId)
                .variableName(varName)
                .singleResult();
        if (hvi == null) {
            return null;
        }
        return String.valueOf(hvi.getValue());
    }


}
