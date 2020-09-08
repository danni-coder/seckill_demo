<%--
  Created by IntelliJ IDEA.
  User: hu
  Date: 2020/8/27
  Time: 15:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="common/tag.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title>活动详情</title>
    <!--静态包含-->
    <%@include file="common/head.jsp"%>
</head>
<body>
<div class="container">
    <div class="panel panel-default text-center">
        <div class="pannel-heading">
            <h1>${seckill.name}</h1>
        </div>
    </div>
    <div class="pannel-body text-center">
        <h2 class="text-danger">
            <!--显示TIME图标-->
            <span  class="glyphicon glyphicon-time"></span>
            <!--显示倒计时-->
            <span class="glyphicon" id="seckill-box"></span>
        </h2>
    </div>
</div>
<!--登录弹出层 输入电话-->
<div id="killPhoneModal" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h3 class="modal-title text-center">
                    <span class="glyphico glyphico-phone"></span>秒杀电话：
                </h3>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-xs-8 col-xs-offset-2">
                        <input type="text" name="killPhone" id="killPhoneKey"
                               placeholder="请输入手机号" class="form-control"/>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <!--验证信息-->
                <span id="killPhoneMessage" class="glyphico"></span>
                <button type="button" id="killPhoneBtn" class="btn btn-success">
                    <span class="glyphico glyphico=phone"></span>
                    提交
                </button>
            </div>
        </div>
    </div>
</div>
</body>
<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="https://cdn.staticfile.org/jquery/2.1.1/jquery.min.js"></script>
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
<!--JQuery cookie插件-->
<script src="https://cdn.bootcdn.net/ajax/libs/jquery-cookie/1.4.1/jquery.cookie.min.js"></script>
<!--引入jquery countDown倒计时插件-->
<script src="/resources/script/jquery.countdown.min.js"></script>
<!--开始编写交互逻辑-->
<script src="/resources/script/seckill.js"></script>
<script type="text/javascript">
    $(function () {

        //使用EL表达式传输参数
        seckill.detail.init({
            seckillId : ${seckill.seckillId},
            startTime : ${seckill.startTime.time},//通过.time直接拿到Date类型的毫秒数
            endTime : ${seckill.endTime.time}
        })
    })
</script>
</html>