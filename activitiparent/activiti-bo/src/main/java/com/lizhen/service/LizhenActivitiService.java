package com.lizhen.service;

import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * activiti接口
 *
 * @author wangyz
 * @since 2019-01-14
 */
public interface LizhenActivitiService {


    /**
     * 根据流程定义key部署流程
     *
     * @param key
     * @return
     */
    public String deployByKey(String key);

    /**
     * 启动流程实例
     *
     * @param key
     * @param vars
     * @return
     */
    public ProcessInstance startProcessByKey(String key, Map<String, Object> vars);

    /**
     * 领取任务
     *
     * @param taskId
     * @param userId
     */
    public void claimTask(String taskId, String userId);


    /**
     * 启动流程实例（带业务表Id)
     *
     * @param key
     * @param businessKey
     * @param vars
     * @return
     */
    public String startProcessByKey(String key, String businessKey, Map<String, Object> vars);

    /**
     * 根据用户id查询待办任务
     *
     * @param userid
     * @return
     */
    public List<Task> queryTaskListByUser(String userid);

    /**
     * 完成用户任务
     *
     * @param taskId
     * @param vars
     * @return
     */
    public String complete(String taskId, Map<String, Object> vars);

    public void completeByPiId(String processInstanceId);

    public List<HistoricProcessInstance> queryHistoricInstance();

    public List<HistoricActivityInstance> queryHistoricActivityInstance();

    /**
     * 查询历史流转信息
     *
     * @return
     */
    public List<HistoricTaskInstance> queryHistoricTaskInstance();

    /**
     * 根据businessId,流程定义key，userId查询用户任务
     *
     * @param businessId
     * @param processDefKey
     * @param userId
     * @return
     */
    public Task queryTaskByBusinessId(String businessId, String processDefKey, String userId);

    /**
     * 根据businessId,流程定义key查询任务
     *
     * @param businessId
     * @param processDefKey
     * @return
     */
    public Task queryTaskByBusinessId(String businessId, String processDefKey);


    /**
     * 根据流程实例id查询用户任务
     * @param proInsId
     * @return
     */
    public Task queryTaskByProInsId(String proInsId);

    /**
     * 根据流程实例id，userId查询用户任务
     * @param proInsId
     * @param userId
     * @return
     */
    public Task queryTaskByProInsId(String proInsId,String userId);

    /**
     * 读取带跟踪的图片
     *
     * @param executionId 环节ID
     * @return 封装了各种节点信息
     */
    public InputStream tracePhoto(String processDefinitionId, String executionId);

    public ProcessInstance queryProInstanceByBusinessId(String businessId, String processDefinitionKey);
    public HistoricProcessInstance queryHisProInstanceByBusinessId(String businessId,String processDefinitionKey);

    /**
     * 根据id查询流程历史实例
     * @param procInsId
     * @return
     */
    public HistoricProcessInstance queryHisProInstance(String procInsId);

    /**
     * 根据流程实例Id查询流程实例
     * @param procInsId
     * @return
     */
    public ProcessInstance queryProcInstance(String procInsId);

    /**
     * 撤回立项
     * @param procInsId
     * @throws Exception
     */
    public void revoke(String procInsId) throws Exception;

    /**
     * 根据流程实例id和变量名获取流程变量值
     * @param proInsId
     * @param varName
     * @return
     */
    public String getHisVar(String proInsId,String varName);


}
