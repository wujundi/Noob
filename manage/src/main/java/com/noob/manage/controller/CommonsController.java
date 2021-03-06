package com.noob.manage.controller;

import com.google.gson.Gson;
import com.noob.manage.model.async.State;
import com.noob.manage.model.async.Task;
import com.noob.manage.model.commons.Webpage;
import com.noob.manage.model.utils.ResultBundle;
import com.noob.manage.model.utils.ResultListBundle;
import com.noob.manage.service.SpiderTaskService;
import com.noob.manage.service.WebpageService;
import com.noob.manage.utils.TablePage;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * CommonsSpiderPanel
 *
 * @author Gao Shen
 * @version 16/5/11
 */
@Controller
//@RequestMapping("panel/commons")
@RequestMapping("/")
public class CommonsController /*extends BaseController*/ {
    private static final Gson gson = new Gson();
    private Logger LOG = LogManager.getLogger(CommonsController.class);
    @Autowired
    private SpiderTaskService spiderTaskService;
    @Autowired
    private WebpageService webpageService;

    /**
     * 已抓取的网页列表
     *
     * @param query  查询词
     * @param domain 域名
     * @param page   页码
     * @return
     */
    @RequestMapping(value = {"panel/commons/list", "panel/commons"}, method = RequestMethod.GET)
    public ModelAndView list(@RequestParam(required = false) String query, @RequestParam(required = false) String domain, @RequestParam(defaultValue = "1", required = false) int page) {
        ModelAndView modelAndView = new ModelAndView("panel/commons/list");
        StringBuilder sbf = new StringBuilder();
        sbf.append("&query=");
        if (StringUtils.isNotBlank(query)) {
            query = query.trim();
            sbf.append(query);
        }
        sbf.append("&domain=");
        if (StringUtils.isNotBlank(domain)) {
            domain = domain.trim();
            sbf.append(domain);
        }
        page = page < 1 ? 1 : page;
        TablePage tp = null;
        ResultBundle<Pair<List<Webpage>, Long>> resultBundle = webpageService.getWebPageByKeywordAndDomain(query, domain, 10, page);
        if (resultBundle.getResult().getRight() > 0) {
            tp = new TablePage(resultBundle.getResult().getRight(), page, 10);
            tp.checkAgain();
            tp.setOtherParam(sbf.toString());
        }
        // 2019-08-13 20:06 这里 addObject 的变量就能在前台通过 el 表达式取到
        modelAndView.addObject("tablePage", tp).addObject("resultBundle", resultBundle.getResult().getKey());
        return modelAndView;
    }


    /**
     * 域名列表
     *
     * @return
     */
    @RequestMapping(value = "panel/commons/domainList", method = RequestMethod.GET)
    public ModelAndView domainList(@RequestParam(defaultValue = "50", required = false, value = "size") int size) {
        ModelAndView modelAndView = new ModelAndView("panel/commons/domainList");
        modelAndView.addObject("domainList", webpageService.countDomain(size).getResult());
        return modelAndView;
    }

    /**
     * 所有的抓取任务列表
     *
     * @return
     */
    @RequestMapping(value = "panel/commons/tasks", method = RequestMethod.GET)
    public ModelAndView tasks(@RequestParam(required = false, defaultValue = "false") boolean showRunning) {
        ModelAndView modelAndView = new ModelAndView("panel/commons/listTasks");
        ResultListBundle<Task> listBundle;
        if (!showRunning) {
            listBundle = spiderTaskService.getTaskList(true);
        } else {
            listBundle = spiderTaskService.getTasksFilterByState(State.RUNNING, true);
        }
        ResultBundle<Long> runningTaskCount = spiderTaskService.countByState(State.RUNNING);
        modelAndView.addObject("resultBundle", listBundle)
                .addObject("runningTaskCount", runningTaskCount.getResult())
                .addObject("spiderInfoList", listBundle.getResultList().stream()
                        .map(task -> StringEscapeUtils.escapeHtml4(
                                gson.toJson(task.getExtraInfoByKey("spiderInfo")
                                ))
                        ).collect(Collectors.toList()));
        return modelAndView;
    }


    /**
     * 获取query的关联信息
     *
     * @param query 查询queryString
     * @param size  结果集数量
     * @return 相关信息
     */
    @RequestMapping(value = "panel/commons/showRelatedInfo", method = {RequestMethod.GET})
    public ModelAndView showRelatedInfo(String query, @RequestParam(required = false, defaultValue = "10") int size) {
        ModelAndView modelAndView = new ModelAndView("panel/commons/showRelatedInfo");
        Pair<Map<String, List<Terms.Bucket>>, List<Webpage>> result = webpageService.relatedInfo(query, size).getResult();
        String title = "";
        String[] queryArray = query.split(":");
        String field = queryArray[0];
        String queryWord = queryArray[1];
        switch (field) {
            case "keywords":
                title += "关键词:";
                break;
            case "namedEntity.nr":
                title += "人物:";
                break;
            case "namedEntity.ns":
                title += "地点:";
                break;
            case "namedEntity.nt":
                title += "机构:";
                break;
        }
        title += queryWord + "的相关信息";
        modelAndView.addObject("relatedPeople", result.getKey().get("relatedPeople"))
                .addObject("title", title)
                .addObject("relatedLocation", result.getKey().get("relatedLocation"))
                .addObject("relatedInstitution", result.getKey().get("relatedInstitution"))
                .addObject("relatedKeywords", result.getKey().get("relatedKeywords"))
                .addObject("relatedWebpageList", result.getValue());
        return modelAndView;
    }

    @RequestMapping(value = "panel/commons/listQuartz")
    public String listQuartz(Model model) {
        model.addAttribute("list", spiderTaskService.listAllQuartzJobs().getResult());
        return "panel/commons/listQuartz";
    }

    @RequestMapping(value = "panel/commons/createQuartz", method = RequestMethod.POST)
    public String createQuartz(String spiderInfoId, int hourInterval, RedirectAttributes redirectAttributes) {
        spiderTaskService.createQuartzJob(spiderInfoId, hourInterval);
        redirectAttributes.addFlashAttribute("msg", "添加成功");
        return "redirect:/panel/commons/listQuartz";
    }

    @RequestMapping(value = "panel/commons/createQuartz", method = RequestMethod.GET)
    public String createQuartz(String spiderInfoId, Model model, RedirectAttributes redirectAttributes) {
        model.addAttribute("spiderInfoId", spiderInfoId);
        return "panel/commons/createQuartz";
    }
}

// 2019-07-22 19:25 是爬虫平台功能入口的核心 controller