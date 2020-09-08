package org.seckill.web;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/seckill")  //url:模块/资源
public class SeckillController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private SeckillService seckillService;

    /**
     * 活动列表
     * @param model model
     * @return 返回列表页
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public String list(Model model){
        //获取列表页
        List<Seckill> seckillList = seckillService.getSeckillList();
        model.addAttribute("list",seckillList);
        return "list";
    }

    /**
     * 活动详情
     * @param seckillId 秒杀活动ID
     * @param model model
     * @return 返回详情页
     */
    @RequestMapping(value = "/{seckillId}/detail",method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model){
        if(seckillId == null){
            return "redirect:/seckill/list";
        }
        Seckill seckill = seckillService.getById(seckillId);
        if(seckill == null){
            return "forward:/seckill/list";
        }
        model.addAttribute("seckill",seckill);
        return "detail";
    }

    /**
     * ajax json
     * @param seckillId 活动ID
     * @return 活动暴露对象
     */
    @RequestMapping(value ="/{seckillId}/exposer",method = RequestMethod.POST,
        produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId){
        SeckillResult<Exposer> seckillResult;
        try{
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            seckillResult = new SeckillResult<>(true, exposer);
        }catch(Exception e){
            seckillResult = new SeckillResult<>(false, e.getMessage());
            logger.error(e.getMessage(),e);
        }
        return seckillResult;
    }

    /**
     *
     * @param seckillId 秒杀活动ID
     * @param md5 加密MD5
     * @param userPhone 秒杀手机号
     * @return 返回执行结果
     */
    @RequestMapping(value = "/{seckillId}/{md5}/excute",method = RequestMethod.POST,
        produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> excute(@PathVariable("seckillId") Long seckillId,
                                                  @PathVariable("md5") String md5,
                                                  @CookieValue(value = "killPhone",required = false) Long userPhone){
        SeckillResult<SeckillExecution> seckillResult;

        if(userPhone == null){
            return new SeckillResult<>(true, "未注册");
        }
        try{
            //执行秒杀
            SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId,userPhone,md5);
            seckillResult = new SeckillResult<>(true, seckillExecution);
            return seckillResult;
        } catch(SeckillCloseException e){
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStateEnum.END);
            return new SeckillResult<>(true, seckillExecution);
        } catch(RepeatKillException e){
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
            return new SeckillResult<>(true, seckillExecution);
        }catch(Exception e) {
            logger.error(e.getMessage(), e);
            SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
            return new SeckillResult<>(true, seckillExecution);
        }
    }

    /**
     * 获取系统当前时间
     * @return  放回当前时间的毫秒数
     */
    @RequestMapping(value = "/time/now",method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> getTimeNow(){
        Date now = new Date();
        return new SeckillResult<>(true, now.getTime());
    }
}
