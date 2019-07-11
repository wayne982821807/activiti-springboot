package com.lizhen.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.google.gson.Gson;
import com.lizhen.service.LizhenActivitiService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/lizhen")
public class ActivitiController {
    @Reference(version = "1.0.0",timeout = 6000,interfaceClass = LizhenActivitiService.class)
    private LizhenActivitiService lizhenActivitiService;

    private static Gson gson = new Gson();

    @RequestMapping("/deploy")
    public String deployByName(HttpServletRequest request) {
        String fileName = request.getParameter("name");
        return lizhenActivitiService.deployByKey("processes/" + fileName + ".bpmn");
    }

    @RequestMapping("/startProcess")
    public String startProcess(HttpServletRequest request) {
//        String key=request.getParameter("key");
        String key = "raise";
        String applyUser = request.getParameter("applyUser");
        String certification = request.getParameter("certification");
        String user = request.getParameter("user");
        Map<String, Object> vars = new HashMap<>(3);
        vars.put("applyUser", applyUser);
        vars.put("certification", certification);
        vars.put("user", user);
        ProcessInstance pi = lizhenActivitiService.startProcessByKey(key, vars);
        if (pi != null) {
            String piId = pi.getId();
            lizhenActivitiService.completeByPiId(piId);
        }
        return pi.toString();


    }

//    @RequestMapping("/startTemplateProcess")
//    public String startTemplateProcess(HttpServletRequest request) {
////        String key=request.getParameter("key");
//        String key = "template";
//        String admin = request.getParameter("admin");
////        String certification=request.getParameter("certification");
////        String user=request.getParameter("user");
//        Map<String, Object> vars = new HashMap<>(3);
//        vars.put("admin", admin);
////        vars.put("certification",certification);
////        vars.put("user",user);
//        String businessKey = "12306";
//        String pi = lizhenActivitiService.startProcessByKey(key, businessKey, vars);
//        if (pi != null) {
//
//            lizhenActivitiService.completeByPiId(pi);
//        }
//        return pi.toString();
//
//
//    }

    @RequestMapping("/startTemplateMixProcess")
    public String startTemplateMixProcess(HttpServletRequest request) {
//        String key=request.getParameter("key");
        String key = "signtemplate";
        String admin = request.getParameter("admin");
        String hoster = request.getParameter("hoster");
        String simu = request.getParameter("lizhen");
        String isAdmin = request.getParameter("isAdmin");
        String businessId = request.getParameter("businessId");
        String res="dev bug fix 1.0 branch ";

//        String certification=request.getParameter("certification");
//        String user=request.getParameter("user");
        Map<String, Object> vars = new HashMap<>(4);
        vars.put("admin", admin);
//        vars.put("certification",certification);
//        vars.put("user",user);
        vars.put("hoster", hoster);
        vars.put("simu", simu);
        vars.put("isAdmin", isAdmin);
        String pi = lizhenActivitiService.startProcessByKey(key, businessId, vars);
        if (pi != null) {
            lizhenActivitiService.completeByPiId(pi);
        }

        return lizhenActivitiService.queryProcInstance(pi).toString();


    }

//    @RequestMapping("/completeByBusinessId")
//    public String completeByBusinessId(HttpServletRequest request) {
//        String businessId = request.getParameter("businessId");
//        String userId = request.getParameter("userId");
//
//        Task task = lizhenActivitiService.queryTaskByBusinessId(businessId, "process2", userId);
//        //托管方管理立项申请
//        if (task != null) {
//            if (task.getTaskDefinitionKey().equals("apply")) {
//                HashMap<String, Object> vars = new HashMap<>();
//                vars.put("isTrue", "Y");
//                if (task.getAssignee() != null) {
//                    lizhenActivitiService.claimTask(task.getId(), userId);
//                }
//                lizhenActivitiService.complete(task.getId(), vars);
//
//
//            }
//            //托管方上传合同文档环节
//            if (task.getTaskDefinitionKey().equals("upload1") || task.getTaskDefinitionKey().equals("upload4")) {
//                HashMap<String, Object> vars = new HashMap<>();
//                vars.put("docType", "pdf");
//                if (task.getAssignee() != null) {
//                    lizhenActivitiService.claimTask(task.getId(), userId);
//                }
//                lizhenActivitiService.complete(task.getId(), vars);
//
//
//            }
//
//
//            //初审合同环节
//            if (task.getTaskDefinitionKey().equals("audit1") || task.getTaskDefinitionKey().equals("audit3")) {
//                HashMap<String, Object> vars = new HashMap<>();
//                //传入初审意见
//                vars.put("audit1", "Y");
//                if (task.getAssignee() != null) {
//                    lizhenActivitiService.claimTask(task.getId(), userId);
//                }
//                lizhenActivitiService.complete(task.getId(), vars);
//            }
//            //二审合同环节
//            if (task.getTaskDefinitionKey().equals("audit2") || task.getTaskDefinitionKey().equals("audit4")) {
//                HashMap<String, Object> vars = new HashMap<>();
//                //传入二审意见
//                vars.put("audit2", "Y");
//                if (task.getAssignee() != null) {
//                    lizhenActivitiService.claimTask(task.getId(), userId);
//                }
//                lizhenActivitiService.complete(task.getId(), vars);
//
//
//            }
//            //一方盖章用印
//            if (task.getTaskDefinitionKey().equals("confirm1") || task.getTaskDefinitionKey().equals("confirm2")
//                    //另一方盖章用印
//                    || task.getTaskDefinitionKey().equals("use1") || task.getTaskDefinitionKey().equals("use2")
//                    //易私募上传排版后word合同环节
//                    || task.getTaskDefinitionKey().equals("upload2") || task.getTaskDefinitionKey().equals("upload5")
//                    //管理人上传pdf合同环节
//                    || task.getTaskDefinitionKey().equals("upload3") || task.getTaskDefinitionKey().equals("upload6")
//                    //易私募保存盖章处信息
//                    || task.getTaskDefinitionKey().equals("save1") || task.getTaskDefinitionKey().equals("save2")
//                    //选择托管并申请电子合同
//                    || task.getTaskDefinitionKey().equals("choose")
//
//            ) {
//                if (task.getAssignee() != null) {
//                    lizhenActivitiService.claimTask(task.getId(), userId);
//                }
//                lizhenActivitiService.complete(task.getId(), null);
//
//
//            }
//
//
//            return task.toString();
//        }
//        Task othersTask = lizhenActivitiService.queryTaskByBusinessId(businessId, "process2");
//        if (othersTask != null) {
//            return "对不起，当前暂无您的任务，该任务当前环节：" + othersTask.getName();
//        }
//
//
//        return "对不起，暂无您的任务,该任务已经完成";
//
//
//    }





    @RequestMapping("/queryTaskListByUser")
    public String queryTaskListByUser(HttpServletRequest request) {
        String userId = request.getParameter("userId");
        List<Task> tasks = lizhenActivitiService.queryTaskListByUser(userId);
        for (Task tsk : tasks) {
            System.out.println(tsk.getTaskDefinitionKey());


        }
        StringBuilder sb = new StringBuilder();
        for (Task task : tasks) {
            sb.append(task.toString()).append("</br>");
        }
        return sb.toString();
    }

    @RequestMapping("/completeTask")
    public String completeTask(HttpServletRequest request) {
        String taskId = request.getParameter("taskId");
        return lizhenActivitiService.complete(taskId, null);

    }
    @RequestMapping("/queryTask")
    public String queryTask(HttpServletRequest request) {
        String taskId = request.getParameter("proInsId");
        Task task= lizhenActivitiService.queryTaskByProInsId(taskId);
        return task.toString();


    }

//    @RequestMapping("/completeTaskWithUser")
//    public String completeTaskWithUser(HttpServletRequest request) {
//        String taskId = request.getParameter("taskId");
//        String user = request.getParameter("user");
//        Map<String, Object> vars = new HashMap<>(2);
//        vars.put("user", user);
//        return lizhenActivitiService.complete(taskId, vars);
//
//    }

    @RequestMapping("/completeTaskWithPass")
    public String completeTaskWithPass(HttpServletRequest request) {
        String taskId = request.getParameter("taskId");
        String pass = request.getParameter("pass");
        Map<String, Object> vars = new HashMap<>(2);
        vars.put("pass", pass);
        return lizhenActivitiService.complete(taskId, vars);

    }

    @RequestMapping("/completeTemplateTask1")
    public String completeTemplateTask1(HttpServletRequest request) {
        String taskId = request.getParameter("taskId");
        String hoster = request.getParameter("hoster");
        Map<String, Object> vars = new HashMap<>(5);
        vars.put("hoster", hoster);
        return lizhenActivitiService.complete(taskId, vars);

    }


    @RequestMapping("/getHistoryTaskInfo")
    public String getHistoryTaskInfo(HttpServletRequest request) {

        List<HistoricTaskInstance> htiList = lizhenActivitiService.queryHistoricTaskInstance();
        StringBuilder sb = new StringBuilder();
        for (HistoricTaskInstance hti : htiList) {
            sb.append(hti.toString()).append("</br>");
        }
        return sb.toString();

    }

    @RequestMapping("/revoke")
    public String revoke(HttpServletRequest request)throws Exception {

        String proInsId=request.getParameter("proInsId");
        lizhenActivitiService.revoke(proInsId);
        return "success";

    }
    @RequestMapping("/getVar")
    public String getVar(HttpServletRequest request)throws Exception {

        String proInsId=request.getParameter("proInsId");
        String varName=request.getParameter("varName");
        return lizhenActivitiService.getHisVar(proInsId,varName);


    }
    @RequestMapping("/getIns")
    public String getIns(HttpServletRequest request)throws Exception {

        String proInsId=request.getParameter("proInsId");

        ProcessInstance pi= lizhenActivitiService.queryProcInstance(proInsId);
        return pi.toString();


    }





//    @RequestMapping("/getHistoryTaskInfoByUser")
//    public String getHistoryTaskInfoByUser(HttpServletRequest request) {
//
//        String user = request.getParameter("user");
//
//        List<HistoricTaskInstance> htiList = historyService.createHistoricTaskInstanceQuery().taskAssignee(user).list();
//        List<HistoricTaskInstance> htiList2 = historyService.createHistoricTaskInstanceQuery().taskCandidateUser(user).list();
//        htiList.addAll(htiList2);
//        StringBuilder sb = new StringBuilder();
//        for (HistoricTaskInstance hti : htiList) {
//            sb.append(gson.toJson(hti)).append("</br>");
//        }
//        return sb.toString();
//
//    }

//    @RequestMapping("/testHistory")
//    public String testHistory(HttpServletRequest request) {
//
//
//        List<HistoricDetail> htiList = historyService.createHistoricDetailQuery().list();
//        StringBuilder sb = new StringBuilder();
//        for (HistoricDetail hti : htiList) {
//            sb.append(gson.toJson(hti)).append("</br>");
//        }
//        return sb.toString();
//
//    }

//    /**
//     * 读取带跟踪的图片
//     */
//    @RequestMapping(value = "trace/photo")
//    public void tracePhoto(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        // 设置页面不缓存
//        response.setHeader("Pragma", "No-cache");
//        response.setHeader("Cache-Control", "no-cache");
//        response.setDateHeader("Expires", 0);
//        String businessId = request.getParameter("businessId");
//        ProcessInstance pi = simuActivitiService.queryProInstanceByBusinessId(businessId, "process2");
//        String processInstanceId = pi.getId();
//        Task task = simuActivitiService.queryTaskByBusinessId(businessId, "process2");
//        String execId = task.getExecutionId();
//        InputStream imageStream = getActivitiProccessImage(processInstanceId, response);
//
//
////
////        InputStream imageStream = simuActivitiService.tracePhoto(procDefId, execId);
////
//        // 输出资源内容到相应对象
//        byte[] b = new byte[1024];
//        int len;
//        response.setCharacterEncoding("UTF-8");
//        while ((len = imageStream.read(b, 0, 1024)) != -1) {
//            response.getOutputStream().write(b, 0, len);
//        }
//    }
//    /**
//     * 读取带跟踪的图片
//     */
//    @RequestMapping(value = "/test")
//    public void test(HttpServletResponse response) throws Exception {
//
//
//        //processInstanceId
//        String processInstanceId = task1.getProcessInstanceId();
//        //获取历史流程实例
//        HistoricProcessInstance processInstance =  historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
//        //获取流程图
//        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
//        processEngineConfiguration = processEngine.getProcessEngineConfiguration();
//        Context.setProcessEngineConfiguration((ProcessEngineConfigurationImpl) processEngineConfiguration);
//
//        ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
//        ProcessDefinitionEntity definitionEntity = (ProcessDefinitionEntity)repositoryService.getProcessDefinition(processInstance.getProcessDefinitionId());
//
//        List<HistoricActivityInstance> highLightedActivitList =  historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list();
//        //高亮环节id集合
//        List<String> highLightedActivitis = new ArrayList<String>();
//        //高亮线路id集合
//        List<String> highLightedFlows = getHighLightedFlows(definitionEntity,highLightedActivitList);
//
//        for(HistoricActivityInstance tempActivity : highLightedActivitList){
//            String activityId = tempActivity.getActivityId();
//            highLightedActivitis.add(activityId);
//        }
//
//        //中文显示的是口口口，设置字体就好了
//        InputStream imageStream = diagramGenerator.generateDiagram(bpmnModel, "png", highLightedActivitis,highLightedFlows,"宋体","宋体",null,1.0);
//        //单独返回流程图，不高亮显示
////        InputStream imageStream = diagramGenerator.generatePngDiagram(bpmnModel);
//        // 输出资源内容到相应对象
//        byte[] b = new byte[1024];
//        int len;
//        while ((len = imageStream.read(b, 0, 1024)) != -1) {
//            response.getOutputStream().write(b, 0, len);
//        }
//    }
//
//    /**
//     * 获取需要高亮的线
//     * @param processDefinitionEntity
//     * @param historicActivityInstances
//     * @return
//     */
//    private List<String> getHighLightedFlows(
//            ProcessDefinitionEntity processDefinitionEntity,
//            List<HistoricActivityInstance> historicActivityInstances) {
//        List<String> highFlows = new ArrayList<String>();// 用以保存高亮的线flowId
//        for (int i = 0; i < historicActivityInstances.size() - 1; i++) {// 对历史流程节点进行遍历
//            ActivitiEventImpl activityImpl = processDefinitionEntity
//                    .find(historicActivityInstances.get(i)
//                            .getActivityId());// 得到节点定义的详细信息
//            List<ActivitiEventImpl> sameStartTimeNodes = new ArrayList<ActivitiEventImpl>();// 用以保存后需开始时间相同的节点
//            ActivitiEventImpl sameActivityImpl1 = processDefinitionEntity.
//                    .findActivity(historicActivityInstances.get(i + 1)
//                            .getActivityId());
//            // 将后面第一个节点放在时间相同节点的集合里
//            sameStartTimeNodes.add(sameActivityImpl1);
//            for (int j = i + 1; j < historicActivityInstances.size() - 1; j++) {
//                HistoricActivityInstance activityImpl1 = historicActivityInstances
//                        .get(j);// 后续第一个节点
//                HistoricActivityInstance activityImpl2 = historicActivityInstances
//                        .get(j + 1);// 后续第二个节点
//                if (activityImpl1.getStartTime().equals(
//                        activityImpl2.getStartTime())) {
//                    // 如果第一个节点和第二个节点开始时间相同保存
//                    ActivityImpl sameActivityImpl2 = processDefinitionEntity
//                            .findActivity(activityImpl2.getActivityId());
//                    sameStartTimeNodes.add(sameActivityImpl2);
//                } else {
//                    // 有不相同跳出循环
//                    break;
//                }
//            }
//            List<PvmTransition> pvmTransitions = activityImpl
//                    .getOutgoingTransitions();// 取出节点的所有出去的线
//            for (PvmTransition pvmTransition : pvmTransitions) {
//                // 对所有的线进行遍历
//                ActivityImpl pvmActivityImpl = (ActivityImpl) pvmTransition
//                        .getDestination();
//                // 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
//                if (sameStartTimeNodes.contains(pvmActivityImpl)) {
//                    highFlows.add(pvmTransition.getId());
//                }
//            }
//        }
//        return highFlows;
//    }

//    /**
//     * 获取流程图像，已执行节点和流程线高亮显示
//     */
//    public InputStream getActivitiProccessImage(String pProcessInstanceId, HttpServletResponse response) {
//        //logger.info("[开始]-获取流程图图像");
//        try {
//            //  获取历史流程实例
//            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
//                    .processInstanceId(pProcessInstanceId).singleResult();
//
//            if (historicProcessInstance == null) {
//                //throw new BusinessException("获取流程实例ID[" + pProcessInstanceId + "]对应的历史流程实例失败！");
//            } else {
//                // 获取流程定义
//                ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
//                        .getDeployedProcessDefinition(historicProcessInstance.getProcessDefinitionId());
//
//                // 获取流程历史中已执行节点，并按照节点在流程中执行先后顺序排序
//                List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
//                        .processInstanceId(pProcessInstanceId).orderByHistoricActivityInstanceId().asc().list();
//
//                // 已执行的节点ID集合
//                List<String> executedActivityIdList = new ArrayList<String>();
//                int index = 1;
//                //logger.info("获取已经执行的节点ID");
//                for (HistoricActivityInstance activityInstance : historicActivityInstanceList) {
//                    executedActivityIdList.add(activityInstance.getActivityId());
//
//                    //logger.info("第[" + index + "]个已执行节点=" + activityInstance.getActivityId() + " : " +activityInstance.getActivityName());
//                    index++;
//                }
//
//                BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());
//
//                // 已执行的线集合
//                List<String> flowIds = new ArrayList<String>();
//                // 获取流程走过的线 (getHighLightedFlows是下面的方法)
//                flowIds = getHighLightedFlows(bpmnModel, processDefinition, historicActivityInstanceList);
//
//
//                // 获取流程图图像字符流
//                ProcessDiagramGenerator pec = processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator();
//                //配置字体
//                InputStream imageStream = pec.generateDiagram(bpmnModel, "png", executedActivityIdList, flowIds, "宋体", "微软雅黑", "黑体", null, 2.0);
//
//                return imageStream;
//            }
//            //logger.info("[完成]-获取流程图图像");
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            //logger.error("【异常】-获取流程图失败！" + e.getMessage());
//            //throw new BusinessException("获取流程图失败！" + e.getMessage());
//        }
//        return null;
//    }
//
//    public List<String> getHighLightedFlows(BpmnModel bpmnModel, ProcessDefinitionEntity processDefinitionEntity, List<HistoricActivityInstance> historicActivityInstances) {
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //24小时制
//        List<String> highFlows = new ArrayList<String>();// 用以保存高亮的线flowId
//
//        for (int i = 0; i < historicActivityInstances.size() - 1; i++) {
//            // 对历史流程节点进行遍历
//            // 得到节点定义的详细信息
//            FlowNode activityImpl = (FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstances.get(i).getActivityId());
//
//
//            List<FlowNode> sameStartTimeNodes = new ArrayList<FlowNode>();// 用以保存后续开始时间相同的节点
//            FlowNode sameActivityImpl1 = null;
//
//            HistoricActivityInstance activityImpl_ = historicActivityInstances.get(i);// 第一个节点
//            HistoricActivityInstance activityImp2_;
//
//            for (int k = i + 1; k <= historicActivityInstances.size() - 1; k++) {
//                activityImp2_ = historicActivityInstances.get(k);// 后续第1个节点
//
//                if (activityImpl_.getActivityType().equals("userTask") && activityImp2_.getActivityType().equals("userTask") &&
//                        df.format(activityImpl_.getStartTime()).equals(df.format(activityImp2_.getStartTime()))) //都是usertask，且主节点与后续节点的开始时间相同，说明不是真实的后继节点
//                {
//
//                } else {
//                    sameActivityImpl1 = (FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstances.get(k).getActivityId());//找到紧跟在后面的一个节点
//                    break;
//                }
//
//            }
//            sameStartTimeNodes.add(sameActivityImpl1); // 将后面第一个节点放在时间相同节点的集合里
//            for (int j = i + 1; j < historicActivityInstances.size() - 1; j++) {
//                HistoricActivityInstance activityImpl1 = historicActivityInstances.get(j);// 后续第一个节点
//                HistoricActivityInstance activityImpl2 = historicActivityInstances.get(j + 1);// 后续第二个节点
//
//                if (df.format(activityImpl1.getStartTime()).equals(df.format(activityImpl2.getStartTime()))) {// 如果第一个节点和第二个节点开始时间相同保存
//                    FlowNode sameActivityImpl2 = (FlowNode) bpmnModel.getMainProcess().getFlowElement(activityImpl2.getActivityId());
//                    sameStartTimeNodes.add(sameActivityImpl2);
//                } else {// 有不相同跳出循环
//                    break;
//                }
//            }
//            List<SequenceFlow> pvmTransitions = activityImpl.getOutgoingFlows(); // 取出节点的所有出去的线
//
//            for (SequenceFlow pvmTransition : pvmTransitions) {// 对所有的线进行遍历
//                FlowNode pvmActivityImpl = (FlowNode) bpmnModel.getMainProcess().getFlowElement(pvmTransition.getTargetRef());// 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
//                if (sameStartTimeNodes.contains(pvmActivityImpl)) {
//                    highFlows.add(pvmTransition.getId());
//                }
//            }
//
//        }
//        return highFlows;
//
//    }


}
